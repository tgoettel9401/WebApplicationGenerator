<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="roleEntityImport" type="java.lang.String" -->
package ${packageName};

import ${roleEntityImport};
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}

