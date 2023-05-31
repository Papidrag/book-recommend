package com.cxx.project.grms.work;

import org.apache.hadoop.util.ToolRunner;

public class RunApp{
    public static void main(String[] args) throws Exception{
        System.exit(ToolRunner.run(new AvgTempByCombiner(),args));
    }
}
