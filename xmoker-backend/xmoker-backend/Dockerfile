# stage de build
FROM maven:3-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN mvn -B dependency:resolve

COPY src src
RUN mvn -B package -DskipTests

# stage de runtime
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /app/target/*.jar xmoker.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","xmoker.jar"]
