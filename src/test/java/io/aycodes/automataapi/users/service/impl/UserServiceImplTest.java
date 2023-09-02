package io.aycodes.automataapi.users.service.impl;

import io.aycodes.automataapi.common.dtos.CustomException;
import io.aycodes.automataapi.common.dtos.user.UserSignupDto;
import io.aycodes.automataapi.users.model.User;
import io.aycodes.automataapi.users.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserServiceImpl     underTest;
    @Mock
    private UserRepo            userRepo;
    @Mock
    private PasswordEncoder     passwordEncoder;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepo, passwordEncoder);
    }

    @Test
    void testCreateUser_WhenUserDoesNotExist() throws CustomException {

        //Given
        UserSignupDto  userSignupDto = UserSignupDto.builder()
                .email("test@gmail.com")
                .username("test")
                .password("12345")
                .build();

        //When
        underTest.createUser(userSignupDto);

        //Then
        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepo).save(userArgumentCaptor.capture());

    }

    @Test
    void testCreateUser_WhenUserExistWithSuppliedEmail() {

        //Given
        UserSignupDto  userSignupDto = UserSignupDto.builder()
                .email("test@gmail.com")
                .username("test")
                .password("12345")
                .build();

        //When
        given(userRepo.findByEmail(userSignupDto.getEmail()))
                .willReturn(Optional.of(new User()));

        assertThatThrownBy(() -> underTest.createUser(userSignupDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("Email is already in use");
    }


    @Test
    void testCreateUser_WhenUserExistWithSuppliedUsername() {

        //Given
        UserSignupDto  userSignupDto = UserSignupDto.builder()
                .email("test@gmail.com")
                .username("test")
                .password("12345")
                .build();

        //When
        given(userRepo.findByUsername(userSignupDto.getUsername()))
                .willReturn(Optional.of(new User()));

        assertThatThrownBy(() -> underTest.createUser(userSignupDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("Username is already in use");
    }

    @Test
    void testCreateUser_WhenUserIsUnableToPersistInDB() throws CustomException {

        //Given
        UserSignupDto  userSignupDto = UserSignupDto.builder()
                .email("test@gmail.com")
                .username("test")
                .password("12345")
                .build();

        //When
        given(userRepo.save(underTest.createUser(userSignupDto)))
                .willThrow(RuntimeException.class);

        assertThatThrownBy(() -> underTest.createUser(userSignupDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("Unable to create user");
    }

    /**
     * We just need to verify that the findById user repo method was invoked,
     * since we already tested the user repo
     */
    @Test
    void testGetUserById() {
        //when
        underTest.getUserById(1L);

        //then
        verify(userRepo).findById(1L);
    }


    /**Disabled because it is a repitition of testing user repo methods, which was already covered
     *
     * **/
    @Test
    @Disabled
    void testGetUserById_WhenUserWithIdIsFound() {
        //Given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .username("test")
                .password("12345")
                .build();

        //When
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = underTest.getUserById(userId);

        // Then
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getId()).isEqualTo(userId);
    }


    /**Disabled because it is a repitition of testing user repo methods, which was already covered
     *
     * **/
    @Test
    @Disabled
    void testGetUserById_WhenUserWithIdIsNotFound() {
        //Given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .username("test")
                .password("12345")
                .build();

        //When
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        Optional<User> retrievedUser = underTest.getUserById(userId);

        // Then
        assertThat(retrievedUser).isEmpty();
    }
}