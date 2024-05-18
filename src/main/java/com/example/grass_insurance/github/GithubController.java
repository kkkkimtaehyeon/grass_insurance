package com.example.grass_insurance.github;

import com.example.grass_insurance.github.contribution.ContributionService;
import com.example.grass_insurance.github.contribution.Contributions;
import com.example.grass_insurance.github.contribution.Contributions.Contribution;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Controller
public class GithubController {

    @Value("${GITHUB_CLIENT_ID}")
    String GITHUB_CLIENT_ID;

    @Value("${GITHUB_CLIENT_SECRET}")
    String GITHUB_CLIENT_SECRET;

    private final ContributionService contributionService;

    @GetMapping("/api/oauth/github/callback")
    public String getAccessToken(@RequestParam("code") String code) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<OauthInfo> response = restTemplate.exchange("https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                getAccessHttpEntity(code),
                OauthInfo.class
                );

        String accessToken = response.getBody().getAccessToken();

        return "redirect:/api/github/login/success?access_token=" + accessToken;
    }

    public HttpEntity<MultiValueMap<String, String>> getAccessHttpEntity(String code) {
        HttpHeaders header = new HttpHeaders();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", GITHUB_CLIENT_ID);
        body.add("client_secret", GITHUB_CLIENT_SECRET);
        body.add("code", code);

        return new HttpEntity<>(body, header);
    }


    @GetMapping("/api/github/login/success")
    public String getUserInfo(@RequestParam("access_token") String access_token) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserInfo> response = restTemplate.exchange("https://api.github.com/user",
                HttpMethod.GET,
                getUserHttpEntity(access_token),
                UserInfo.class
        );

        String username = response.getBody().getName();
        return "redirect:/api/github/contribution/" + username + "?year=2024";
    }

    private HttpEntity<MultiValueMap<String,String>> getUserHttpEntity(String access_token) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "token " + access_token);
        return new HttpEntity<>(requestHeaders);
    }

    @ResponseBody
    @GetMapping("/api/github/contribution/{username}")
    public int getContributions(@PathVariable("username") String username, @RequestParam("year") int year) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://github-contributions-api.jogruber.de/v4/" + username + "?y=" + year;
        Contributions contributions = restTemplate.getForObject(url, Contributions.class);

        assert contributions != null;
        return contributionService.getTodayCount(contributions);
    }
}
