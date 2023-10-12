package step.learning.services.formparse;

import javax.servlet.http.HttpServletRequest;

public interface IFormParsService
{
    IFormParsResult Parse(HttpServletRequest request);
}
