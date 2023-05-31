package com.cxx.project.grms.work;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.VIntWritable;
import org.apache.hadoop.io.Writable;

public class AvgValue implements Writable{
    private VIntWritable num;
    private DoubleWritable value;

    public AvgValue(VIntWritable num,DoubleWritable value){
        this.num=new VIntWritable(num.get());
        this.value=new DoubleWritable(value.get());
    }

    public AvgValue(){
        this.num=new VIntWritable();
        this.value=new DoubleWritable();
    }

    public AvgValue(AvgValue av){
        this.num=new VIntWritable(av.num.get());
        this.value=new DoubleWritable(av.value.get());
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException{
        num.write(dataOutput);
        value.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException{
        num.readFields(dataInput);
        value.readFields(dataInput);
    }

    public VIntWritable getNum(){
        return num;
    }

    public void setNum(VIntWritable num){
        this.num=new VIntWritable(num.get());
    }

    public void setNum(int num){
        this.num=new VIntWritable(num);
    }

    public DoubleWritable getValue(){
        return value;
    }

    public void setValue(DoubleWritable value){
        this.value=new DoubleWritable(value.get());
    }

    public void setValue(double value){
        this.value=new DoubleWritable(value);
    }

    public void set(AvgValue av){
        this.num=new VIntWritable(av.num.get());
        this.value=new DoubleWritable(av.value.get());
    }

    @Override
    public String toString(){
        return "AvgValue{"+"num="+num+", value="+value+'}';
    }
}
