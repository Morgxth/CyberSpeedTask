package org.example.Mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SymbolScheme {
    @JsonProperty("reward_multiplier")
    private Double rewardMultiplier;
    private String type;
    private Double extra;
    private String impact;
}
