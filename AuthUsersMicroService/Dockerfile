FROM openjdk:17-alpine

EXPOSE 1111

COPY target/AuthUsersMicroService*.jar AuthUsersMicroService.jar

ENTRYPOINT [ "java","-jar","/AuthUsersMicroService.jar" ]
