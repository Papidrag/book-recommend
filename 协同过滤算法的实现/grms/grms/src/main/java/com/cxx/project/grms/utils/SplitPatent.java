package com.cxx.project.grms.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SplitPatent{
    public static void main(String[] args) throws IOException{
        for(int j=2;j<=9;j++) {
            BufferedReader reader = new BufferedReader(new FileReader(new File("G:\\weather\\999999-99999-199"+j)));
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("E:\\data\\999999-99999-199"+j)));
            for (int i = 0; i < 1000; i++) {
                writer.write(reader.readLine());
                writer.newLine();
            }
            writer.flush();
            writer.close();
            reader.close();
        }
    }
}
