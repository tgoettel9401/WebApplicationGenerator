<#-- Variables of DataModel (for autocompletion of IDE)-->
<#-- @ftlvariable name="tableName" type="java.lang.String" -->
<#-- @ftlvariable name="className" type="java.lang.String" -->
<#-- @ftlvariable name="classNamePlural" type="java.lang.String" -->
<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="imports" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="entityName" type="java.lang.String" -->
<#-- @ftlvariable name="attributes" type="java.util.Set<org.dhbw.webapplicationgenerator.webclient.request.EntityAttribute>" -->
<#-- @ftlvariable name="relations" type="java.util.Set<org.dhbw.webapplicationgenerator.webclient.request.EntityRelation>" -->
package ${packageName};

<#-- Imports -->
<#list imports as import>
import ${import};
</#list>

<#-- Class header -->
@Table(name = "${tableName}")
@Entity
public class ${className} implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

<#-- Attributes -->
<#list attributes as attribute>
    @Column(name = "${attribute.getColumnName()}")
    <#if attribute.getDataType() == "LocalDate">
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    </#if>
    private ${attribute.getDataType()} ${attribute.getName()};

</#list>
<#-- Relation-Attributes -->
<#list relations as relation>
    <#if relation.getRelationType() == "ONE_TO_ONE">
        <#if relation.isOwning()>
    @OneToOne
    @JoinColumn(
        name = "${relation.getEntityName()}_id",
        referencedColumnName = "id"
    )
        <#else>
    @OneToOne(mappedBy="${entityName}")
        </#if>
    private ${relation.getEntityClassName()} ${relation.getEntityName()};
    <#elseif relation.getRelationType() == "ONE_TO_MANY">
    @OneToMany(mappedBy = "${entityName}")
    private List<${relation.getEntityClassName()}> ${relation.getEntityNamePlural()} = new ArrayList<>();
    <#elseif relation.getRelationType() == "MANY_TO_ONE">
    @ManyToOne
    private ${relation.getEntityClassName()} ${relation.getEntityName()};
    <#elseif relation.getRelationType() == "MANY_TO_MANY">
    @ManyToMany
    @JoinTable(
        name = "${relation.getJoinTable()}",
        joinColumns = @JoinColumn(name = "${entityName}_id"),
        inverseJoinColumns = @JoinColumn(name = "${relation.getEntityName()}_id")
    )
    private List<${relation.getEntityClassName()}> ${relation.getEntityNamePlural()} = new ArrayList<>();
    </#if>

<#-- Getter and Setter for ID attribute -->
</#list>
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

<#-- Generate Getters and Setters for all other attributes -->
<#list attributes as attribute>
    public ${attribute.getDataType()} get${attribute.getTitle()}() {
        return ${attribute.getName()};
    }

    public void set${attribute.getTitle()}(${attribute.getDataType()} ${attribute.getName()}) {
        this.${attribute.getName()} = ${attribute.getName()};
    }

</#list>
<#-- Generate Relations with their necessary Getters and Setters -->
<#list relations as relation>
    <#-- Getter -->
    <#if relation.getRelationType() == "ONE_TO_ONE" || relation.getRelationType() == "MANY_TO_ONE">
    public ${relation.getEntityClassName()} get${relation.getEntityClassName()}() {
        return ${relation.getEntityName()};
    }
    <#else>
    public List<${relation.getEntityClassName()}> get${relation.getEntityClassNamePlural()}() {
        return ${relation.getEntityNamePlural()};
    }
    </#if>

    <#-- Setter -->
    <#if relation.getRelationType() == "ONE_TO_ONE" || relation.getRelationType() == "MANY_TO_ONE">
    public void set${relation.getEntityClassName()}(${relation.getEntityClassName()} ${relation.getEntityName()}) {
        this.${relation.getEntityName()} = ${relation.getEntityName()};
    }

    <#else><#-- ONE_TO_MANY or MANY_TO_MANY -->
    public void set${relation.getEntityClassNamePlural()}(List<${relation.getEntityClassName()}> ${relation.getEntityNamePlural()}) {
        this.${relation.getEntityNamePlural()} = ${relation.getEntityNamePlural()};
    }
    </#if>
</#list>

<#-- Remove Relations -->
    @PreRemove
    public void removeRelations() {
    <#list relations as relation>
        <#if relation.getRelationType() == "ONE_TO_MANY">
        for(${relation.getEntityClassName()} ${relation.getEntityName()} : this.get${relation.getEntityClassNamePlural()}()) {
            ${relation.getEntityName()}.set${className}(null);
        }
        <#elseif relation.getRelationType() == "MANY_TO_MANY">
        for(${relation.getEntityClassName()} ${relation.getEntityName()} : this.get${relation.getEntityClassNamePlural()}()) {
            ${relation.getEntityName()}.get${classNamePlural}().remove(this);
        }
        <#elseif relation.getRelationType() == "MANY_TO_ONE">
        if(${relation.getEntityName()} != null) {
            ${relation.getEntityName()}.get${classNamePlural}().remove(this);
        }
        <#else> <#-- ONE_TO_ONE -->
        if(${relation.getEntityName()} != null) {
            ${relation.getEntityName()}.set${className}(null);
        }
        </#if>
    </#list>
    }
}
