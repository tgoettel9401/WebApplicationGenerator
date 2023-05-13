<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="appUserEntityImport" type="java.lang.String" -->
package ${packageName};

import ${appUserEntityImport};
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}

