<%@ page import="MainPackage.ReservationsServlet" %><%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/13/2017
  Time: 6:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="/style.css">
<div class="tab">
    <a style="width:363px" href="javascript:void(0)" name="Flights" id="defaultOpen" class="tablinks" onclick="openTab(event, 'Flights')">Flights</a>
    <a style="width:363px" href="javascript:void(0)" name="Reservations" class="tablinks" onclick="openTab(event, 'Reservations')">Reservations</a>
</div>

<div id="Flights" class="tabcontent">
    <%@ include file="flights.jsp"%>
</div>

<div id="Reservations" class="tabcontent">

    <%if(session.getAttribute("currentuser")==null){%>
    <h3 style="color:#2c71c9;">Please sign in to view your reservations.</h3>
    <%}else{ ArrayList<ArrayList<String>> tickets = ReservationsServlet.getTickets(session);%>
    <%if(session.getAttribute("reservationmessage")!=null){%>
    <p style="color:#903723"><%=session.getAttribute("reservationmessage")%></p>
    <%session.removeAttribute("reservationmessage");}%>

    <h3 style="color:#2c71c9;float:left">Your Reservations</h3>
    <form action="/reservationsservlet">
    <table class="prettytable" border="1">
        <tr>
            <th>Ticket Number</th>
            <th>Passenger Name</th>
            <th>Depart</th>
            <th>Arrive</th>
            <th>Manage</th>
        </tr>
        <%for(ArrayList<String> ticket : tickets){%>
        <tr>
            <td><%=ticket.get(0)%></td>
            <td><%=ticket.get(1)%></td>
            <td><%=ticket.get(2)%><br><%=ticket.get(3)+" "%><%=ticket.get(4)%></td>
            <td><%=ticket.get(5)%><br><%=ticket.get(6)+" "%><%=ticket.get(7)%></td>
            <td style="text-align:center;"><%--<input  type="submit" class="prettybutton" value="Email Receipt" name="<%="emailreceipt"+ticket.get(0)%>">--%>
                <input type="submit" class="prettybutton" value="Cancel Ticket" name="<%="cancelticket"+ticket.get(0)%>"></td>
        </tr>
        <%}%>
    </table>
    </form>
    <%}%>

</div>

</html>
<%@ include file="tabs.jsp"%>


