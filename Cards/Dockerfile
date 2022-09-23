#Start with a base image containing Java runtime
FROM openjdk:17 as build

#Information around who maintains the image
LABEL authors="hemkant.india@gmail.com"

# Add the application's jar to the container
COPY target/Accounts-0.0.1-SNAPSHOT.jar Accounts-0.0.1-SNAPSHOT.jar

#execute the application
ENTRYPOINT ["java","-jar","/Accounts-0.0.1-SNAPSHOT.jar"]