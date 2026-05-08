package pt.com.aubay.userservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.com.aubay.userservice.client.UserProfileClient;
import pt.com.aubay.userservice.model.dto.UserProfileResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileClientServiceTest {

    @Mock
    private UserProfileClient userProfileClient;

    @InjectMocks
    private UserProfileClientService service;

    private UserProfileResponse response;

    @BeforeEach
    void setup() {
        response = mock(UserProfileResponse.class);
    }

    @Test
    void shouldReturnUserProfileSuccessfully() {
        when(userProfileClient.getProfile("1"))
                .thenReturn(response);

        UserProfileResponse result = service.getProfile("1");

        assertNotNull(result);
        verify(userProfileClient).getProfile("1");
    }

    @Test
    void shouldExecuteFallbackSuccessfully() {
        Throwable ex = new RuntimeException("Connection error");

        UserProfileResponse result = service.fallback("1", ex);

        assertNotNull(result);
        assertEquals("1", result.userId());
    }

    @Test
    void shouldThrowExceptionWhenClientFails() {
        when(userProfileClient.getProfile("1"))
                .thenThrow(new RuntimeException("External API failure"));

        assertThrows(RuntimeException.class, () -> service.getProfile("1"));
    }
}