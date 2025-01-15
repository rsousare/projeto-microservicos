FROM openjdk:17-jdk-alpine
MAINTAINER com.nttdata
RUN ls -1
COPY target/Gateway-0.0.1-SNAPSHOT.jar /Gateway-0.0.1-SNAPSHOT
ENTRYPOINT ["java","-jar","/Gateway-0.0.1-SNAPSHOT"]
EXPOSE 8080