FROM openjdk:17-jdk-alpine

COPY target/*.jar OrganizationChartApplication-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","OrganizationChartApplication-0.0.1-SNAPSHOT.jar"]
