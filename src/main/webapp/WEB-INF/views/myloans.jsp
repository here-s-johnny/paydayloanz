<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>My Loans</title>

<style>
table {
	font-family: arial, sans-serif;
	border-collapse: collapse;
	width: 100%;
}

td, th {
	border: 1px solid #dddddd;
	text-align: left;
	padding: 8px;
}

tr:nth-child(even) {
	background-color: #dddddd;
}

caption {
	font-weight: bold;
	text-align: left;
	border-style: solid;
	border-width: 1px;
	border-color: #666666;
}
</style>

</head>
<body>
	<div>
		<table>

			<caption>Loans of user ${user.name}</caption>
			<tr>
				<th>Date</th>
				<th>Deadline</th>
				<th>Extended at</th>
				<th>Amount</th>
				<th>To be paid</th>
				<th>Paid Off</th>
				<th>Pay</th>
				<th>Extend</th>
			</tr>
			<c:forEach var="loan" items="${loans}">
				<tr>
					<td><c:out value="${loan.timestamp}" /></td>
					<td><c:out value="${loan.deadline}" /></td>
					<td><c:out value="${loan.extendedAt}" /></td>
					<td><c:out value="${loan.amount}" /></td>
					<td><c:out value="${loan.amountWithInterest}" /></td>
					<c:choose>
						<c:when test="${loan.paidOff == false}">
							<td style="color: red;">No</td>
						</c:when>
						<c:otherwise>
							<td style="color: green;">Yes</td>
						</c:otherwise>
					</c:choose>
					<td><c:choose>
							<c:when test="${loan.paidOff == false}">
								<a href="<c:url value='payoff?loan=${loan.uid}'/>" type="hidden">Pay
									back </a>
							</c:when>
						</c:choose></td>
					<td><c:choose>
							<c:when
								test="${loan.extendedAt == null && loan.paidOff == false}">
								<a href="<c:url value='extend?loan=${loan.uid}'/>" type="hidden">Extend</a>
							</c:when>
						</c:choose></td>
				</tr>
			</c:forEach>

		</table>
	</div>

	<div align=center style="font-style: italic; color: green;">${message}</div>


	<div align=center>
		<a href="hello">Home</a>
	</div>

</body>
</html>