<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="/js/jquery-1.4.2.js"></script>
<script type="text/javascript">
	function changeImg(img) {
	img.src=img.src+"?time="+new Date().getTime();

	}
	function checkForm(){
	//1非空校验
	var cansub=true;
	  cansub=checknull("username","用户名不能为空!")&&cansub;
	  cansub=checknull("password","密码不能为空!")&&cansub;
	  cansub=checknull("password2","确认密码不能为空!")&&cansub;
	  cansub=checknull("email","邮箱不能为空!")&&cansub;
	  cansub=checknull("nickname","昵称不能为空!")&&cansub;
	  cansub=checknull("valistr","验证码不能为空!")&&cansub;
	  //2次密码一致
	  var p1=document.getElementsByName("password")[0].value;
	  var p2=document.getElementsByName("password2")[0].value;
	  if(p1!=p2){
	  cansub=false;
	  document.getElementById("password2_msg").innerHTML="<font color='red'>两次密码不正确</font>";
	  
	  }
	  //3邮箱格式校验
	   var email=document.getElementsByName("email")[0].value;
	  if(email!=null&&email!=""&&!/^\w+@\w+(\.\w+)+$/.test(email)){
	  document.getElementById("email_msg").innerHTML="<font color='red'>邮箱格式不正确</font>";
	  cansub=false;
	  }
	  return cansub;
	}
	function checknull(name,msg){
	document.getElementById(name+"_msg").innerHTML="";
	var objValue= document.getElementsByName(name)[0].value;
	  if(objValue==null||objValue==""){
	  document.getElementById(name+"_msg").innerHTML="<font color='red'>"+msg+"</font>";
	  return false;
	  }
	  return true;
	}
	
	window.onload=function(){
	  		$("input[type='text'][name='username']").blur(function(){
	  			var username = $(this).val();
	  			$.get("/ValiNameServlet",{username:username},function(data){
	  			var json=eval("("+data+")");
	  			if(json.state==0){
	  			$("#username_msg").html("<font color='green'>"+json.msg+"</font>");
	  			}else if(json.state==1){
	  			$("#username_msg").html("<font color='red'>"+json.msg+"</font>");	  			
	  			}  				
	  			});
	  		});
  		}
</script>
</head>
<body>
	<div align="center">
		<h1>Estore注册</h1>
		<hr>
		<form action="/RegistServlet" method="post"
			onsubmit="return checkForm()">
			<table>
				<tr>
					<td>用户名：</td>
					<td><input type="text" name="username"
						value="${param.username }" />
					</td>
					<td id="username_msg"></td>
				</tr>
				<tr>
					<td>密码：</td>
					<td><input type="password" name="password" />
					</td>
					<td id="password_msg"></td>
				</tr>
				<tr>
					<td>确认密码：</td>
					<td><input type="password" name="password2" />
					</td>
					<td id="password2_msg"></td>
				</tr>
				<tr>
					<td>昵称：</td>
					<td><input type="text" name="nickname"
						value="${param.nickname }" />
					</td>
					<td id="nickname_msg"></td>
				</tr>
				<tr>
					<td>邮箱：</td>
					<td><input type="text" name="email" value="${param.email }" />
					</td>
					<td id="email_msg"></td>
				</tr>
				<tr>
					<td>验证码：</td>
					<td><input type="text" name="valistr" />
					</td>
					<td id="valistr_msg">${msg}</td>
				</tr>
				<tr>

					<td><input type="submit" value="注册用户" />
					</td>
					<td><img src="/ValiImg" onclick="changeImg(this)"
						style="cursor: pointer;" />
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>