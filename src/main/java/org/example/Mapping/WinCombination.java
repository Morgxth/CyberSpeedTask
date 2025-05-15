package org.example.Mapping;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WinCombination {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier;
    private String when;
    private Integer count;
    private String group;
    private List<List<String>> coveredAreas;

}
