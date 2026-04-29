FROM maven:3.9.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
COPY test ./test
COPY input ./input
RUN mvn clean package

FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /app/target/module3-1.0-SNAPSHOT.jar app.jar
COPY --from=build /app/input ./input
ENTRYPOINT ["java", "-jar", "app.jar"]

