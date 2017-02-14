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
  <head>
    <title>Iowa Air</title>
  </head>
  <body>
    <h1 style="text-align:center">Iowa Air</h1>
    <div style="text-align:center">
  <img src="/Images/plane.jpg" style="width:300px;height:128px;">
    </div>
    <div style="border:solid;float:left;padding:5px">
        <%if(session.getAttribute("currentuser")==null){%>
        <h3>Sign In</h3>
            <form action="/signin">
            <div style="padding:5px">Email address: </div>
            <input type="text" name="email">
            <div style="padding:5px">Password: </div>
            <input type="password" name="password">
        <div style="padding:5px">
            <input type="submit" name="signin" value="Sign in">
            <input type="submit" name="newaccount" value="Create account">
        </div>
            </form>
        <%if(session.getAttribute("loginmessage")!=null){%>
        <div style="color:red">
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
        <% if(session.getAttribute("newpasswordmessage")!=null){%>
        <%=session.getAttribute("newpasswordmessage")%>
        <%session.removeAttribute("newpasswordmessage");}%>
        <form action="/signin">
            <input type="submit" name="signout" value="Sign out">
            <input type="submit" name="newpassword" value="Change password">
        </form>
        <%}
        %>
  </div>
  </body>
  <% User user = (User) session.getAttribute("currentuser");
      if(user!=null&&user.isAdmin()){%>
        <%@ include file="admin.jsp"%>
  <%} else if(user!=null&&user.isManager()){%>
  <%@ include file="manager.jsp"%>
  <%}else{%>
  <%@ include file="user.jsp"%>
<%}%>
</html>


