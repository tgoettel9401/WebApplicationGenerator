<#-- @ftlvariable name="entities" type="java.util.List<org.dhbw.webapplicationgenerator.model.request.datamodel.Entity>" -->
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<div th:fragment="navbar">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="/">Home</a>
            <div class="collapse navbar-collapse">
            <ul class="navbar-nav">
                <#list entities as entity>
                <li class="nav-item">
                    <a class="nav-link" th:href="@{/${entity.getNamePlural()}}">${entity.getTitlePlural()}</a>
                </li>
                </#list>
                <#-- Add users -->
                <li class="nav-item">
                    <a class="nav-link" th:href="@{/users}">Users</a>
                </li>
            </ul>
            </div>
        </div>
    </nav>
</div>
</html>
