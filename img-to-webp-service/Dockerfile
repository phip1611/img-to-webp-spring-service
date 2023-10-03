FROM eclipse-temurin:17-jre-alpine
RUN apk update && apk add libwebp-tools # install cwebp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
