package edu.uoc.epcsd.user.infrastructure.repository.jpa;

import edu.uoc.epcsd.user.domain.DigitalSession;
import edu.uoc.epcsd.user.domain.repository.DigitalSessionRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Replace with an embedded database
public class DigitalSessionRepositoryIntegrationTest {

    @Autowired
    private DigitalSessionRepository digitalSessionRepository;

    @MockBean
    private SpringDataDigitalSessionRepository jpaDigitalSessionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void whenAddingDigitalSession_thenFindDigitalSessionByUserId() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFullName("John Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setPassword("password");
        userEntity.setPhoneNumber("123456789");

        DigitalSessionEntity digitalSessionEntity = new DigitalSessionEntity();
        digitalSessionEntity.setId(1L);
        digitalSessionEntity.setDescription("description");
        digitalSessionEntity.setLocation("location");
        digitalSessionEntity.setLink("link");
        digitalSessionEntity.setUser(userEntity);

        entityManager.persist(userEntity);
        entityManager.persist(digitalSessionEntity);

        when(jpaDigitalSessionRepository.findDigitalSessionByUser(userEntity.getId())).thenReturn(List.of(digitalSessionEntity));

        // Act
        List<DigitalSession> sessions = digitalSessionRepository.findDigitalSessionByUser(userEntity.getId());

        // Assert
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getId()).isEqualTo(digitalSessionEntity.getId());
        assertThat(sessions.get(0).getDescription()).isEqualTo(digitalSessionEntity.getDescription());
        assertThat(sessions.get(0).getLocation()).isEqualTo(digitalSessionEntity.getLocation());
        assertThat(sessions.get(0).getLink()).isEqualTo(digitalSessionEntity.getLink());
        assertThat(sessions.get(0).getUserId()).isEqualTo(digitalSessionEntity.getUser().getId());    }

}
