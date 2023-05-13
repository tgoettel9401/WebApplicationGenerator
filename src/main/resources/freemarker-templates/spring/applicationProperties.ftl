<#-- @ftlvariable name="apiPath" type="java.lang.String" -->
<#-- @ftlvariable name="h2ConsoleEnabled" type="java.lang.Boolean" -->
<#-- @ftlvariable name="h2ConsolePath" type="java.lang.String" -->
<#-- @ftlvariable name="h2JdbcUrl" type="java.lang.String" -->
spring.data.rest.base-path=${apiPath}

<#if h2ConsoleEnabled>
spring.h2.console.enabled=${h2ConsoleEnabled?c}
spring.h2.console.settings.web-allow-others=${h2ConsoleEnabled?c}
spring.h2.console.path=${h2ConsolePath}
spring.datasource.url=jdbc:${h2JdbcUrl}
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
</#if>
