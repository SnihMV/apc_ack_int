FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

EXPOSE 8080

COPY target/*.jar apk_ack_integration_service.jar

ENTRYPOINT ["java", "-jar", "apk_ack_integration_service.jar"]