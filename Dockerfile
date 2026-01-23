FROM openjdk:21
LABEL authors="egorm"

WORKDIR /app
ADD maven/AttributeService-0.0.1-SNAPSHOT.jar /app/attribute.jar
EXPOSE 4040
ENTRYPOINT ["java", "-jar", "attribute.jar"]