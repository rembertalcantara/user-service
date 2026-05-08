package pt.com.aubay.userservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import pt.com.aubay.userservice.model.dto.UserProfileResponse;

@Tag(
    name = "User Profile",
    description = "Operations related to user profile retrieval and cache management"
)
public interface UserProfileController {

  @Operation(
      summary = "Get user profile",
      description = "Retrieves the profile information of a user by user ID.",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "User profile retrieved successfully",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserProfileResponse.class)
              )
          ),
          @ApiResponse(responseCode = "404", description = "User profile not found"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  UserProfileResponse getProfile(
      @Parameter(description = "User ID", required = true, example = "12345")
      @PathVariable String userId
  );

  @Operation(
      summary = "Evict all user profile cache",
      description = "Removes all entries from the userProfiles cache.",
      responses = {
          @ApiResponse(responseCode = "204", description = "Cache evicted successfully"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  void evictAllCache();

  @Operation(
      summary = "Evict user profile cache by user ID",
      description = "Removes a specific user profile entry from the userProfiles cache.",
      responses = {
          @ApiResponse(responseCode = "204", description = "User cache evicted successfully"),
          @ApiResponse(responseCode = "500", description = "Internal server error")
      }
  )
  void evictCacheByUserId(
      @Parameter(description = "User ID", required = true, example = "12345")
      @PathVariable String userId
  );
}