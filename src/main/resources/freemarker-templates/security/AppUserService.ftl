<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="applicationImports" type="java.util.List<java.lang.String>" -->
package ${packageName};

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
<#list applicationImports as applicationImport>
import ${applicationImport};
</#list>

@Service
public class AppUserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(AppUserService.class);
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser registerUser(RegistrationRequest registrationRequest) {
        logger.info("Creating user with username " + registrationRequest.getUsername());
        AppUser user = new AppUser();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setFirstName(registrationRequest.getFirstname());
        user.setLastName(registrationRequest.getLastname());
        user.setEmail(registrationRequest.getEmail());
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("No user found with username " + username);
        }
            return createSpringUser(userOptional.get());
    }

    private User createSpringUser(AppUser user) {
        return new User(
            user.getUsername(),
            user.getPassword(),
            getAuthorities(user.getRoles())
        );
    }

    private List<GrantedAuthority> getAuthorities(List<Role> roles) {
        return roles.stream().map(Role::getAuthority).collect(Collectors.toList());
    }

}
