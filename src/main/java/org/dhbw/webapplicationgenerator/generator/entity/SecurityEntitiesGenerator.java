package org.dhbw.webapplicationgenerator.generator.entity;

import lombok.AllArgsConstructor;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SecurityEntitiesGenerator extends FileFolderGenerator {

    private static final String TMP_PATH = ".tmp/";
    private static final String JAVA_CLASS_ENDING = ".java";

    private final PackageNameResolver packageNameResolver;

    public Project create(Project project, CreationRequest request) {

        ProjectDirectory artifactDir = getMainProjectDirectory(project, request);
        try {
            create(request, artifactDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return project;
    }

    private void create(CreationRequest request, ProjectDirectory parent) throws IOException {

        ProjectDirectory domainDir = (ProjectDirectory) parent.getChildren().stream().filter(dir -> dir.getTitle().equals("domain")).collect(Collectors.toList());

        String packageName = packageNameResolver.resolveEntity(request);

        addFile(addUserEntity(packageName), domainDir);
        addFile(addRoleEntity(packageName), domainDir);

    }

    private File addUserEntity(String packageName) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "User" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageName + ";");
            printWriter.println();

            printWriter.println("import javax.persistency.*;");
            printWriter.println("import java.util.List;");
            printWriter.println();

            printWriter.println("@Entity");
            printWriter.println("public class User {");
            printWriter.println();

            printWriter.println("@Id");
            printWriter.println("@GeneratedValue");
            printWriter.print("@Column(name = \"id\", nullable = false)");
            printWriter.println("private Long id;");
            printWriter.println();

            printWriter.println("private String username;");
            printWriter.println("private String password;");
            printWriter.println();

            printWriter.println("private String email;");
            printWriter.println("private String firstName;");
            printWriter.println("private String lastName;");
            printWriter.println();

            printWriter.println("@OneToMany(fetch = FetchType.EAGER)");
            printWriter.println("@JoinTable(");
            printWriter.println("name = \"users_roles\",");
            printWriter.println("joinColumns = @JoinColumn(");
            printWriter.println("name = \"user_id\", referencedColumnName = \"id\"),");
            printWriter.println("inverseJoinColumns = @JoinColumn(");
            printWriter.println("name = \"role_id\", referencedColumName = \"id\"))");
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

            printWriter.println("public List<Rol> getRoles() {");
            printWriter.println("return roles;");
            printWriter.println("}");
            printWriter.println();

            printWriter.println("public void setRoles(List<Role> roles) {");
            printWriter.println("this.roles = roles;");
            printWriter.println("}");
            printWriter.println();

        }
        return file;
    }

    private File addRoleEntity(String packageName) throws IOException {
        File file = new File(String.valueOf(Files.createFile(Path.of(TMP_PATH + "Role" + JAVA_CLASS_ENDING))));
        FileWriter fileWriter = new FileWriter(file);
        try (PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println("package " + packageName + ";");
            printWriter.println();

            printWriter.println("import org.springframework.security.core.GrantedAuthority;");
            printWriter.println("import org.springframework.security.core.authority.SimpleGrantedAuthority;");


            printWriter.println("import javax.persistency.*;");
            printWriter.println("import java.util.Collection;");
            printWriter.println();

            printWriter.println("@Entity");
            printWriter.println("public class User {");
            printWriter.println();

            printWriter.println("@Id");
            printWriter.println("@GeneratedValue");
            printWriter.print("@Column(name = \"id\", nullable = false)");
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

            printWriter.println("public Long getId() {");
            printWriter.println("return id;");
            printWriter.println("}");
            printWriter.println();

            printWriter.println("public void setId(Long id) {");
            printWriter.println("this.id = id;");
            printWriter.println("}");
            printWriter.println();

            printWriter.println("public String getName() {");
            printWriter.println("return id;");
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
            printWriter.println();

        }
        return file;
    }

}
