<%@ page import="MainPackage.Flight" %>
<%@ page import="MainPackage.Aircraft" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 3/13/2017
  Time: 12:19 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="/style.css">
<head>
    <title>Edit Flight</title>
</head>
<body style="color:white;height:1200px;background:url(Images/homebackground.jpg)">
<%Flight flight = (Flight) (session.getAttribute("flight"));
    ArrayList<String> classes= (ArrayList<String>)(session.getAttribute("classes"));
    ArrayList<Float> prices = (ArrayList<Float>) (session.getAttribute("prices"));%>
<h1>Edit Flight <%=flight.getFlight_id()%></h1>
<form action="/adminflightservlet">
    <h3>Prices:</h3>
    <table>
        <%for(int i = 0; i < classes.size();i++){%>
            <tr>
                <td><%=classes.get(i)%>: </td>
                <td>$ <input type="number" step="0.01" style="width:50px" name="<%=classes.get(i)%>" value="<%=prices.get(i)%>"></td>
            </tr>
        <%}%>
    </table>
    <h3>Frequency:</h3>
    <input type="radio" id="onceradio" name="frequency" value="onceradio">
    <label for="onceradio">Once </label>
    <input type="radio" name="frequency" id="weeklyradio" value="weeklyradio">
    <label for="weeklyradio">Weekly</label>
    <input type="radio" id="monthlyradio" name="frequency" value="monthlyradio">
    <label for="monthlyradio">Monthly</label>
    <br>
    <div id="until" style="visibility: hidden">
    <label for="untilpicker">Until </label>
    <input type="date" name = "until" value="<%=flight.getUntil()%>" id="untilpicker">
        </div>
    <div id="oncepicker" style="display:inline-block; visibility: hidden">
        <label for="once">Date: </label>
        <input type="date" id="once" name="once" <%if(flight.getOnce()!=null){%>value="<%=flight.getOnce()%>"<%}%>>
    </div>
    <br>
    <div id="monthlypicker" style="visibility:hidden; display:inline-block">
        Day: <input type="number" min="1" max="31" style="width:50px" name="monthday" <%if(flight.getMonthly()!=null){%>value="<%=flight.getMonthly()%>"<%}%>>
    </div>
    <br>
    <div id="weeklypicker" style="visibility:hidden; display:inline-block">
<table style="font-weight: bold">
        <tr>
            <td><input type="checkbox" name="sunday" <%if(flight.getWeekly()!=null&&flight.getWeekly().contains("sunday")){%>checked<%}%>></td>
            <td><input type="checkbox" name="monday" <%if(flight.getWeekly()!=null&&flight.getWeekly().contains("monday")){%>checked<%}%>></td>
            <td><input type="checkbox" name="tuesday" <%if(flight.getWeekly()!=null&&flight.getWeekly().contains("tuesday")){%>checked<%}%>></td>
            <td><input type="checkbox" name="wednesday" <%if(flight.getWeekly()!=null&&flight.getWeekly().contains("wednesday")){%>checked<%}%>></td>
            <td><input type="checkbox" name="thursday" <%if(flight.getWeekly()!=null&&flight.getWeekly().contains("thursday")){%>checked<%}%>></td>
            <td><input type="checkbox" name="friday" <%if(flight.getWeekly()!=null&&flight.getWeekly().contains("friday")){%>checked<%}%>></td>
            <td><input type="checkbox" name="saturday" <%if(flight.getWeekly()!=null&&flight.getWeekly().contains("saturday")){%>checked<%}%>></td>
        </tr>
        <tr>
            <td>Sunday</td>
            <td>Monday</td>
            <td>Tuesday</td>
            <td>Wednesday</td>
            <td>Thursday</td>
            <td>Friday</td>
            <td>Saturday</td>
        </tr>
    </table>
    </div>
    <br>
    <input type="submit" class="signinbutton" name="editflight" value="Save Edits">
</form>

</body>
<script>
    document.getElementById("onceradio").onclick = function once() {
        if(document.getElementById("oncepicker")!=null&&document.getElementById("weeklypicker")!=null&&document.getElementById("monthlypicker")!=null) {
            document.getElementById("oncepicker").style.visibility = 'visible';
            document.getElementById("weeklypicker").style.visibility = 'hidden';
            document.getElementById("monthlypicker").style.visibility = 'hidden';
            document.getElementById("until").style.visibility = 'hidden';
        }
    }
    document.getElementById("weeklyradio").onclick = function once() {
        if(document.getElementById("oncepicker")!=null&&document.getElementById("weeklypicker")!=null&&document.getElementById("monthlypicker")!=null) {
            document.getElementById("oncepicker").style.visibility = 'hidden';
            document.getElementById("weeklypicker").style.visibility = 'visible';
            document.getElementById("monthlypicker").style.visibility = 'hidden';
            document.getElementById("until").style.visibility = 'visible';
        }
    }
    document.getElementById("monthlyradio").onclick = function once() {
        if(document.getElementById("oncepicker")!=null&&document.getElementById("weeklypicker")!=null&&document.getElementById("monthlypicker")!=null) {
            document.getElementById("oncepicker").style.visibility = 'hidden';
            document.getElementById("weeklypicker").style.visibility = 'hidden';
            document.getElementById("monthlypicker").style.visibility = 'visible';
            document.getElementById("until").style.visibility = 'visible';
        }
    }
    <%if(flight.getWeekly()!=null){%>
    document.getElementById("weeklyradio").click();
    <%}else if(flight.getMonthly()!=null){%>
    document.getElementById("monthlyradio").click();
    <%}else{%>
    document.getElementById("onceradio").click();
    <%}%>
</script>
</html>


