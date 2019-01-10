package com.example.personal.server;

import com.example.personal.until.FileTool;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.List;

public class SparkSqlServer {

    private SparkSession sparkSession = null;

    public SparkSqlServer(){
        getInstance();
    }

    public SparkSession getInstance() {
            sparkSession = SparkSession
                    .builder().master("local[2]").appName("Java Spark SQL basic example")
                    .getOrCreate();
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
            Dataset<Row> dataset = parsingFileBySuffic(files.get(i));
            if (dataset != null){
                list1.addAll(dataset.collectAsList());
                if (list == null){
                    list = dataset;
                }
            }
        }
        Dataset<Row> dataFrame = this.sparkSession.createDataFrame(list1, list.schema());
        dataFrame.createTempView("qs");
        this.sparkSession.sql(sql).write().json(output);
    }

    /**
     * 根据文件后缀生成对应的Dataset
     * @param filePath
     * @return
     */
    public Dataset<Row> parsingFileBySuffic(String filePath){
        String suffic = FileTool.getFileSuffic(filePath);
        Dataset<Row> dataset = null;
        if (suffic.equals("json")){
            dataset = this.sparkSession.read().json(filePath);
        } else if (suffic.equals("csv")){
            dataset = this.sparkSession.read().format("csv")
                    .option("sep", ";")
                    .option("inferSchema", "true")
                    .option("header", "true")
                    .load(filePath);
        } else if (suffic.equals("parquet")){
            dataset = this.sparkSession.read().parquet(filePath);
        } else if (suffic.equals("orc")){
            dataset = this.sparkSession.read().orc(filePath);
        }
        return dataset;
    }

    public void close(){
        sparkSession.close();
    }

}