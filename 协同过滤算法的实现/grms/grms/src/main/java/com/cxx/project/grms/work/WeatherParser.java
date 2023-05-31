package com.cxx.project.grms.work;

import org.apache.hadoop.io.Text;

import java.io.*;

public class WeatherParser{
    private String sid;
    private double tem;
    private int year;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File("E:\\cxx\\999999-99999-1990")));
        for (int i = 0; i <1000 ; i++) {
            System.out.println(reader.readLine());
            WeatherParser a = new WeatherParser();
            a.parse(reader.readLine());
        }
    }

    public boolean parse(String line){
        if(line.length()<93) return false;
        if("+9999".equals(line.substring(87,92))) return false;
        if(!("01459".contains(line.substring(92,93)))) return false;
        sid=line.substring(0,15);
        tem=Double.parseDouble(line.substring(87,92));
        year=Integer.parseInt(line.substring(15,19));
        System.out.println("解析有用的数据为sid:"+sid+"\ttem:"+tem+"\tyear:"+year);
        return true;
    }

    public boolean parse(Text line){
        return parse(line.toString());
    }

    public String getSid(){
        return sid;
    }

    public void setSid(String sid){
        this.sid=sid;
    }

    public double getTem(){
        return tem;
    }

    public void setTem(double tem){
        this.tem=tem;
    }

    public int getYear(){
        return year;
    }

    public void setYear(int year){
        this.year=year;
    }
}
