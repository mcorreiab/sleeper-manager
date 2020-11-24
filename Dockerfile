FROM openjdk:11-jdk-slim
COPY build/libs/sleepermanager-*-all.jar sleepermanager.jar
EXPOSE 8080
CMD java -jar sleepermanager.jar
