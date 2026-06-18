## Apache Hudi CDC



#### Architecture Diagram
<img width="1449" height="319" alt="hudi excalidraw" src="https://github.com/user-attachments/assets/b3894508-b20c-4dd3-8c40-a7ad60256c0e" />


```sh
http://localhost:8181/swagger-ui/index.html
```


```sh
docker exec -it db  psql -U root -d demo
```

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

http POST http://localhost:8080/api/v1/inventory \
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

```sh
http PUT http://localhost:8181/api/v1/inventory/537803de-d6fa-4e25-a866-19bc30f142ae status="OUT_OF_STOCK"

```


```sh
http DELETE http://localhost:8181/api/v1/inventory/537803de-d6fa-4e25-a866-19bc30f142ae 

```


```sh
curl http://localhost:8083/connectors/postgres-source/status | jq
```

```sh
curl -X POST http://localhost:8083/connectors \
-H "Content-Type: application/json" \ 
-d @postgres-source.json | jq
```
```sh
curl http://localhost:8083/connectors/postgres-source/offsets | jq

```


```sh
docker cp \
target/gmart-0.0.1-SNAPSHOT.jar \
spark-master:/opt/spark/
```

```sh
/opt/spark/bin/spark-submit \
  --master spark://spark-master:7077 \
  --class gmart.process.Process \
  --packages org.apache.spark:spark-sql-kafka-0-10_2.12:3.5.1 \
  --conf spark.jars.ivy=/tmp/ivy \
  /opt/spark/gmart-0.0.1-SNAPSHOT.jar
```

Duckdb configure minio

```sh
INSTALL httpfs;
LOAD httpfs;

SET s3_endpoint='minio:9000';
SET s3_access_key_id='admin';
SET s3_secret_access_key='password';
SET s3_use_ssl=false;
SET s3_url_style='path';
```

Query  Duckdb

```sh
SELECT COUNT(*)
FROM read_parquet('s3://warehouse/**/*.parquet');
```

```sh
SELECT *
FROM read_parquet('s3://warehouse/**/*.parquet')
LIMIT 10;
```
