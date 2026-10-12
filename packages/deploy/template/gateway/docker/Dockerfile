FROM nexus.guhecloud.com:5000/kaihe/openjdk:temurin-21

ADD entrypoint.sh /docker-entrypoint.sh
ADD wait-for-it.sh /wait-for-it.sh
ADD {OUTPUT_JAR} /{OUTPUT_JAR}

RUN chmod +x /docker-entrypoint.sh && chmod +x /wait-for-it.sh

ENTRYPOINT ["/docker-entrypoint.sh"]