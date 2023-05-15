package cdu.diploma.mediamonitoring.nlp.pipeline;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

public class Pipeline {
    private static final Properties properties;
    private static StanfordCoreNLP stanfordCoreNLP;

    private Pipeline() {

    }

    static {
        properties = new Properties();
        properties.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
    }

    public static StanfordCoreNLP getPipeline() {
        if (stanfordCoreNLP == null) {
            stanfordCoreNLP = new StanfordCoreNLP(properties);
        }

        return stanfordCoreNLP;
    }
}
