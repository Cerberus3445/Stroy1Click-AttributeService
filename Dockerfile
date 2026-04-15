FROM eclipse-temurin:21-jre-alpine
LABEL authors="egorm"

WORKDIR /app
ADD maven/attribute-service-0.0.1-SNAPSHOT.jar /app/attribute.jar
EXPOSE 4040
ENTRYPOINT ["java", "-jar", "attribute.jar"]