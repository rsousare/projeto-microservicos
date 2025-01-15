FROM openjdk:17-jdk-alpine
MAINTAINER com.nttdata
RUN ls -1
COPY target/Service-registry-0.0.1-SNAPSHOT.jar /Service-registry-0.0.1-SNAPSHOT
ENTRYPOINT ["java","-jar","/Service-registry-0.0.1-SNAPSHOT"]
EXPOSE 8761