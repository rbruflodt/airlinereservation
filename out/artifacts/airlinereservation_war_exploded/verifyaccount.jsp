<%@ page import="MainPackage.User" %><%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/11/2017
  Time: 6:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Verify Iowa Air Account
    <%if(session.getAttribute("enteredinfo")!=null){%>
        for <%=((User) session.getAttribute("enteredinfo")).getEmail()%>
    <%}%></title>
</head>
<body>
<h2>Enter the code sent to you by email: </h2>
<form action="/verifyaccountservlet">
<input type="text" name="codeinput">
<input type = "submit" name="submit">
<input type="submit" name="resend" value="Resend Email">
</form>
<%if(session.getAttribute("codeerrormessage")!=null){%>
<div style="color:red">
<%=session.getAttribute("codeerrormessage")%>
</div>
<%session.removeAttribute("codeerrormessage");}%>
</body>
</html>
