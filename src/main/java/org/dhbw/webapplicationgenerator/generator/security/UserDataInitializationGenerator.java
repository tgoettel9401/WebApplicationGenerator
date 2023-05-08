package org.dhbw.webapplicationgenerator.generator.security;

import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
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

        ProjectDirectory resourcesDir = getMainProjectDirectoryOld(project, request);

        try {
            createUserDataInitializer(request, resourcesDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    public Project create(Project project, ProjectRequest request, ProjectDirectory parent) {

        try {
            createUserDataInitializer(request, parent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
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
            printWriter.println("import " + packageNameEntities + ".AppUser;");
            printWriter.println("import " + packageNameRepositories + ".RoleRepository;");
            printWriter.println("import " + packageNameRepositories + ".AppUserRepository;");

            printWriter.println("import org.springframework.boot.ApplicationArguments;");
            printWriter.println("import org.springframework.boot.ApplicationRunner;");
            printWriter.println("import org.springframework.security.crypto.password.PasswordEncoder;");
            printWriter.println("import org.springframework.stereotype.Component;");
            printWriter.println("import java.util.Arrays;");
            printWriter.println();


            printWriter.println("@Component");
            printWriter.println("public class UserDataInitializer implements ApplicationRunner {");
            printWriter.println();
            printWriter.println("private final AppUserRepository userRepository;");
            printWriter.println("private final RoleRepository roleRepository;");
            printWriter.println("private final PasswordEncoder passwordEncoder;");
            printWriter.println();

            printWriter.println("public UserDataInitializer(AppUserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {");
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
            printWriter.println("AppUser user = new AppUser();");
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

    // TODO: Migrate to Freemarker
    private void createUserDataInitializer(ProjectRequest request, ProjectDirectory directory) throws IOException {

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
            printWriter.println("import " + packageNameEntities + ".AppUser;");
            printWriter.println("import " + packageNameRepositories + ".RoleRepository;");
            printWriter.println("import " + packageNameRepositories + ".AppUserRepository;");

            printWriter.println("import org.springframework.boot.ApplicationArguments;");
            printWriter.println("import org.springframework.boot.ApplicationRunner;");
            printWriter.println("import org.springframework.security.crypto.password.PasswordEncoder;");
            printWriter.println("import org.springframework.stereotype.Component;");
            printWriter.println("import java.util.Arrays;");
            printWriter.println();


            printWriter.println("@Component");
            printWriter.println("public class UserDataInitializer implements ApplicationRunner {");
            printWriter.println();
            printWriter.println("private final AppUserRepository userRepository;");
            printWriter.println("private final RoleRepository roleRepository;");
            printWriter.println("private final PasswordEncoder passwordEncoder;");
            printWriter.println();

            printWriter.println("public UserDataInitializer(AppUserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {");
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
            printWriter.println("AppUser user = new AppUser();");
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

}
