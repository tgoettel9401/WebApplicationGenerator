<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="imports" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="requestPath" type="java.lang.String" -->
<#-- @ftlvariable name="controllerClassName" type="java.lang.String" -->
<#-- @ftlvariable name="repositoryClassName" type="java.lang.String" -->
<#-- @ftlvariable name="repositoryVariableName" type="java.lang.String" -->
<#-- @ftlvariable name="relations" type="java.util.List<org.dhbw.webapplicationgenerator.webclient.request.EntityRelation>" -->
<#-- @ftlvariable name="relationsToOne" type="java.util.List<org.dhbw.webapplicationgenerator.webclient.request.EntityRelation>" -->
<#-- @ftlvariable name="relationsToMany" type="java.util.List<org.dhbw.webapplicationgenerator.webclient.request.EntityRelation>" -->
<#-- @ftlvariable name="entityVariableName" type="java.lang.String" -->
<#-- @ftlvariable name="entityClassName" type="java.lang.String" -->
<#-- @ftlvariable name="entityVariableNamePlural" type="java.lang.String" -->
<#-- @ftlvariable name="entityClassNamePlural" type="java.lang.String" -->
<#-- @ftlvariable name="attributes" type="java.util.List<org.dhbw.webapplicationgenerator.webclient.request.EntityAttribute>" -->
package ${packageName};

<#list imports as import>
import ${import};
</#list>

@Controller
@RequestMapping("/${requestPath}")
public class ${controllerClassName} {
    <#-- Repository of the entity the controller belongs to -->
    private final ${repositoryClassName} ${repositoryVariableName};

    <#-- Repositories for all relations of the entity -->
    <#list relations as relation>
    private final ${relation.getRepositoryClassName()} ${relation.getRepositoryVariableName()};
    </#list>

    <#-- constructor for the controller -->
    public ${controllerClassName}(
        ${repositoryClassName} ${repositoryVariableName},
        <#list relations as relation>
        ${relation.getRepositoryClassName()} ${relation.getRepositoryVariableName()}<#if relation?has_next>,</#if>
        </#list>
    ) {
        this.${repositoryVariableName} = ${repositoryVariableName};
        <#list relations as relation>
        this.${relation.getRepositoryVariableName()} = ${relation.getRepositoryVariableName()};
        </#list>
    }

    <#-- Index View -->
    @GetMapping()
    public String index(Model model) {
        model.addAttribute("${entityVariableNamePlural}", ${repositoryVariableName}.findAll());
        model.addAttribute("title", "${entityClassNamePlural} - Index");
        return "${entityVariableNamePlural}";
    }

    <#-- Create View -->
    @GetMapping("/create")
    public String create(Model model) {
        ${entityClassName} ${entityVariableName} = new ${entityClassName}();
        model.addAttribute("${entityVariableName}", ${entityVariableName});
        model.addAttribute("title", "${entityClassNamePlural} - Create");
        <#-- For all relations, the relation data is added as well (for filling dropdown-options) -->
        <#list relations as relation>
        model.addAttribute("${relation.getEntityNamePlural()}", ${relation.getRepositoryVariableName()}.findAll());
        </#list>
        return "${entityVariableName}Details";
    }

    <#-- Update View -->
    @GetMapping("/edit/{id}")
    public String update(Model model, @PathVariable("id") Long id) throws NotFoundException {
        ${entityClassName} ${entityVariableName} = ${repositoryVariableName}.findById(id)
            .orElseThrow(() -> new NotFoundException("${entityClassName}", id));
        model.addAttribute("${entityVariableName}", ${entityVariableName});
        model.addAttribute("title", "${entityClassNamePlural} - Update");
        <#-- For all relations, the relation data is added as well (for filling dropdown-options) -->
        <#list relations as relation>
        model.addAttribute("${relation.getEntityNamePlural()}", ${relation.getRepositoryVariableName()}.findAll());
        </#list>
        return "${entityVariableName}Details";
    }

    <#-- Saving an entity -->
    @Transactional
    @PostMapping("save")
    public RedirectView save(@ModelAttribute ${entityClassName}Request ${entityVariableName}Request) throws NotFoundException {
        boolean isUpdate = ${entityVariableName}Request.getId() != null;
        ${entityClassName} ${entityVariableName} = new ${entityClassName}();
        ${entityVariableName}.setId(${entityVariableName}Request.getId());
        <#list attributes as attribute>
        ${entityVariableName}.set${attribute.getTitle()}(${entityVariableName}Request.get${attribute.getTitle()}());
        </#list>
        <#-- All toOne relations are added as single relation object to the entity -->
        <#list relationsToOne as relation>
        <#-- Relation object is set -->
        if (${entityVariableName}Request.get${relation.getEntityClassName()}Id() != null) {
            <#-- Owning Side => only update the relation to save it later -->
            <#if relation.isOwning()>
            ${entityVariableName}.set${relation.getEntityClassName()}(
                ${relation.getRepositoryVariableName()}.findById(${entityVariableName}Request.get${relation.getEntityClassName()}Id()).orElse(null)
            );
            <#-- Not Owning Side => get the owning entity and update it respectively -->
            <#else>
            ${relation.getEntityClassName()} ${relation.getEntityName()} = ${relation.getRepositoryVariableName()}.findById(${entityVariableName}Request.get${relation.getEntityClassName()}Id())
                .orElseThrow(() -> new NotFoundException("${relation.getEntityClassName()}", ${entityVariableName}Request.get${relation.getEntityClassName()}Id()));
            ${relation.getEntityName()}.set${entityClassName}(${entityVariableName});
            ${relation.getRepositoryVariableName()}.save(${relation.getEntityName()});
            ${entityVariableName}.set${relation.getEntityClassName()}(${relation.getEntityName()});
            </#if>
        }

        <#-- Relation object is not set and relation is OneToOne -->
        <#if relation.getRelationType() == "ONE_TO_ONE">
        else if(isUpdate) {
            ${entityClassName} previous${entityClassName} = ${repositoryVariableName}.findById(${entityVariableName}Request.getId())
                .orElseThrow(() -> new NotFoundException("${relation.getEntityClassName()}", ${entityVariableName}Request.get${relation.getEntityClassName()}Id()));
            ${relation.getEntityClassName()} previous${relation.getEntityClassName()} = previous${entityClassName}.get${relation.getEntityClassName()}();
            if (previous${relation.getEntityClassName()} != null) {
                previous${relation.getEntityClassName()}.set${entityClassName}(null);
                ${relation.getRepositoryVariableName()}.save(previous${relation.getEntityClassName()});
            }
        }
        </#if>
        </#list>

        <#-- All toMany relations => potentially multiple relation objects have to be added to the entity. However, -->
        <#-- because a list may contain the same element multiple times, the list has to be cleared first. -->
        <#list relationsToMany as relation>
        ${entityVariableName}.get${relation.getEntityClassNamePlural()}().clear();
        for (Long ${relation.getEntityName()}Id : ${entityVariableName}Request.get${relation.getEntityClassName()}Ids()) {
            ${relation.getEntityClassName()} ${relation.getEntityName()} = ${relation.getRepositoryVariableName()}.findById(${relation.getEntityName()}Id).orElseThrow(() -> new NotFoundException("${relation.getEntityName()}", ${relation.getEntityName()}Id));
            <#if relation.getRelationType() == "ONE_TO_MANY">
            ${relation.getEntityName()}.set${entityClassName}(${entityVariableName});
            ${relation.getRepositoryVariableName()}.save(${relation.getEntityName()});
            ${entityVariableName}.get${relation.getEntityClassNamePlural()}().add(
                ${relation.getRepositoryVariableName()}.findById(${relation.getEntityName()}Id).orElse(null));
            </#if>
            <#if relation.getRelationType() == "MANY_TO_MANY">
            ${entityVariableName}.get${relation.getEntityClassNamePlural()}().add(${relation.getEntityName()});
            </#if>
        }
        <#if relation.getRelationType() == "ONE_TO_MANY">
        if (${entityVariableName}Request.get${relation.getEntityClassName()}Ids().isEmpty() && isUpdate) {
            for (${relation.getEntityClassName()} ${relation.getEntityName()} : ${relation.getRepositoryVariableName()}.findBy${entityClassName}Id(${entityVariableName}Request.getId())) {
                ${relation.getEntityName()}.set${entityClassName}(null);
                ${relation.getRepositoryVariableName()}.save(${relation.getEntityName()});
            }
        }
        </#if>
        </#list>
        ${repositoryVariableName}.save(${entityVariableName});
        return new RedirectView("/${entityVariableNamePlural}");
    }

    <#-- Deleting an entity -->
    @GetMapping("delete/{id}")
    public RedirectView delete(@PathVariable("id") Long ${entityVariableName}Id) {
        ${repositoryVariableName}.deleteById(${entityVariableName}Id);
        return new RedirectView("/${entityVariableNamePlural}");
    }
}
