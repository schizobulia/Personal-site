package com.example.personal.server;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.List;

public class SparkSqlServer {

    private static SparkSession sparkSession = null;

    public SparkSqlServer(){
        getInstance();
    }

    public SparkSession getInstance() {
//       if (sparkSession == null) {
            sparkSession = SparkSession
                    .builder().master("local[2]").appName("Java Spark SQL basic example")
                    .getOrCreate();
//       }
        return sparkSession;
    }

    /**
     * 将多个json文件使用sql过滤并生成新的文件
     * @param files  文件路径
     * @param sql    sql语句
     * @param output 输出文件的路径
     * @throws AnalysisException
     */
    public void parsingMoreJson(List<String> files, String sql, String output) throws AnalysisException {
        Dataset<Row> list = null;
        List<Row> list1 = new ArrayList<Row>();
        for (int i = 0; i < files.size(); i++) {
            Dataset<Row> dataset = sparkSession.read().json(files.get(i));
            list1.addAll(dataset.collectAsList());
            if (list == null){
                list = dataset;
            }
        }
        Dataset<Row> dataFrame = sparkSession.createDataFrame(list1, list.schema());
        dataFrame.createTempView("qs");
        sparkSession.sql(sql).write().json(output);
    }

    public void close(){
        sparkSession.close();
    }

}