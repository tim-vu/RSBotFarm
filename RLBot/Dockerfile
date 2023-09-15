FROM gradle:jdk17 AS build
COPY --chown=gradle:gradle api/ /src/api
COPY --chown=gradle:gradle client/ /src/client
COPY --chown=gradle:gradle build.gradle /src
COPY --chown=gradle:gradle settings.gradle /src

WORKDIR /src
RUN gradle jarWithDeps --no-daemon


FROM ubuntu:lunar-20230420 AS final

ARG VNC_PASSWORD

RUN dpkg --add-architecture i386
RUN apt-get update -y && apt-get upgrade -y && apt-get install -y openjdk-17-jre:i386 xvfb tigervnc-scraping-server websockify
RUN mkdir ~/.vnc
RUN echo ${VNC_PASSWORD} | vncpasswd -f > ~/.vnc/passwd
RUN mkdir /app
RUN mkdir /app/lib
COPY --from=build /src/build/lib/*.jar /app/lib/
COPY --from=build /src/build/client.jar /app/client.jar
COPY ./entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh
WORKDIR /app

ENTRYPOINT ["/app/entrypoint.sh"]