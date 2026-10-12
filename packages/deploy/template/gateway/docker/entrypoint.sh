#!/bin/sh

PARAMS_OPENS="--add-opens=java.base/java.base=ALL-UNNAMED \
              --add-opens=java.base/java.io=ALL-UNNAMED \
              --add-opens=java.base/java.nio=ALL-UNNAMED \
              --add-opens=java.base/sun.reflect.annotation=ALL-UNNAMED \
              --add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
              --add-opens=java.base/java.lang=ALL-UNNAMED"
PARAMS_JVM_FLAGS="-Xmx4G \
                  -XX:SurvivorRatio=8 \
                  -XX:MetaspaceSize=256m \
                  -XX:MaxMetaspaceSize=1024m \
                  -XX:+UseParallelGC \
                  -XX:ParallelGCThreads=4 \
                  -XX:+UseParallelGC \
                  -XX:+UseAdaptiveSizePolicy \
                  -XX:+PrintGCDetails \
                  -XX:+HeapDumpOnOutOfMemoryError \
                  -XX:HeapDumpPath=/"
set -x
java ${PARAMS_OPENS} -server ${PARAMS_JVM_FLAGS} ${JVM_FLAGS} ${JAVA_OPTS} -jar $(dirname "${BASH_SOURCE[0]}")/{OUTPUT_JAR} --show-config