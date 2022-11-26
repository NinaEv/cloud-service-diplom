FROM openjdk:17
EXPOSE 5500
ADD target/cloudService-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]