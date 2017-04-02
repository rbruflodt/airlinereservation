<%@ page import="MainPackage.SignInServlet" %>
<%@ page import="MainPackage.User" %><%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/1/2017
  Time: 8:57 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="/style.css">
  <head>
    <title>Iowa Air</title>
  </head>
  <body style="background:url(Images/homebackground.jpg);height:1200px">
    <h1 style="text-align:center;color:white;font-size:50pt;">Iowa Air</h1>
    <div style="text-align:center;">
            <div style="text-align:left;color:white; padding:5px; display:inline-block; border:solid; padding:5px; width:200px">
        <%if(session.getAttribute("currentuser")==null){%>
        <h3>Sign In</h3>
            <form action="/signin">
            <div style="padding:5px">Email address: </div>
            <input type="text" name="email">
            <div style="padding:5px">Password: </div>
            <input type="password" name="password">
        <div style="padding:5px">
            <input type="submit" class="signinbutton" name="signin" onclick="Delete_Cookie('currentTab')" value="Sign in">
            <input type="submit" class="signinbutton"name="newaccount" onclick="Delete_Cookie('currentTab')" value="Create account">
        </div>
                <%if(session.getAttribute("verificationoption")!=null){%>
                <p style="font-weight:bold;color:#903723">Account not verified.</p>
                <input type="submit" class="signinbutton" onclick="Delete_Cookie('currentTab')" name="verifyaccount" value="Verify Account">
                <%session.removeAttribute("verificationoption");}%>
            </form>
        <%if(session.getAttribute("loginmessage")!=null){%>
        <div style="color:#903723;font-weight:bold" id="loginmessage">
        <%=session.getAttribute("loginmessage")%>
        </div>
        <%
            session.removeAttribute("loginmessage");
        }
        }
        else{
            User user = (User) session.getAttribute("currentuser");
        %>
        <h3>Welcome, <%=user.getFirstName() + " "+user.getLastName()+"!"%></h3>
        <form action="/signin">
            <input type="submit" class="signinbutton" onclick="Delete_Cookie('currentTab')" name="signout" value="Sign out">
            <input type="submit" class="signinbutton" onclick="Delete_Cookie('currentTab')" name="newpassword" value="Change password">
        </form>
        <% if(session.getAttribute("newpasswordmessage")!=null){%>
        <%=session.getAttribute("newpasswordmessage")%>
        <%session.removeAttribute("newpasswordmessage");}%>
        <%}
        %>
        </div>
        <div style="display:inline-block;width:800px; vertical-align: top">
            <% User user = (User) session.getAttribute("currentuser");
                if(user!=null&&user.isAdmin()){%>
            <%@ include file="admin.jsp"%>
            <%} else if(user!=null&&user.isManager()){%>
            <%@ include file="manager.jsp"%>
            <%}else{%>
            <%@ include file="user.jsp"%>
            <%}%>
        </div>
    </div>
  </body>
</html>



