FROM amazoncorretto:17-alpine-jdk

# create a directory for the app
WORKDIR /app

# copy everything from current directory to app directory
COPY . .

# build the app
RUN ./gradlew clean build -x test

# expose port 8080
EXPOSE 8080

# run the app
CMD ["java", "-jar", "./build/libs/splitter-0.0.1-SNAPSHOT.jar"]