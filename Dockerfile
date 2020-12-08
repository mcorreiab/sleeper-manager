FROM openjdk:11 as build
ENV APP_HOME=/root/dev/sleepermanager/
ENV http_proxy "http://127.0.0.1:3001"
ENV https_proxy "http://127.0.0.1:3001"
RUN mkdir -p $APP_HOME/src/main/kotlin
WORKDIR $APP_HOME
COPY build.gradle gradle.properties gradlew gradlew.bat $APP_HOME
COPY gradle $APP_HOME/gradle
RUN ./gradlew build -x test --continue
COPY src src
RUN ./gradlew build

FROM openjdk:11-jre-slim as production
WORKDIR /root/
COPY --from=build /root/dev/sleepermanager/build/libs/sleepermanager-*-all.jar sleepermanager.jar
EXPOSE 8080
ENV MICRONAUT_ENVIRONMENTS "prod"
CMD java -jar sleepermanager.jar
