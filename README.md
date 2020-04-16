# Flink to S3

This example publishes records into S3 (Minio).

## Configurations

scala: 2.12

Apacha Flink: 1.10

Sbt: 1.2.8
## How to execute
First is necessary to generate the artifact of the application to do that execute the command `sbt assembly`, after that you can can start the Flink Cluster.

To start the Flink Cluster run the following commands:
1) docker-compose build
2) docker-compose up -d

Now the cluster is up and running and you can access the Flink UI (http://localhost:8081/) to upload and start the Flink Job.

The generated data will be written in Minio (http://localhost:9000/minio user: minio, passwd: minio123).
 
