package org.example.Mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Probabilities {
    @JsonProperty("standard_symbols")
    private List<StandardSymbolProbability> standardSymbols;
    @JsonProperty("bonus_symbols")
    private BonusSymbolProbability bonusSymbols;
}
