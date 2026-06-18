## Apache Hudi CDC

This project demonstrates a real-time Change Data Capture (CDC) pipeline using Apache Hudi, where inventory changes from  PostgreSQL are streamed through Kafka and materialized into a lakehouse storage layer on MinIO.
Built using an open-source lakehouse stack with Apache Hudi as the core storage layer.

![Apache Spark](https://img.shields.io/badge/Apache%20Spark-3.5+-E25A1C?logo=apachespark&logoColor=white)
![Apache Hudi](https://img.shields.io/badge/Apache%20Hudi-Lakehouse-005571?logo=apache&logoColor=white)
![Kafka](https://img.shields.io/badge/Apache%20Kafka-3+-231F20?logo=apachekafka)
![Debezium](https://img.shields.io/badge/Debezium-CDC-6DB33F?logo=debezium)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-4169E1?logo=postgresql&logoColor=white)
![MinIO](https://img.shields.io/badge/MinIO-S3%20Storage-C72E49?logo=minio&logoColor=white)
![DuckDB](https://img.shields.io/badge/DuckDB-Analytics-FFF000?logo=duckdb&logoColor=black)
![Java](https://img.shields.io/badge/Java-17+-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3+-6DB33F?logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![Kafka Connect](https://img.shields.io/badge/Kafka%20Connect-CDC%20Pipeline-231F20?logo=apachekafka)
![REST API](https://img.shields.io/badge/REST%20API-SpringBoot-6DB33F)
![Swagger](https://img.shields.io/badge/API%20Docs-Swagger-85EA2D?logo=swagger&logoColor=black)




#### Architecture Diagram
<img width="1449" height="319" alt="hudi excalidraw" src="https://github.com/user-attachments/assets/b3894508-b20c-4dd3-8c40-a7ad60256c0e" />

#### Getting Started

#### Build & Start Infrastructure
```sh
docker compose build --no-cache
```

```sh
docker compose up -d
```
#### Build Application

```sh
mvn clean compile
mvn clean package -DskipTests
```
#### Run Spring Boot API

```sh
mvn spring-boot:run
```

#### API Documentation
```sh
http://localhost:8181/swagger-ui/index.html
```

####  Generate Sample Data
```sh
http POST http://localhost:8181/api/v1/inventory \
name="Logitech MX Master 3S" \
category="Accessories" \
quantity:=40 \
price:=99.99


http POST http://localhost:8181/api/v1/inventory \
name="Dell Latitude 5440" \
category="Electronics" \
quantity:=15 \
price:=850.00

http POST http://localhost:8181/api/v1/inventory \
name="HP EliteBook 840" \
category="Electronics" \
quantity:=10 \
price:=920.00


http POST http://localhost:8181/api/v1/inventory \
name="Samsung 27 Inch Monitor" \
category="Electronics" \
quantity:=12 \
price:=280.00


http POST http://localhost:8181/api/v1/inventory \
name="Office Chair" \
category="Furniture" \
quantity:=25 \
price:=120.00


http POST http://localhost:8181/api/v1/inventory \
name="Standing Desk" \
category="Furniture" \
quantity:=8 \
price:=350.00


http POST http://localhost:8181/api/v1/inventory \
name="Logitech MX Master 3S" \
category="Accessories" \
quantity:=40 \
price:=99.99


http POST http://localhost:8181/api/v1/inventory \
name="USB-C Docking Station" \
category="Accessories" \
quantity:=18 \
price:=145.00

```
#### PostgreSQL Verification

```sh
docker exec -it db  psql -U root -d demo
```
```sh
select count(*) from inventory;
```

#### Update & Delete Examples

Update inventory item
```sh
http PUT http://localhost:8181/api/v1/inventory/<id> \
status="OUT_OF_STOCK"

```

Delete inventory item
```sh
http DELETE http://localhost:8181/api/v1/inventory/<id>

```

#### Kafka Connect

Create connector
```sh
curl -X POST http://localhost:8083/connectors \
-H "Content-Type: application/json" \
-d @postgres-source.json | jq
```

Check status

```sh
curl http://localhost:8083/connectors/postgres-source/status | jq
```

```sh
curl http://localhost:8083/connectors/postgres-source/offsets | jq

```
Kafka UI (AKHQ)

```sh
http://localhost:8087
```


#### Spark Job

Copy JAR into Spark container

```sh
docker cp \
target/gmart-0.0.1-SNAPSHOT.jar \
spark-master:/opt/spark/
```

Log in spark container

```sh
docker compose exec -it  spark-master bash
```

Submit job
```sh
/opt/spark/bin/spark-submit \
  --master spark://spark-master:7077 \
  --class gmart.process.Process \
  --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.1 \
  --conf spark.jars.ivy=/tmp/ivy \
  /opt/spark/gmart-0.0.1-SNAPSHOT.jar
```

Spark UI

```sh
http://localhost:8080
```

#### Query Lakehouse with DuckDB

```sh
docker compose exec -it duckdb bash
```
```sh
duckdb
```
Enable S3

```sh
INSTALL httpfs;
LOAD httpfs;

SET s3_endpoint='minio:9000';
SET s3_access_key_id='admin';
SET s3_secret_access_key='password';
SET s3_use_ssl=false;
SET s3_url_style='path';
```

Run Queries

```sh
SELECT COUNT(*)
FROM read_parquet('s3://warehouse/**/*.parquet');
```

Verify data 
```sh
SELECT *
FROM read_parquet('s3://warehouse/**/*.parquet')
LIMIT 10;
```

```sh
SELECT COUNT(DISTINCT _hoodie_record_key)
FROM read_parquet('s3://warehouse/hudi/inventory/**/*.parquet');

```


#### Reset Hudi Table

```sh
mc rm --recursive --force local/warehouse/hudi/inventory
mc rm --recursive --force local/warehouse/checkpoints/inventory
```

