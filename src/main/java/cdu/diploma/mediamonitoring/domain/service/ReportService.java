package cdu.diploma.mediamonitoring.domain.service;

import cdu.diploma.mediamonitoring.external.api.PdfReportGenerator;
import cdu.diploma.mediamonitoring.domain.dto.AllDataDto;
import cdu.diploma.mediamonitoring.domain.model.PlatformName;
import cdu.diploma.mediamonitoring.domain.model.Project;
import cdu.diploma.mediamonitoring.domain.model.SocialMediaPlatform;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service
public class ReportService {
    private final AnalysingService analysingService;

    public ReportService(AnalysingService analysingService) {
        this.analysingService = analysingService;
    }

    public void generateReport(Model model, HttpServletResponse response, Project project) {
        String templatePath = "templates/forPdfReport.ftl";
        String outputFileName = "report.pdf";

        SocialMediaPlatform socialMediaPlatform = project.getSocialMediaPlatform();

        HashSet<String> dates = new HashSet<>();
        ArrayList<AllDataDto> allData = analysingService.getAllDataDtos(socialMediaPlatform, dates, PlatformName.NONE.name());

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("totalReachCount", analysingService.totalReachCount(allData, dates));
        dataModel.put("totalMentionCount", analysingService.totalMentionsCount(allData));
        dataModel.put("totalRepostCount", analysingService.retweetTotalCount(allData));
        dataModel.put("totalLikesCount", analysingService.likeCount(allData));
        dataModel.put("totalPositiveCount", analysingService.positiveCount(allData));
        dataModel.put("totalNegativeCount", analysingService.negativeCount(allData));
        dataModel.put("popularMentions", analysingService.mostPopularMentionsForTheReport(allData));

        PdfReportGenerator pdfReportGenerator = new PdfReportGenerator();
        try {
            byte[] pdfBytes = pdfReportGenerator.generatePdfReport(templatePath, dataModel);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + outputFileName + "\"");
            response.setContentLength(pdfBytes.length);

            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(pdfBytes);
            outputStream.flush();
            outputStream.close();

            model.addAttribute("messageType", "success");
            model.addAttribute("message", "PDF report generated successfully.");
            System.out.println("PDF report generated successfully.");
        } catch (Exception e) {
            System.out.println("Failed to generate PDF report: " + e.getMessage());
        }
    }
}
