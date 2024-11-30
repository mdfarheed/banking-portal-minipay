FROM openjdk:11-jdk-slim
ARG JAR_FILE=target/bankingportal-0.0.1-SNAPSHOT.jar 
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-cp", "app.jar", "com.webapp.bankingportal.BankingPortalApplication"]


