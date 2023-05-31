package com.hao.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * @Author 刘思豪
 * @create 2022/5/27 20:36
 */
public class Books {

    private int bookID;
    private String bookName;
    private int expect;
    private String detail;


}
