<#-- @ftlvariable name="entityVariableName" type="java.lang.String" -->
<#-- @ftlvariable name="entityVariableNamePlural" type="java.lang.String" -->
<#-- @ftlvariable name="entityClassName" type="java.lang.String" -->
<#-- @ftlvariable name="entityClassNamePlural" type="java.lang.String" -->
<#-- @ftlvariable name="attributes" type="java.util.Set<org.dhbw.webapplicationgenerator.model.request.datamodel.Attribute>" -->
<#-- @ftlvariable name="relationsToOne" type="java.util.List<org.dhbw.webapplicationgenerator.model.request.datamodel.EntityRelation>" -->
<#-- @ftlvariable name="relationsToMany" type="java.util.List<org.dhbw.webapplicationgenerator.model.request.datamodel.EntityRelation>" -->
<#-- Header -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<div th:insert="main.html"></div>
<body>
    <div class="container-fluid" style="margin-top: 10px">

        <#-- Update or Create -->
        <h1 th:if="${r"${"}${entityVariableName}.getId() != null}">Update an existing ${entityClassName}</h1>
        <h1 th:if="${r"${"}${entityVariableName}.getId() == null}">Create a new ${entityClassName}</h1>

        <form th:action="@{'/${entityVariableNamePlural}/save'}" th:object="${r"${"}${entityVariableName}}" method="post">
            <input type="hidden" name="id" th:value="${r"${"}${entityVariableName}.getId()}">

            <#-- For all attributes add an input of the right DataType -->
            <#list attributes as attribute>
            <label for="${attribute.getName()}">${attribute.getTitle()}</label>
            <input type="${attribute.getDataType().getInputType()}" id="${attribute.getName()}" name="${attribute.getName()}" class="input-group"
               th:value="${r"${"}${entityVariableName}.get${attribute.getTitle()}()}">
            <br>
            </#list>

            <#-- For all toOne relation (hence not toMany) add a simple select to choose the correct option. -->
            <#list relationsToOne as relation>
            <label for="${relation.getName()}">${relation.getTitle()}</label>
            <select id="${relation.getName()}" name="${relation.getEntityName()}Id" class="form-control"
                th:value="${r"${"}${entityVariableName}.get${relation.getEntityClassName()}()}">
                <option th:value="null">none</option>
                <option
                    th:each="${relation.getEntityName()} : ${r"${"}${relation.getEntityNamePlural()}}"
                    th:text="${r"${"}${relation.getEntityName()}.get${relation.getEntityObject().getReferenceAttribute().getTitle()}()}"
                    th:value="${r"${"}${relation.getEntityName()}.getId()}"
                    <#if relation.getRelationType().isFromMany()>
                    th:selected="${r"${"}${relation.getEntityName()}.get${entityClassNamePlural}().contains(${entityVariableName})}">
                    <#else>
                    th:selected="${r"${"}${entityVariableName}.equals(${relation.getEntityName()}.get${entityClassName}())}">
                    </#if>
                </option>
            </select>
            <br>
            </#list>

            <#-- For all toMany relation we add a multiple select to choose multiple options. -->
            <#list relationsToMany as relation>
            <label for="${relation.getNamePlural()}">${relation.getTitle()}</label>
            <select multiple id="${relation.getNamePlural()}" name="${relation.getEntityName()}Ids" class="form-control"
                    th:value="${r"${"}${entityVariableName}.get${relation.getEntityClassNamePlural()}()}">
                <option th:value="null">none</option>
                <option
                    th:each="${relation.getEntityName()} : ${r"${"}${relation.getEntityNamePlural()}}"
                    th:text="${r"${"}${relation.getEntityName()}.get${relation.getEntityObject().getReferenceAttribute().getTitle()}()}"
                    th:value="${r"${"}${relation.getEntityName()}.getId()}"
                    <#if relation.getRelationType().isFromMany()>
                    th:selected="${r"${"}${relation.getEntityName()}.get${entityClassNamePlural}().contains(${entityVariableName})}">
                    <#else>
                        th:selected="${r"${"}${entityVariableName}.equals(${relation.getEntityName()}.get${entityClassName}())}">
                    </#if>
                </option>
            </select>
            <br>
            </#list>

            <a th:href="@{/${entityVariableNamePlural}}">
                <input type="button" class="btn btn-warning" value="Cancel">
            </a>
            <input type="submit" class="btn btn-primary" value="Save">
            <br>
        </form>

        <div th:insert="footer.html"></div>
    </div>
</body>
