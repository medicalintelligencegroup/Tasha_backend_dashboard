FROM amazoncorretto:11
WORKDIR /patient-service
COPY build/libs/patient-service-RELEASE.jar patient-service-RELEASE.jar
CMD ["java", "-jar", "/patient-service/patient-service-RELEASE.jar"]
EXPOSE 8080