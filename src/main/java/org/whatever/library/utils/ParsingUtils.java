package org.whatever.library.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ParsingUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String objectToJSON(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    public static String objectToJSON(Object object, String... ignoredProperties) throws IOException {
        String json = objectToJSON(object);
        String[] propsArray = json.split(",");
        List<String> properties = Arrays.stream(propsArray).collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        List<String> toIgnore = Arrays.stream(ignoredProperties).collect(Collectors.toList());

        properties = properties.stream().filter(s -> {
            for (String ignore : toIgnore) {
                if (s.contains(ignore)) return false;
            }
            return true;
        }).collect(Collectors.toList());

        for (String p : properties) {
            sb.append(p).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }
}
