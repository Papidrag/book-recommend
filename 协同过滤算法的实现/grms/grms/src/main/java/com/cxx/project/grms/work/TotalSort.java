package com.cxx.project.grms.work;

import java.io.IOException;
import java.net.URI;
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
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler.RandomSampler;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler.Sampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class TotalSort extends Configured implements Tool{
    static class TotalSortMapper extends Mapper<LongWritable,Text, LongWritable,Text>{
        @Override
        protected void map(LongWritable key,Text value,Context context) throws IOException, InterruptedException{
            String[] strs=value.toString().split(",");
            context.write(new LongWritable(1),new Text("1"));
        }
    }

    static class TotalSortReducer extends Reducer<LongWritable,Text,LongWritable,Text> {
        @Override
        protected void reduce(LongWritable key, Iterable<Text> values,Context context) throws IOException, InterruptedException{
            StringBuilder str=new StringBuilder();
            for(Text value : values){
                str.append(value.toString()).append(",");
            }
            context.write(new LongWritable(1),new Text(str.toString()));
        }
    }



    @Override
    public int run(String[] strings) throws Exception{
        Configuration conf=getConf();
        Path in=new Path(strings[0]);
        Path out=new Path(conf.get(strings[1]));

        Job job=Job.getInstance(conf,this.getClass().getSimpleName());
        job.setJarByClass(this.getClass());

        job.setMapperClass(TotalSortMapper.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,in);

        job.setReducerClass(TotalSortReducer.class);
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,out);
        job.setNumReduceTasks(5);

        job.setPartitionerClass(TotalOrderPartitioner.class);
        Sampler<Text,LongWritable> sampler= new RandomSampler<Text, LongWritable>(0.8,1000,6);
        InputSampler.writePartitionFile(job,sampler);
        String file=TotalOrderPartitioner.getPartitionFile(conf);
        job.addCacheFile(new URI(file));
        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception{
        System.exit(ToolRunner.run(new TotalSort(),args));
    }
}
