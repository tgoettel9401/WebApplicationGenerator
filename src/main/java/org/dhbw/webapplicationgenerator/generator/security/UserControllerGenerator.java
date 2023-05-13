package org.dhbw.webapplicationgenerator.generator.security;

import lombok.AllArgsConstructor;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.util.ResourceFileHelper;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserControllerGenerator extends FileFolderGenerator {

    // TODO: Properly implement with Freemarker rather than PrintWriter!!

    private static final String TMP_PATH = ".tmp/";
    private static final String JAVA_CLASS_ENDING = ".java";

    private final ResourceFileHelper resourceFileHelper;
    private final PackageNameResolver packageNameResolver;

    public Project create(Project project, CreationRequest request) {

        ProjectDirectory controllerDir = getControllerDirectory(project, request);
        ProjectDirectory transferObjectDir = getTransferObjectDirectory(project, request);
        ProjectDirectory serviceDir = addDirectory("service", Optional.of(getMainProjectDirectoryOld(project, request)));

        try {
            addFile(createUserController(request), controllerDir);
            addFile(createRegistrationRequest(request), transferObjectDir);
            addFile(createUserService(request), serviceDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    public Project create(Project project, ProjectRequest request) {

        ProjectDirectory mainSourceDir = getMainProjectDirectory(project, request);
        ProjectDirectory controllerDir = addDirectory("controller", Optional.of(mainSourceDir));
        ProjectDirectory transferObjectDir = getTransferObjectDirectory(project, request);
        ProjectDirectory serviceDir = addDirectory("service", Optional.of(getMainProjectDirectory(project, request)));

        try {
            addFile(createUserController(request), controllerDir);
            addFile(createRegistrationRequest(request), transferObjectDir);
            addFile(createUserService(request), serviceDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    private File createUserController(CreationRequest request) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "AppUserController" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageNameResolver.resolveController(request) + ";");
            printWriter.println();

            // Basic Imports
            printWriter.println("import org.springframework.security.access.prepost.PreAuthorize;");
            printWriter.println("import org.springframework.stereotype.Controller;");
            printWriter.println("import org.springframework.ui.Model;");
            printWriter.println("import org.springframework.web.bind.annotation.GetMapping;");
            printWriter.println("import org.springframework.web.bind.annotation.ModelAttribute;");
            printWriter.println("import org.springframework.web.bind.annotation.PostMapping;");
            printWriter.println("import org.springframework.web.bind.annotation.RequestMapping;");
            printWriter.println("import org.springframework.web.servlet.view.RedirectView;");

            // Application Imports
            printWriter.println("import " + packageNameResolver.resolveService(request) + ".AppUserService;");
            printWriter.println("import " + packageNameResolver.resolveRepository(request) + ".AppUserRepository;");
            printWriter.println("import " + packageNameResolver.resolveEntity(request) + ".AppUser;");
            printWriter.println("import " + packageNameResolver.resolveTransferObjects(request) + ".RegistrationRequest;");

            printWriter.println("");

            // Annotations
            printWriter.println("@Controller");
            printWriter.println("@RequestMapping(\"/users\")");

            // Class Header & Attributes
            printWriter.println("public class AppUserController {");
            printWriter.println("");

            // Attributes
            printWriter.println("private final AppUserService userService;");
            printWriter.println("private final AppUserRepository userRepository;");

            // Constructor
            printWriter.println("public AppUserController(AppUserService userService, AppUserRepository userRepository) {");
            printWriter.println("this.userService = userService;");
            printWriter.println("this.userRepository = userRepository;");
            printWriter.println("}");

            // Registration Page
            printWriter.println("@GetMapping(\"register\")");
            printWriter.println("public String showRegister() {");
            printWriter.println("return \"register\";");
            printWriter.println("}");

            // Saving Registration
            printWriter.println("@PostMapping(\"register\")");
            printWriter.println("public RedirectView saveRegister(@ModelAttribute(\"user\") RegistrationRequest registrationRequest) {");
            printWriter.println("AppUser user = userService.registerUser(registrationRequest);");
            printWriter.println("return new RedirectView(\"/\");");
            printWriter.println("}");

            // Overview Page for Users
            printWriter.println("@GetMapping()");
            printWriter.println("@PreAuthorize(\"hasRole('ROLE_ADMIN')\")");
            printWriter.println("public String index(Model model) {");
            printWriter.println("model.addAttribute(\"users\", userRepository.findAll());");
            printWriter.println("return \"users\";");
            printWriter.println("}");

            // Close the final curly bracket
            printWriter.println("}");

        }

        return file;
    }

    // TODO: Migrate to Freemarker
    private File createUserController(ProjectRequest request) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "AppUserController" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageNameResolver.resolveController(request) + ";");
            printWriter.println();

            // Basic Imports
            printWriter.println("import org.springframework.security.access.prepost.PreAuthorize;");
            printWriter.println("import org.springframework.stereotype.Controller;");
            printWriter.println("import org.springframework.ui.Model;");
            printWriter.println("import org.springframework.web.bind.annotation.GetMapping;");
            printWriter.println("import org.springframework.web.bind.annotation.ModelAttribute;");
            printWriter.println("import org.springframework.web.bind.annotation.PostMapping;");
            printWriter.println("import org.springframework.web.bind.annotation.RequestMapping;");
            printWriter.println("import org.springframework.web.servlet.view.RedirectView;");

            // Application Imports
            printWriter.println("import " + packageNameResolver.resolveService(request) + ".AppUserService;");
            printWriter.println("import " + packageNameResolver.resolveRepository(request) + ".AppUserRepository;");
            printWriter.println("import " + packageNameResolver.resolveEntity(request) + ".AppUser;");
            printWriter.println("import " + packageNameResolver.resolveTransferObjects(request) + ".RegistrationRequest;");

            printWriter.println("");

            // Annotations
            printWriter.println("@Controller");
            printWriter.println("@RequestMapping(\"/users\")");

            // Class Header & Attributes
            printWriter.println("public class AppUserController {");
            printWriter.println("");

            // Attributes
            printWriter.println("private final AppUserService userService;");
            printWriter.println("private final AppUserRepository userRepository;");

            // Constructor
            printWriter.println("public AppUserController(AppUserService userService, AppUserRepository userRepository) {");
            printWriter.println("this.userService = userService;");
            printWriter.println("this.userRepository = userRepository;");
            printWriter.println("}");

            // Registration Page
            printWriter.println("@GetMapping(\"register\")");
            printWriter.println("public String showRegister() {");
            printWriter.println("return \"register\";");
            printWriter.println("}");

            // Saving Registration
            printWriter.println("@PostMapping(\"register\")");
            printWriter.println("public RedirectView saveRegister(@ModelAttribute(\"user\") RegistrationRequest registrationRequest) {");
            printWriter.println("AppUser user = userService.registerUser(registrationRequest);");
            printWriter.println("return new RedirectView(\"/\");");
            printWriter.println("}");

            // Overview Page for Users
            printWriter.println("@GetMapping()");
            printWriter.println("@PreAuthorize(\"hasRole('ROLE_ADMIN')\")");
            printWriter.println("public String index(Model model) {");
            printWriter.println("model.addAttribute(\"users\", userRepository.findAll());");
            printWriter.println("return \"users\";");
            printWriter.println("}");

            // Close the final curly bracket
            printWriter.println("}");

        }

        return file;
    }

    private File createRegistrationRequest(CreationRequest request) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "RegistrationRequest" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageNameResolver.resolveTransferObjects(request) + ";");
            printWriter.println();

            // Class Header & Attributes
            printWriter.println("public class RegistrationRequest {");
            printWriter.println("");

            // Attributes
            printWriter.println("private String firstName;");
            printWriter.println("private String lastName;");
            printWriter.println("private String username;");
            printWriter.println("private String email;");
            printWriter.println("private String password;");

            // Getter and Setter
            createGetterAndSetter("firstName", printWriter);
            createGetterAndSetter("lastName", printWriter);
            createGetterAndSetter("username", printWriter);
            createGetterAndSetter("email", printWriter);
            createGetterAndSetter("password", printWriter);

            // Close the final curly bracket
            printWriter.println("}");

        }

        return file;
    }

    private File createRegistrationRequest(ProjectRequest request) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "RegistrationRequest" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageNameResolver.resolveTransferObjects(request) + ";");
            printWriter.println();

            // Class Header & Attributes
            printWriter.println("public class RegistrationRequest {");
            printWriter.println("");

            // Attributes
            printWriter.println("private String firstName;");
            printWriter.println("private String lastName;");
            printWriter.println("private String username;");
            printWriter.println("private String email;");
            printWriter.println("private String password;");

            // Getter and Setter
            createGetterAndSetter("firstName", printWriter);
            createGetterAndSetter("lastName", printWriter);
            createGetterAndSetter("username", printWriter);
            createGetterAndSetter("email", printWriter);
            createGetterAndSetter("password", printWriter);

            // Close the final curly bracket
            printWriter.println("}");

        }

        return file;
    }

    private File createUserService(CreationRequest request) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "AppUserService" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageNameResolver.resolveService(request) + ";");
            printWriter.println();

            // Basic Imports
            printWriter.println("import org.slf4j.Logger;");
            printWriter.println("import org.slf4j.LoggerFactory;");
            printWriter.println("import org.springframework.security.core.GrantedAuthority;");
            printWriter.println("import org.springframework.security.core.userdetails.User;");
            printWriter.println("import org.springframework.security.core.userdetails.UserDetails;");
            printWriter.println("import org.springframework.security.core.userdetails.UserDetailsService;");
            printWriter.println("import org.springframework.security.core.userdetails.UsernameNotFoundException;");
            printWriter.println("import org.springframework.security.crypto.password.PasswordEncoder;");
            printWriter.println("import org.springframework.stereotype.Service;");
            printWriter.println("import java.util.List;");
            printWriter.println("import java.util.Optional;");
            printWriter.println("import java.util.stream.Collectors;");

            // Application Imports
            printWriter.println("import " + packageNameResolver.resolveTransferObjects(request) + ".RegistrationRequest;");
            printWriter.println("import " + packageNameResolver.resolveEntity(request) + ".Role;");
            printWriter.println("import " + packageNameResolver.resolveEntity(request) + ".AppUser;");
            printWriter.println("import " + packageNameResolver.resolveRepository(request) + ".AppUserRepository;");

            printWriter.println("");

            // Annotations
            printWriter.println("@Service");

            // Class Header & Attributes
            printWriter.println("public class AppUserService implements UserDetailsService {");
            printWriter.println("");

            // Attributes
            printWriter.println("private final Logger logger = LoggerFactory.getLogger(AppUserService.class);");
            printWriter.println("private final AppUserRepository userRepository;");
            printWriter.println("private final PasswordEncoder passwordEncoder;");
            printWriter.println("");

            // Constructor
            printWriter.println("public AppUserService(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {");
            printWriter.println("this.userRepository = userRepository;");
            printWriter.println("this.passwordEncoder = passwordEncoder;");
            printWriter.println("}");
            printWriter.println("");

            // RegisterUser
            printWriter.println("public AppUser registerUser(RegistrationRequest registrationRequest) {");
            printWriter.println("logger.info(\"Creating user with username \" + registrationRequest.getUsername());");
            printWriter.println("AppUser user = new AppUser();");
            printWriter.println("user.setUsername(registrationRequest.getUsername());");
            printWriter.println("user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));");
            printWriter.println("user.setFirstName(registrationRequest.getFirstname());");
            printWriter.println("user.setLastName(registrationRequest.getLastname());");
            printWriter.println("user.setEmail(registrationRequest.getEmail());");
            printWriter.println("return userRepository.save(user);");
            printWriter.println("}");
            printWriter.println("");

            // LoadByUsername
            printWriter.println("@Override");
            printWriter.println("public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {");
            printWriter.println("Optional<AppUser> userOptional = userRepository.findByUsername(username);");
            printWriter.println("if (userOptional.isEmpty()) {");
            printWriter.println("throw new UsernameNotFoundException(\"No user found with username \" + username);");
            printWriter.println("}");
            printWriter.println("return createSpringUser(userOptional.get());");
            printWriter.println("}");
            printWriter.println("");

            // CreateSpringUser(AppUser user)
            printWriter.println("private User createSpringUser(AppUser user) {");
            printWriter.println("return new User(");
            printWriter.println("user.getUsername(),");
            printWriter.println("user.getPassword(),");
            printWriter.println("getAuthorities(user.getRoles())");
            printWriter.println(");");
            printWriter.println("}");
            printWriter.println("");

            // GetAuthorities(List<Role> roles)
            printWriter.println("private List<GrantedAuthority> getAuthorities(List<Role> roles) {");
            printWriter.println("return roles.stream().map(Role::getAuthority).collect(Collectors.toList());");
            printWriter.println("}");
            printWriter.println("");

            // Close the final curly bracket
            printWriter.println("}");
        }

        return file;

    }

    private File createUserService(ProjectRequest request) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "AppUserService" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageNameResolver.resolveService(request) + ";");
            printWriter.println();

            // Basic Imports
            printWriter.println("import org.slf4j.Logger;");
            printWriter.println("import org.slf4j.LoggerFactory;");
            printWriter.println("import org.springframework.security.core.GrantedAuthority;");
            printWriter.println("import org.springframework.security.core.userdetails.User;");
            printWriter.println("import org.springframework.security.core.userdetails.UserDetails;");
            printWriter.println("import org.springframework.security.core.userdetails.UserDetailsService;");
            printWriter.println("import org.springframework.security.core.userdetails.UsernameNotFoundException;");
            printWriter.println("import org.springframework.security.crypto.password.PasswordEncoder;");
            printWriter.println("import org.springframework.stereotype.Service;");
            printWriter.println("import java.util.List;");
            printWriter.println("import java.util.Optional;");
            printWriter.println("import java.util.stream.Collectors;");

            // Application Imports
            printWriter.println("import " + packageNameResolver.resolveTransferObjects(request) + ".RegistrationRequest;");
            printWriter.println("import " + packageNameResolver.resolveEntity(request) + ".Role;");
            printWriter.println("import " + packageNameResolver.resolveEntity(request) + ".AppUser;");
            printWriter.println("import " + packageNameResolver.resolveRepository(request) + ".AppUserRepository;");

            printWriter.println("");

            // Annotations
            printWriter.println("@Service");

            // Class Header & Attributes
            printWriter.println("public class AppUserService implements UserDetailsService {");
            printWriter.println("");

            // Attributes
            printWriter.println("private final Logger logger = LoggerFactory.getLogger(AppUserService.class);");
            printWriter.println("private final AppUserRepository userRepository;");
            printWriter.println("private final PasswordEncoder passwordEncoder;");
            printWriter.println("");

            // Constructor
            printWriter.println("public AppUserService(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {");
            printWriter.println("this.userRepository = userRepository;");
            printWriter.println("this.passwordEncoder = passwordEncoder;");
            printWriter.println("}");
            printWriter.println("");

            // RegisterUser
            printWriter.println("public AppUser registerUser(RegistrationRequest registrationRequest) {");
            printWriter.println("logger.info(\"Creating user with username \" + registrationRequest.getUsername());");
            printWriter.println("AppUser user = new AppUser();");
            printWriter.println("user.setUsername(registrationRequest.getUsername());");
            printWriter.println("user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));");
            printWriter.println("user.setFirstName(registrationRequest.getFirstname());");
            printWriter.println("user.setLastName(registrationRequest.getLastname());");
            printWriter.println("user.setEmail(registrationRequest.getEmail());");
            printWriter.println("return userRepository.save(user);");
            printWriter.println("}");
            printWriter.println("");

            // LoadByUsername
            printWriter.println("@Override");
            printWriter.println("public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {");
            printWriter.println("Optional<AppUser> userOptional = userRepository.findByUsername(username);");
            printWriter.println("if (userOptional.isEmpty()) {");
            printWriter.println("throw new UsernameNotFoundException(\"No user found with username \" + username);");
            printWriter.println("}");
            printWriter.println("return createSpringUser(userOptional.get());");
            printWriter.println("}");
            printWriter.println("");

            // CreateSpringUser(AppUser user)
            printWriter.println("private User createSpringUser(AppUser user) {");
            printWriter.println("return new User(");
            printWriter.println("user.getUsername(),");
            printWriter.println("user.getPassword(),");
            printWriter.println("getAuthorities(user.getRoles())");
            printWriter.println(");");
            printWriter.println("}");
            printWriter.println("");

            // GetAuthorities(List<Role> roles)
            printWriter.println("private List<GrantedAuthority> getAuthorities(List<Role> roles) {");
            printWriter.println("return roles.stream().map(Role::getAuthority).collect(Collectors.toList());");
            printWriter.println("}");
            printWriter.println("");

            // Close the final curly bracket
            printWriter.println("}");
        }

        return file;

    }

    private void createGetterAndSetter(String fieldName, PrintWriter printWriter) {
        createGetter(fieldName, printWriter);
        createSetter(fieldName, printWriter);
    }

    private void createGetter(String fieldName, PrintWriter printWriter) {
        printWriter.println("public String get" + capitalize(fieldName) + "(){");
        printWriter.println("return " + fieldName + ";");
        printWriter.println("}");
        printWriter.println();
    }

    private void createSetter(String fieldName, PrintWriter printWriter) {
        printWriter.println("public void set" + capitalize(fieldName) + "(String " + fieldName + ") {");
        printWriter.println("this." + fieldName + " = " + fieldName + ";");
        printWriter.println("}");
        printWriter.println();
    }

}
