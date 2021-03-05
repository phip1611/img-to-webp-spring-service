FROM adoptopenjdk/openjdk11:alpine-jre
RUN wget https://storage.googleapis.com/downloads.webmproject.org/releases/webp/libwebp-1.2.0-linux-x86-64.tar.gz
RUN tar -xzf libwebp-1.2.0-linux-x86-64.tar.gz
ENV PATH="${PATH}:${PWD}/libwebp-1.2.0-linux-x86-64/bin/"
# from https://hub.docker.com/r/adoptopenjdk/openjdk11/
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","de.phip1611.img_to_webp.ImgToWebPServiceApplication"]
