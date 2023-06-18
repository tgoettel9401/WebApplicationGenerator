<#-- @ftlvariable name="apiPath" type="java.lang.String" -->
<#-- @ftlvariable name="h2ConsoleEnabled" type="java.lang.Boolean" -->
<#-- @ftlvariable name="h2ConsolePath" type="java.lang.String" -->
<#-- @ftlvariable name="databaseConnectionString" type="java.lang.String" -->
spring.data.rest.base-path=${apiPath}

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
