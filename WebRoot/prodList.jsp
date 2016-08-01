<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

</head>
<body>
	<h1>Estore_商品列表</h1>
	<hr>
	<table width="100%" style="text-align: center;">
		<c:forEach items="${requestScope.list}" var="prod">
			<tr>
				<td width="20%"><a href="/ProdInfoServlet?id=${prod.id }"><img src="/ImgServlet?imgurl=${prod.imgurls } " style="cursor: pointer;" /></a>
				</td>
				<td width="40%">${prod.name }<br> ${prod.price }<br>
					${prod.category }<br> ${prod.pnum }<br></td>
			    <td width="40%">
  				<c:if test="${prod.pnum>0}">
  					<font color="blue">有货</font>
  				</c:if>
  				<c:if test="${prod.pnum<=0}">
  					<font color="red">缺货</font>
  				</c:if>
  			</td>
			</tr>
			
			<tr>
				<td colspan="3"><hr></td>
			</tr>
		</c:forEach>
	</table>
</body>
</body>
</html>