package pl.teo.realworldstarterkit.app;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.teo.realworldstarterkit.model.repository.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new MyUserDetails(userRepo.findByEmailIgnoreCase(s)
                .orElseThrow(() -> new UsernameNotFoundException("User dont exist")));
    }
}
