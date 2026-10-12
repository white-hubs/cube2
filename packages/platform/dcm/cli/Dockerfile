FROM 192.168.0.213:5000/be/jdk8-alpine:latest

RUN mkdir -p /work

WORKDIR /work
ADD target/classes/dcm-cli.conf /work/
ADD target/classes/service_name.map /work/
ARG JAR_FILE
ADD ${JAR_FILE} /opt/app.jar
CMD ["java", "-Dspring.profiles.active=local", "-jar", "/opt/app.jar"]
