package org.dhbw.webapplicationgenerator.generator.frontend;

import org.dhbw.webapplicationgenerator.generator.Project;
import org.dhbw.webapplicationgenerator.generator.base_project.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.model.ProjectDirectory;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.webclient.request.CreationRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class UserDataInitializationGenerator extends FileFolderGenerator {

    private static final String TMP_PATH = ".tmp/";
    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;

    public UserDataInitializationGenerator(PackageNameResolver packageNameResolver) {
        this.packageNameResolver = packageNameResolver;
    }

    public Project create(Project project, CreationRequest request) {

        ProjectDirectory resourcesDir = getMainProjectDirectory(project, request);

        try {
            createUserDataInitializer(request, resourcesDir);
            createUserDataEntities(request, resourcesDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    private void createUserDataEntities(CreationRequest request, ProjectDirectory directory) throws IOException {

        ProjectDirectory entityDir = (ProjectDirectory) directory.getChildren().stream()
                .filter(dir -> dir.getTitle().equals("domain"))
                .findFirst()
                .orElseThrow(IOException::new);

        addFile(createUserEntity(request), entityDir);
        addFile(createRoleEntity(request), entityDir);

    }

    private void createUserDataInitializer(CreationRequest request, ProjectDirectory directory) throws IOException {

        ProjectDirectory configDir = (ProjectDirectory) directory.getChildren().stream()
                .filter(dir -> dir.getTitle().equals("config"))
                .findFirst()
                .orElseThrow(IOException::new);

        String packageName = packageNameResolver.resolveConfig(request);
        String packageNameEntities = packageNameResolver.resolveEntity(request);
        String packageNameRepositories = packageNameResolver.resolveRepository(request);

        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "UserDataInitializer" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);

        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageName + ";");
            printWriter.println();
            printWriter.println("import " + packageNameEntities + ".Role;");
            printWriter.println("import " + packageNameEntities + ".User;");
            printWriter.println("import " + packageNameRepositories + ".RoleRepository;");
            printWriter.println("import " + packageNameRepositories + ".UserRepository;");

            printWriter.println("import org.springframework.boot.ApplicationArguments;");
            printWriter.println("import org.springframework.boot.ApplicationRunner;");
            printWriter.println("import org.springframework.security.crypto.password.PasswordEncoder;");
            printWriter.println("import org.springframework.stereotype.Component;");
            printWriter.println("import java.util.Arrays;");
            printWriter.println();


            printWriter.println("@Component");
            printWriter.println("public class UserDataInitializer implements ApplicationRunner {");
            printWriter.println();
            printWriter.println("private final UserRepository userRepository;");
            printWriter.println("private final RoleRepository roleRepository;");
            printWriter.println("private final PasswordEncoder passwordEncoder;");
            printWriter.println();

            printWriter.println("public UserDataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {");
            printWriter.println("this.userRepository = userRepository;");
            printWriter.println("this.roleRepository = roleRepository;");
            printWriter.println("this.passwordEncoder = passwordEncoder;");
            printWriter.println("}");
            printWriter.println();

            printWriter.println("@Override");
            printWriter.println("public void run(ApplicationArguments args) {");
            printWriter.println("Role adminRole = new Role();");
            printWriter.println("adminRole.setName(\"ROLE_ADMIN\");");
            printWriter.println("adminRole.setAdmin(true);");
            printWriter.println("adminRole = roleRepository.save(adminRole);");
            printWriter.println("User user = new User();");
            printWriter.println("user.setFirstName(\"Admin\");");
            printWriter.println("user.setLastName(\"User\");");
            printWriter.println("user.setPassword(passwordEncoder.encode(\"secret\"));");
            printWriter.println("user.setUsername(\"admin\");");
            printWriter.println("user.setEmail(\"admin@data.com\");");
            printWriter.println("user.setRoles(Arrays.asList(adminRole));");
            printWriter.println("userRepository.save(user);");
            printWriter.println("}");
            printWriter.println("}");

        }

        addFile(file, configDir);

    }

    private File createRoleEntity(CreationRequest request) throws IOException {

        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "Role" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);

        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {

            String packageName = packageNameResolver.resolveEntity(request);

            printWriter.println("package " + packageName + ";");
            printWriter.println();
            printWriter.println("import org.springframework.security.core.GrantedAuthority;");
            printWriter.println("import org.springframework.security.core.authority.SimpleGrantedAuthority;");
            printWriter.println("import javax.persistence.*;");
            printWriter.println("import java.util.Collection;");
            printWriter.println();

            printWriter.println("@Entity");
            printWriter.println("public class Role {");
            printWriter.println();
            printWriter.println("@Id");
            printWriter.println("@GeneratedValue");
            printWriter.println("@Column(name = \"id\", nullable = false)");
            printWriter.println("private Long id;");
            printWriter.println();
            printWriter.println("private String name;");
            printWriter.println("private boolean admin;");
            printWriter.println();
            printWriter.println("@ManyToMany(mappedBy = \"roles\")");
            printWriter.println("private Collection<User> users;");
            printWriter.println();
            printWriter.println("public GrantedAuthority getAuthority() {");
            printWriter.println("return new SimpleGrantedAuthority(name);");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public Long getId() {");
            printWriter.println("return id;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public void setId(Long id) {");
            printWriter.println("this.id = id;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public String getName() {");
            printWriter.println("return name;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public void setName(String name) {");
            printWriter.println("this.name = name;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public Collection<User> getUsers() {");
            printWriter.println("return users;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public void setUsers(Collection<User> users) {");
            printWriter.println("this.users = users;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public boolean isAdmin() {");
            printWriter.println("return admin;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public void setAdmin(boolean admin) {");
            printWriter.println("this.admin = admin;");
            printWriter.println("}");
            printWriter.println("}");
        }

        return file;

    }

    private File createUserEntity(CreationRequest request) throws IOException {

        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "User" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);

        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {

            String packageName = packageNameResolver.resolveEntity(request);

            printWriter.println("package " + packageName + ";");
            printWriter.println();
            printWriter.println("import javax.persistence.*;");
            printWriter.println("import java.util.List;");
            printWriter.println();

            printWriter.println("@Entity");
            printWriter.println("public class User {");
            printWriter.println();
            printWriter.println("@Id");
            printWriter.println("@GeneratedValue");
            printWriter.println("@Column(name = \"id\", nullable = false)");
            printWriter.println("private Long id;");
            printWriter.println();
            printWriter.println("private String username;");
            printWriter.println("private String password;");
            printWriter.println();
            printWriter.println("private String email;");
            printWriter.println("private String firstName;");
            printWriter.println("private String lastName;");
            printWriter.println();
            printWriter.println("@ManyToMany(fetch = FetchType.EAGER)");
            printWriter.println("@JoinTable(");
            printWriter.println("name = \"users_roles\",");
            printWriter.println("joinColumns = @JoinColumn(name = \"user_id\", referencedColumnName = \"id\"),");
            printWriter.println("inverseJoinColumns = @JoinColumn(name = \"role_id\", referencedColumnName = \"id\"))");
            printWriter.println();
            printWriter.println("private List<Role> roles;");
            printWriter.println();
            printWriter.println("public Long getId() {");
            printWriter.println("return id;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public void setId(Long id) {");
            printWriter.println("this.id = id;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public String getUsername() {");
            printWriter.println("return username;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public void setUsername(String username) {");
            printWriter.println("this.username = username;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public String getPassword() {");
            printWriter.println("return password;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public void setPassword(String password) {");
            printWriter.println("this.password = password;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public String getEmail() {");
            printWriter.println("return email;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public void setEmail(String email) {");
            printWriter.println("this.email = email;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public String getFirstName() {");
            printWriter.println("return firstName;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public void setFirstName(String firstName) {");
            printWriter.println("this.firstName = firstName;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public String getLastName() {");
            printWriter.println("return lastName;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public void setLastName(String lastName) {");
            printWriter.println("this.lastName = lastName;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public List<Role> getRoles() {");
            printWriter.println("return roles;");
            printWriter.println("}");
            printWriter.println();
            printWriter.println("public void setRoles(List<Role> roles) {");
            printWriter.println("this.roles = roles;");
            printWriter.println("}");
            printWriter.println("}");
        }

        return file;

    }

}
