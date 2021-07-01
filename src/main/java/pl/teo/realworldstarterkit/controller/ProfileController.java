package pl.teo.realworldstarterkit.controller;

import org.springframework.web.bind.annotation.*;
import pl.teo.realworldstarterkit.model.dto.Profile;
import pl.teo.realworldstarterkit.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public Profile get(@PathVariable String username, Principal principal) {
        return userService.getProfile(username, principal);
    }

    @PostMapping("/{username}/follow")
    public Profile follow(@PathVariable String username, Principal principal) {
        return userService.follow(username, principal);
    }

    @DeleteMapping("/{username}/follow")
    public Profile unfollow(@PathVariable String username, Principal principal) {
        return userService.unfollow(username, principal);
    }

}
