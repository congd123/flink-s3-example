package com.examples.s3

import com.typesafe.scalalogging.LazyLogging
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.{Schema, SchemaBuilder}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.util.Collector
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.s3a.Constants._
import org.apache.parquet.avro.AvroParquetWriter
import org.apache.parquet.hadoop.ParquetFileWriter

object GenericRecordSchema {
  val schema = SchemaBuilder
    .record("person")
    .fields()
    .requiredString("id")
    .requiredString("name")
    .endRecord()
}

object Main extends App with LazyLogging {

  private val env = StreamExecutionEnvironment.getExecutionEnvironment
  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

  val persons = 0 to 4 map (i => {
    new GenericData.Record(GenericRecordSchema.schema) {
      put("id", s"id-$i")
      put("name", s"name $i")
    }
  })

  val ds: DataStream[String] = env
    .fromElements(persons(0), persons(1), persons(2), persons(3), persons(4))
    .flatMap((value: GenericRecord, out: Collector[String]) => {
      val writer = AvroParquetWriter
        .builder[GenericRecord](new Path(s"s3a://data/persons/id=xpto/person${value.get("id")}.parquet"))
        .withConf(new org.apache.hadoop.conf.Configuration() {
          set(ENDPOINT, "http://minio:9000")
          set(ACCESS_KEY, "minio")
          set(SECRET_KEY, "minio123")
          set(MAX_ERROR_RETRIES, "0")
          setBoolean(PATH_STYLE_ACCESS, true)
        })
        .withSchema(new Schema.Parser().parse(GenericRecordSchema.schema.toString))
        .withDataModel(GenericData.get)
        .withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
        .build()

      writer.write(value)
      writer.close()

      value.get("name")
    })
    .returns(classOf[String])

  ds.print()

  env.execute()
}
