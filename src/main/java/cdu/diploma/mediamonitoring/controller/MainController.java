package cdu.diploma.mediamonitoring.controller;


import cdu.diploma.mediamonitoring.external.api.Pipeline;
import cdu.diploma.mediamonitoring.model.User;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal User user, Model model) {
        if (user != null)
            model.addAttribute("username", user.getUsername());
        return "greeting";
    }

    @GetMapping("/dashboard")
    public String main() {
        return "main";
    }

    @GetMapping("/panel")
    public String panel() {
        return "panel";
    }

    private String doSentimentAnalysis(String message) {
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument(message);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> sentences = coreDocument.sentences();
        String sentiment = null;
        for (CoreSentence sentence : sentences) {
            sentiment = sentence.sentiment();
        }
        return sentiment;
    }
}
