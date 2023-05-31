package com.cxx.project.grms.step8;

import java.util.Properties;

import com.cxx.project.grms.step1.UserByList;
import com.cxx.project.grms.step2.CommonCount;
import com.cxx.project.grms.step3.GoodLikeMatrix;
import com.cxx.project.grms.step4.GoodByList;
import com.cxx.project.grms.step5.MultiplyMatrix;
import com.cxx.project.grms.step6.CutRepeat;
import com.cxx.project.grms.step7.WriteDataToMysql;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 作业控制
 * 构建作业流对象（JobControl），让程序自行提交作业
 * --------------步骤--------------
 * 1.分别创建step1到step7的Job对象，然后进行各自的作业配置。
 * 2.创建7个ControlledJob对象，将上一步的Job对象转化成可被控制的作业。
 * 3.对可被控制的作业添加依赖关系。
 * 4.构建JobControl对象，将7个可被控制的作业逐个添加。
 * 5.构建线程对象，并启动线程，执行作业。
 */
public class JobController extends Configured implements Tool{
	@Override
	public final int run(String[] args) throws Exception{
		Configuration conf=getConf();
		//路径设置
		Path in1=new Path(conf.get("in1"));
		Path out1=new Path(conf.get("out1"));
		Path out2=new Path(conf.get("out2"));
		Path out3=new Path(conf.get("out"));
		Path out4=new Path(conf.get("out"));
		Path out5=new Path(conf.get("out5"));
		Path out6=new Path(conf.get("out6"));

		//----第1步作业配置--------------------
		Job job1=Job.getInstance(conf,UserByList.class.getSimpleName());
		job1.setJarByClass(this.getClass());

		job1.setMapperClass(UserByList.UserByListMapper.class);
		job1.setMapOutputKeyClass(Text.class);
		job1.setMapOutputValueClass(Text.class);
		job1.setInputFormatClass(TextInputFormat.class);
		// 数据来源：原始数据
		TextInputFormat.addInputPath(job1,in1);

		job1.setReducerClass(UserByList.UserByListReduce.class);
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);
		job1.setOutputFormatClass(TextOutputFormat.class);
		TextOutputFormat.setOutputPath(job1,out1);

		//----第2步作业配置--------------------
		Job job2=Job.getInstance(conf,CommonCount.class.getSimpleName());
		job2.setJarByClass(this.getClass());

		job2.setMapperClass(CommonCount.CommonCountMapper.class);
		job2.setMapOutputKeyClass(Text.class);
		job2.setMapOutputValueClass(IntWritable.class);
		job2.setInputFormatClass(TextInputFormat.class);
		// 数据来源：第1步的计算结果
		TextInputFormat.addInputPath(job2,out1);

		job2.setReducerClass(CommonCount.CommonCountReduce.class);
		job2.setOutputKeyClass(Text.class);
		job2.setOutputValueClass(IntWritable.class);
		job2.setOutputFormatClass(TextOutputFormat.class);
		TextOutputFormat.setOutputPath(job2,out2);

		//----第3步作业配置--------------------
		Job job3=Job.getInstance(conf,GoodLikeMatrix.class.getSimpleName());
		job3.setJarByClass(this.getClass());

		job3.setMapperClass(GoodLikeMatrix.GoodLikeMatrixMapper.class);
		job3.setMapOutputKeyClass(Text.class);
		job3.setMapOutputValueClass(Text.class);
		job3.setInputFormatClass(TextInputFormat.class);
		// 数据来源：第2步的计算结果
		TextInputFormat.addInputPath(job3,out2);

		job3.setReducerClass(GoodLikeMatrix.GoodLikeMatrixReduce.class);
		job3.setOutputKeyClass(Text.class);
		job3.setOutputValueClass(Text.class);
		job3.setOutputFormatClass(TextOutputFormat.class);
		TextOutputFormat.setOutputPath(job3,out3);

		//----第4步作业配置--------------------
		Job job4=Job.getInstance(conf,GoodByList.class.getSimpleName());
		job4.setJarByClass(this.getClass());

		job4.setMapperClass(GoodByList.GoodByListMapper.class);
		job4.setMapOutputKeyClass(Text.class);
		job4.setMapOutputValueClass(Text.class);
		job4.setInputFormatClass(TextInputFormat.class);
		// 数据来源：第1步的计算结果或者原始数据
		TextInputFormat.addInputPath(job4,out1);

		job4.setReducerClass(GoodByList.GoodByListReduce.class);
		job4.setOutputKeyClass(Text.class);
		job4.setOutputValueClass(Text.class);
		job4.setOutputFormatClass(TextOutputFormat.class);
		TextOutputFormat.setOutputPath(job4,out4);

		//----第5步作业配置--------------------
		Job job5=Job.getInstance(conf,MultiplyMatrix.class.getSimpleName());
		job5.setJarByClass(this.getClass());

		// 数据来源：第1步的计算结果或者原始数据
		MultipleInputs.addInputPath(job5,out3,TextInputFormat.class,MultiplyMatrix.MultiplyMatrixMapper1.class);
		MultipleInputs.addInputPath(job5,out4,TextInputFormat.class,MultiplyMatrix.MultiplyMatrixMapper2.class);
		job5.setMapOutputKeyClass(Text.class);
		job5.setMapOutputValueClass(Text.class);

		job5.setReducerClass(MultiplyMatrix.MultiplyMatrixReduce.class);
		job5.setOutputKeyClass(Text.class);
		job5.setOutputValueClass(IntWritable.class);
		job5.setOutputFormatClass(TextOutputFormat.class);
		TextOutputFormat.setOutputPath(job5,out5);

		//----第6步作业配置--------------------
		Job job6=Job.getInstance(conf,CutRepeat.class.getSimpleName());
		job6.setJarByClass(this.getClass());

		MultipleInputs.addInputPath(job6,out1,TextInputFormat.class,CutRepeat.CutRepeatMapper1.class);
		MultipleInputs.addInputPath(job6,out5,TextInputFormat.class,CutRepeat.CutRepeatMapper2.class);
		job6.setMapOutputKeyClass(Text.class);
		job6.setMapOutputValueClass(Text.class);

		job6.setReducerClass(CutRepeat.CutRepeatReduce.class);
		job6.setOutputKeyClass(Text.class);
		job6.setOutputValueClass(Text.class);
		job6.setOutputFormatClass(TextOutputFormat.class);
		TextOutputFormat.setOutputPath(job6,out6);
		job6.setNumReduceTasks(1);

		//----第7步作业配置--------------------
		Job job7=Job.getInstance(conf,WriteDataToMysql.class.getSimpleName());
		job7.setJarByClass(this.getClass());

		job7.setMapperClass(WriteDataToMysql.ConnMysqlMapper.class);
		job7.setMapOutputKeyClass(Text.class);
		job7.setMapOutputValueClass(Text.class);
		job7.setInputFormatClass(TextInputFormat.class);
		TextInputFormat.addInputPath(job7,out6);

		job7.setReducerClass(WriteDataToMysql.ConnMysqlReducer.class);
		job7.setOutputFormatClass(DBOutputFormat.class);
		DBConfiguration.configureDB(conf,"com.mysql.jdbc.Driver","jdbc:mysql://60.205.212.196:3306/grms","root","123456");
		DBOutputFormat.setOutput(job7,"results","uid","gid","expect");


		// JobController
		ControlledJob cj1=new ControlledJob(job1.getConfiguration());
		cj1.setJob(job1);

		ControlledJob cj2=new ControlledJob(job2.getConfiguration());
		cj2.setJob(job2);

		ControlledJob cj3=new ControlledJob(job3.getConfiguration());
		cj3.setJob(job3);

		ControlledJob cj4=new ControlledJob(job4.getConfiguration());
		cj4.setJob(job4);

		ControlledJob cj5=new ControlledJob(job5.getConfiguration());
		cj5.setJob(job5);

		ControlledJob cj6=new ControlledJob(job6.getConfiguration());
		cj6.setJob(job6);

		ControlledJob cj7=new ControlledJob(job7.getConfiguration());
		cj7.setJob(job7);

		// 添加作业之间的依赖关系
		cj2.addDependingJob(cj1);

		cj3.addDependingJob(cj2);

		cj4.addDependingJob(cj1);

		cj5.addDependingJob(cj3);
		cj5.addDependingJob(cj4);

		cj6.addDependingJob(cj5);

		cj7.addDependingJob(cj1);
		cj7.addDependingJob(cj6);

		// 创建JobControl对象，添加ControlledJob
		JobControl jc=new JobControl(this.getClass().getSimpleName());
		jc.addJob(cj1);
		jc.addJob(cj2);
		jc.addJob(cj3);
		jc.addJob(cj4);
		jc.addJob(cj5);
		jc.addJob(cj6);
		jc.addJob(cj7);

		// 构建线程类对象，执行作业
		Thread thread=new Thread(jc);
		thread.start();
		do{
			for(ControlledJob cj : jc.getRunningJobList()){
				cj.getJob().monitorAndPrintJob();
			}
		}while(!jc.allFinished());
		return 0;
	}

	public static void 		main(String[] args) throws Exception {
		System.exit(ToolRunner.run(new JobController(),args));
	}
}
