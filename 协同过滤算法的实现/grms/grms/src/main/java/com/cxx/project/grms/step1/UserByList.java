package com.cxx.project.grms.step1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * @Author: cxx
 * 用户购买列表
 * @Date: 2018/4/9 11:46
 * 原始数据****************
 * 10001	20001	1
 * 10001	20002	1
 * 计算结果****************
 * 10001	20001,20005,20006,20007,20002
 * 10002	20006,20003,20004
 */

public class UserByList extends Configured implements Tool{
    //mapper
    public static class UserByListMapper
            extends Mapper<LongWritable,Text,Text,Text>{
        @Override
        protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
            String[] strs = value.toString().split("\t");
            context.write(new Text(strs[0].trim()),new Text(strs[1].trim()));
        }
    }
    //reduce
    public static class UserByListReduce extends Reducer<Text,Text,Text,Text> {
        @Override
        protected void reduce(Text key,Iterable<Text> values,Context context) throws IOException, InterruptedException {
            StringBuilder sb = new StringBuilder();
            for (Text value:values){
                sb.append(value.toString()).append(",");
            }
            String result = sb.substring(0,sb.length()-1);
            context.write(key,new Text(result));
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        Path input=new Path("/grms/rawdata/matrix.txt");
        Path output=new Path("/grms/rawdata/222");
        Job job = Job.getInstance(conf,this.getClass().getSimpleName());
        job.setJarByClass(this.getClass());

        job.setMapperClass(UserByListMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,input);

        job.setReducerClass(UserByListReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,output);
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new UserByList(),args));
    }
}
