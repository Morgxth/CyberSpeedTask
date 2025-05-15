package org.example.Mapping;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class EvaluationResult {
    private final String[][] matrix;
    private final double reward;
    private final Map<String, List<String>> appliedWinningCombinations;
    private final String appliedBonusSymbol;
}

