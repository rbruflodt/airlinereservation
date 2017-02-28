<%@ page import="MainPackage.User" %><%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/11/2017
  Time: 11:35 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="/css folder/css/style.css">
<head>
    <title>New Iowa Air Account</title>
</head>
<body class="banner" style="height:650px">
    <div style="border:solid;float:left;padding:5px;background:white">
        <h2>Iowa Air New Account Information</h2>
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
            <div style="color:#6f0000; padding:5px">
            <%=session.getAttribute("errormessage")%>
            </div>
            <%session.removeAttribute("errormessage");}%>
            <input type="submit" name="createaccount" value="Create Account">
        </form>
    </div>
</body>
</html>


