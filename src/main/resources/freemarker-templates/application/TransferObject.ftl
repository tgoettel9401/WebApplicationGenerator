<#-- Variables of DataModel (for autocompletion of IDE)-->
<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="imports" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="className" type="java.lang.String" -->
<#-- @ftlvariable name="attributes" type="java.util.List<org.dhbw.webapplicationgenerator.model.request.datamodel.Attribute>" -->
<#-- @ftlvariable name="relations" type="java.util.List<org.dhbw.webapplicationgenerator.model.request.datamodel.EntityRelation>" -->

<#-- Package -->
package ${packageName};

<#-- Imports -->
<#list imports as import>
import ${import};
</#list>

<#-- TransferObject Class -->
public class ${className}Request implements Serializable {

    <#-- Attributes -->
    private Long id;

    <#list attributes as attribute>
        <#-- LocalDate attributes need a DateTimeFormat -->
        <#if attribute.getDataType() == "LOCAL_DATE">
    @DateTimeFormat(pattern = "yyyy-MM-dd")
        </#if>
    private ${attribute.getDataType().getName()} ${attribute.getName()};

    </#list>
    <#-- Add either the id (toOne) or the ids (toMany) of the relation-entity -->
    <#list relations as relation>
        <#if relation.getRelationType().isToMany()>
    private List${"<Long>"} ${relation.getEntityName()}Ids = new ArrayList<>();
        <#else>
    private Long ${relation.getEntityName()}Id;
        </#if>

    </#list>
    <#-- Getter and Setter for id-attribute -->
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    <#-- Getter and Setter for other attributes -->
    <#list attributes as attribute>
    public ${attribute.getDataType().getName()} get${attribute.getTitle()}() {
        return ${attribute.getName()};
    }

    public void set${attribute.getTitle()}(${attribute.getDataType().getName()} ${attribute.getName()}) {
        this.${attribute.getName()} = ${attribute.getName()};
    }

    </#list>
    <#-- Getter and Setter for relation attributes -->
    <#list relations as relation>
        <#-- Getter first -->
        <#-- For toMany relations the list of ids is returned in the Getter -->
        <#if relation.getRelationType().isToMany()>
    public List${"<Long>"} get${relation.getEntityClassName()}Ids() {
        return ${relation.getEntityName()}Ids;
    }
        <#else>
        <#-- Otherwise (toOne) the id is returned in the Getter -->
    public Long get${relation.getEntityClassName()}Id() {
        return ${relation.getEntityName()}Id;
    }
        </#if>

        <#-- Setter -->
        <#-- For toMany relations the list of ids is updated in the Setter -->
        <#if relation.getRelationType().isToMany()>
    public void set${relation.getEntityClassName()}Ids(List${"<Long>"} ${relation.getEntityName()}Ids) {
        this.${relation.getEntityName()}Ids = ${relation.getEntityName()}Ids;
    }
        <#else>
        <#-- Otherwise (toOne) only the id is updated in the Setter-->
    public void set${relation.getEntityClassName()}Id(Long ${relation.getEntityName()}Id) {
        this.${relation.getEntityName()}Id = ${relation.getEntityName()}Id;
    }
        </#if>

    </#list>
}
