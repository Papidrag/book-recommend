package com.hao.dao;

import com.hao.pojo.Books;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 刘思豪
 * @create 2022/5/27 21:36
 */
public interface BookMapper {

    //添加
    int addBook(Books books);

    //删除
    int deleteBookById(@Param("bookId") int id);

    //修改
    int updateBook(Books books);

    //查询
    Books queryBookById(@Param("bookId") int id);

    //查询全部书籍
    List<Books> queryAllBook();

    Books queryBookByName(@Param("bookName") String bookName);
}
