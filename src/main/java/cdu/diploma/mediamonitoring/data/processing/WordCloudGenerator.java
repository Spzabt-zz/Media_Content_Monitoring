package cdu.diploma.mediamonitoring.data.processing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class WordCloudGenerator {
    private final List<Map<String, Object>> data;

    public WordCloudGenerator(List<Map<String, Object>> data) {
        this.data = data;
    }

    public List<Map<String, Object>> generateWordCloud() throws FileNotFoundException {
        List<String> commentWords = new ArrayList<>();
        Set<String> stopWords = new HashSet<>();

        File readStopWords = new File("stopwords.txt");
        try (Scanner reader = new Scanner(readStopWords)) {
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                stopWords.add(data);
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
        }

        Map<String, Integer> wordCloud = new HashMap<>();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> item : data) {
            String text = item.get("title").toString().replaceAll("[^A-Za-z\\d_, ]+", "");
            String[] tokens = text.split("\\s+");

            for (int i = 0; i < tokens.length; i++) {
                String word = tokens[i].toLowerCase();

                if (!stopWords.contains(word)) {
                    commentWords.add(word);
                }
            }
        }

        for (String word : commentWords) {
            if (!wordCloud.containsKey(word)) {
                wordCloud.put(word, 0);
            }

            wordCloud.put(word, wordCloud.get(word) + 1);
        }

        for (String word : wordCloud.keySet()) {
            if (wordCloud.get(word) > 1) {
                Map<String, Object> individual = new HashMap<>();
                individual.put("text", word);
                individual.put("size", wordCloud.get(word));
                result.add(individual);
            }
        }

        return result;
    }
}