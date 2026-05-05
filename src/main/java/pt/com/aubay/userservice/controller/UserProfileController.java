package pt.com.aubay.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import pt.com.aubay.userservice.model.dto.UserProfileResponse;
import pt.com.aubay.userservice.service.UserProfileClientService;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserProfileController {
  private final UserProfileClientService client;

  public UserProfileController(UserProfileClientService client) {
    this.client = client;
  }

  @GetMapping("/{userId}/profile")
  public UserProfileResponse getProfile(@PathVariable String userId) {
    log.info("Getting user profile for userId={}", userId);
    return client.getProfile(userId);
  }

  @DeleteMapping("/cache")
  @CacheEvict(cacheNames = "userProfiles", allEntries = true)
  public void evictAllCache() {
    log.info("Evicting all userProfiles cache");
  }

  @DeleteMapping("/cache/{userId}")
  @CacheEvict(cacheNames = "userProfiles", key = "#userId")
  public void evictCacheByUserId(@PathVariable String userId) {
    log.info("Evicting cache for userId={}", userId);
  }
}
