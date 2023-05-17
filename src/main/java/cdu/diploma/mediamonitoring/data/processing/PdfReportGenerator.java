package cdu.diploma.mediamonitoring.data.processing;

import cdu.diploma.mediamonitoring.util.FreeMarkerUtil;
import com.lowagie.text.DocumentException;
import freemarker.template.TemplateException;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class PdfReportGenerator {

//    public void generatePdfReport(String templatePath, Map<String, Object> dataModel, ServletOutputStream response)
//            throws IOException, TemplateException, DocumentException {
//        FreeMarkerUtil freeMarkerUtil = new FreeMarkerUtil();
//
//        String htmlContent = freeMarkerUtil.processTemplate(templatePath, dataModel);
//
////        response.setContentType("application/pdf");
////        response.setHeader("Content-Disposition", "attachment; filename=\"report.pdf\"");
//
//        try (response) {
//            ITextRenderer renderer = new ITextRenderer();
//            renderer.setDocumentFromString(htmlContent);
//            renderer.layout();
//            renderer.createPDF(response);
//            renderer.finishPDF();
//        }
//        // Close the response stream after generating the PDF report
//    }

    public byte[] generatePdfReport(String templatePath, Map<String, Object> dataModel)
            throws IOException, TemplateException, DocumentException {
        FreeMarkerUtil freeMarkerUtil = new FreeMarkerUtil();

        String htmlContent = freeMarkerUtil.processTemplate(templatePath, dataModel);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();
        } finally {
            outputStream.close();
        }

        return outputStream.toByteArray();
    }
}
