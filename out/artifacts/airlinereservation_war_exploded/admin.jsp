<%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/13/2017
  Time: 6:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Iowa Air Admin</title>
</head>
<body>
<div class="tab">
    <a href="javascript:void(0)" id="defaultOpen" class="tablinks" onclick="openTab(event, 'Manager Accounts')">Manager Accounts</a>
    <a href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Flight Schedule')">Flight Schedule</a>
    <a href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Manage Aircraft')">Manage Aircraft</a>
</div>

<div id="Manager Accounts" class="tabcontent">
    <h3>Manager Accounts</h3>
    <p></p>
</div>

<div id="Flight Schedule" class="tabcontent">
    <h3>Flight Schedule</h3>
    <p></p>
</div>
<div id="Manage Aircraft" class="tabcontent">
    <h3>Manage Aircraft</h3>
    <p></p>
</div>
</body>
</html>
<%@ include file="tabs.jsp"%>
