<%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/13/2017
  Time: 6:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<div class="tab">
    <a style="width:363px" href="javascript:void(0)" id="defaultOpen" class="tablinks" onclick="openTab(event, 'Flights')">Flights</a>
    <a style="width:363px" href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Reservations')">Reservations</a>
</div>

<div id="Flights" class="tabcontent">
    <%@ include file="flights.jsp"%>
</div>

<div id="Reservations" class="tabcontent">
    <%if(session.getAttribute("currentuser")==null){%>
    <h3 style="color:#2c71c9;">Please sign in to view your reservations.</h3>
    <%}else{%>
    <h3 style="color:#2c71c9;float:left">Your Reservations</h3>
    <table class="prettytable" border="1">
        <tr>
            <th>Ticket Number</th>
            <th>Passenger Name</th>
            <th>Depart</th>
            <th>Arrive</th>
            <th>Manage</th>
        </tr>
        <tr>
            <td>123456789</td>
            <td>John Smith</td>
            <td>Iowa City, IA<br>4/14/17 4:00 PM (CST)</td>
            <td>Atlanta, GA<br>4/14/17 8:00 PM (EST)</td>
            <td style="text-align:center"><input type="submit" class="prettybutton" value="Cancel Ticket" name="editaircraft"></td>
        </tr>
    </table>
    <%}%>
</div>

</html>
<%@ include file="tabs.jsp"%>


