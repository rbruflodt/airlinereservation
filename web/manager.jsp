<%@ page import="MainPackage.ManageTickets" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/13/2017
  Time: 6:35 PM
  To change this template use File | Settings | File Templates.
  Edit by Lei Xiang
  Date: 4/23/2017
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="/style.css">
<head>
    <title>Iowa Air Admin</title>
</head>
<body>
<div class="tab">
    <a style="width:363px" href="javascript:void(0)" name="Flights" id="defaultOpen" class="tablinks" onclick="openTab(event, 'Flights')">Flights</a>
    <a style="width:363px" href="javascript:void(0)" name="Passenger Tickets" class="tablinks" onclick="openTab(event, 'Passenger Tickets')">Passenger Tickets</a>
</div>

<div id="Flights" class="tabcontent">
    <%@ include file="flights.jsp"%>
</div>

<div id="Passenger Tickets" class="tabcontent">
    <form action="/managetickets"  style="padding:10px; text-align:left; border-bottom:solid 1px #ccc">
        <h3 style="color:#2c71c9;">Search for Passenger Tickets</h3>
        <label for="tnum">Ticket number: </label>
        <input type="number" id="tnum" style="width:150px" value="<%=session.getAttribute("ticketnum")%>" name="ticketnum">
        <label for="lastname">Last name: </label>
        <input type="text" id="lastname" style="width:150px" <%if(session.getAttribute("lname")!=null){%>value="<%=session.getAttribute("lname")%>"<%}%> name="lname">
        <label for="firstname">First name: </label>
        <input type="text" id="firstname" style="width:150px" <%if(session.getAttribute("fname")!=null){%>value="<%=session.getAttribute("fname")%>"<%}%> name="fname">
        <br>
        <%ArrayList<ArrayList<String>> tickets = ManageTickets.getTickets(session);%>
        <input type="submit" class="prettybutton" name="searchtickets" value="Search">
        <br>
        <% if(tickets!=null){%>
        <h3 style="color:#2c71c9;float:left">Passenger Tickets</h3>
        <table class="prettytable" border="1">
        <tr>
            <th>Ticket Number</th>
            <th>Passenger Name</th>
            <th>Depart</th>
            <th>Arrive</th>
            <th>Manage</th>
        </tr>
        <%for(ArrayList<String> t : tickets){%>
        <tr>
            <td><%=t.get(0)%></td>
            <td><%=t.get(1)%></td>
            <td><%=t.get(2)%><br><%=t.get(3)+" "%><%=t.get(4)%></td>
            <td><%=t.get(5)%><br><%=t.get(6)+" "%><%=t.get(7)%></td>
            <% if(t.size()==8){%>
            <td style="text-align:center"><input type="submit" class="prettybutton" value="Check in" name="checkinticket">
                <input type="submit" class="prettybutton" value="Cancel ticket" name="cancelticketnumber"></td>
            <%}else{%>
            <td><p style="color:#903723">Checked in</p></td>
            <%}%>
        </tr>
        <%}}%>
    </table>
    </form>

</div>
</body>
</html>
<%@ include file="tabs.jsp"%>