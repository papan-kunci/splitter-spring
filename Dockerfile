FROM --platform=linux/amd64 amazoncorretto:17-alpine-jdk

# create a directory for the app
WORKDIR /app

# copy everything from current directory to app directory
COPY . .

# Ensure gradlew script has permissions
RUN dos2unix /app/gradlew && chmod +x /app/gradlew

# build the app
RUN ./gradlew clean build -x test --info

# expose port 45450
EXPOSE 45450

# run the app
CMD ["java", "-jar", "./build/libs/splitter-0.0.1-SNAPSHOT.jar"]