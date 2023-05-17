package cdu.diploma.mediamonitoring.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerUtil {

    public String processTemplate(String templatePath, Map<String, Object> dataModel)
            throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);
        configuration.setClassForTemplateLoading(getClass(), "/");
        configuration.setDefaultEncoding("UTF-8");

        Template template = configuration.getTemplate(templatePath);

        StringWriter writer = new StringWriter();
        template.process(dataModel, writer);

        return writer.toString();
    }
}
