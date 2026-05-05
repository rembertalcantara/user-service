package pt.com.aubay.userservice.model.dto;

import pt.com.aubay.userservice.model.enums.*;

import java.math.BigDecimal;
import java.util.List;

import java.time.LocalDate;
public record UserProfileResponse(
        String userId,
        Demographics demographics,
        Preferences preferences,
        PurchaseHistory purchaseHistory,
        BehaviorPatterns behaviorPatterns
) {

  public record Demographics(
          Integer age,
          Gender gender,
          String location,
          String income,
          Education education
  ) {}

  public record Preferences(
          List<String> categories,
          PriceRange priceRange,
          List<String> brands,
          String style
  ) {}

  public record PriceRange(
          Integer min,
          Integer max
  ) {}

  public record PurchaseHistory(
          Integer totalOrders,
          BigDecimal averageOrderValue,
          LocalDate lastPurchaseDate,
          List<String> favoriteCategories,
          SeasonalPatterns seasonalPatterns
  ) {}

  public record SeasonalPatterns(
          Integer spring,
          Integer summer,
          Integer fall,
          Integer winter
  ) {}

  public record BehaviorPatterns(
          Frequency browsingFrequency,
          Frequency purchaseFrequency,
          Level priceSensitivity,
          Level brandLoyalty
  ) {}
}