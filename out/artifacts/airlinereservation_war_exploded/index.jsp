<%@ page import="Sample.HelloWorld" %><%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/1/2017
  Time: 8:57 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Simple JSP Page</title>
  </head>
  <body>
  <h3 class="message"><%=HelloWorld.getMessage()%></h3>
  <form action="HelloWorld" method="post">
    <button type="submit" name="show">Show</button>
    <button type="submit" name="hide">Hide</button>
    <br>
    <button type="submit" name="otherpage">Go to Other Page</button>
  </form>
  <h4><%=HelloWorld.getPassengers()%></h4>
  </body>
</html>

