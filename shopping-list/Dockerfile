FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
LABEL maintainer="wouter.vandam@kbc.be"
COPY pom.xml /tmp/build/
COPY src /tmp/build/src/
WORKDIR /tmp/build/
RUN mvn install -DskipTests

FROM openjdk:8u181-jre
LABEL app="integrated-hello"
COPY --from=MAVEN_TOOL_CHAIN /tmp/build/target/shopping-list-0.0.1-SNAPSHOT.jar hello.jar
EXPOSE 8080
CMD [ "java", "-Xms256m", "-Xmx512m", "-Duser.timezone=UTC", "-jar", "hello.jar" ]