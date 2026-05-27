# Based on the minimum LTS version supported by this service.
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache libwebp-tools \
    && addgroup -S app \
    && adduser -S -G app app
WORKDIR /app
COPY --chown=app:app target/*.jar app.jar
USER app
ENTRYPOINT ["java","-jar","/app/app.jar"]
