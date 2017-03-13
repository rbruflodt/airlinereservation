<%@ page import="MainPackage.ManagerAccountsServlet" %>
<%@ page import="MainPackage.Aircraft" %>
<%@ page import="MainPackage.ManageAircraftServlet" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="sun.misc.Request" %>


<%--
  Created by IntelliJ IDEA.
  User: Rachel
  Date: 2/13/2017
  Time: 6:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<link rel="stylesheet" href="/style.css">
<head>
    <title>Iowa Air Admin</title>
</head>
<body>
<div class="tab">
    <a style="width:230px" href="javascript:void(0)" name="Manager Accounts" id="defaultOpen" class="tablinks" onclick="openTab(event,'Manager Accounts')">Manager Accounts</a>
    <a style="width:230px" href="javascript:void(0)" name="Flight Schedule" class="tablinks" onclick="openTab(event,'Flight Schedule')">Flight Schedule</a>
    <a style="width:230px" href="javascript:void(0)" name="Manage Aircraft" class="tablinks" onclick="openTab(event,'Manage Aircraft')">Manage Aircraft</a>
</div>

<div id="Manager Accounts" class="tabcontent" style="text-align:left">
    <form action="/manageraccountsservlet">
        <input type="submit" class="prettybutton" value="Create new Manager Account" name="newmanager">
        <h3 style="color:#2c71c9;">Managers</h3>
        <table class="prettytable" border="1">
            <tr>
                <th>Last Name</th>
                <th>First Name</th>
                <th>Email Address</th>
                <th>Phone #</th>
                <th>Delete Manager</th>
            </tr>
            <% User[] managers = ManagerAccountsServlet.getManagers();
                for(User m : managers){%>
            <tr>
                <td><%=m.getLastName()%></td>
                <td><%=m.getFirstName()%></td>
                <td><%=m.getEmail()%></td>
                <td><%=m.getPhoneNumber()%></td>
                <form action="/manageraccountsservlet">
                <td style="text-align:center"><input type="submit" class="prettybutton" value="Delete" name="<%=m.getEmail()%>"></td>
                </form>
            </tr>
            <%}%>
        </table>
    </form>
</div>

<div id="Flight Schedule" class="tabcontent">
    <form action="/adminflightservlet" style="padding:10px; text-align:left; border-bottom:solid 1px #ccc">
        <h3 style="color:#2c71c9;">Search For Flights</h3>
        <label for="flightidfield" >Flight ID: </label>
        <%if(session.getAttribute("flightidfield")!=null){%>
        <input type="search" size="15" value="<%=session.getAttribute("flightidfield")%>" id="flightidfield" name="flightidfield">
        <%}else{%>
        <input type="search" size="10" id="flightidfield" name="flightidfield">
        <%}%>
        <label for="aircraftnamefield" >Aircraft name: </label>
        <%if(session.getAttribute("aircraftnamefield")!=null){%>
        <input type="search" size="15" value="<%=session.getAttribute("aircraftnamefield")%>" id="aircraftnamefield" name="aircraftnamefield">
        <%}else{%>
        <input type="search" size="10" id="aircraftnamefield" name="aircraftnamefield">
        <%}%>
        <label for="from">From: </label>
        <select id="from">
            <option></option>
            <option>Iowa City, IA</option>
            <option>Chicago, IL</option>
            <option>New York City, NY</option>
            <option>Atlanta, GA</option>
            <option>San Fransisco, CA</option>
        </select>
        <label for="to">To: </label>
        <select id="to">
            <option></option>
            <option>Iowa City, IA</option>
            <option>Chicago, IL</option>
            <option>New York City, NY</option>
            <option>Atlanta, GA</option>
            <option>San Fransisco, CA</option>
        </select>
        <br>
        <input type="submit" class="prettybutton" name="searchflights" value="Search">
    </form>
    <input type="submit" class="prettybutton" name="newflight" value="Add new flight" style="float:left">
    <br>
    <table class="prettytable" border="1">
        <tr>
            <th>Flight ID</th>
            <th>Depart</th>
            <th>Arrive</th>
            <th>Aircraft</th>
            <th>Manage</th>
        </tr>
        <%ArrayList<Flight> flights = FlightsServlet.getFlights(session);
            session.removeAttribute("namefield");
            session.removeAttribute("typefield");
            ArrayList<Aircraft> flight_aircraft = ManageAircraftServlet.getAircraft(session);
            for(Flight f : flights){%>
        <tr>
            <form action="/adminflightservlet">
            <td><input type="text" style="width:100px" name="<%="name"+f.getFlight_id()%>" value="<%=f.getFlight_id()%>"></td>
            <td><select name="<%="depart_city"+f.getFlight_id()%>">
                <option <%if (f.getDepart_city().equals("Iowa City, IA")) {%>selected<%}%>>Iowa City, IA</option>
                <option <%if (f.getDepart_city().equals("Chicago, IL")) {%>selected<%}%>>Chicago, IL</option>
                <option <%if (f.getDepart_city().equals("New York City, NY")) {%>selected<%}%>>New York City, NY</option>
                <option <%if (f.getDepart_city().equals("Atlanta, GA")) {%>selected<%}%>>Atlanta, GA</option>
                <option <%if (f.getDepart_city().equals("San Fransisco, CA")) {%>selected<%}%>>San Fransisco, CA</option>
            </select> <br>
                <input type="number" style="width:50px" value="<%=f.getDepart_time().substring(0,1)%>">: <input type="number" style="width:50px" value="<%=f.getDepart_time().substring(2,4)%>">
                <select name ="<%="depart_AMPM"+f.getDepart_AMPM()%>">
                    <option <%if (f.getDepart_AMPM().equals("AM")) {%>selected<%}%>>AM</option>
                    <option <%if (f.getDepart_AMPM().equals("PM")) {%>selected<%}%>>PM</option>
                </select>
                <select name ="<%="depart_timezone"+f.getDepart_timezone()%>">
                    <option <%if (f.getDepart_timezone().equals("CST")) {%>selected<%}%>>CST</option>
                    <option <%if (f.getDepart_timezone().equals("EST")) {%>selected<%}%>>EST</option>
                    <option <%if (f.getDepart_timezone().equals("PDT")) {%>selected<%}%>>PDT</option>
                </select>
            </td>
            <td><select name="<%="arrive_city"+f.getFlight_id()%>">
                <option <%if (f.getArrive_city().equals("Iowa City, IA")) {%>selected<%}%>>Iowa City, IA</option>
                <option <%if (f.getArrive_city().equals("Chicago, IL")) {%>selected<%}%>>Chicago, IL</option>
                <option <%if (f.getArrive_city().equals("New York City, NY")) {%>selected<%}%>>New York City, NY</option>
                <option <%if (f.getArrive_city().equals("Atlanta, GA")) {%>selected<%}%>>Atlanta, GA</option>
                <option <%if (f.getArrive_city().equals("San Fransisco, CA")) {%>selected<%}%>>San Fransisco, CA</option>
            </select><br>
                <input type="number" style="width:50px" value="<%=f.getArrive_time().substring(0,1)%>">: <input type="number" style="width:50px" value="<%=f.getArrive_time().substring(2,4)%>">
                <select name ="<%="arrive_AMPM"+f.getDepart_AMPM()%>">
                    <option <%if (f.getArrive_AMPM().equals("AM")) {%>selected<%}%>>AM</option>
                    <option <%if (f.getArrive_AMPM().equals("PM")) {%>selected<%}%>>PM</option>
                </select>
                <select name ="<%="arrive_timezone"+f.getDepart_timezone()%>">
                    <option <%if (f.getArrive_timezone().equals("CST")) {%>selected<%}%>>CST</option>
                    <option <%if (f.getArrive_timezone().equals("EST")) {%>selected<%}%>>EST</option>
                    <option <%if (f.getArrive_timezone().equals("PDT")) {%>selected<%}%>>PDT</option>
                </select></td>
            <td><select name="<%="aircraft"+f.getFlight_id()%>">
                <%for(Aircraft a : flight_aircraft){%>
                <option <% if (f.getAircraft_name().equals(a.getName())) {%>selected<%}%>><%=a.getName()%></option>
                <%}%>
            </select></td>
            <td><input type="submit" class="prettybutton" value="Edit" name="<%="edit"+f.getFlight_id()%>">
                <input type="submit" class="prettybutton" value="Save" name="<%="save"+f.getFlight_id()%>">
                <input type="submit" class="prettybutton" value="Delete" name="<%="delete"+f.getFlight_id()%>"></td>
            </form>
        </tr>
        <%}%>
    </table>
</div>

<div id="Manage Aircraft" class="tabcontent">
    <form action="/manageaircraftservlet">
        <input type="submit" class="prettybutton" style="bottom-padding:10px;float:left" value="Add Aircraft" name="newaircraft">
        <br>
        <div style="float:left;height:10px;padding-top:15px">
        <label for="namefield" >Name: </label>
            <%if(session.getAttribute("namefield")!=null){%>
        <input type="search" size="15" value="<%=session.getAttribute("namefield")%>" id="namefield" name="namefield">
            <%}else{%>
            <input type="search" size="10" id="namefield" name="namefield">
            <%}%>
        <label for="typefield">Type: </label>
        <select id="typefield" name="typefield">
            <option></option>
            <option <%if (session.getAttribute("typefield")!=null&&session.getAttribute("typefield").equals("Boeing 777")) {%>selected<%}%>>Boeing 777</option>
            <option <%if (session.getAttribute("typefield")!=null&&session.getAttribute("typefield").equals("Boeing 767")) {%>selected<%}%>>Boeing 767</option>
            <option <%if (session.getAttribute("typefield")!=null&&session.getAttribute("typefield").equals("Boeing 747")) {%>selected<%}%>>Boeing 747</option>
            <option <%if (session.getAttribute("typefield")!=null&&session.getAttribute("typefield").equals("Airbus 380")) {%>selected<%}%>>Airbus 380</option>
        </select>
        <input type="submit" class="prettybutton" name="searchaircraft" value="Search">
        </div>
        <br>
        <br>
            <div>
        <h3 style="color:#2c71c9;float:left;">Aircraft</h3>


        <%if(session.getAttribute("aircrafterror")!=null){%>
        <h4 style="color:#903723;"><%=session.getAttribute("aircrafterror")%></h4>
        <% session.removeAttribute("aircrafterror");}%>
        <% ArrayList<Aircraft> aircrafts = ManageAircraftServlet.getAircraft(session);%>
        <table class="prettytable" border="1">
            <tr>
                <th>Name</th>
                <th>Type</th>
                <th>Classes</th>
                <th>Manage</th>
            </tr>
            <% for(Aircraft a : aircrafts){%>
            <tr>
                <td><input type="text" style="text-align:center" size="10" name="<%="name"+a.getName()%>" value="<%=a.getName()%>"></td>
                <td><select id="type" name="<%="type"+a.getName()%>">
                    <option <%if (a.getAircraft_type().equals("Boeing 777")) {%>selected<%}%>>Boeing 777</option>
                    <option <%if (a.getAircraft_type().equals("Boeing 767")) {%>selected<%}%>>Boeing 767</option>
                    <option <%if (a.getAircraft_type().equals("Boeing 747")) {%>selected<%}%>>Boeing 747</option>
                    <option <%if (a.getAircraft_type().equals("Airbus 380")) {%>selected<%}%>>Airbus 380</option>
                </select></td>
                <td>
                    <table style="padding:1px;">
                        <% if(a.getClasses()!=null){for(int i = 0; i<a.getClasses().size();i++){%>
                    <tr><td><input type="text" size="10" style="text-align:center" name="<%="class"+a.getName()+a.getClasses().get(i)%>" value="<%=a.getClasses().get(i)%>"></td>
                        <td><input type="number" style="width:50px;text-align:center" min="0" name="<%="seats"+a.getName()+a.getClasses().get(i)%>" value=<%=a.getSeats().get(i)%>> seats</td>
                    <td><input type="submit" class="prettybutton" onclick="getScroll()" name="<%="deleteclass"+a.getName()+a.getClasses().get(i)%>" value="X"></td></tr>
                    <br>
                        <%}}%>
                    </table>
                    <input style="float:left" class="prettybutton" value="Add Class" onclick="getScroll()" type="submit" name="<%=a.getName()%>">
                </td>
                <td style="text-align:center"><input type="submit" onclick="getScroll()" class="prettybutton" value="Save Changes" name="<%=a.getName()%>">
                <input type="submit" class="prettybutton" value="Delete" name="<%=a.getName()%>"></td>
            </tr>
            <%}%>
        </table>
        </div>
    </form>
</div>
</body>
</html>
<%@ include file="tabs.jsp"%>


