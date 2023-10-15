package step.learning.services.formparse;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MixedFormParsService implements IFormParsService
{
    private static final int _MEMORY_THRESHOLD = 10 * 1024 * 1024;  // 10MB = file in memory.
    private static final int _MAX_FILE_SIZE = 10 * 1024 * 1024;  // max size one file.
    private static final int _MAX_FORM_SIZE = 20 * 1024 * 1024;  // max size all data from form.
    private final ServletFileUpload _file_upload;

    @Inject
    public MixedFormParsService()
    {
        DiskFileItemFactory file_item_factory = new DiskFileItemFactory();

        file_item_factory.setSizeThreshold(_MEMORY_THRESHOLD);
        file_item_factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

        _file_upload = new ServletFileUpload(file_item_factory);

        _file_upload.setFileSizeMax(_MAX_FILE_SIZE);
        _file_upload.setSizeMax(_MAX_FORM_SIZE);
    }

    @Override
    public IFormParsResult Parse(HttpServletRequest request)
    {
        final Map<String, String> fields = new HashMap<>();
        final Map<String, FileItem> files = new HashMap<>();
        final HttpServletRequest req = request;
        boolean is_multipart = request.getHeader("Content-Type") != null && request.getHeader("Content-Type").startsWith("multipart/form-data");
        String charset = (String) request.getAttribute("charset");

        if(charset == null)
            charset = StandardCharsets.UTF_8.name();
        if(is_multipart)
        {
            try
            {
                for (FileItem item : _file_upload.parseRequest(request))
                {
                    if (item.isFormField())
                        fields.put(item.getFieldName(), item.getString(charset));
                    else
                        files.put(item.getFieldName(), item);
                }
            }
            catch (Exception e) { e.printStackTrace(); }
        }
        else
        {
            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet())
                fields.put(entry.getKey(), entry.getValue()[0]);
        }

        return new IFormParsResult()
        {
            @Override
            public Map<String, String> GetFields() { return fields; }
            @Override
            public Map<String, FileItem> GetFiles() { return files; }
            @Override
            public HttpServletRequest GetRequest() { return req; }
        };
    }
}
