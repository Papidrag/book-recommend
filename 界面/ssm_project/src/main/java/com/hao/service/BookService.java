package com.hao.service;

import com.hao.pojo.Books;

import java.util.List;

/**
 * @Author 刘思豪
 * @create 2022/5/27 22:00
 */
public interface BookService {

    //添加
    int addBook(Books books);

    //删除
    int deleteBookById(int id);

    //修改
    int updateBook(Books books);

    //查询
    Books queryBookById(int id);

    //查询全部书籍
    List<Books> queryAllBook();

    Books queryBookByName(String bookName);
}
