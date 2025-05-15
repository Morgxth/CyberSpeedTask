package org.example.Mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class GameScheme {
    private int columns;
    private int rows;
    private Map<String, SymbolScheme> symbols;
    private Probabilities probabilities;
    @JsonProperty("win_combinations")
    private Map<String, WinCombination> winCombinations;
}

