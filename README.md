# Linkchecker Persistence API

## Introduction
The linkchecker persistence API (LPA) is a project, based on [Spring data JPA 2.7.10](https://spring.io/projects/spring-data-jpa), which serves as a common persistence layer for the following projects:
   - [Clarin Curation Dashboard](https://github.com/clarin-eric/curation-dashboard)
   - [Link Checker](https://github.com/clarin-eric/linkchecker)
   - [Link Checker API](https://github.com/clarin-eric/linkchecker-api)
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

   - linkchecker-persistence is using a schema.sql file to set up database structure with tables and views. The SQL script works for H2 and MariaDB and we strongly discourage an automatic setup via hibernate.ddl-auto=create.    

   - version 0.0.6+ uses the hibernate-enhance-maven-plugin to allow lazy fetching for toOne associations. This is basically important when you lookup a significant number of Url instances 
   since otherwise for each instance a query is send to the database to look up the associated Status instance. **If you use another persistence API than hibernate, which is currently 
   the default in Spring data JPA, you should make sure lazy loading for toOne associations.**     
