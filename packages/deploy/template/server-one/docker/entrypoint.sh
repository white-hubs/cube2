#!/bin/sh

PARAMS_OPENS="--add-opens=java.base/java.base=ALL-UNNAMED \
              --add-opens=java.base/java.io=ALL-UNNAMED \
              --add-opens=java.base/java.nio=ALL-UNNAMED \
              --add-opens=java.base/sun.reflect.annotation=ALL-UNNAMED \
              --add-opens=java.base/java.lang.reflect=ALL-UNNAMED \
              --add-opens=java.base/java.util=ALL-UNNAMED \
              --add-opens=java.desktop/sun.font=ALL-UNNAMED \
              --add-exports=java.desktop/sun.font=ALL-UNNAMED \
              --add-opens=java.base/java.lang=ALL-UNNAMED \
              --add-opens=java.base/java.time=ALL-UNNAMED \
              --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED \
              --add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED"
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
PARAMS_DEFAULT_OPTS="-Dreactor.netty.pool.leasingStrategy=lifo \
                    -Djava.security.egd=file:/dev/./urandom \
                    -Dlogging.file.path=./logs/servone/ \
                    -Dspring.profiles.active=default \
                    -Dlogging.level.RocketmqClient=info \
                    "

set -x
java ${PARAMS_OPENS} -server ${PARAMS_JVM_FLAGS} ${JVM_FLAGS} ${PARAMS_DEFAULT_OPTS} ${JAVA_OPTS} -jar $(dirname "${BASH_SOURCE[0]}")/{OUTPUT_JAR} --show-config