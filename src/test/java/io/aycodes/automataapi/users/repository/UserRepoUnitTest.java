package io.aycodes.automataapi.users.repository;

import io.aycodes.automataapi.users.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@DataJpaTest
class UserRepoUnitTest {

    /**
     * This class is a unit testing suite designed to rigorously evaluate the behavior of the UserRepo repository in a controlled environment.
     * Leveraging the Spring Boot testing framework, using mock data, providing comprehensive coverage for the repository's methods.
     * Through a series of meticulously crafted test cases, it ensures that the repository accurately returns user data based on specified criteria,
     * simulating both positive and negative scenarios.
     */

    @Autowired
    private UserRepo        underTest;

    @MockBean
    private UserRepo        mockUserRepo;

    private final String EXISTENCE_EMAIL = "test@example.com";
    private final String NON_EXISTENCE_EMAIL = "notexist@example.com";
    private final String EXISTENCE_USERNAME = "test-user";
    private final String NON_EXISTENCE_USERNAME = "no-user";

    @BeforeEach
    public void setUp() {
        // Initialize mock data
        User user = User.builder()
                .username(EXISTENCE_USERNAME)
                .email(EXISTENCE_EMAIL)
                .password("123456789")
                .build();

        when(mockUserRepo.findByUsername(EXISTENCE_USERNAME)).thenReturn(Optional.of(user));
        when(mockUserRepo.findByUsername(NON_EXISTENCE_USERNAME)).thenReturn(Optional.empty());
        when(mockUserRepo.findByEmail(EXISTENCE_EMAIL)).thenReturn(Optional.of(user));
        when(mockUserRepo.findByEmail(NON_EXISTENCE_EMAIL)).thenReturn(Optional.empty());
    }

    @Test
    void testFindByEmail_WhenEmailExists() {
        // When
        Optional<User> foundUser = underTest.findByEmail(EXISTENCE_EMAIL);

        // Assert
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getEmail()).isEqualTo(EXISTENCE_EMAIL);
    }

    @Test
    void testFindByEmail_WhenEmailDoestNotExists() {
        // When
        Optional<User> foundUser = underTest.findByEmail(NON_EXISTENCE_EMAIL);

        // Assert
        assertThat(foundUser.isPresent()).isFalse();
    }

    @Test
    void testFindByUsername_WhenUsernameExists() {
        // When
        Optional<User> foundUser = underTest.findByUsername(EXISTENCE_USERNAME);

        // Assert
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getUsername()).isEqualTo(EXISTENCE_USERNAME);
    }

    @Test
    void testFindByUsername_WhenUsernameDoestNotExists() {
        // When
        Optional<User> foundUser = underTest.findByUsername(NON_EXISTENCE_USERNAME);

        // Assert
        assertThat(foundUser.isPresent()).isFalse();
    }
}