package step.learning.services.formparse;

import org.apache.commons.fileupload.FileItem;

import java.util.Map;

public interface IFormParsResult
{
    Map<String, String> GetFields();
    Map<String, FileItem> GetFiles();
}
