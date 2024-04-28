FROM amazoncorretto:17-alpine-jdk

# create a directory for the app
WORKDIR /app

# copy everything from current directory to app directory
COPY . .

# Ensure gradlew script has permissions
RUN dos2unix /app/gradlew && chmod +x /app/gradlew

# build the app
RUN ./gradlew --no-daemon clean build -x test

# expose port 3000
EXPOSE 3000

# run the app
CMD ["java", "-jar", "./build/libs/splitter-0.0.1-SNAPSHOT.jar"]