package com.cxx.project.grms.step5;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
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

/**
 * @Author: cxx
 * 矩阵相乘并相加（推荐结果）
 * @Date: 2018/4/9 17:35
 * 输入数据*************
 * 商品共现矩阵step3
 * 用户购买向量step4
 * 计算结果*************
 * 10001,20001	2
 * 10001,20001	2
 * 10001,20001	3
 * .....
 */
public class MultiplyMatrix extends Configured implements Tool {

    //2个map
    public static class MultiplyMatrixMapper1 extends Mapper<LongWritable,Text,Text,Text>{
        @Override
        protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
            //物品相似矩阵
            String[] strs = value.toString().split("\t");
            context.write(new Text(strs[0]),new Text("s"+strs[1]));
        }
    }

   public static class MultiplyMatrixMapper2 extends Mapper<LongWritable,Text,Text,Text>{
        @Override
        protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException {
            //物品向量
            String[] strs = value.toString().split("\t");
            context.write(new Text(strs[0]),new Text("t"+strs[1]));
        }
    }
    //reduce
    public static class MultiplyMatrixReduce extends Reducer<Text,Text,Text,IntWritable>{
        @Override
        protected void reduce(Text key,Iterable<Text> values,Context context) throws IOException, InterruptedException {
            String[] str1 = null;
            String[] str2 = null;
            for (Text value:values){
                String flag = value.toString().substring(0,1);
                String result = value.toString().substring(1);
                if (flag.equals("s")){
                    str1 = result.split(",");
                }else if (flag.equals("t")){
                    str2 = result.split(",");
                }
            }

            for (int i=0;i<str1.length;i++){
                for (int j=0;j<str2.length;j++){
                    String u = str1[i].split(":")[0];
                    int uV = Integer.valueOf(str1[i].split(":")[1]);
                    String g = str2[j].split(":")[0];
                    int gV = Integer.valueOf(str2[j].split(":")[1]);
                    context.write(new Text(g+","+u),new IntWritable(uV*gV));
                }
            }
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        Path input1=new Path("/grms/rawdata/3/");
        Path input2=new Path("/grms/rawdata/step3/");
        Path output=new Path("/grms/rawdata/4");
        Job job = Job.getInstance(conf,getClass().getSimpleName());
        job.setJarByClass(MultiplyMatrix.class);

        job.setMapperClass(MultiplyMatrixMapper1.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        MultipleInputs.addInputPath(job,input1,TextInputFormat.class,
                MultiplyMatrixMapper1.class);
        MultipleInputs.addInputPath(job,input2,TextInputFormat.class,
                MultiplyMatrixMapper2.class);
        job.setReducerClass(MultiplyMatrixReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,output);
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        System.exit(ToolRunner.run(new MultiplyMatrix(),args));
    }
}
