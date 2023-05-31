package com.cxx.project.grms.step4;

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
 * 计算用户的购买向量
 * @Date: 2018/4/9 11:46
 * 原始数据****************
 * 10001	20001	1
 * 10001	20002	1
 * 计算结果****************
 * 20001	10001:1,10004:1,10005:1
 * 20002	10001:1,10003:1,10004:1
 * 20003	10002:1
 * 20004	10002:1,10006:1
 * 20005	10001:1,10004:1
 * 20006	10001:1,10002:1,10004:1
 * 20007	10001:1,10003:1,10006:1
 */
public class GoodByList extends Configured implements Tool{
    //mapper
    public static class GoodByListMapper extends Mapper<LongWritable,Text,Text,Text>{
        @Override
        protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
            String[] strs = value.toString().split("\t");
            context.write(new Text(strs[1].trim()),new Text(strs[0].trim()));
        }
    }
    //reduce
   public static class GoodByListReduce extends Reducer<Text,Text,Text,Text> {
        @Override
        protected void reduce(Text key,Iterable<Text> values,Context context) throws IOException, InterruptedException {
            StringBuilder sb = new StringBuilder();
            for (Text value:values){
                sb.append(value.toString()+":"+1).append(",");
            }
            String result = sb.substring(0,sb.length()-1);
            context.write(key,new Text(result));
        }
    }
    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        /*Path input = new Path(conf.get("in"));
        Path output = new Path(conf.get("out"));*/
        Path input=new Path("/grms/rawdata/matrix.txt");
        Path output=new Path("/grms/rawdata/step3/");
        Job job = Job.getInstance(conf,this.getClass().getSimpleName());
        job.setJarByClass(this.getClass());

        job.setMapperClass(GoodByListMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,input);
        job.setReducerClass(GoodByListReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,output);
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new GoodByList(),args));
    }
}
