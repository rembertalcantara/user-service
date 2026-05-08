package pt.com.aubay.userservice.controller;

import org.junit.jupiter.api.Test;
import pt.com.aubay.userservice.controller.impl.UserProfileControllerImpl;
import pt.com.aubay.userservice.model.dto.UserProfileResponse;
import pt.com.aubay.userservice.service.UserProfileClientService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class UserProfileControllerImplTest {

    private final UserProfileClientService client =
            mock(UserProfileClientService.class);

    private final UserProfileControllerImpl controller =
            new UserProfileControllerImpl(client);

    @Test
    void shouldGetProfile() {
        UserProfileResponse response = mock(UserProfileResponse.class);

        when(client.getProfile("1")).thenReturn(response);

        UserProfileResponse result = controller.getProfile("1");

        assertNotNull(result);
        verify(client).getProfile("1");
    }

    @Test
    void shouldEvictAllCache() {
        controller.evictAllCache();

        verifyNoInteractions(client);
    }

    @Test
    void shouldEvictCacheByUserId() {
        controller.evictCacheByUserId("1");

        verifyNoInteractions(client);
    }
}