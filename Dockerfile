FROM maven:3.6.3-jdk-11-slim AS build
COPY src /app/src
COPY pom.xml /app
WORKDIR /app
RUN mvn clean package -DskipTests


FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/insurance-agency-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-jar", "insurance-agency-0.0.1-SNAPSHOT.jar"]