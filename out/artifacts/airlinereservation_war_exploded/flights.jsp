<%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/11/2017
  Time: 9:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Flights</title>
</head>
<body>
    <form action="flightsservlet" style="padding:10px; text-align:left; border-bottom:solid 1px #ccc">
        <h3>Search For Flights</h3>
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
        <input type="submit" name="searchflights" value="Search">
    </form>
    <table border="1" style="width:750px">
        <tr>
            <th>Flight</th>
            <th>Depart</th>
            <th>Arrive</th>
            <th>Price</th>
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




