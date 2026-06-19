package gmart.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import static org.apache.spark.sql.functions.coalesce;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.from_json;
import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.when;
import org.apache.spark.sql.types.StructType;

public class Process {

    public static void main(String[] args) throws Exception {

        SparkSession spark = SparkSession.builder()
                .appName("KafkaToHudiInventory")
                .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .config("spark.sql.extensions",
                        "org.apache.spark.sql.hudi.HoodieSparkSessionExtension")
                .config("spark.sql.catalog.spark_catalog",
                        "org.apache.spark.sql.hudi.catalog.HoodieCatalog")

                .config("spark.hadoop.fs.s3a.endpoint", "http://minio:9000")
                .config("spark.hadoop.fs.s3a.access.key", "admin")
                .config("spark.hadoop.fs.s3a.secret.key", "password")
                .config("spark.hadoop.fs.s3a.path.style.access", "true")
                .config("spark.hadoop.fs.s3a.impl",
                        "org.apache.hadoop.fs.s3a.S3AFileSystem")
                .getOrCreate();

        Dataset<Row> kafkaDf = spark.readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "kafka:9092")
                .option("subscribe", "postgres.public.inventory")
                .option("startingOffsets", "earliest")
                .load();

        StructType inventorySchema = new StructType()
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
                .select(from_json(col("json"),
                        new StructType()
                                .add("before", inventorySchema)
                                .add("after", inventorySchema)
                                .add("op", "string")
                ).as("data"))
                .select(
                        coalesce(col("data.after.id"), col("data.before.id")).alias("id"),

                        coalesce(col("data.after.category"), col("data.before.category")).alias("category"),
                        coalesce(col("data.after.created"), col("data.before.created")).alias("created"),
                        coalesce(col("data.after.name"), col("data.before.name")).alias("name"),
                        coalesce(col("data.after.price"), col("data.before.price")).alias("price"),
                        coalesce(col("data.after.quantity"), col("data.before.quantity")).alias("quantity"),
                        coalesce(col("data.after.status"), col("data.before.status")).alias("status"),

                        coalesce(col("data.after.updated"), col("data.before.updated")).alias("updated"),

                        when(col("data.op").equalTo("d"), lit(true))
                        .otherwise(lit(false))
                        .alias("_hoodie_is_deleted")
                )
                .filter(col("id").isNotNull());

        String basePath = "s3a://warehouse/hudi/inventory";

        Map<String, String> hudiOpts = new HashMap<>();

        hudiOpts.put("hoodie.table.name", "inventory_hudi");
        hudiOpts.put("hoodie.datasource.write.recordkey.field", "id");
        hudiOpts.put("hoodie.datasource.write.precombine.field", "updated");
        hudiOpts.put("hoodie.datasource.write.partitionpath.field", "category");
        hudiOpts.put("hoodie.datasource.write.table.type", "COPY_ON_WRITE");
        hudiOpts.put("hoodie.datasource.write.operation", "upsert");
        hudiOpts.put("hoodie.datasource.hive_sync.enable", "false");
        hudiOpts.put("hoodie.datasource.write.payload.class","org.apache.hudi.common.model.DefaultHoodieRecordPayload");

        

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
