package pl.teo.realworldstarterkit.service;

import pl.teo.realworldstarterkit.model.dto.*;
import pl.teo.realworldstarterkit.model.entity.User;

import java.security.Principal;

public interface UserService {
    UserAuthenticationDto save(UserRegistrationDto userRegis);
    UserAuthenticationDto update(UserUpdateDto userUpdate, Principal principal);
    Profile getProfile(String username, Principal principal);
    Profile follow(String username, Principal principal);
    Profile unfollow(String username, Principal principal);
    UserAuthenticationDto getUser(String username);
    UserAuthenticationDto getUser(Principal principal);
    User getCurrentUser(Principal principal);

    UserAuthenticationDto login(UserLoginDto dto);
}
