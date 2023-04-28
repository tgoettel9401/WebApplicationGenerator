<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="imports" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="entityClassName" type="java.lang.String" -->
<#-- @ftlvariable name="relationsToOne" type="java.util.List<org.dhbw.webapplicationgenerator.webclient.request.EntityRelation>" -->


package ${packageName};

<#list imports as import>
import ${import};
</#list>

public interface ${entityClassName}Repository extends JpaRepository<${entityClassName}, Long> {
    <#list relationsToOne as relation>
    List<${entityClassName}> findBy${relation.getEntityClassName()}Id(Long ${relation.getEntityName()}Id);
    </#list>
}
