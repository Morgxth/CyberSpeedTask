package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Mapping.EvaluationResult;
import org.example.Mapping.GameScheme;

import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws JsonProcessingException {
        if (args.length < 1) {
            System.out.println("Usage: java Main <config.json>");
            return;
        }

        String configPath = args[0];
        GameScheme config;

        try {
            config = GameConfigLoader.load(configPath);
        } catch (IOException e) {
            System.out.println("Failed to load config: " + e.getMessage());
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter bet amount: ");
        double bet = scanner.nextDouble();

        GameFieldGenerator generator = new GameFieldGenerator(config);
        String[][] field = generator.generate();

        GameEvaluator evaluator = new GameEvaluator(config);
        EvaluationResult result = evaluator.evaluate(field, bet);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);

        System.out.println(json);

    }
}
