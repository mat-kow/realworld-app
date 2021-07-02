package pl.teo.realworldstarterkit.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import pl.teo.realworldstarterkit.model.dto.Profile;
import pl.teo.realworldstarterkit.model.dto.UserAuthenticationDto;
import pl.teo.realworldstarterkit.model.dto.UserRegistrationDto;
import pl.teo.realworldstarterkit.model.dto.UserUpdateDto;
import pl.teo.realworldstarterkit.model.entity.User;
import pl.teo.realworldstarterkit.model.repository.UserRepo;
import pl.teo.realworldstarterkit.service.UserService;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;

@Service
public class UserServiceDefault implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceDefault(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserAuthenticationDto save(UserRegistrationDto userRegis) {
        if (userRepo.existsByEmailOrUsername(userRegis.getEmail(), userRegis.getUsername())) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "error!!!"); //todo exception
        }
        User userSaved = userRepo.save(userRegisDtoToUser(userRegis));
        return userToUserAuthDto(userSaved);
    }

    @Override
    @Transactional
    public UserAuthenticationDto update(UserUpdateDto userUpdate, Principal principal) {
        User user = getCurrentUser(principal);
        String email = userUpdate.getEmail();
        if (!(email == null || email.isBlank())) {
            user.setEmail(email);
        }
        String password = userUpdate.getPassword();
        if (!(password == null || password.isBlank())) {
            user.setPassword(passwordEncoder.encode(password));
        }
        String username = userUpdate.getUsername();
        if (!(username == null || username.isBlank())) {
            user.setUsername(username);
        }
        String bio = userUpdate.getBio();
        if (!(bio == null || bio.isBlank())) {
            user.setBio(bio);
        }
        String image = userUpdate.getImage();
        if (!(image == null || image.isBlank())) {
            user.setImage(image);
        }
        User saved = userRepo.save(user);
        return userToUserAuthDto(saved);
    }

    @Override
    public Profile getProfile(String username, Principal principal) {
        User currentUser = null;
        if (principal != null) {
            currentUser = getCurrentUser(principal);
        }
        User user = userRepo.findByUsernameIgnoreCase(username).orElseThrow(RuntimeException::new);//todo exception
        return userToProfile(user, currentUser);
    }

    @Override
    public Profile follow(String username, Principal principal) {
        User toFollow = userRepo.findByUsernameIgnoreCase(username).orElseThrow(RuntimeException::new);//todo exception
        User currentUser = getCurrentUser(principal);
        List<User> lst = currentUser.getFallowingList();
        lst.add(toFollow);
        currentUser.setFallowingList(lst);
        userRepo.save(currentUser);
        return userToProfile(toFollow, currentUser);
    }

    @Override
    public Profile unfollow(String username, Principal principal) {
        User toUnfollow = userRepo.findByUsernameIgnoreCase(username).orElseThrow(RuntimeException::new);//todo exception
        User currentUser = getCurrentUser(principal);
        List<User> lst = currentUser.getFallowingList();
        lst.remove(toUnfollow);
        currentUser.setFallowingList(lst);
        userRepo.save(currentUser);
        return userToProfile(toUnfollow, currentUser);
    }

    @Override
    public UserAuthenticationDto getUser(String username) {
        return userToUserAuthDto(userRepo.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " dont exist")));
    }

    @Override
    public UserAuthenticationDto getUser(Principal principal) {
        return userToUserAuthDto(getCurrentUser(principal));
    }

    private Profile userToProfile(User viewedUser, User currentUser) {
        Profile profile = new Profile();
        profile.setUsername(viewedUser.getUsername());
        profile.setBio(viewedUser.getBio());
        profile.setImage(viewedUser.getImage());
        if (currentUser == null) {
            profile.setFollowing(false);
        } else {
            profile.setFollowing(currentUser.getFallowingList().stream().anyMatch(u -> u.getId() == viewedUser.getId()));
        }

        return profile;
    }
    private User userRegisDtoToUser(UserRegistrationDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        return user;
    }

    private UserAuthenticationDto userToUserAuthDto(User user) {
        UserAuthenticationDto userAuthenticationDto = new UserAuthenticationDto();

        userAuthenticationDto.setUsername(user.getUsername());
        userAuthenticationDto.setEmail(user.getEmail());
        userAuthenticationDto.setBio(user.getBio());
        userAuthenticationDto.setImage(user.getImage());

        return userAuthenticationDto;
    }

    @Override
    public User getCurrentUser(Principal principal) {
        return  userRepo.findById(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
