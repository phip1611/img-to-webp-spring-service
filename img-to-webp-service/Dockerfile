# Based on the minimum LTS version supported by this service.
FROM eclipse-temurin:11-jre-alpine
RUN apk update && apk add libwebp-tools # install cwebp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
