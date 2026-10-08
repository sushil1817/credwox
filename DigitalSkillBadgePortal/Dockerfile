FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM tomcat:9.0-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY --from=build /app/target/DigitalSkillBadgePortal.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

# Adapt Tomcat HTTP port dynamically to Render's $PORT environment variable if set, otherwise default to 8080
CMD ["sh", "-c", "sed -i 's/port=\"8080\"/port=\"'\"${PORT:-8080}\"'\"/g' /usr/local/tomcat/conf/server.xml && catalina.sh run"]
