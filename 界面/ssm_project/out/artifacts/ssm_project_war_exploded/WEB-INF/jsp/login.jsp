<%--
  Created by IntelliJ IDEA.dQ
  User: 刘思豪
  Date: 2022/3/17
  Time: 17:52
  To change this template use File | Settings | File Templates.
--%>
<%@page language="java" pageEncoding="UTF-8"  contentType="text/html;utf-8" isELIgnored="false"%>

<html>

<style>
  body{
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
    font-family: "微软雅黑";
    overflow: hidden;
  }
  #bg1{
    position: absolute;
    width: 100%;
    height: 100%;
    /*background: url("images/1.jpg");*/
  }
  #bg2{
    position: absolute;
    height: 100%;
    width: 100%;
    /*background: url("images/2.jpg");*/
  }
  #container{
    position: absolute;
    background: #fff;
    height: 300px;
    width: 100%;
    top: 50%;
    margin-top: -150px;
    opacity: 0.4;
  }
  #loginContainer{
    background-color: #fff;
    border-radius: 20px;
    width: 300px;
    height: 350px;
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%,-50%);
    /*position: absolute;*/
    /*height: 400px;*/
    /*width: 300px;*/
    /*top: 50%;*/
    /*right: 10%;*/
    /*margin-top: -200px;*/
    /*background: #fff;*/
    /*opacity: 0.5;*/
    /*border-radius: 5px;*/
    /*z-index: 50;*/
  }
  #loginHeader{
    text-align:center;
    position: absolute;
    height: 50px;
    width: 300px;
    top: 66%;
    right: 10%;
    margin-top: -190px;
    z-index: 80;
  }
  #photo{
    position: absolute;
    top: 28%;
    right:19%;
    z-index:90;

  }
  #photo img{
    height: 90px;
    width: 90px;
  }
  #loginHeader span{
    margin-left: 20px;
    text-align: center;
    font-weight: bolder;
    color: #0e475a;
  }
  #loginHeader hr{
    margin: 0 auto;
  }
  #loginContent{
    position: absolute;
    width: 300px;
    height: 200px;
    top: 70%;
    right: 10%;
    margin-top: -150px;
    z-index: 80;
  }
  #loginContent form{
    margin-left: 20px;
  }
  .loginLabel{
    font-weight: bolder;
    font-size: 14px;
    color: #0e475a;
  }
  .loginInput{
    width: 260px;
    height: 25px;
    padding: 3px 0;
    font-size: 12px;
    color: #797979;
    border: 1px solid #797979;
    margin-bottom: 10px;

  }
  .loginInput:focus{
    outline: none;
    border: 1px solid #2ca8be;
  }
  #loginBtn{
    width: 260px;
    height: 25px;
    margin-top: 10px;
    border: 1px solid #2ca8be;
    background: #2ca8be;
    font-weight: bolder;
    color: #fff;
  }

</style>
<script type="text/javascript">
  function message () {
    var message="${message}"
    if( message !=""){
      alert(message);
    }
  }
</script>
<body onload="message()">
<div id='bg1'></div>
<div id='bg2'></div>
<div id='container'></div>
<div id='loginContainer'>
  <div id='loginHeader'>
    <span>用户登录中心</span>
    <hr size="3px" color="#2ca8be" />
  </div>
<%--<div id='photo'>--%>
<%--  <img src="images/icon.png" style="border-radius: 50%">--%>
<%--</div>--%>
  <div id='loginContent'>

    <form action="login">
      <label for="username" class="loginLabel">用户名</label>
      <br/>
      <input id="username" class="loginInput" type="text" placeholder="请输入用户名" name="userName" />
      <br/>
      <label for="password" class="loginLabel">密码</label>
      <br/>
      <input id="password" class="loginInput" type="password" placeholder="请输入密码" name="password" />
      <br/>
      <input type="submit" value="进入系统" id="loginBtn" onclick="return checkForm()"/>
    </form>
  </div>
</div>
<script>
  function checkForm(){
    //是否为空
    function ifnull(txt){
      if(txt.length==0){
        return true;
      }
      var $reg=/\s+/;
      return $reg.test(txt);
    }

    //验证用户名
    if(ifnull($("#username").val())){
      $("span").show().text("用户名不能为空");
      return false;
    }
    //验证密码
    if(ifnull($("#password").val())){
      $("span").show().text("密码不能为空");
      return false;
    }else{
      var $reg=/^\w{6,}$/;
      if(!$reg.test($("#password").val())){
        $("span").show().text("密码至少6位");
        return false;
      }
    }
    return true;
  }


  // $(function(){
  //   // 图片自动切换
  //   // -4张图片  -自动定时器切换  -动画效果
  //   var page = 1;
  //   function changePage(){
  //     console.log(11);
  //     if (page!=4) {
  //       page ++;
  //     }else{
  //       page =1;
  //     }
  //     if (((page-1)%2)==0) {
  //       $("#bg1").css("background-image", "url(images/" + page + ".jpg)");
  //       $("#bg1").fadeIn(1500);
  //       $('#bg2').fadeOut(1500);
  //     }else{
  //       $("#bg2").css("background-image","url(images/"+page+".jpg)");
  //       $("#bg2").fadeIn(1500);
  //       $('#bg1').fadeOut(1500);
  //     }
  //   }
  //   // 自动定时器 调用切换图片的方法
  //   setInterval(changePage,5000);
  // })
</script>
</body>
</html>