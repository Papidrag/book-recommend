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

public class AvgTempByCombiner extends Configured implements Tool{

    static class AvgTempByCombinerMapper extends Mapper<LongWritable,Text,Text,AvgValue>{
        WeatherParser parser=new WeatherParser();
        private AvgValue av=new AvgValue();

        @Override
        protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{
            if(parser.parse(value)){
                String sid=parser.getSid();
                double tem=parser.getTem();
                av.setNum(1);
                av.setValue(tem);
                System.out.println(av);
                context.write(new Text(sid),av);
            }
        }
    }

    static class AvgTempByCombinerCombiner extends Reducer<Text,AvgValue,Text,AvgValue>{
        private AvgValue av=new AvgValue();
        @Override
        protected void reduce(Text key,Iterable<AvgValue> values,Context context) throws IOException, InterruptedException{
            int snum=0;
            double svalue=0;
            for(AvgValue av : values){
                snum+=av.getNum().get();
                svalue+=av.getValue().get()*av.getNum().get();
            }
            this.av.setNum(snum);
            this.av.setValue(svalue/snum);
            context.write(key,this.av);
        }
    }

    static class AvgTempByCombinerReducer extends Reducer<Text,AvgValue,Text,DoubleWritable>{
        @Override
        protected void reduce(Text key,Iterable<AvgValue> values,Context context) throws IOException, InterruptedException{
            int snum=0;
            double svalue=0;
            for(AvgValue av : values){
                snum+=av.getNum().get();
                svalue+=av.getValue().get()*av.getNum().get();
            }
            context.write(key,new DoubleWritable(svalue/snum));
        }
    }

    @Override
    public int run(String[] strings) throws Exception{
        JobUtil.setConf(this.getConf(),this.getClass(),this.getClass().getSimpleName(),strings[0],strings[1]);
        JobUtil.setMapper(AvgTempByCombinerMapper.class,Text.class,AvgValue.class,TextInputFormat.class);
        JobUtil.setReducer(AvgTempByCombinerReducer.class,Text.class,DoubleWritable.class,TextOutputFormat.class);
        JobUtil.setCombiner(true,AvgTempByCombinerCombiner.class);
        return JobUtil.commit();
    }
}
