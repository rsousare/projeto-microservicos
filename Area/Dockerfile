FROM openjdk:17-jdk-alpine
MAINTAINER com.nttdata
RUN ls -1
COPY target/area-0.0.1-SNAPSHOT.jar /area-0.0.1-SNAPSHOT
ENTRYPOINT ["java","-jar","/area-0.0.1-SNAPSHOT"]
EXPOSE 9091