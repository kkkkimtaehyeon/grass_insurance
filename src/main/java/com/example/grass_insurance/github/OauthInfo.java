package com.example.grass_insurance.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OauthInfo {

    @JsonProperty("access_token")
    private String accessToken;
}
