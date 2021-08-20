package pl.teo.realworldstarterkit.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.teo.realworldstarterkit.app.exception.ApiForbiddenException;
import pl.teo.realworldstarterkit.app.exception.ApiValidationException;
import pl.teo.realworldstarterkit.app.jwt.JwtBuilder;
import pl.teo.realworldstarterkit.model.dto.*;
import pl.teo.realworldstarterkit.model.entity.User;
import pl.teo.realworldstarterkit.model.repository.UserRepo;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceDefaultTest {

    @Mock UserRepo userRepo;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtBuilder jwtBuilder;
    UserServiceDefault underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceDefault(userRepo, passwordEncoder, jwtBuilder);
    }

    @Test
    void shouldSaveUserFromDto() {

        UserRegistrationDto dto = getRegisDtoTom();

        User user = new User();
        user.setUsername("Tom");
        user.setEmail("tom@gmail.com");
        user.setPassword("password");

        when(userRepo.save(any())).thenReturn(user);
        when(userRepo.existsByEmailOrUsername(any(), any())).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("password");

        underTest.save(dto);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepo).save(userArgumentCaptor.capture());

        User captorValue = userArgumentCaptor.getValue();

        user.setId(0);
        assertEquals(captorValue, user);
    }

    @Test
    void shouldThrowWhenUsernameOrEmailExist() {
        UserRegistrationDto dto = getRegisDtoTom();

        when(userRepo.existsByEmailOrUsername(any(), any())).thenReturn(true);
        assertThrows(ApiValidationException.class, () -> underTest.save(dto));
    }

    @Nested
    class Update {
        @Test
        void shouldUpdateUsername() {
            Principal principal = () -> "1";
            User user = getUserTom();
            UserUpdateDto updateDto = new UserUpdateDto();
            updateDto.setUsername("Bob");
            when(userRepo.findById(1L)).thenReturn(Optional.of(user));
            when(userRepo.save(any())).thenReturn(user);

            underTest.update(updateDto, principal);
            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

            verify(userRepo).save(userArgumentCaptor.capture());

            User captorValue = userArgumentCaptor.getValue();
            User updatedUser = getUserTom();
            updatedUser.setUsername("Bob");

            assertEquals(updatedUser, captorValue);

        }

        @Test
        void shouldUpdateEmail() {
            Principal principal = () -> "1";
            User user = getUserTom();
            UserUpdateDto updateDto = new UserUpdateDto();
            updateDto.setEmail("bob@gmail.com");
            when(userRepo.findById(1L)).thenReturn(Optional.of(user));
            when(userRepo.save(any())).thenReturn(user);

            underTest.update(updateDto, principal);
            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

            verify(userRepo).save(userArgumentCaptor.capture());

            User captorValue = userArgumentCaptor.getValue();
            User updatedUser = getUserTom();
            updatedUser.setEmail("bob@gmail.com");

            assertEquals(updatedUser, captorValue);

        }

        @Test
        void shouldUpdatePassword() {
            Principal principal = () -> "1";
            User user = getUserTom();
            UserUpdateDto updateDto = new UserUpdateDto();
            updateDto.setPassword("differentPass");
            when(userRepo.findById(1L)).thenReturn(Optional.of(user));
            when(userRepo.save(any())).thenReturn(user);
            when(passwordEncoder.encode("differentPass")).thenReturn("differentPass");

            underTest.update(updateDto, principal);
            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

            verify(userRepo).save(userArgumentCaptor.capture());

            User captorValue = userArgumentCaptor.getValue();
            User updatedUser = getUserTom();
            updatedUser.setPassword("differentPass");

            assertEquals(updatedUser, captorValue);

        }

        @Test
        void shouldUpdateBio() {
            Principal principal = () -> "1";
            User user = getUserTom();
            UserUpdateDto updateDto = new UserUpdateDto();
            updateDto.setBio("I'm Bob");
            when(userRepo.findById(1L)).thenReturn(Optional.of(user));
            when(userRepo.save(any())).thenReturn(user);

            underTest.update(updateDto, principal);
            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

            verify(userRepo).save(userArgumentCaptor.capture());

            User captorValue = userArgumentCaptor.getValue();
            User updatedUser = getUserTom();
            updatedUser.setBio("I'm Bob");

            assertEquals(updatedUser, captorValue);

        }

        @Test
        void shouldUpdateImage() {
            Principal principal = () -> "1";
            User user = getUserTom();
            UserUpdateDto updateDto = new UserUpdateDto();
            updateDto.setImage("image.com/bob");
            when(userRepo.findById(1L)).thenReturn(Optional.of(user));
            when(userRepo.save(any())).thenReturn(user);

            underTest.update(updateDto, principal);
            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

            verify(userRepo).save(userArgumentCaptor.capture());

            User captorValue = userArgumentCaptor.getValue();
            User updatedUser = getUserTom();
            updatedUser.setImage("image.com/bob");

            assertEquals(updatedUser, captorValue);

        }

        @Test
        void shouldUpdateEverything() {
            Principal principal = () -> "1";
            User user = getUserTom();
            UserUpdateDto updateDto = new UserUpdateDto();
            updateDto.setImage("image.com/bob");
            updateDto.setPassword("differentPass");
            updateDto.setUsername("Bob");
            updateDto.setEmail("bob@gmail.com");
            updateDto.setBio("I'm Bob");
            when(userRepo.findById(1L)).thenReturn(Optional.of(user));
            when(userRepo.save(any())).thenReturn(user);
            when(passwordEncoder.encode("differentPass")).thenReturn("differentPass");

            underTest.update(updateDto, principal);
            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

            verify(userRepo).save(userArgumentCaptor.capture());

            User captorValue = userArgumentCaptor.getValue();
            User updatedUser = new User();
            updatedUser.setBio("I'm Bob");
            updatedUser.setUsername("Bob");
            updatedUser.setPassword("differentPass");
            updatedUser.setId(1);
            updatedUser.setImage("image.com/bob");
            updatedUser.setEmail("bob@gmail.com");
            updatedUser.setFallowingList(new ArrayList<>());

            assertEquals(updatedUser, captorValue);

        }

    }

    @Nested
    class GetProfile {
        @Test
        void shouldReturnProfileFromUsernameWithFallowingSetToFalseWhenNoCurrentUser() {
            Profile expected = new Profile();
            expected.setFollowing(false);
            expected.setUsername("Tom");
            expected.setBio("I'm Tom");
            expected.setImage("image.com/tom");

            when(userRepo.findByUsernameIgnoreCase("tom")).thenReturn(Optional.of(getUserTom()));

            Profile actual = underTest.getProfile("tom", null);
            assertEquals(expected, actual);
        }

        @Test
        void shouldReturnProfileFromUsernameWithFallowingSetToTrue() {
            Profile expected = new Profile();
            expected.setFollowing(true);
            expected.setUsername("Tom");
            expected.setBio("I'm Tom");
            expected.setImage("image.com/tom");

            User currentUser = getUserBob();
            currentUser.setFallowingList(Collections.singletonList(getUserTom()));

            Principal principal = () -> "2";

            when(userRepo.findByUsernameIgnoreCase("tom")).thenReturn(Optional.of(getUserTom()));
            when(userRepo.findById(2L)).thenReturn(Optional.of(currentUser));
            Profile actual = underTest.getProfile("tom", principal);
            assertEquals(expected, actual);
        }

        @Test
        void shouldReturnProfileFromUsernameWithFallowingSetToFalseWhenEmptyListOfFallowing() {
            Profile expected = new Profile();
            expected.setFollowing(false);
            expected.setUsername("Tom");
            expected.setBio("I'm Tom");
            expected.setImage("image.com/tom");

            User currentUser = getUserBob();

            Principal principal = () -> "2";

            when(userRepo.findByUsernameIgnoreCase("tom")).thenReturn(Optional.of(getUserTom()));
            when(userRepo.findById(2L)).thenReturn(Optional.of(currentUser));
            Profile actual = underTest.getProfile("tom", principal);
            assertEquals(expected, actual);
        }

        @Test
        void shouldReturnProfileFromUsernameWithFallowingSetToFalseWhenSomeoneElseOnFallowingList() {
            Profile expected = new Profile();
            expected.setFollowing(false);
            expected.setUsername("Tom");
            expected.setBio("I'm Tom");
            expected.setImage("image.com/tom");

            User differentUser = new User();
            differentUser.setImage("image.com/jeff");
            differentUser.setPassword("differentPass");
            differentUser.setUsername("jeff");
            differentUser.setEmail("jeff@gmail.com");
            differentUser.setBio("I'm jeff");
            differentUser.setId(3);

            User currentUser = getUserBob();
            currentUser.setFallowingList(Collections.singletonList(differentUser));

            Principal principal = () -> "2";

            when(userRepo.findByUsernameIgnoreCase("tom")).thenReturn(Optional.of(getUserTom()));
            when(userRepo.findById(2L)).thenReturn(Optional.of(currentUser));
            Profile actual = underTest.getProfile("tom", principal);
            assertEquals(expected, actual);
        }

    }

    @Test
    void shouldFallowGivenUser() {
        Principal principal = () -> "2";

        when(userRepo.findById(2L)).thenReturn(Optional.of(getUserBob()));
        when(userRepo.findByUsernameIgnoreCase("tom")).thenReturn(Optional.of(getUserTom()));

        Profile actualProfile = underTest.follow("tom", principal);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userArgumentCaptor.capture());
        User captorValue = userArgumentCaptor.getValue();

        User expectedUser = getUserBob();
        expectedUser.setFallowingList(Collections.singletonList(getUserTom()));

        Profile expectedProfile = new Profile();
        expectedProfile.setFollowing(true);
        expectedProfile.setUsername("Tom");
        expectedProfile.setBio("I'm Tom");
        expectedProfile.setImage("image.com/tom");

        assertAll(() -> assertEquals(expectedUser, captorValue),
                () -> assertEquals(expectedProfile, actualProfile)
        );
    }

    @Test
    void shouldUnFallowGivenUser() {
        Principal principal = () -> "2";
        User currentUser = getUserBob();
        List<User> fallowingList = new ArrayList<>();
        fallowingList.add(getUserTom());
        currentUser.setFallowingList(fallowingList);

        when(userRepo.findById(2L)).thenReturn(Optional.of(currentUser));
        when(userRepo.findByUsernameIgnoreCase("tom")).thenReturn(Optional.of(getUserTom()));

        Profile actualProfile = underTest.unfollow("tom", principal);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userArgumentCaptor.capture());
        User captorValue = userArgumentCaptor.getValue();

        User expectedUser = getUserBob();

        Profile expectedProfile = new Profile();
        expectedProfile.setFollowing(false);
        expectedProfile.setUsername("Tom");
        expectedProfile.setBio("I'm Tom");
        expectedProfile.setImage("image.com/tom");

        assertAll(() -> assertEquals(expectedUser, captorValue),
                () -> assertEquals(expectedProfile, actualProfile)
        );
    }

    @Test
    void shouldReturnUserAuthDtoFromUsername() {
        when(userRepo.findByUsernameIgnoreCase("tom")).thenReturn(Optional.of(getUserTom()));

        UserAuthenticationDto expected = new UserAuthenticationDto();
        expected.setUsername("Tom");
        expected.setEmail("tom@gmail.com");
        expected.setBio("I'm Tom");
        expected.setImage("image.com/tom");

        UserAuthenticationDto actual = underTest.getUser("tom");

        assertEquals(expected, actual);

    }

    @Test
    void shouldReturnUserAuthDtoFromPrincipal() {
        Principal principal = () -> "1";
        when(userRepo.findById(1L)).thenReturn(Optional.of(getUserTom()));

        UserAuthenticationDto expected = new UserAuthenticationDto();
        expected.setUsername("Tom");
        expected.setEmail("tom@gmail.com");
        expected.setBio("I'm Tom");
        expected.setImage("image.com/tom");

        UserAuthenticationDto actual = underTest.getUser(principal);

        assertEquals(expected, actual);

    }

    @Test
    void shouldLogin() {
        User user = getUserTom();

        UserLoginDto credentials = new UserLoginDto();
        credentials.setEmail(user.getEmail());
        credentials.setPassword(user.getPassword());

        when(userRepo.findByEmailIgnoreCase(credentials.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtBuilder.getToken(any())).thenReturn("token");

        UserAuthenticationDto expected = new UserAuthenticationDto();
        expected.setToken("token");
        expected.setUsername("Tom");
        expected.setEmail("tom@gmail.com");
        expected.setBio("I'm Tom");
        expected.setImage("image.com/tom");

        assertEquals(expected, underTest.login(credentials));
    }

    @Test
    void shouldThrowWhenEPasswordDontMatch() {
        User user = getUserTom();

        UserLoginDto credentials = new UserLoginDto();
        credentials.setEmail(user.getEmail());
        credentials.setPassword(user.getPassword());

        when(userRepo.findByEmailIgnoreCase(credentials.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(ApiForbiddenException.class, () -> underTest.login(credentials));

    }

    private User getUserTom() {
        User user = new User();
        user.setUsername("Tom");
        user.setEmail("tom@gmail.com");
        user.setPassword("password");
        user.setBio("I'm Tom");
        user.setImage("image.com/tom");
        user.setId(1);
        user.setFallowingList(new ArrayList<>());

        return user;
    }

    private User getUserBob() {
        User bob = new User();
        bob.setImage("image.com/bob");
        bob.setPassword("differentPass");
        bob.setUsername("Bob");
        bob.setEmail("bob@gmail.com");
        bob.setBio("I'm Bob");
        bob.setId(2);
        bob.setFallowingList(new ArrayList<>());
        return bob;
    }

    private UserRegistrationDto getRegisDtoTom() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setUsername("Tom");
        dto.setEmail("tom@gmail.com");
        dto.setPassword("password");
        return dto;
    }
}