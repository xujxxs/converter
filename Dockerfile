FROM gradle:jdk21 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle
COPY src ./src

RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /app/build/libs/convertor-0.0.1-SNAPSHOT.jar convertor.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "convertor.jar" ]