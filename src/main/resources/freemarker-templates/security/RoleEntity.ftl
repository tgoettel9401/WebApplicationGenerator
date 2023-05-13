<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="roleTableName" type="java.lang.String" -->
package ${packageName};

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "${roleTableName}")
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)private Long id;

    private String name;
    private boolean admin;

    @ManyToMany(mappedBy = "roles")
    private Collection<AppUser> users;

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(name);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<AppUser> getUsers() {
        return users;
    }

    public void setUsers(Collection<AppUser> users) {
        this.users = users;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

}
