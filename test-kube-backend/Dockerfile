FROM openjdk:17-slim
COPY target/*.jar test-kube-backend.jar
ENTRYPOINT ["java","-jar","/test-kube-backend.jar"]