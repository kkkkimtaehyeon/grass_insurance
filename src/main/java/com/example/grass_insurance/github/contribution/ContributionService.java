package com.example.grass_insurance.github.contribution;

import com.example.grass_insurance.github.contribution.Contributions.Contribution;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ContributionService {

    public int getTodayCount(Contributions contributions) {
        List<Contribution> contributionList = contributions.getContributionList();
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);

        Contribution contribution = contributionList.stream()
                .filter(c -> c.getDate().equals(nowDate))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        return contribution.getCount();
    }
}
