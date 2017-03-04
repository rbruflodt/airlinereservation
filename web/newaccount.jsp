<%@ page import="MainPackage.User" %><%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/11/2017
  Time: 11:35 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="/style.css">
<head>
    <title>New Iowa Air Account</title>
</head>
<body style="background:url(Images/homebackground.jpg);height:1200px">
    <div style="padding:5px;width:35%;margin:auto;color:white">
        <h2 style="color:white;font-weight: bold">Iowa Air New Account Information</h2>
        <%User currentInfo = (User)session.getAttribute("enteredinfo");%>
        <form action="/newaccountservlet">
            <div style="padding:10px">
            <span>First name: </span>
            <input type="text" name="first name" style="float:right" value="<%=currentInfo.getFirstName()%>">
            </div>
            <div style="padding:10px">
            <span>Last name: </span>
            <input type="text" name="last name" style="float:right" value="<%=currentInfo.getLastName()%>">
            </div>
            <div style="padding:10px">
            <span>Phone number: </span>
            <input type="number" name="phone number" style="float:right" value="<%=currentInfo.getPhoneNumber()%>">
              </div>
            <div style="padding:10px">
                <span>Email address: </span>
            <input type="text" name="email" style="float:right" value="<%=currentInfo.getEmail()%>">
            </div>
            <%session.setAttribute("enteredinfo",new User());%>
            <div style="padding:10px">
                <p style="width:200px">Password must be at least 8 characters and contain one capital letter.</p>
            <span>Password: </span>
            <input type="password" name="password" style="float:right">
                </div>
            <div style="padding:10px">
                <span style="padding:0px 10px 0px 0px">Confirm Password: </span>
            <input type="password" name="confirmpassword" style="float:right">
                </div>
            <% if(session.getAttribute("errormessage")!=null){%>
            <div style="font-weight:bold;color:#932013; padding:5px">
            <%=session.getAttribute("errormessage")%>
            </div>
            <%session.removeAttribute("errormessage");}%>
            <input type="submit" class="signinbutton" name="createaccount" value="Create Account">
        </form>
    </div>
</body>
</html>
<style>
    .prettybutton{
        background-color: #2c71c9;
        color:white;
    }
</style>

