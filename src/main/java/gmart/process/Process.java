package gmart.process;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.from_json;
import org.apache.spark.sql.types.StructType;

public class Process {

    public static void main(String[] args) throws Exception {

        SparkSession spark = SparkSession.builder()
                .appName("KafkaToHudiInventory")
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .config("spark.sql.extensions", "org.apache.spark.sql.hudi.HoodieSparkSessionExtension")
                .config("spark.sql.catalog.spark_catalog", "org.apache.spark.sql.hudi.catalog.HoodieCatalog")

                .config("spark.hadoop.fs.s3a.endpoint", "http://minio:9000")
                .config("spark.hadoop.fs.s3a.access.key", "admin")
                .config("spark.hadoop.fs.s3a.secret.key", "password")
                .config("spark.hadoop.fs.s3a.path.style.access", "true")
                .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
                .getOrCreate();


        Dataset<Row> kafkaDf = spark.readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "kafka:9092")
                .option("subscribe", "postgres.public.inventory")
                .option("startingOffsets", "earliest")
                .load();


        StructType schema = new StructType()
                .add("id", "string")
                .add("category", "string")
                .add("created", "string")
                .add("name", "string")
                .add("price", "double")
                .add("quantity", "integer")
                .add("status", "string")
                .add("updated", "string");


        Dataset<Row> parsed = kafkaDf
                .selectExpr("CAST(value AS STRING) as json")
                .select(from_json(col("json"), new StructType()
                        .add("after", schema)
                        .add("op", "string")
                ).as("data"))
                .select("data.after.*", "data.op");


        String basePath = "s3a://warehouse/hudi/inventory";

        java.util.Map<String, String> hudiOpts = new java.util.HashMap<>();

        hudiOpts.put("hoodie.table.name", "inventory_hudi");
        hudiOpts.put("hoodie.datasource.write.recordkey.field", "id");
        hudiOpts.put("hoodie.datasource.write.precombine.field", "updated");
        hudiOpts.put("hoodie.datasource.write.partitionpath.field", "category");
        hudiOpts.put("hoodie.datasource.write.table.type", "COPY_ON_WRITE");
        hudiOpts.put("hoodie.datasource.write.operation", "upsert");

        hudiOpts.put("hoodie.datasource.hive_sync.enable", "false");


        parsed.writeStream()
                .foreachBatch((batch, batchId) -> {
                    batch.write()
                            .format("hudi")
                            .options(hudiOpts)
                            .mode("append")
                            .save(basePath);
                })
                .option("checkpointLocation", "s3a://warehouse/checkpoints/inventory")
                .start()
                .awaitTermination();
    }
}