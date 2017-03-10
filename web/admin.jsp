<%@ page import="MainPackage.ManagerAccountsServlet" %>
<%@ page import="MainPackage.Aircraft" %>
<%@ page import="MainPackage.ManageAircraftServlet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="sun.misc.Request" %>


<%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/13/2017
  Time: 6:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="/style.css">
<head>
    <title>Iowa Air Admin</title>
</head>
<body>
<div class="tab">
    <a style="width:230px" href="javascript:void(0)" name="Manager Accounts" id="defaultOpen" class="tablinks" onclick="openTab(event,'Manager Accounts')">Manager Accounts</a>
    <a style="width:230px" href="javascript:void(0)" name="Flight Schedule" class="tablinks" onclick="openTab(event,'Flight Schedule')">Flight Schedule</a>
    <a style="width:230px" href="javascript:void(0)" name="Manage Aircraft" class="tablinks" onclick="openTab(event,'Manage Aircraft')">Manage Aircraft</a>
</div>

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
        <div style="float:left;height:10px;padding-top:15px">
        <label for="namefield" >Name: </label>
            <%if(session.getAttribute("namefield")!=null){%>
        <input type="search" size="15" value="<%=session.getAttribute("namefield")%>" id="namefield" name="namefield">
            <%}else{%>
            <input type="search" size="15" id="namefield" name="namefield">
            <%}%>
        <label for="typefield">Type: </label>
        <select id="typefield" name="typefield">
            <option></option>
            <option <%if (session.getAttribute("typefield")!=null&&session.getAttribute("typefield").equals("Boeing 777")) {%>selected<%}%>>Boeing 777</option>
            <option <%if (session.getAttribute("typefield")!=null&&session.getAttribute("typefield").equals("Boeing 767")) {%>selected<%}%>>Boeing 767</option>
            <option <%if (session.getAttribute("typefield")!=null&&session.getAttribute("typefield").equals("Boeing 747")) {%>selected<%}%>>Boeing 747</option>
            <option <%if (session.getAttribute("typefield")!=null&&session.getAttribute("typefield").equals("Airbus 380")) {%>selected<%}%>>Airbus 380</option>
        </select>
        <input type="submit" class="prettybutton" name="searchaircraft" value="Search">
        </div>
        <br>
        <br>
            <div>
        <h3 style="color:#2c71c9;float:left;">Aircraft</h3>


        <%if(session.getAttribute("aircrafterror")!=null){%>
        <h4 style="color:#903723;"><%=session.getAttribute("aircrafterror")%></h4>
        <% session.removeAttribute("aircrafterror");}%>
        <% ArrayList<Aircraft> aircrafts = ManageAircraftServlet.getAircraft(session);%>
        <table class="prettytable" border="1">
            <tr>
                <th>Name</th>
                <th>Type</th>
                <th>Classes</th>
                <th>Manage</th>
            </tr>
            <% for(Aircraft a : aircrafts){%>
            <tr>
                <td><input type="text" style="text-align:center" size="10" name="<%="name"+a.getName()%>" value="<%=a.getName()%>"></td>
                <td><select id="type" name="<%="type"+a.getName()%>">
                    <option <%if (a.getAircraft_type().equals("Boeing 777")) {%>selected<%}%>>Boeing 777</option>
                    <option <%if (a.getAircraft_type().equals("Boeing 767")) {%>selected<%}%>>Boeing 767</option>
                    <option <%if (a.getAircraft_type().equals("Boeing 747")) {%>selected<%}%>>Boeing 747</option>
                    <option <%if (a.getAircraft_type().equals("Airbus 380")) {%>selected<%}%>>Airbus 380</option>
                </select></td>
                <td>
                    <table style="padding:1px;">
                        <% if(a.getClasses()!=null){for(int i = 0; i<a.getClasses().size();i++){%>
                    <tr><td><input type="text" size="10" style="text-align:center" name="<%="class"+a.getName()+a.getClasses().get(i)%>" value="<%=a.getClasses().get(i)%>"></td>
                        <td><input type="number" style="width:50px;text-align:center" min="0" name="<%="seats"+a.getName()+a.getClasses().get(i)%>" value=<%=a.getSeats().get(i)%>> seats</td>
                    <td><input type="submit" class="prettybutton" onclick="getScroll()" name="<%="deleteclass"+a.getName()+a.getClasses().get(i)%>" value="X"></td></tr>
                    <br>
                        <%}}%>
                    </table>
                    <input style="float:left" class="prettybutton" value="Add Class" onclick="getScroll()" type="submit" name="<%=a.getName()%>">
                </td>
                <td style="text-align:center"><input type="submit" onclick="getScroll()" class="prettybutton" value="Save Changes" name="<%=a.getName()%>">
                <input type="submit" class="prettybutton" value="Delete" name="<%=a.getName()%>"></td>
            </tr>
            <%}%>
        </table>
        </div>
    </form>
</div>
</body>
</html>
<%@ include file="tabs.jsp"%>


