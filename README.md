# Linkchecker Persistence API

## Introduction
The linkchecker persistence API (LPA) is a project, based on [Spring data JPA 2.7.1](https://spring.io/projects/spring-data-jpa), which serves as a common persistence layer for the following projects:
   - [Clarin Curation Dashboard](https://github.com/clarin-eric/curation-dashboard)
   - [Link Checker](https://github.com/clarin-eric/linkchecker)
   - [Link Checker Web](https://github.com/clarin-eric/linkchecker-web)
   - [Virtual Language Observatory](https://github.com/clarin-eric/VLO)
   
## Set up   

When you load LPA as a dependency into your Spring boot parent project, you have to specify a database connection there.  
A typical entry in the application.yml of your project would look like this:

```
spring:
   datasource:
      url: jdbc:mariadb://<databse server address>:3306/<your database>
      username: <your database user>
      password: <your database user's password>
      driver-class-name: <your database driver class>
   jpa:
      hibernate:
         ddl-auto: none
```

Spring data JPA uses the Hikari connection pool by default. Hikari specific properties can be set by:

```
spring:
   datasource:
      hikari:
         <hikari-property>: <value>
```
For details on Hikari properties, have a look on the [Hikari Readme page](https://github.com/brettwooldridge/HikariCP#frequently-used), please. 


## Important notes

   - We're using a schema.sql file to set up database structure with tables and views. The SQL script works for H2 and MariaDB and we strongly discourage an automatic setup via hibernate.ddl-auto=create.    

   - Spring data JPA 2.7.1 uses an h2 v1.4.200 database as dependency for testing. This version doesn't have mode=MariaDB so far. Therefore we had to use mode=MYSQL     