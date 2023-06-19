FROM openjdk:17-alpine

EXPOSE 3333

COPY target/Notification*.jar Notification.jar

ENTRYPOINT [ "java","-jar","/Notification.jar" ]
