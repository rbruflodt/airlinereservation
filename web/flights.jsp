<%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/11/2017
  Time: 9:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="/style.css">
<head>
    <title>Flights</title>
</head>
<body>
    <form action="flightsservlet" style="padding:10px; text-align:left; border-bottom:solid 1px #ccc">
        <h3 style="color:#2c71c9;">Search For Flights</h3>
        <label for="from">From: </label>
        <select id="from">
            <option>Iowa City, IA</option>
            <option>Chicago, IL</option>
            <option>New York City, NY</option>
            <option>Atlanta, GA</option>
            <option>San Fransisco, CA</option>
        </select>
        <label for="to">To: </label>
        <select id="to">
            <option>Iowa City, IA</option>
            <option>Chicago, IL</option>
            <option>New York City, NY</option>
            <option>Atlanta, GA</option>
            <option>San Fransisco, CA</option>
        </select>
        <label for="numpassengers">Passengers: </label>
        <input type="number" id="numpassengers" style="width:50px">
        <input type="radio" id="oneway" name="radio" value="oneway">
        <label for="oneway">One way </label>
        <input type="radio" id="roundtrip" name="radio" value="roundtrip">
        <label for="roundtrip">Round trip</label>
        <br>
        <div id="departpicker" style="display:inline-block">
        <label for="depart">Depart: </label>
        <input type="date" id="depart">
        </div>
        <div id="returnpicker" style="visibility:hidden; display:inline-block">
        <label for="return">Return: </label>
        <input type="date" id="return">
        </div><br>
        <input type="submit" class="prettybutton" name="searchflights" value="Search">
    </form>
    <%//User user = (User) session.getAttribute("currentuser");%>
    <%if(user!=null&&user.isAdmin()){%>
    <form action="/newflightservlet" style="padding-bottom:10px">
        <input type="submit" class="prettybutton" name="newflight" value="Add new flight" style="float:left">
    </form>
    <%}%>
    <table class="prettytable" border="1">
        <tr>
            <th>Depart</th>
            <th>Arrive</th>
            <th>Price</th>
            <th>Aircraft</th>
            <%if(user!=null&&user.isAdmin()){%>
               <th>Frequency</th>
            <th>Manage</th>
            <%}else{%>
            <th>Book</th>
            <%}%>
        </tr>
        <tr>
            <td>Iowa City, IA<br>4/14/17 4:00 PM (CST)</td>
            <td>Atlanta, GA<br>4/14/17 8:00 PM (EST)</td>
            <td>$400</td>
            <%if(user!=null&&user.isAdmin()){%>
            <td>Group 1</td>
            <td>Weekly: Mon Wed Fri Sat Sun</td>
            <td><input type="submit" class="prettybutton" value="Edit" name="editflight">
            <input type="submit" class="prettybutton" value="Delete" name="deleteflight"></td>
            <%}else if(user!=null){%>
            <td>Boeing 777</td>
            <td><input type="submit" class="prettybutton" value="Book Flight" name="bookflight"></td>
            <%}else{%>
            <td>Boeing 777</td>
            <td style="color:#932013;font-size:12px">Sign in to book flight</td>
            <%}%>
        </tr>
        </table>
    <script>
        document.getElementById("oneway").onclick = function oneway() {
            if(document.getElementById("departpicker")!=null&&document.getElementById("returnpicker")!=null) {
                document.getElementById("departpicker").style.visibility = 'visible';
                document.getElementById("returnpicker").style.visibility = 'hidden';
            }
        }
        document.getElementById("roundtrip").onclick = function roundtrip() {
            if(document.getElementById("departpicker")!=null&&document.getElementById("returnpicker")!=null) {
                document.getElementById("departpicker").style.visibility = 'visible';
                document.getElementById("returnpicker").style.visibility = 'visible';
            }
        }
    </script>
</body>
</html>





