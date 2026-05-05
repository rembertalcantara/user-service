package pt.com.aubay.userservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pt.com.aubay.userservice.client.WireMockUserClient;
import pt.com.aubay.userservice.model.dto.UserProfileResponse;
import pt.com.aubay.userservice.service.factory.UserProfileFallbackFactory;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class UserProfileClientService {
  private final WireMockUserClient wireMockUserClient;

  public UserProfileClientService(WireMockUserClient wireMockUserClient) {
    this.wireMockUserClient = wireMockUserClient;
  }

  @Cacheable(cacheNames = "userProfiles", key = "#userId")
  @Retry(name = "wiremockUser")
  @CircuitBreaker(name = "wiremockUser", fallbackMethod = "fallback")
  public UserProfileResponse getProfile(String userId) {
    log.info("Fetching user profile for userId={}", userId);
    return wireMockUserClient.getProfile(userId);
  }

  public UserProfileResponse fallback(String userId, Throwable ex) {
    log.warn("Fallback triggered for userId={}", userId, ex);
    return UserProfileFallbackFactory.defaultProfile(userId);
  }
}
