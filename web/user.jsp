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
    <a style="width:360px" href="javascript:void(0)" id="defaultOpen" class="tablinks" onclick="openTab(event, 'Flights')">Flights</a>
    <a style="width:360px" href="javascript:void(0)" class="tablinks" onclick="openTab(event, 'Reservations')">Reservations</a>
</div>

<div id="Flights" class="tabcontent">
    <h3>Flights</h3>
    <p>Search for flights</p>
</div>

<div id="Reservations" class="tabcontent">
    <h3>Reservations</h3>
    <p>Your Reservations</p>
</div>

</html>
<%@ include file="tabs.jsp"%>


