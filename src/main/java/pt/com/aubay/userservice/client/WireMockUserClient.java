package pt.com.aubay.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pt.com.aubay.userservice.model.dto.UserProfileResponse;

@FeignClient(
        name = "wiremock-user-client",
        url = "${external.wiremock.base-url}"
)
public interface WireMockUserClient {

    @GetMapping("/api/users/{userId}/profile")
    UserProfileResponse getProfile(@PathVariable String userId);
}