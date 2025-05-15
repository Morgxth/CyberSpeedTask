package org.example;

import org.example.Mapping.GameScheme;
import org.example.Mapping.StandardSymbolProbability;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class GameFieldGenerator {
    private final GameScheme config;
    private final Random random = new Random();

    public GameFieldGenerator(GameScheme config) {
        this.config = config;
    }

    public String[][] generate() {
        int rows = config.getRows();
        int columns = config.getColumns();
        String[][] field = new String[rows][columns];

        List<StandardSymbolProbability> standardProbabilities = config.getProbabilities().getStandardSymbols();
        Map<String, Integer> bonusSymbolProbabilities = config.getProbabilities().getBonusSymbols().getSymbols();
        int totalBonusWeight = bonusSymbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                boolean placeBonus = random.nextDouble() < 0.15;

                if (placeBonus && totalBonusWeight > 0) {
                    field[row][col] = pickSymbolByWeight(bonusSymbolProbabilities, totalBonusWeight);
                } else {
                    int finalRow = row;
                    int finalCol = col;
                    Optional<StandardSymbolProbability> cellProbOpt = standardProbabilities.stream()
                            .filter(p -> p.getRow() == finalRow && p.getColumn() == finalCol)
                            .findFirst();

                    if (cellProbOpt.isPresent()) {
                        Map<String, Integer> cellSymbolProbs = cellProbOpt.get().getSymbols();
                        int totalWeight = cellSymbolProbs.values().stream().mapToInt(Integer::intValue).sum();
                        field[row][col] = pickSymbolByWeight(cellSymbolProbs, totalWeight);
                    } else {
                        field[row][col] = pickRandomStandardSymbol();
                    }
                }
            }
        }

        return field;
    }

    private String pickSymbolByWeight(Map<String, Integer> symbolWeights, int totalWeight) {
        int r = random.nextInt(totalWeight);
        int cumulative = 0;
        for (Map.Entry<String, Integer> entry : symbolWeights.entrySet()) {
            cumulative += entry.getValue();
            if (r < cumulative) {
                return entry.getKey();
            }
        }
        return symbolWeights.keySet().iterator().next();
    }

    private String pickRandomStandardSymbol() {
        return config.getSymbols().entrySet().stream()
                .filter(entry -> "standard".equalsIgnoreCase(entry.getValue().getType()))
                .map(Map.Entry::getKey)
                .findAny()
                .orElse("?");
    }
}
