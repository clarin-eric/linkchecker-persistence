# Curation Rersistence API

## Introduction
The curation persistence API (CPA) is a project, based on [Spring data JPA 2.7.1](https://spring.io/projects/spring-data-jpa), which serves as a common persistence layer for the following projects:
   - [Clarin Curation Dashboard](https://github.com/clarin-eric/curation-dashboard)
   - [Link Checker](https://github.com/clarin-eric/linkchecker)
   - [Link Checker Web](https://github.com/clarin-eric/linkchecker-web)
   - [Virtual Language Observatory](https://github.com/clarin-eric/VLO)
   
## Set up   

A typical entry in the application.yml of your project would look like this:

```
spring:
   datasource:
      url: jdbc:mariadb://<databse server address>:3306/<your database>
      username: <your database user>
      password: <your database user's password>
      driver-class-name: org.mariadb.jdbc.Driver
   jpa:
      hibernate:
         ddl-auto: none
```


## Important notes

   - Spring data JPA 2.7.1 uses an h2 v1.4.200 database as dependency for testing. This version doesn't have mode=MariaDB so far. Therefore we had to use mode=MYSQL     