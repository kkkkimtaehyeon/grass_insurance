package com.example.grass_insurance.github.contribution;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class Contributions {

    @JsonProperty("total")
    private Total total;

    @JsonProperty("contributions")
    private List<Contribution> contributionList;

    @Data
    public static class Total {
        @JsonProperty("2024")
        private int year2024;
    }

    @Data
    public static class Contribution {
        private String date;
        private int count;
        private int level;
    }
}
