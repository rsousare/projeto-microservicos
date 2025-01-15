FROM openjdk:17-jdk-alpine
MAINTAINER com.nttdata
RUN ls -1
COPY target/people-0.0.1-SNAPSHOT.jar /people-0.0.1-SNAPSHOT
ENTRYPOINT ["java","-jar","/people-0.0.1-SNAPSHOT"]
EXPOSE 9094