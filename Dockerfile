FROM openjdk:17-jdk-alpine

WORKDIR /build
COPY . .

RUN ./mvnw package -Dmaven.test.skip
ARG JAR_FILE=target/*.jar
RUN mv ${JAR_FILE} /app.jar

WORKDIR /
RUN rm -Rf /work

EXPOSE 8000 8000
ENTRYPOINT ["java","-jar","/app.jar"]