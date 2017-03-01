<%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/13/2017
  Time: 6:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Iowa Air Admin</title>
</head>
<body>
<div class="tab">
    <a style="width:363px" href="javascript:void(0)" id="defaultOpen" class="tablinks" onclick="openTab(event, 'Flights')">Flights</a>
    <a style="width:363px" href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Passenger Tickets')">Passenger Tickets</a>
</div>

<div id="Flights" class="tabcontent">
    <%@ include file="flights.jsp"%>
</div>

<div id="Passenger Tickets" class="tabcontent">
    <h3>Passenger Tickets</h3>
    <p></p>
</div>
</body>
</html>
<%@ include file="tabs.jsp"%>
