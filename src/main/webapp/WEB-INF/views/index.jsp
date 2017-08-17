<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome</title>
</head>
<body>

	<h4>Welcome to PayDay Loanz, ${username}!</h4>

	This is where you take a loan and pay it back by clicking a button,
	because it's just a dummy page ;)


	<br>
	<br>
	<br>
	<table>
		<tr>
			<td><a href="login">Login</a></td>
			<td><a href="register">Register</a></td>
			<c:choose>
				<c:when test="${user != null}">
					<td><a href="myloans">My loans</a></td>

					<td><a href="logout">Logout</a></td>
				</c:when>
			</c:choose>

			<td>
		</tr>
	</table>

	<br>
	<br>
	<br>
	<c:choose>
		<c:when test="${user != null}">
			<a href="apply" style="font-size: 300%">Apply for loan</a>
		</c:when>
	</c:choose>


</body>
</html>