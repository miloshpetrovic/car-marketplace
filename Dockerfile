FROM amazoncorretto:21.0.1-alpine
MAINTAINER miloshpetrovic
COPY target/car-marketplace-0.0.1-SNAPSHOT.jar car-marketplace.jar
ENTRYPOINT ["java","-jar","/car-marketplace.jar"]