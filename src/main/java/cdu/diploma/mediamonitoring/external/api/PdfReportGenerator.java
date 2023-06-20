package cdu.diploma.mediamonitoring.external.api;

import cdu.diploma.mediamonitoring.util.FreeMarkerUtil;
import com.lowagie.text.DocumentException;
import freemarker.template.TemplateException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class PdfReportGenerator {
    public byte[] generatePdfReport(String templatePath, Map<String, Object> dataModel)
            throws IOException, TemplateException, DocumentException {
        FreeMarkerUtil freeMarkerUtil = new FreeMarkerUtil();

        String htmlContent = freeMarkerUtil.processTemplate(templatePath, dataModel);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (outputStream) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();
        }

        return outputStream.toByteArray();
    }
}
