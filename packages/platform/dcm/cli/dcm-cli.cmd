@ECHO OFF
set JAR_FILE=.\target\dcm-cli-1.0.1-SNAPSHOT.jar

set JAVA_OPTS=%JAVA_OPTS% -Dspring.profiles.active=local
@echo java %JAVA_OPTS% -jar %JAR_FILE% %*
java %JAVA_OPTS% -jar %JAR_FILE% %*
