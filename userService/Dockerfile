FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

# Copy the built jar into the image
COPY target/userService-0.0.1-SNAPSHOT.jar app.jar

# Expose application port (change if needed)
EXPOSE 8282

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
