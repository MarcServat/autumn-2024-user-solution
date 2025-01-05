package edu.uoc.epcsd.user.domain.service;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DigitalSessionUnitTest {
    
    @InjectMocks
    private DigitalSessionServiceImpl service;

    @Before
    public void setUp() {
        Long id = 1L;
        String description = "Test Description Session";
        String location = "Barcelona";
        String link = "https://creative-world.com/session";
        Long userId = 10L;

        edu.uoc.epcsd.user.domain.DigitalSession session = edu.uoc.epcsd.user.domain.DigitalSession.builder()
                .id(id)
                .description(description)
                .location(location)
                .link(link)
                .userId(userId)
                .build();

        List<edu.uoc.epcsd.user.domain.DigitalSession> digitalSessions = List.of(session);

        Mockito.when(service.findDigitalSessionByUser(1L)).thenReturn(digitalSessions);
        Mockito.when(service.findDigitalSessionByUser(2L)).thenThrow(UserNotFoundException.class);
    }

    @Test
    void testDigitalSessionLinkIsNotNull() {
        // Arrange
        Long id = 1L;
        String description = "Test Description Session";
        String location = "Barcelona";
        String link = "https://creative-world.com/session";
        Long userId = 10L;

        // Act
        DigitalSession session = DigitalSession.builder()
                .id(id)
                .description(description)
                .location(location)
                .link(link)
                .userId(userId)
                .build();

        // Assert
        assertThat(session).isNotNull();
        assertThat(session.getLink()).isNotNull();
    }

}
