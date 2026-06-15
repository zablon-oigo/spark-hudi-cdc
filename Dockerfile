FROM apache/spark:3.5.1

USER root

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk && \
    apt-get clean


RUN mkdir -p /home/spark/.ivy2/cache \
    && mkdir -p /home/spark/.ivy2/jars \
    && chown -R spark:spark /home/spark/.ivy2

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH=$JAVA_HOME/bin:$PATH

RUN java -version

USER spark