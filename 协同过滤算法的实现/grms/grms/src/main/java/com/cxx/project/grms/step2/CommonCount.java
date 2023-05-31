package com.cxx.project.grms.step2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
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
 * 计算商品的共现关系(列表)
 * @Date: 2018/4/9 11:46
 * 输入数据****************
 * 10001	20001,20005,20006,20007,20002
 * 10002	20006,20003,20004	1
 * 计算结果****************
 * 20001	20001
 * 20001	20001
 * 20001	20002....
 */
public class CommonCount extends Configured implements Tool{
    //mapper
    public static class CommonCountMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
        @Override
        protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
            String[] strs = value.toString().split("\t")[1].split(",");
            for (int x=0;x<strs.length;x++){
                for (int z=0;z<strs.length;z++){
                    context.write(new Text(strs[x]+"\t"+strs[z]),new IntWritable(1));
                }
            }
        }
    }
    //reduce
    public static class CommonCountReduce extends Reducer<Text,IntWritable,Text,IntWritable> {
        @Override
        protected void reduce(Text key,Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {
            int sum =0;
            for (IntWritable value:values){
                sum+=value.get();
            }
            context.write(key,new IntWritable(sum));
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        Path input=new Path("/grms/rawdata/222/part-r-00000");
        Path output=new Path("/grms/rawdata/2");
        /*Path input = new Path(conf.get("in"));
        Path output = new Path(conf.get("out"));*/
        Job job = Job.getInstance(conf,this.getClass().getSimpleName());
        job.setJarByClass(this.getClass());

        job.setMapperClass(CommonCountMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,input);

        job.setReducerClass(CommonCountReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,output);
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new CommonCount(),args));
    }
}
