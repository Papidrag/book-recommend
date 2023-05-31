package com.cxx.project.grms.step7;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapred.lib.db.DBConfiguration;
import org.apache.hadoop.mapred.lib.db.DBOutputFormat;
import org.apache.hadoop.mapred.lib.db.DBWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: cxx
 * 将推荐结果保存到MySQL数据库中
 * @Date: 2018/4/17 10:31
 */
public class WriteDataToMysql extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        Path input=new Path("/grms/rawdata/6");
        DBConfiguration.configureDB(conf,
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/grms",
                "root",
                "000000");
        Job job = Job.getInstance(conf,"test-mysql");
        job.setJarByClass(WriteDataToMysql.class);


        job.setMapperClass(ConnMysqlMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,input);

        job.setReducerClass(ConnMysqlReducer.class);
        job.setOutputFormatClass(DBOutputFormat.class);
        DBOutputFormat.setOutput(job,
                "results",
                "uid","gid","expect");
//        job.setOutputKeyClass(TblsWritable.class);
        return job.waitForCompletion(true)?0:1;
    }

    //重写DBWriter
    public static class TblsWritable implements Writable,DBWritable{
        String uid;
        String gid;
        Integer expect;

        public TblsWritable(){

        }

        public TblsWritable(String uid, String gid, Integer expect) {
            this.uid = uid;
            this.gid = gid;
            this.expect = expect;
        }

        @Override
        public void write(DataOutput dataOutput) throws IOException {
            dataOutput.writeUTF(this.uid);
            dataOutput.writeUTF(this.gid);
            dataOutput.write(this.expect);
        }

        @Override
        public void readFields(DataInput dataInput) throws IOException {
            this.uid=dataInput.readUTF();
            this.gid=dataInput.readUTF();
            this.expect=dataInput.readInt();
        }

        @Override
        public void write(PreparedStatement prep) throws SQLException {
            prep.setString(1,this.uid);
            prep.setString(2,this.gid);
            prep.setInt(3,this.expect);
        }

        @Override
        public void readFields(ResultSet rs) throws SQLException {
            this.uid=rs.getString(1);
            this.gid=rs.getString(2);
            this.expect=rs.getInt(3);
        }
    }



    //maperduce
    public static class ConnMysqlMapper extends Mapper<LongWritable,Text,Text,Text>{
        @Override
        protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
            String[] strs = value.toString().split("\t");
            context.write(new Text(strs[0].trim()+","+strs[1].trim()),new Text(strs[2].trim()));
        }
    }


    public static class ConnMysqlReducer extends Reducer<Text,Text,TblsWritable,NullWritable>{
        @Override
        public void reduce(Text key,Iterable<Text> values,Context context) throws IOException, InterruptedException {
            for (Text value:values){
                String uid = key.toString().split(",")[0].trim();
                String gid = key.toString().split(",")[1].trim();
                Integer expect = Integer.parseInt(value.toString().trim());
                context.write(new TblsWritable(uid,gid,expect),null);
            }
        }
    }

    public static void main(String[] args) throws Exception {
       System.exit(ToolRunner.run(new WriteDataToMysql(),args));
    }
}
