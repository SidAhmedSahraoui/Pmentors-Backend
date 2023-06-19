FROM openjdk:17-alpine

EXPOSE 8888

COPY target/Registry*.jar Registry.jar

ENTRYPOINT [ "java","-jar","/Registry.jar" ]
