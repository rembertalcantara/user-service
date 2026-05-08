package pt.com.aubay.userservice.service.factory;

import org.junit.jupiter.api.Test;
import pt.com.aubay.userservice.model.dto.UserProfileResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserProfileFallbackFactoryTest {

    @Test
    void shouldCreateDefaultProfile() {
        UserProfileResponse response =
                UserProfileFallbackFactory.defaultProfile("1");

        assertNotNull(response);
        assertEquals("1", response.userId());
        assertNotNull(response.demographics());
        assertNotNull(response.preferences());
        assertNotNull(response.purchaseHistory());
        assertNotNull(response.behaviorPatterns());
    }
}