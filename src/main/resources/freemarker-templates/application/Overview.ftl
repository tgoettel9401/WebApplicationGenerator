<#-- @ftlvariable name="entityVariableName" type="java.lang.String" -->
<#-- @ftlvariable name="entityVariableNamePlural" type="java.lang.String" -->
<#-- @ftlvariable name="entityClassName" type="java.lang.String" -->
<#-- @ftlvariable name="entityClassNamePlural" type="java.lang.String" -->
<#-- @ftlvariable name="attributes" type="java.util.Set<org.dhbw.webapplicationgenerator.model.request.datamodel.Attribute>" -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">

<!-- Add Main file -->
<div th:insert="main.html"></div>

<body>
    <div class="container-fluid" style="margin-top: 10px">
        <h1>${entityClassNamePlural}</h1>
        <br>
        <a th:href="@{/${entityVariableNamePlural}/create}">
            <input type="button" class="btn btn-primary" value="New" style="margin-bottom: 10px">
        </a>

        <table class="table table-bordered">
            <tr>
                <#-- Column headings -->
                <#list attributes as attribute>
                <th>${attribute.getTitle()}</th>
                </#list>
            </tr>
            <tr th:each="${entityVariableName} : ${r"$"}{${entityVariableNamePlural}}">
                <#list attributes as attribute>
                <td th:text="${r"$"}{${entityVariableName}.get${attribute.getTitle()}()}"></td>
                </#list>
                <!-- Buttons -->
                <td>
                    <a th:href="@{'/${entityVariableNamePlural}/edit/' + ${r"$"}{${entityVariableName}.getId()}}">
                        <input type="button" class="btn btn-light" value="Edit">
                    </a>
                    <a th:href="@{'/${entityVariableNamePlural}/delete/' + ${r"$"}{${entityVariableName}.getId()}}">
                        <input type="button" class="btn btn-danger" value="Delete">
                    </a>
                </td>
            </tr>
        </table>

        <!-- Add Footer file -->
        <div th:insert="footer.html"></div>
    </div>
</body>
