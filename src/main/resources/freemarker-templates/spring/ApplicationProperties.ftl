<#-- @ftlvariable name="apiPath" type="java.lang.String" -->
<#-- @ftlvariable name="h2ConsoleEnabled" type="java.lang.Boolean" -->
<#-- @ftlvariable name="h2ConsolePath" type="java.lang.String" -->
<#-- @ftlvariable name="databaseConnectionString" type="java.lang.String" -->
<#-- @ftlvariable name="databaseUsername" type="java.lang.String" -->
<#-- @ftlvariable name="databasePassword" type="java.lang.String" -->
<#-- @ftlvariable name="databaseDialect" type="java.lang.String" -->
spring.data.rest.base-path=${apiPath}

<#if h2ConsoleEnabled>
spring.h2.console.enabled=${h2ConsoleEnabled?c}
spring.h2.console.settings.web-allow-others=${h2ConsoleEnabled?c}
spring.h2.console.path=${h2ConsolePath}
<#else>
spring.datasource.url=${databaseConnectionString}
spring.datasource.username=${databaseUsername}
spring.datasource.password=${databasePassword}
spring.jpa.database-platform=${databaseDialect}
spring.jpa.hibernate.ddl-auto=create-drop <#-- Use create-drop temporarily until Flyway-Scripts are generated automatically-->
</#if>
