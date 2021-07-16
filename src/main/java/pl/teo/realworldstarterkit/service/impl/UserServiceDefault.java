package pl.teo.realworldstarterkit.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.teo.realworldstarterkit.app.exception.ApiForbiddenException;
import pl.teo.realworldstarterkit.app.exception.ApiNotFoundException;
import pl.teo.realworldstarterkit.app.exception.ApiValidationException;
import pl.teo.realworldstarterkit.app.jwt.JwtBuilder;
import pl.teo.realworldstarterkit.model.dto.*;
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
    private final JwtBuilder jwtBuilder;
    private final ModelMapper mapper;


    public UserServiceDefault(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtBuilder jwtBuilder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtBuilder = jwtBuilder;
        this.mapper = new ModelMapper();
    }

    @Override
    @Transactional
    public UserAuthenticationDto save(UserRegistrationDto userRegis) {
        if (userRepo.existsByEmailOrUsername(userRegis.getEmail(), userRegis.getUsername())) {
            throw new ApiValidationException();
        }
        userRegis.setPassword(passwordEncoder.encode(userRegis.getPassword()));
        User userSaved = userRepo.save(mapper.map(userRegis, User.class));
        return mapper.map(userSaved, UserAuthenticationDto.class);
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
        return mapper.map(saved, UserAuthenticationDto.class);
    }

    @Override
    public Profile getProfile(String username, Principal principal) {
        User currentUser = null;
        if (principal != null) {
            currentUser = getCurrentUser(principal);
        }
        User user = userRepo.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ApiNotFoundException("User does not exist"));

        Profile profile = mapper.map(user, Profile.class);
        if (currentUser == null) {
            profile.setFollowing(false);
        } else {
            profile.setFollowing(currentUser.getFallowingList().stream().anyMatch(u -> u.getId() == user.getId()));
        }
        return profile;
    }

    @Override
    public Profile follow(String username, Principal principal) {
        User toFollow = userRepo.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ApiNotFoundException("User does not exist"));
        User currentUser = getCurrentUser(principal);
        List<User> lst = currentUser.getFallowingList();
        lst.add(toFollow);
        currentUser.setFallowingList(lst);
        userRepo.save(currentUser);
        Profile profile = mapper.map(toFollow, Profile.class);
        profile.setFollowing(true);
        return profile;
    }

    @Override
    public Profile unfollow(String username, Principal principal) {
        User toUnfollow = userRepo.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ApiNotFoundException("User does not exist"));
        User currentUser = getCurrentUser(principal);
        List<User> lst = currentUser.getFallowingList();
        lst.remove(toUnfollow);
        currentUser.setFallowingList(lst);
        userRepo.save(currentUser);

        Profile profile = mapper.map(toUnfollow, Profile.class);
        profile.setFollowing(false);
        return profile;
    }

    @Override
    public UserAuthenticationDto getUser(String username) {
        User user = userRepo.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ApiNotFoundException("User does not exist"));
        return mapper.map(user, UserAuthenticationDto.class);
    }

    @Override
    public UserAuthenticationDto getUser(Principal principal) {
        return mapper.map(getCurrentUser(principal), UserAuthenticationDto.class);
    }

    @Override
    public User getCurrentUser(Principal principal) {
        return  userRepo.findById(Long.valueOf(principal.getName()))
                .orElseThrow(() -> new ApiNotFoundException("User does not exist"));
    }

    @Override
    public UserAuthenticationDto login(UserLoginDto dto) {
        User user = userRepo.findByEmailIgnoreCase(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User dont exist"));
        if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            UserAuthenticationDto authDto = mapper.map(user, UserAuthenticationDto.class);
            authDto.setToken(jwtBuilder.getToken(String.valueOf(user.getId())));
            return authDto;
        } else {
            throw new ApiForbiddenException();
        }
    }
}
