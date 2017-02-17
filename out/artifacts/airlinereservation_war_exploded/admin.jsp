<%@ page import="MainPackage.ManagerAccountsServlet" %><%--
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
    <a style="width:230px" href="javascript:void(0)" id="defaultOpen" class="tablinks" onclick="openTab(event, 'Manager Accounts')">Manager Accounts</a>
    <a style="width:230px" href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Flight Schedule')">Flight Schedule</a>
    <a style="width:230px" href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Manage Aircraft')">Manage Aircraft</a>
</div>

<div id="Manager Accounts" class="tabcontent" style="text-align:left">
    <form action="/manageraccountsservlet">
        <input type="submit" value="Create new Manager Account" name="newmanager">
        <h3>Managers</h3>
        <table border="1">
            <tr>
                <th>Last Name</th>
                <th>First Name</th>
                <th>Email Address</th>
                <th>Phone #</th>
                <th>Delete Manager</th>
            </tr>
            <% User[] managers = ManagerAccountsServlet.getManagers();
                for(User m : managers){%>
            <tr>
                <td><%=m.getLastName()%></td>
                <td><%=m.getFirstName()%></td>
                <td><%=m.getEmail()%></td>
                <td><%=m.getPhoneNumber()%></td>
                <td><input type="submit" value="Delete" name="deletemanager"></td>
            </tr>
            <%}%>
        </table>
    </form>
</div>

<div id="Flight Schedule" class="tabcontent">
    <%@ include file="flights.jsp"%>
</div>
<div id="Manage Aircraft" class="tabcontent">
    <h3>Manage Aircraft</h3>
    <p></p>
</div>
</body>
</html>
<%@ include file="tabs.jsp"%>
