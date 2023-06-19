FROM openjdk:17-alpine

EXPOSE 7777

COPY target/MsGateway*.jar MsGateway.jar

ENTRYPOINT [ "java","-jar","/MsGateway.jar" ]
