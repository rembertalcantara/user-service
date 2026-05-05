package pt.com.aubay.userservice.service.factory;

import pt.com.aubay.userservice.model.dto.UserProfileResponse;
import pt.com.aubay.userservice.model.enums.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class UserProfileFallbackFactory {

    public static UserProfileResponse defaultProfile(String userId) {
        return new UserProfileResponse(
                userId,
                new UserProfileResponse.Demographics(30, Gender.OTHER, "UNKNOWN", "0-0", Education.BACHELOR),
                new UserProfileResponse.Preferences(
                        List.of("ELECTRONICS"),
                        new UserProfileResponse.PriceRange(10, 500),
                        List.of("APPLE"),
                        "BUDGET_CONSCIOUS"
                ),
                new UserProfileResponse.PurchaseHistory(
                        0,
                        BigDecimal.ZERO,
                        LocalDate.now(),
                        List.of("ELECTRONICS"),
                        new UserProfileResponse.SeasonalPatterns(0,0,0,0)
                ),
                new UserProfileResponse.BehaviorPatterns(
                        Frequency.MONTHLY,
                        Frequency.MONTHLY,
                        Level.MEDIUM,
                        Level.LOW
                )
        );
    }
}