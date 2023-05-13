<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="applicationImports" type="java.util.List<java.lang.String>" -->
package ${packageName};

<#list applicationImports as applicationImport>
import ${applicationImport};
</#list>
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
public class UserDataInitializer implements ApplicationRunner {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataInitializer(AppUserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        adminRole.setAdmin(true);
        adminRole = roleRepository.save(adminRole);

        AppUser user = new AppUser();
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setPassword(passwordEncoder.encode("secret"));
        user.setUsername("admin");
        user.setEmail("admin@data.com");
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);
    }
}
