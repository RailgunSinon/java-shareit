FROM amazoncorretto:11-alpine-jdk
WORKDIR /app
COPY  target/*.jar ./shareit-server.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","shareit-server.jar"]