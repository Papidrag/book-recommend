package com.cxx.project.grms.step3;

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
 * 计算商品的共现次数(共现矩阵)
 * @Date: 2018/4/9 11:46
 * 输入数据****************
 *  20001	20001
 *  20001	20001
 *  20001	20002
 * 计算结果****************
 *  20001	20001:3,20002:2,20005:2,20006:2,20007:1
 *  20002	20001:2,20002:3,20005:2,20006:2,20007:2
 *  20003	20003:1,20004:1,20006:1
 *  20004	20003:1,20004:2,20006:1,20007:1
 *  20005	20001:2,20002:2,20005:2,20006:2,20007:1
 *  20006	20001:2,20002:2,20003:1,20004:1,20005:2,20006:3,20007:1
 *  20007	20001:1,20002:2,20004:1,20005:1,20006:1,20007:3
 */
public class GoodLikeMatrix extends Configured implements Tool{
    //mapper
   public static class GoodLikeMatrixMapper extends Mapper<LongWritable,Text,Text,Text>{
        @Override
        protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
            String[] strs = value.toString().split("\t");
            context.write(new Text(strs[0]),new Text(strs[1]+":"+strs[2]));
        }
    }
    //reduce
    public static class GoodLikeMatrixReduce extends Reducer<Text,Text,Text,Text> {
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
        Path input=new Path("/grms/rawdata/2/");
        Path output=new Path("/grms/rawdata/3");
        /*Path input = new Path(conf.get("in"));
        Path output = new Path(conf.get("out"));*/
        Job job = Job.getInstance(conf,this.getClass().getSimpleName());
        job.setJarByClass(this.getClass());

        job.setMapperClass(GoodLikeMatrixMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,input);

        job.setReducerClass(GoodLikeMatrixReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,output);
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new GoodLikeMatrix(),args));
    }
}
