package cdu.diploma.mediamonitoring.util;

import cdu.diploma.mediamonitoring.domain.dto.KeywordDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class KeywordsUtil {
    @NotNull
    public static StringBuilder getKeywordsStringFromJson(String keywords) {
        StringBuilder keys = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<KeywordDto> keywordList = objectMapper.readValue(keywords, new TypeReference<List<KeywordDto>>() {
            });

            for (KeywordDto keyword : keywordList) {
                keys.append(keyword.getValue()).append(", ");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return keys;
    }

    @NotNull
    public static String[] separateKeywords(String keywords) {
        String[] keys = keywords.split(",");

        for (int i = 0; i < keys.length; i++) {
            if (Objects.equals(keys[i], " "))
                break;
            else
                keys[i] = keys[i].trim();
        }

        String[] newKeys = new String[keys.length - 1];
        System.arraycopy(keys, 0, newKeys, 0, keys.length - 1);

        return newKeys;
    }
}
