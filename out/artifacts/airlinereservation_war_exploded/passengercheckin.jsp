<%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 4/29/2017
  Time: 5:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="/style.css">
<head>
    <title>Check In</title>
</head>
<body style="color:white;height:1200px;background:url(Images/homebackground.jpg)">
<h1>Passenger Check In</h1>
<form action="/managetickets">
    <div style="padding:10px">
        <span>Number of Bags: </span>
        <input type="number" name="numbags">
    </div>
    <div style="padding:10px">
        <span>Seat Number: </span>
        <input type="text" name="seatnumber">
    </div>
    <input class="prettybutton" type="submit" value="Generate Boarding Pass" name="boardingpass">
    <input class="prettybutton" type="submit" value="Finish Check In" name="finishcheckin">
    </form>
<%if(session.getAttribute("error1")!=null){%>
<p><%=session.getAttribute("error1")%></p>
<%}%>
</body>
</html>
