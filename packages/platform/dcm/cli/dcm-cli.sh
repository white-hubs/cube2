#/bin/sh
JAR_FILE=./target/dcm-cli-1.0.1-SNAPSHOT.jar

java $JAVA_OPTS -jar $JAR_FILE $*
