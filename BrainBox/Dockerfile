FROM openjdk:17-alpine
COPY target/BrainBox-0.0.1-SNAPSHOT.jar /app.jar
WORKDIR /app
EXPOSE 8888
CMD ["java" ,"-jar" ,"/app.jar"]