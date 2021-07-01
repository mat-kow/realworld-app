package pl.teo.realworldstarterkit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.teo.realworldstarterkit.model.dto.*;
import pl.teo.realworldstarterkit.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/users/after_login")
    public UserAuthenticationDto login(Principal principal) {
        // todo login
        return new UserAuthenticationDto();
    }

    @PostMapping("/users")
    public UserAuthenticationDto register(@RequestBody UserRegistrationDto dto) {
        return userService.save(dto);
    }

    @GetMapping("/user")
    public UserAuthenticationDto getCurrentUser() {
        //todo principal
        return null;
    }

    @PutMapping("/user")
    public UserAuthenticationDto update(@RequestBody UserUpdateDto userUpdateDto) {
        return userService.update(userUpdateDto);
    }


}
