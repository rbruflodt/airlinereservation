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
    <a style="width:230px" href="javascript:void(0)" id="defaultOpen" class="tablinks" onclick="openTab(event,'Manager Accounts')">Manager Accounts</a>
    <a style="width:230px" href="javascript:void(0)" class="tablinks" onclick="openTab(event,'Flight Schedule')">Flight Schedule</a>
    <a style="width:230px" href="javascript:void(0)" class="tablinks" onclick="openTab(event,'Manage Aircraft')">Manage Aircraft</a>
</div>

<%if(session.getAttribute("currenttab")!=null){
    if(session.getAttribute("currenttab").equals("Flight Schedule")){%>
        <script>
            openTab(event,"Flight Schedule")
        </script>
    <%}
    else if(session.getAttribute("currenttab").equals("Manage Aircraft")){%>
        <script>
            openTab(event,"Manage Aircraft")
        </script>
    <%}
}%>

<div id="Manager Accounts" class="tabcontent" style="text-align:left">
    <form action="/manageraccountsservlet">
        <input type="submit" class="prettybutton" value="Create new Manager Account" name="newmanager">
        <h3 style="color:#2c71c9;">Managers</h3>
        <table class="prettytable" border="1">
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
                <form action="/manageraccountsservlet">
                <td style="text-align:center"><input type="submit" class="prettybutton" value="Delete" name="<%=m.getEmail()%>"></td>
                </form>
            </tr>
            <%}%>
        </table>
    </form>
</div>

<div id="Flight Schedule" class="tabcontent">
    <%@ include file="flights.jsp"%>
</div>

<div id="Manage Aircraft" class="tabcontent">
    <form action="/manageaircraftservlet">
        <input type="submit" class="prettybutton" style="bottom-padding:10px;float:left" value="Add Aircraft" name="newaircraft">
        <br>
        <h3 style="color:#2c71c9;float:left">Aircraft</h3>
        <table class="prettytable" border="1">
            <tr>
                <th>Name</th>
                <th>Type</th>
                <th>Classes</th>
                <th>Number</th>
                <th>Manage</th>
            </tr>
            <tr>
                <td>Group 1</td>
                <td>Boeing 777</td>
                <td>
                    <table>
                    <tr><td>First Class</td><td>40 seats</td></tr>
                    <br>
                    <tr><td>Economy Class</td><td>160 seats</td></tr>
                    </table>
                </td>
                <td>8</td>
                <td style="text-align:center"><input type="submit" class="prettybutton" value="Edit" name="editaircraft">
                <input type="submit" class="prettybutton" value="Delete" name="deleteaircraft"></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
<%@ include file="tabs.jsp"%>
