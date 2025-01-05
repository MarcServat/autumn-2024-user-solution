package edu.uoc.epcsd.user.domain.service;

import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import edu.uoc.epcsd.user.application.rest.response.GetUserResponseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class DigitalSessionServiceUnitTest {

    @Mock
    private DigitalSessionRepository digitalSessionRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DigitalSessionServiceImpl digitalSessionService;

    @Value("${userService.getUserById.url}")
    private String getUserById;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenValidUserId_thenSessionsShouldBeFound() {
        // Arrange
        Long userId = 1L;
        DigitalSession session = DigitalSession.builder()
                .id(1L)
                .description("Test Description Session")
                .location("Barcelona")
                .link("https://creative-world.com/session")
                .userId(userId)
                .build();
        List<DigitalSession> expectedSessions = List.of(session);

        when(digitalSessionRepository.findDigitalSessionByUser(userId)).thenReturn(expectedSessions);
        when(restTemplate
                .getForEntity(getUserById, GetUserResponseTest.class, userId))
                .thenReturn(new ResponseEntity<>(new GetUserResponseTest(), HttpStatus.OK));

        // Act
        List<DigitalSession> actualSessions = digitalSessionService.findDigitalSessionByUser(userId);

        // Assert
        assertThat(actualSessions).isEqualTo(expectedSessions);
    }

    @Test
    void whenInvalidUserId_thenUserNotFoundExceptionShouldBeThrown() {
        // Arrange
        Long userId = 1L;

        when(restTemplate
                .getForEntity(getUserById, GetUserResponseTest.class, userId))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act and Assert
        assertThatThrownBy(() -> digitalSessionService.findDigitalSessionByUser(userId))
                .isInstanceOf(UserNotFoundException.class);
    }
}