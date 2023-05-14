<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="applicationImports" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="entityVariableNamePlural" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="entityClassName" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="entityClassNamePlural" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="entityVariableName" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="entityRepositoryName" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="entityRepositoryClassName" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="attributes" type="java.util.List<org.dhbw.webapplicationgenerator.model.request.datamodel.Attribute>" -->
<#-- @ftlvariable name="relations" type="java.util.List<org.dhbw.webapplicationgenerator.model.request.datamodel.EntityRelation>" -->
package ${packageName};

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
<#list applicationImports as import>
import ${import};
</#list>

@Route("${entityVariableNamePlural}/details")
public class ${entityClassName}Details extends VerticalLayout implements HasUrlParameter<String> {

    private final Logger logger = LoggerFactory.getLogger(${entityClassName}Details.class);
    private final ${entityRepositoryClassName} ${entityRepositoryName};
    <#list relations as relation>
    private final ${relation.getRepositoryClassName()} ${relation.getRepositoryVariableName()};
    </#list>

    private ${entityClassName} ${entityVariableName};
    private H1 heading;
    <#list attributes as attribute>
    private ${attribute.getDataType().getVaadinFieldType()} ${attribute.getName()}Field;
    </#list>
    <#list relations as relation>
        <#if relation.getRelationType().isToMany()>
    private MultiSelectComboBox<${relation.getEntityClassName()}> ${relation.getEntityNamePlural()}Select;
        <#else>
    private ComboBox<${relation.getEntityClassName()}> ${relation.getEntityName()}Select;
        </#if>
    </#list>

    private Button saveButton;

    public ${entityClassName}Details(${entityRepositoryClassName} ${entityRepositoryName},
        <#list relations as relation>
        ${relation.getRepositoryClassName()} ${relation.getRepositoryVariableName()}<#if relation_has_next>,<#else>) {</#if>
        </#list>
        add(new Navigation());
        this.${entityRepositoryName} = ${entityRepositoryName};
        <#list relations as relation>
        this.${relation.getRepositoryVariableName()} = ${relation.getRepositoryVariableName()};
        </#list>
        logger.info("Creating ${entityClassName}Details View");
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        if (parameter.equals("new")) {
            ${entityVariableName} = new ${entityClassName}();
            <#list attributes as attribute>
            ${entityVariableName}.set${attribute.getTitle()}(${attribute.getDataType().getDefaultValue()});
            </#list>
        } else {
            Long id = Long.valueOf(parameter);
            ${entityVariableName} = ${entityRepositoryName}.findById(id).orElseThrow(
                () -> new RuntimeException("${entityVariableName} with ID " + id + " has not been found"));
            <#list relations as relation>
                <#if relation.getRelationType().isToMany()>
                    <#if relation.getRelationType().isFromMany()>
            List<${relation.getEntityClassName()}> ${relation.getEntityNamePlural()} = ${relation.getRepositoryVariableName()}.findBy${entityClassNamePlural}Id(id);
            ${entityVariableName}.set${relation.getEntityClassNamePlural()}(${relation.getEntityNamePlural()});
                    <#else>
            List<${relation.getEntityClassName()}> ${relation.getEntityNamePlural()} = ${relation.getRepositoryVariableName()}.findBy${entityClassName}Id(id);
            ${entityVariableName}.set${relation.getEntityClassNamePlural()}(${relation.getEntityNamePlural()});
                    </#if>
                <#else>
            ${relation.getEntityClassName()} ${relation.getEntityName()} = ${relation.getRepositoryVariableName()}.findBy${entityClassName}Id(id);
            ${entityVariableName}.set${relation.getEntityClassName()}(${relation.getEntityName()});
                </#if>
            </#list>
        }

        buildUI(${entityVariableName});
    }

    private void buildUI(${entityClassName} ${entityVariableName}) {
        logger.info("Building UI");
        heading = new H1("${entityClassName} with ID " + ${entityVariableName}.getId());
        add(heading);

        <#list attributes as attribute>
        <#if attribute.getDataType() == "LOCAL_DATE">
        ${attribute.getName()}Field = new ${attribute.getDataType().getVaadinFieldType()}("${attribute.getTitle()}", ${entityVariableName}.get${attribute.getTitle()}());
        <#else>
        ${attribute.getName()}Field = new ${attribute.getDataType().getVaadinFieldType()}("${attribute.getName()}", ${entityVariableName}.get${attribute.getTitle()}(), "${attribute.getTitle()}");
        </#if>
        </#list>

        <#list relations as relation>
        <#if relation.getRelationType().isToMany()>
        ${relation.getEntityNamePlural()}Select = new MultiSelectComboBox<>();
        ${relation.getEntityNamePlural()}Select.setLabel("${relation.getEntityClassNamePlural()}");
        ${relation.getEntityNamePlural()}Select.setItems(${relation.getRepositoryVariableName()}.findAll());
        ${relation.getEntityNamePlural()}Select.setValue(${entityVariableName}.get${relation.getEntityClassNamePlural()}());
        ${relation.getEntityNamePlural()}Select.setItemLabelGenerator(${relation.getEntityName()} -> ${relation.getEntityName()}.get${relation.getEntityObject().getReferenceAttribute().getTitle()}());
        <#else>
        ${relation.getEntityName()}Select = new ComboBox<>();
        ${relation.getEntityName()}Select.setLabel("${relation.getEntityClassNamePlural()}");
        ${relation.getEntityName()}Select.setItems(${relation.getRepositoryVariableName()}.findAll());
        ${relation.getEntityName()}Select.setValue(${entityVariableName}.get${relation.getEntityClassName()}());
        ${relation.getEntityName()}Select.setItemLabelGenerator(${relation.getEntityName()} -> ${relation.getEntityName()}.get${relation.getEntityObject().getReferenceAttribute().getTitle()}());
        </#if>
        </#list>

        <#list attributes as attribute>
        add(${attribute.getName()}Field);
        </#list>
        <#list relations as relation>
        <#if relation.getRelationType().isToMany()>
        add(${relation.getEntityNamePlural()}Select);
        <#else>
        add(${relation.getEntityName()}Select);
        </#if>
        </#list>

        saveButton = new Button("Save");
        saveButton.addClickListener(event -> save());
        add(saveButton);

        logger.info("Building UI finished");
    }

    private void save() {
        <#list attributes as attribute>
        ${entityVariableName}.set${attribute.getTitle()}(${attribute.getName()}Field.getValue());
        </#list>
        <#list relations as relation>
        <#if relation.getRelationType().isToMany()>
        List<${relation.getEntityClassName()}> new${relation.getEntityClassNamePlural()} = ${relation.getEntityNamePlural()}Select.getValue().stream().collect(Collectors.toList());
        ${entityVariableName}.set${relation.getEntityClassNamePlural()}(new${relation.getEntityClassNamePlural()});
        <#else>
        ${relation.getEntityClassName()} new${relation.getEntityClassName()} = ${relation.getEntityName()}Select.getValue();
        ${entityVariableName}.set${relation.getEntityClassName()}(new${relation.getEntityClassName()});
        </#if>
        </#list>
        ${entityVariableName} = ${entityRepositoryName}.save(${entityVariableName});

        Dialog dialog = new Dialog();
        dialog.add(new Text("${entityClassName} successfully saved"));
        dialog.open();
        dialog.addDialogCloseActionListener(event -> navigate(${entityVariableName}.getId(), dialog));
    }

    private void navigate(Long id) {
        removeAll();
        this.getUI().get().navigate("/${entityVariableNamePlural}/details/" + id);
    }

    private void navigate(Long id, Dialog dialog) {
        dialog.close();
        navigate(id);
    }

}
