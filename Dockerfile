FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/task_slideshow-0.0.1-SNAPSHOT.jar /app/task-slideshow.jar

EXPOSE 8080

CMD ["java", "-jar", "task-slideshow.jar"]
