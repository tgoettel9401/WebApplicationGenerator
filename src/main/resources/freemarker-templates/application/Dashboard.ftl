<#-- @ftlvariable name="entities" type="java.util.List<org.dhbw.webapplicationgenerator.model.request.datamodel.Entity>" -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">

<div th:insert="main.html"></div>
<body>
    <div class="container-fluid" style="margin-top: 10px">
    <h1>Dashboard</h1>
    <br>
    <ul>
        <#list entities as entity>
        <li>
            <a th:href="@{${entity.getNamePlural()}}">${entity.getTitlePlural()}</a>
        </li>
        </#list>
    </ul>
    <div th:insert="footer.html"></div>
    </div>
</body>
