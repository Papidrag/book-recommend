package com.cxx.project.grms.step6;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.Iterator;

/**
 * @Author: cxx
 * @Date: 2018/4/9 17:35
 * 数据去重，在推荐结果中去掉用户已购买的商品信息
 * @Date: 2018/4/9 11:46
 * 输入数据****************
 * --step5
 * 10005,20007	1
 * 10006,20001	1
 * 10006,20002	2
 * --原始数据--
 * 10001	20001	1
 * 10001	20002	1
 * 计算结果****************
 * 10001	20004	2
 * 10001	20003	1
 * 10002	20002	2
 */
public class CutRepeat extends Configured implements Tool {

    //2个map
    public static class CutRepeatMapper1 extends Mapper<LongWritable,Text,Text,Text>{
        @Override
        protected void map(LongWritable key,Text value,Context context)
            throws IOException, InterruptedException {
            //原数据
            String[] strs = value.toString().split("\t");
            context.write(new Text(strs[0]+"\t"+strs[1]),
                          new Text("*"));
        }
    }

    public static class CutRepeatMapper2 extends Mapper<LongWritable,Text,Text,Text>{
        @Override
        protected void map(LongWritable key,Text value,Context context)
            throws IOException, InterruptedException {
            //推荐数据
            String[] strs = value.toString().split("\t");
            String[] strs1 = strs[0].split(",");
            context.write(new Text(strs1[0]+"\t"+strs1[1]),
                          new Text(strs[1]));
        }
    }
    //reduce
    public static class CutRepeatReduce extends Reducer<Text,Text,Text,Text>{
        @Override
        protected void reduce(Text key,Iterable<Text> values,Context context)
            throws IOException, InterruptedException {
            Iterator<Text> iterator = values.iterator();
            Text next = iterator.next();
            if(!iterator.hasNext()){
                context.write(key,new Text(next));
            }
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        Path input1=new Path("/grms/rawdata/matrix.txt/");
        Path input2=new Path("/grms/rawdata/4/part-r-00000");
        Path output=new Path("/grms/rawdata/6");
        Job job = Job.getInstance(conf,getClass().getSimpleName());
        job.setJarByClass(CutRepeat.class);

        job.setMapperClass(CutRepeatMapper1.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        MultipleInputs.addInputPath(job,input1,TextInputFormat.class,
                CutRepeatMapper1.class);
        MultipleInputs.addInputPath(job,input2,TextInputFormat.class,
                CutRepeatMapper2.class);
        job.setReducerClass(CutRepeatReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,output);
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new CutRepeat(),args));
    }
}
