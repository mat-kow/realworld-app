package pl.teo.realworldstarterkit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.teo.realworldstarterkit.model.dto.Profile;
import pl.teo.realworldstarterkit.service.UserService;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public Profile get(@PathVariable String username) {
        return userService.getProfile(username);
    }

    @PostMapping("/{username}/follow")
    public Profile follow(@PathVariable String username) {
        return userService.follow(username);
    }

    @DeleteMapping("/{username}/follow")
    public Profile unfollow(@PathVariable String username) {
        return userService.unfollow(username);
    }

}
