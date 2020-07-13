package org.whatever.library.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ParsingUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String objectToJSON(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }
}
