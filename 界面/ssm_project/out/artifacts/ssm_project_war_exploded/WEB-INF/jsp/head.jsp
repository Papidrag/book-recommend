<%--
  Created by IntelliJ IDEA.
  User: 刘思豪
  Date: 2022/6/18
  Time: 17:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>首页</title>
    <style>
        a{
            /*去出超链接下面的横线*/
            text-decoration: none;
            color: black;
            font-size: 18px;
        }
        h3 {
            width: 180px;
            height: 38px;
            margin: 100px auto;
            text-align: center;
            line-height: 38px;
            background: deepskyblue;
            border-radius: 5px;
        }
    </style>
</head>
<body>
<h3>
    <a href="${pageContext.request.contextPath}/book/allBook">进入书籍推荐页面</a>
</h3>
</body>
</html>