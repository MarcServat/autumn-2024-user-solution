package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.UserApplication;
import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserApplication.class)
public class DigitalSessionRepositoryIntegrationTest {

    @Autowired
    private DigitalSessionRepository digitalSessionRepository;

    @Autowired
    private SpringDataDigitalSessionRepository springDataDigitalSessionRepository;

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    @Test
    void whenAddingDigitalSession_thenFindDigitalSessionByUserId() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setFullName("John Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setPassword("password");
        userEntity.setPhoneNumber("123456789");

        DigitalSessionEntity digitalSessionEntity = new DigitalSessionEntity();
        digitalSessionEntity.setId(1L);
        digitalSessionEntity.setDescription("description");
        digitalSessionEntity.setLocation("location");
        digitalSessionEntity.setLink("link");

        springDataUserRepository.save(userEntity);
        digitalSessionEntity.setUser(userEntity);
        Long userId = springDataDigitalSessionRepository.save(digitalSessionEntity).getUser().getId();

        // Act
        List<DigitalSession> sessions = digitalSessionRepository.findDigitalSessionByUser(userId);

        // Assert
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getId()).isEqualTo(digitalSessionEntity.getId());
        assertThat(sessions.get(0).getDescription()).isEqualTo(digitalSessionEntity.getDescription());
        assertThat(sessions.get(0).getLocation()).isEqualTo(digitalSessionEntity.getLocation());
        assertThat(sessions.get(0).getLink()).isEqualTo(digitalSessionEntity.getLink());
        assertThat(sessions.get(0).getUserId()).isEqualTo(digitalSessionEntity.getUser().getId());
    }

}
