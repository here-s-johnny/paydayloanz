<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Apply for loan</title>
</head>
<body>

	Please enter the amount you would like to loan and the number of days
	you will need to pay back.

	<br> Please note that you cannot take a loan for more than
	<c:out value="${maxAmount}"></c:out>
	zł and
	<c:out value="${maxDays}"></c:out>
	days.

	<br>
	<br>

	<form method="POST" action="applyProcess">
		<table align="center">
			<tr>
				<td><label for="amount">Amount</label></td>
				<td><input type="number" name="amount" value="" required min="${minAmount}" max="${maxAmount}" step="1"></td>
			</tr>
			<tr>
				<td><label for="term">Term</label></td>
				<td><input type="number" name="term" value="" required min="${minDays}" max="${maxDays}" step="1"/></td>
			</tr>
			<tr>
				<td></td>
				<td align="left"><input type="submit" value="Apply" onclick="" ></td>
			</tr>
		</table>

		<div align=center style="font-style: italic; color: red;">${message}</div>
	</form>

	<div align=center>
		<a href="hello">Home</a>
	</div>

</body>
</html>