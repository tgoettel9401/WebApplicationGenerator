<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="userTableName" type="java.lang.String" -->
package ${packageName};

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "${userTableName}")
public class AppUser {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)private Long id;

    private String username;
    private String password;

    private String email;
    private String firstName;
    private String lastName;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(
        name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
            name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Role> getRoles() {
        return roles;
        }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
