#
# Build stage
FROM maven:3.8.3-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests


FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/UrlShortener-0.0.1-SNAPSHOT.jar UrlShortener.jar
COPY src/main/resources/root.crt /app/root.crt
EXPOSE 8066

CMD ["java", "-Djavax.net.ssl.trustStore=/app/root.crt", "-jar", "UrlShortener.jar"]



