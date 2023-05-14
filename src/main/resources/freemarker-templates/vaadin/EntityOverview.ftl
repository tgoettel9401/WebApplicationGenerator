<#-- @ftlvariable name="packageName" type="java.lang.String" -->
<#-- @ftlvariable name="applicationImports" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="entityVariableNamePlural" type="java.lang.String" -->
<#-- @ftlvariable name="entityVariableName" type="java.lang.String" -->
<#-- @ftlvariable name="entityClassName" type="java.lang.String" -->
<#-- @ftlvariable name="entityClassNamePlural" type="java.lang.String" -->
<#-- @ftlvariable name="repositoryVariableName" type="java.lang.String" -->
<#-- @ftlvariable name="repositoryClassName" type="java.lang.String" -->
<#-- @ftlvariable name="attributes" type="java.util.List<org.dhbw.webapplicationgenerator.model.request.datamodel.Attribute>" -->
package ${packageName};

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

<#list applicationImports as import>
import ${import};
</#list>

@Route("${entityVariableNamePlural}")
public class ${entityClassName}Overview extends VerticalLayout {

    private final ${repositoryClassName} ${repositoryVariableName};

    public ${entityClassName}Overview(${repositoryClassName} ${repositoryVariableName}) {
        add(new Navigation());
        this.${repositoryVariableName} = ${repositoryVariableName};
        buildUI();
    }

    private void buildUI() {
        add(new H1("${entityClassNamePlural} Overview"));

        Button newButton = new Button("New");
        newButton.addClickListener(event -> navigateToNew());
        add(newButton);

        Grid<${entityClassName}> ${entityVariableName}Grid = new Grid<>(${entityClassName}.class, false);

        <#list attributes as attribute>
        ${entityVariableName}Grid.addColumn(${entityClassName}::get${attribute.getTitle()}).setHeader("${attribute.getTitle()}");
        </#list>
        ${entityVariableName}Grid.addItemDoubleClickListener(event -> navigateToDetails(event.getItem().getId()));

        List<${entityClassName}> ${entityVariableNamePlural} = ${repositoryVariableName}.findAll();
        ${entityVariableName}Grid.setItems(${entityVariableNamePlural});
        add(${entityVariableName}Grid);
    }

    private void navigateToNew() {
        this.getUI().get().navigate("${entityVariableNamePlural}/details/new");
    }

    private void navigateToDetails(Long id) {
        this.getUI().get().navigate("${entityVariableNamePlural}/details/" + id);
    }

}
