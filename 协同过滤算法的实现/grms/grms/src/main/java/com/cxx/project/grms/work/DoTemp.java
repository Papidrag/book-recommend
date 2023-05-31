package com.cxx.project.grms.work;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
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
 * @Date: 2018/4/13 14:49
 */
public class DoTemp extends Configured implements Tool
{


    static class DoTempMapper extends
        Mapper<LongWritable,Text,Text,DoubleWritable> {
        WeatherParser parser=new WeatherParser();
        @Override
        protected void map(LongWritable key,Text value,Context context)
                throws IOException, InterruptedException{
            if(parser.parse(value)){
                String sid=parser.getSid();
                double tem=parser.getTem();
                int year = parser.getYear();
                System.out.println(sid+"::::"+tem+":::"+year);
                context.write(new Text(year+":"+sid),new DoubleWritable(tem));
            }
        }
    }


    static class DoTempReducer extends
        Reducer<Text,DoubleWritable,Text,Text> {
        @Override
        protected void reduce(Text key,Iterable<DoubleWritable> values,
            Context context) throws IOException, InterruptedException{
            int num=0;
            double sum=0;
            double minValue = Double.MAX_VALUE;
            double maxValue = Double.MIN_VALUE;
            for(DoubleWritable value : values){
                num++;
                sum+=value.get();
                minValue = Math.min(minValue,value.get());
                maxValue = Math.max(maxValue,value.get());
            }
            System.out.println(sum+":::"+num);
            context.write(key,new Text("最低温度："+
                    minValue/10+"℃\t"+"最高温度："+
                    maxValue/10+"℃\t"+"平均温度："+
                    sum/num/10+"℃\t"));
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        Job job = Job.getInstance(conf,getClass().getSimpleName());
        job.setJarByClass(this.getClass());
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);
        job.setMapperClass(DoTempMapper.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,new Path(strings[0]));

        job.setReducerClass(DoTempReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,new Path(strings[1]));
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        String[] paths = {"/weather/","/wr/1"};
        System.exit(ToolRunner.run(new DoTemp(),paths));
    }
}
