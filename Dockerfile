FROM openjdk:17
LABEL authors="Alex"

WORKDIR /app
COPY /target/bd_coursework-1.0.jar /app/bd_coursework-1.0.jar
