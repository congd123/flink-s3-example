FROM flink:1.10.0-scala_2.12

RUN curl https://archive.apache.org/dist/flink/flink-1.10.0/flink-1.10.0-bin-scala_2.12.tgz -o /opt/flink-1.10.0-bin-scala_2.12.tgz
RUN tar -xzf /opt/flink-1.10.0-bin-scala_2.12.tgz
RUN mv flink-1.10.0 /opt

RUN rm -rf /opt/flink/*
RUN rm -rf /opt/flink/plugins/*
RUN rm -rf /opt/flink/lib/*

RUN cp -R /opt/flink-1.10.0/* /opt/flink

RUN ls -ltra /opt/flink

RUN mv -v /opt/flink/opt/flink-metrics-*.jar /opt/flink/lib/
RUN mv -v /opt/flink/opt/flink-python*.jar /opt/flink/lib/
RUN mkdir -p /opt/flink/plugins/s3-fs-presto /opt/flink/plugins/s3-fs-hadoop
RUN mv -v /opt/flink/opt/flink-s3-fs-presto-*.jar /opt/flink/plugins/s3-fs-presto
RUN mv -v /opt/flink/opt/flink-s3-fs-hadoop-*.jar /opt/flink/plugins/s3-fs-hadoop
RUN mv -v /opt/flink/opt/flink-*.jar /opt/flink/plugins/

RUN wget https://repo.maven.apache.org/maven2/org/apache/flink/flink-shaded-hadoop-2-uber/2.8.3-10.0/flink-shaded-hadoop-2-uber-2.8.3-10.0.jar -P /opt/flink/lib/
RUN chown -R flink:flink /opt/flink/plugins/
RUN chown -R flink:flink /opt/flink/lib/