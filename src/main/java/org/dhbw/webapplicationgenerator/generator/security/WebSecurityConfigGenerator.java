package org.dhbw.webapplicationgenerator.generator.security;

import org.dhbw.webapplicationgenerator.generator.baseproject.FileFolderGenerator;
import org.dhbw.webapplicationgenerator.generator.util.PackageNameResolver;
import org.dhbw.webapplicationgenerator.model.request.ProjectRequest;
import org.dhbw.webapplicationgenerator.model.response.Project;
import org.dhbw.webapplicationgenerator.model.response.ProjectDirectory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class WebSecurityConfigGenerator extends FileFolderGenerator {

    private static final String TMP_PATH = ".tmp/";
    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;

    public WebSecurityConfigGenerator(PackageNameResolver packageNameResolver) {
        this.packageNameResolver = packageNameResolver;
    }

    public Project create(Project project, ProjectRequest request, ProjectDirectory parent) {

        try {
            createSecurityConfg(request, parent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    // TODO: Migrate to Freemarker
    private void createSecurityConfg(ProjectRequest request, ProjectDirectory directory) throws IOException {

        ProjectDirectory configDir = (ProjectDirectory) directory.getChildren().stream()
                .filter(dir -> dir.getTitle().equals("config"))
                .findFirst()
                .orElseThrow(IOException::new);

        String packageName = packageNameResolver.resolveConfig(request);

        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "WebSecurityConfig" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);

        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageName + ";");
            printWriter.println();
            printWriter.println("import org.springframework.context.annotation.Bean;");
            printWriter.println("import org.springframework.context.annotation.Configuration;");
            printWriter.println("import org.springframework.security.config.annotation.web.builders.HttpSecurity;");
            printWriter.println("import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;");
            printWriter.println("import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;");
            printWriter.println("import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;");
            printWriter.println("import org.springframework.security.crypto.password.PasswordEncoder;");
            printWriter.println();
            printWriter.println("@Configuration");
            printWriter.println("@EnableWebSecurity");
            printWriter.println("public class WebSecurityConfig extends WebSecurityConfigurerAdapter {");
            printWriter.println("@Override");
            printWriter.println("protected void configure(HttpSecurity http) throws Exception {");
            printWriter.println("http.authorizeRequests()");
            printWriter.println(".antMatchers(\"/users/register\").permitAll()");
            printWriter.println(".anyRequest().authenticated()");
            printWriter.println(".and()");
            printWriter.println(".formLogin()");
            printWriter.println(".loginPage(\"/login\")");
            printWriter.println(".permitAll()");
            printWriter.println(".and()");
            printWriter.println(".logout()");
            printWriter.println(".permitAll();");
            printWriter.println("}");
            printWriter.println("@Bean");
            printWriter.println("public PasswordEncoder encoder() {");
            printWriter.println("return new BCryptPasswordEncoder();");
            printWriter.println("}");
            printWriter.println("}");
            printWriter.println();
        }

        addFile(file, configDir);

    }

}
