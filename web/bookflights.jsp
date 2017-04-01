<%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 4/1/2017
  Time: 10:54 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="/style.css">
<head>
    <title>Book Flights</title>
</head>
<body style="color:white;height:1200px;background:url(Images/homebackground.jpg)">
<div style="padding:5px;width:35%;margin:auto;color:white">
    <h2 style="color:white;font-weight: bold">Iowa Air Book Flights</h2>
    <form action="/bookflightsservlet">
        <h3 style="color:white;font-weight: bold">Passenger Information</h3>
        <% for(int i = 0; i < Integer.valueOf((String)session.getAttribute("numpassengers"));i++){%>
        <h4 style="color:white;font-weight: bold"><%="Passenger "+String.valueOf(i+1)%></h4>
        <label for="<%="firstname"+i%>">First name: </label>
        <input type="text" id="<%="firstname"+i%>" size="15" name="<%="firstname"+i%>">

        <label for="<%="lastname"+i%>">Last name: </label>
        <input type="text" id="<%="lastname"+i%>" size="15" name="<%="gender"+i%>">
        <br>
        <label for="<%="gender"+i%>">Gender: </label>
        <input type="text" id="<%="gender"+i%>" size="15" name="<%="gender"+i%>">

        <label for="<%="dob"+i%>">Date of birth: </label>
        <input type="date" id="<%="dob"+i%>" size="15" name="<%="dob"+i%>">
<br>
        <label for="<%="id"+i%>">ID #: </label>
        <input type="text" id="<%="id"+i%>" size="15" name="<%="id"+i%>">
        <br>
        <%}%>
        <h3 style="color:white;font-weight: bold">Credit Card Information</h3>
        <label for="creditcardtype">Card Type: </label>
        <input type="text" id="creditcardtype" size="8" name="creditcardtype">
        <br>
        <label for="creditcardnumber">Card Number: </label>
        <input type="text" id="creditcardnumber" size="8" name="creditcardnumber">
        <br>
        <label for="creditcardexpiration">Card Expiration (mm/yy): </label>
        <input type="text" id="creditcardexpiration" size="8" name="creditcardexpiration">
        <br>
        <label for="creditcardsecurity">Card Security Code: </label>
        <input type="number" id="creditcardsecurity" min="0" max="999" name="creditcardsecurity">
        <br>
        <h4 style="color:white;font-weight: bold">Your total is $<%=String.format("%.2f",Double.valueOf((String)session.getAttribute("totalprice")))%></h4>
        <br>
        <h4 style="color:white;font-weight: bold">Confirm by entering the same amount below</h4>
        <br>
        <label for="priceconfirmation">$</label>
        <input type="number" step="0.01" id="priceconfirmation"  name="priceconfirmation">
        <br>
        <br>
        <input type="submit" class="prettybutton" name="bookflights">

    </form>
</div>
</body>
</html>
