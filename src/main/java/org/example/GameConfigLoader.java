package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.example.Mapping.GameScheme;

import java.io.File;
import java.io.IOException;

public class GameConfigLoader {
    public static GameScheme load(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return mapper.readValue(new File(path), GameScheme.class);
    }
}

