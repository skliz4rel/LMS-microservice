FROM openjdk:17-jdk-slim
ADD target/*.jar /home/LMS-microservice-0.0.1.jar
#CMD ["java", "-jar", "/home/LMS-microservice-0.0.1.jar"]

ENTRYPOINT exec java $JAVA_OPTS -jar /home/LMS-microservice-0.0.1.jar

#CMD ["java", "-jar", "/home/LMS-microservice-0.0.1.jar"]
