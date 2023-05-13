package ${packageName};

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
<#list applicationImports as applicationImport>
    import ${applicationImport};
</#list>

@Controller
@RequestMapping("/users")
public class AppUserController {

    private final AppUserService userService;
    private final AppUserRepository userRepository;

    public AppUserController(AppUserService userService, AppUserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("register")
        public String showRegister() {
        return "register";
    }

    @PostMapping("register")
    public RedirectView saveRegister(@ModelAttribute("user") RegistrationRequest registrationRequest) {
        AppUser user = userService.registerUser(registrationRequest);
        return new RedirectView("/");
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String index(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }
}
