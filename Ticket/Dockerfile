FROM openjdk:17-jdk-alpine
MAINTAINER com.nttdata
RUN ls -1
COPY target/ticket-0.0.1-SNAPSHOT.jar /ticket-0.0.1-SNAPSHOT
ENTRYPOINT ["java","-jar","/ticket-0.0.1-SNAPSHOT"]
EXPOSE 9093