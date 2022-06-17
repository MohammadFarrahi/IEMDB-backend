FROM openjdk:17-jdk-alpine

WORKDIR /build
COPY . .

RUN ./mvnw package
ARG JAR_FILE=target/*.jar
RUN mv ${JAR_FILE} /app.jar

WORKDIR /
RUN rm -Rf /work

ENTRYPOINT ["java","-jar","/app.jar"]