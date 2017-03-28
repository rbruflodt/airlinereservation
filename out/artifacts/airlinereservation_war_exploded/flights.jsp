<%@ page import="MainPackage.Flight" %>
<%@ page import="MainPackage.FlightsServlet" %>
<%@ page import="MainPackage.AdminFlightServlet" %>
<%@ page import="java.time.LocalDate" %><%--
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
        <select name="flightfromfield" id="from">
            <option <%if(session.getAttribute("flightfromfield")!=null&&session.getAttribute("flightfromfield").equals("Iowa City, IA")){%>selected<%}%>>Iowa City, IA</option>
            <option <%if(session.getAttribute("flightfromfield")!=null&&session.getAttribute("flightfromfield").equals("Chicago, IL")){%>selected<%}%>>Chicago, IL</option>
            <option <%if(session.getAttribute("flightfromfield")!=null&&session.getAttribute("flightfromfield").equals("New York City, NY")){%>selected<%}%>>New York City, NY</option>
            <option <%if(session.getAttribute("flightfromfield")!=null&&session.getAttribute("flightfromfield").equals("Atlanta, GA")){%>selected<%}%>>Atlanta, GA</option>
            <option <%if(session.getAttribute("flightfromfield")!=null&&session.getAttribute("flightfromfield").equals("San Fransisco, CA")){%>selected<%}%>>San Fransisco, CA</option>
        </select>
        <label for="to">To: </label>
        <select name="flighttofield" id="to">
            <option <%if(session.getAttribute("flighttofield")!=null&&session.getAttribute("flighttofield").equals("Iowa City, IA")){%>selected<%}%>>Iowa City, IA</option>
            <option <%if(session.getAttribute("flighttofield")!=null&&session.getAttribute("flighttofield").equals("Chicago, IL")){%>selected<%}%>>Chicago, IL</option>
            <option <%if(session.getAttribute("flighttofield")!=null&&session.getAttribute("flighttofield").equals("New York City, NY")){%>selected<%}%>>New York City, NY</option>
            <option <%if(session.getAttribute("flighttofield")!=null&&session.getAttribute("flighttofield").equals("Atlanta, GA")){%>selected<%}%>>Atlanta, GA</option>
            <option <%if(session.getAttribute("flighttofield")!=null&&session.getAttribute("flighttofield").equals("San Fransisco, CA")){%>selected<%}%>>San Fransisco, CA</option>
        </select>
        <label for="numpassengers">Passengers: </label>
        <input type="number" name="numpassengers" id="numpassengers" min="1" style="width:50px">
        <input type="radio" id="oneway" <%if(session.getAttribute("tripfield")==null||session.getAttribute("tripfield").equals("oneway")){%>checked="checked" <%}%>name="radio" value="oneway">
        <label for="oneway">One way </label>
        <input type="radio" id="roundtrip" name="radio" <%if(session.getAttribute("tripfield")!=null&&session.getAttribute("tripfield").equals("roundtrip")){%>checked="checked" <%}%> value="roundtrip">
        <label for="roundtrip">Round trip</label>
        <br>
        <div id="departpicker" style="display:inline-block">
        <label for="depart">Depart: </label>
        <input type="date" name="departfield" value="<%=session.getAttribute("departfield")%>" id="depart">
        </div>
        <div id="returnpicker" <%if(session.getAttribute("tripfield")==null||!session.getAttribute("tripfield").equals("roundtrip")){%>style="visibility:hidden;display:inline-block"<%}else{%>style="display:inline-block"<%}%> >
        <label for="return">Return: </label>
        <input type="date" id="return" value="<%=session.getAttribute("returnfield")%>" name="returnfield">
        </div><br>
        <%ArrayList<ArrayList<Flight>> requestedflights=FlightsServlet.getFlights(session);%>
        <input type="submit" class="prettybutton" name="searchflights" value="Search">
        <%if(session.getAttribute("flightsearcherror")!=null){%>
        <p style="color:#903723"><%=session.getAttribute("flightsearcherror")%></p>
        <%session.removeAttribute("flightsearcherror");}%>
        <br>
        <br>

        <% if(requestedflights!=null){%>
        <div style="float:left">
            <label  for="flightsort">Sort by:</label>
            <select  id="flightsort" name="flightsort">
                <option <%if(session.getAttribute("flightsort")!=null&&session.getAttribute("flightsort").equals("Price")){%>selected<%}%>>Price</option>
                <option <%if(session.getAttribute("flightsort")!=null&&session.getAttribute("flightsort").equals("Depart Time")){%>selected<%}%>>Depart Time</option>
                <option <%if(session.getAttribute("flightsort")!=null&&session.getAttribute("flightsort").equals("Arrive Time")){%>selected<%}%>>Arrive Time</option>
            </select>
            <br>

            <input class="prettybutton"  type="submit" name="sortbutton" value="Sort" onclick="getScroll()">
        </div>
        <br>
        <br>
        <%if(session.getAttribute("tripfield")!=null&&session.getAttribute("tripfield").equals("roundtrip")){%>
        <h2 style="color:#2c71c9">Departure Trip:</h2>
        <br>
        <%}%>
        <table class="prettytable" border="solid">
            <tr>
                <th>Depart</th>
                <th>Arrive</th>
                <th>Aircraft Type</th>
                <th>Price</th>
                <th>Book</th>
            </tr>
        <%for(ArrayList<Flight> flightArrayList : requestedflights){%>

            <%for(int k = 0;k<flightArrayList.size();k++){
                Flight f=flightArrayList.get(k);
        ArrayList<String> fclasses= AdminFlightServlet.getClasses(f);
        ArrayList<Float> fprices = AdminFlightServlet.getPrices(f);%>
            <tr <%if(k!=flightArrayList.size()-1){%>style="border-bottom: none"<%}%>>
                <td><%=f.getDepart_city()%><br><%=f.getDepart_date()%><br><%=f.getDepart_hours()+":"+String.format("%02d",f.getDepart_minutes())+" "+f.getDepart_AMPM()+" "+f.getDepart_timezone()%></td>
                <td><%=f.getArrive_city()%><br><%if(f.isSame_day()){%><%=f.getDepart_date()%><%}else{%><%=LocalDate.parse(f.getDepart_date()).plusDays(1)%><%}%><br><%=f.getArrive_hours()+":"+String.format("%02d",f.getArrive_minutes())+" "+f.getArrive_AMPM()+" "+f.getArrive_timezone()%></td>
                <td><%=ManageAircraftServlet.getAircraftType(f.getAircraft_name())%></td>
                <td><table>
                    <%for(int i = 0; i < fclasses.size();i++){%>
                        <tr>
                            <td><%=fclasses.get(i)%></td>
                            <td><%="$"+fprices.get(i)%></td>
                        </tr>
                    <%}%>
                </table></td>
                <td>
                <%if(session.getAttribute("currentuser")!=null){%>
                    <table style="padding-top:40px">
                        <%for(String c : fclasses){%><tr><td>
                        <input type="radio" checked="checked" name="<%="bookclass"+f.getFlight_id()%> value="<%=c%>">
                    </td></tr>
                    <%}%>
                        <%if(k==flightArrayList.size()-1){%><tr><td><input type="submit" value="Book" class="prettybutton" name="<%="book"+f.getFlight_id()%>"></td></tr><%}%></table>
                    <%}else{%>
                    <%if(k==flightArrayList.size()-1){%><p style="color:#903723">Sign in to book flight</p><%}%>
                    <%}%>
                </td>
            </tr>
            <%}}}%>
        </table>



        <%if(session.getAttribute("tripfield")!=null&&session.getAttribute("tripfield").equals("roundtrip")){
            ArrayList<ArrayList<Flight>> returnflights = FlightsServlet.getReturnFlights(session);
        if(returnflights!=null){%>
        <br>
        <br>
        <h2 style="color:#2c71c9">Return Trip:</h2>
        <br>
        <table class="prettytable" border="solid">
            <tr>
                <th>Depart</th>
                <th>Arrive</th>
                <th>Aircraft Type</th>
                <th>Price</th>
                <th>Book</th>
            </tr>
            <%for(ArrayList<Flight> flightArrayList : returnflights){%>

            <%for(int k = 0;k<flightArrayList.size();k++){
                Flight f=flightArrayList.get(k);
                ArrayList<String> fclasses= AdminFlightServlet.getClasses(f);
                ArrayList<Float> fprices = AdminFlightServlet.getPrices(f);%>
            <tr <%if(k!=flightArrayList.size()-1){%>style="border-bottom: none"<%}%>>
                <td><%=f.getDepart_city()%><br><%=f.getDepart_date()%><br><%=f.getDepart_hours()+":"+String.format("%02d",f.getDepart_minutes())+" "+f.getDepart_AMPM()+" "+f.getDepart_timezone()%></td>
                <td><%=f.getArrive_city()%><br><%if(f.isSame_day()){%><%=f.getDepart_date()%><%}else{%><%=LocalDate.parse(f.getDepart_date()).plusDays(1)%><%}%><br><%=f.getArrive_hours()+":"+String.format("%02d",f.getArrive_minutes())+" "+f.getArrive_AMPM()+" "+f.getArrive_timezone()%></td>
                <td><%=ManageAircraftServlet.getAircraftType(f.getAircraft_name())%></td>
                <td><table>
                    <%for(int i = 0; i < fclasses.size();i++){%>
                    <tr>
                        <td><%=fclasses.get(i)%></td>
                        <td><%="$"+fprices.get(i)%></td>
                    </tr>
                    <%}%>
                </table></td>
                <td>
                    <%if(session.getAttribute("currentuser")!=null){%>
                    <table style="padding-top:40px">
                        <%for(String c : fclasses){%><tr><td>
                        <input type="radio" checked="checked" name="<%="bookclass"+f.getFlight_id()%> value="<%=c%>">
                    </td></tr>
                        <%}%>
                        <%if(k==flightArrayList.size()-1){%><tr><td><input type="submit" value="Book" class="prettybutton" name="<%="book"+f.getFlight_id()%>"></td></tr><%}%></table>
                    <%}else{%>
                    <%if(k==flightArrayList.size()-1){%><p style="color:#903723">Sign in to book flight</p><%}%>
                    <%}%>
                </td>
            </tr>
            <%}}}}%>
        </table>
    </form>

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





