package org.example;

import org.example.Mapping.EvaluationResult;
import org.example.Mapping.GameScheme;
import org.example.Mapping.SymbolScheme;
import org.example.Mapping.WinCombination;

import java.util.*;

public class GameEvaluator {

    private final GameScheme config;

    public GameEvaluator(GameScheme config) {
        this.config = config;
    }

    public EvaluationResult evaluate(String[][] field, double betAmount) {
        Map<String, Integer> symbolCounts = countStandardSymbols(field);
        Map<String, SymbolScheme> symbolConfigs = config.getSymbols();

        double totalReward = 0.0;
        Map<String, List<String>> appliedCombinations = new HashMap<>();
        String appliedBonusSymbol = null;

        for (Map.Entry<String, Integer> entry : symbolCounts.entrySet()) {
            String symbol = entry.getKey();
            int count = entry.getValue();
            SymbolScheme symConfig = symbolConfigs.get(symbol);

            if (!"standard".equalsIgnoreCase(symConfig.getType())) continue;

            double baseReward = symConfig.getRewardMultiplier() * betAmount;
            double multiplier = 1.0;

            for (Map.Entry<String, WinCombination> comboEntry : config.getWinCombinations().entrySet()) {
                WinCombination combo = comboEntry.getValue();
                if ("same_symbols".equalsIgnoreCase(combo.getWhen()) && combo.getCount() == count) {
                    multiplier *= combo.getRewardMultiplier();
                    appliedCombinations
                            .computeIfAbsent(symbol, k -> new ArrayList<>())
                            .add(comboEntry.getKey());
                }
            }

            totalReward += baseReward * multiplier;
        }

        for (Map.Entry<String, WinCombination> comboEntry : config.getWinCombinations().entrySet()) {
            WinCombination combo = comboEntry.getValue();
            if (!"linear_symbols".equalsIgnoreCase(combo.getWhen())) continue;

            List<List<String>> areas = combo.getCoveredAreas();
            for (List<String> area : areas) {
                String firstSymbol = null;
                boolean allMatch = true;

                for (String position : area) {
                    String[] parts = position.split(":");
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);

                    String symbol = field[row][col];
                    SymbolScheme symConf = symbolConfigs.get(symbol);

                    if (symConf == null || !"standard".equalsIgnoreCase(symConf.getType())) {
                        allMatch = false;
                        break;
                    }

                    if (firstSymbol == null) {
                        firstSymbol = symbol;
                    } else if (!firstSymbol.equals(symbol)) {
                        allMatch = false;
                        break;
                    }
                }

                if (allMatch) {
                    SymbolScheme matchedSym = symbolConfigs.get(firstSymbol);
                    double baseReward = matchedSym.getRewardMultiplier() * betAmount * area.size();
                    totalReward += baseReward * combo.getRewardMultiplier();

                    appliedCombinations
                            .computeIfAbsent(firstSymbol, k -> new ArrayList<>())
                            .add(comboEntry.getKey());
                }
            }
        }

        outer:
        for (String[] row : field) {
            for (String symbol : row) {
                SymbolScheme sym = symbolConfigs.get(symbol);
                if (sym == null || !"bonus".equalsIgnoreCase(sym.getType())) continue;

                appliedBonusSymbol = symbol;
                switch (sym.getImpact()) {
                    case "multiply_reward":
                        totalReward *= sym.getRewardMultiplier();
                        break;
                    case "extra_bonus":
                        totalReward += sym.getExtra();
                        break;
                    case "miss":
                        totalReward = 0;
                        break outer;
                }
            }
        }

        return new EvaluationResult(field, totalReward, appliedCombinations, appliedBonusSymbol);
    }

    private Map<String, Integer> countStandardSymbols(String[][] field) {
        Map<String, Integer> counts = new HashMap<>();
        Map<String, SymbolScheme> symbolConfigs = config.getSymbols();

        for (String[] row : field) {
            for (String symbol : row) {
                SymbolScheme sym = symbolConfigs.get(symbol);
                if (sym != null && "standard".equalsIgnoreCase(sym.getType())) {
                    counts.merge(symbol, 1, Integer::sum);
                }
            }
        }

        return counts;
    }
}
