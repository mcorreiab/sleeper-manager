FROM oracle/graalvm-ce:20.1.0-java8 as graalvm
RUN gu install native-image

COPY . /home/app/sleepermanager
WORKDIR /home/app/sleepermanager

RUN native-image --no-server -cp build/libs/sleepermanager-*-all.jar

FROM frolvlad/alpine-glibc
RUN apk update && apk add libstdc++
EXPOSE 8080
COPY --from=graalvm /home/app/sleepermanager/sleepermanager /app/sleepermanager
ENTRYPOINT ["/app/sleepermanager"]
