# Description
This module exists purely for distributed key rotation. It is completely fine if it would move
to some other repository eventually.

# How to use

1. Open [example](example-spring-distributed-keyrotation) and launch REST application using 
[Application](example-spring-distributed-keyrotation/src/main/java/de/adorsys/keymanagement/keyrotation/Application.java)
class. This will launch H2-in-memory backed distributed keyrotation REST application with configuration from
[application.yml](example-spring-distributed-keyrotation/src/main/resources/application.yml).

1. Swagger will be available at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

1. Mongo/MySql/Postgres integrations - see [tests](example-spring-distributed-keyrotation/src/test/java/de/adorsys/keymanagement/keyrotation)
and their configuration in [resources](example-spring-distributed-keyrotation/src/test/resources) folder 
(note that tests has scheduling disabled)
