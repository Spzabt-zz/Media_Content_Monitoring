package cdu.diploma.mediamonitoring.controller;


import cdu.diploma.mediamonitoring.external.api.Pipeline;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class MainController {
    @PostMapping
    public String main(@RequestBody String message) {
        return doSentimentAnalysis(message);
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
