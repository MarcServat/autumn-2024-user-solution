package edu.uoc.epcsd.user.application.rest;

import edu.uoc.epcsd.user.domain.service.DigitalSessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import edu.uoc.epcsd.user.domain.DigitalSession;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DigitalSessionRESTController.class)
public class DigitalSessionControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DigitalSessionService digitalSessionService;

    @Test
    void testFindDigitalSessionByUser() throws Exception {
        Long userId = 1L;
        List<DigitalSession> sessions = List.of(
                new DigitalSession(1L, "Session 1", "Location 1", "Link 1", 1L),
                new DigitalSession(2L, "Session 2", "Location 2", "Link 2", 1L)
        );

        when(digitalSessionService.findDigitalSessionByUser(userId)).thenReturn(sessions);

        mockMvc.perform(get("/digital/digitalByUser?userId=1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(sessions.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(sessions.get(0).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].location").value(sessions.get(0).getLocation()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].link").value(sessions.get(0).getLink()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userId").value(sessions.get(0).getUserId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(sessions.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(sessions.get(1).getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].location").value(sessions.get(1).getLocation()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].link").value(sessions.get(1).getLink()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].userId").value(sessions.get(1).getUserId()));
    }
}
