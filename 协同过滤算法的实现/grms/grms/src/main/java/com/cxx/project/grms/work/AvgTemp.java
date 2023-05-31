package com.cxx.project.grms.work;

import java.io.IOException;

import com.cxx.project.grms.utils.JobUtil;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class AvgTemp extends Configured implements Tool{

    static class AvgTempMapper extends Mapper<LongWritable,Text,Text,DoubleWritable>{
        WeatherParser parser=new WeatherParser();
        @Override
        protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{
            if(parser.parse(value)){
                String sid=parser.getSid();
                double tem=parser.getTem();
                System.out.println(sid+"::::"+tem);
                context.write(new Text(sid),new DoubleWritable(tem));
            }
        }
    }

    static class AvgTempReducer extends Reducer<Text,DoubleWritable,Text,DoubleWritable>{
        @Override
        protected void reduce(Text key,Iterable<DoubleWritable> values,Context context) throws IOException, InterruptedException{
            double sum=0;
            int num=0;
            for(DoubleWritable value : values){
                num++;
                sum+=value.get();
            }
            System.out.println(sum+":::"+num);
            context.write(key,new DoubleWritable(sum/num));
        }
    }


    @Override
    public int run(String[] strings) throws Exception{
        JobUtil.setConf(this.getConf(),this.getClass(),this.getClass().getSimpleName(),strings[0],strings[1]);
        JobUtil.setMapper(AvgTempMapper.class,Text.class,DoubleWritable.class,TextInputFormat.class);
        JobUtil.setReducer(AvgTempReducer.class,Text.class,DoubleWritable.class,TextOutputFormat.class);
        return JobUtil.commit();
    }

    public static void main(String[] args) throws Exception {
        String[] paths = {"/weather/","/wr/1"};
        System.exit(ToolRunner.run(new AvgTemp(),paths));
    }
}
