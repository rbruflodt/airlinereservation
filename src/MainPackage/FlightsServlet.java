package MainPackage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Rachel on 2/16/2017.
 */
@WebServlet("/flightsservlet")
public class FlightsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        HttpSession session = request.getSession();
        if(request.getParameter("searchflights")!=null){
            session.setAttribute("flightfromfield",request.getParameter("flightfromfield"));
            session.setAttribute("flighttofield",request.getParameter("flighttofield"));
            session.setAttribute("numpassengers",request.getParameter("numpassengers"));
            session.removeAttribute("departfield");
            session.removeAttribute("tripfield");
            session.removeAttribute("returnfield");
            String flightsearcherror="";
            if(!request.getParameter("departfield").equals("")){
                if(LocalDate.parse(request.getParameter("departfield")).compareTo(LocalDate.now())<0){
                    flightsearcherror="Must select future date.";
                }
                else {
                    session.setAttribute("departfield", request.getParameter("departfield"));
                    session.setAttribute("tripfield", "oneway");
                }
            }
            else{
                flightsearcherror="All fields required.";
            }
            if((request.getParameter("radio").equals("roundtrip"))&&flightsearcherror==""){
                if(request.getParameter("returnfield")==null||request.getParameter("returnfield").equals("")){
                    flightsearcherror="All fields required";
                    session.setAttribute("tripfield","roundtrip");
                }
                else if(LocalDate.parse(request.getParameter("returnfield")).compareTo(LocalDate.parse(request.getParameter("departfield")))<0){
                    flightsearcherror="Must select future date.";
                    session.setAttribute("tripfield","roundtrip");
                }else {
                    session.setAttribute("tripfield", "roundtrip");
                    session.setAttribute("returnfield", request.getParameter("returnfield"));
                }
            }
            else if(!request.getParameter("radio").equals("oneway")){
                flightsearcherror="All fields required.";
            }
            session.setAttribute("flightsearcherror",flightsearcherror);
            response.sendRedirect("/index.jsp");
        }
        else if(request.getParameter("sortbutton")!=null){
            session.setAttribute("flightsort",request.getParameter("flightsort"));
            response.sendRedirect("/index.jsp");
        }else{
            session.setAttribute("numpassengers",request.getParameter("numpassengers"));
            ArrayList<ArrayList<Flight>> flights = getFlights(session);
            if(session.getAttribute("tripfield").equals("roundtrip")) {
                flights.addAll(getReturnFlights(session));
            }
            for(ArrayList<Flight> flightArrayList : flights){
                if(request.getParameter("book"+flightArrayList.get(flightArrayList.size()-1).getFlight_id())!=null){
                    session.setAttribute("flightstobook",flightArrayList);
                    float totalprice=0;
                    ArrayList<String> bookedclasses = new ArrayList<>();
                    ArrayList<Float> bookedprices = new ArrayList<>();
                    for(Flight f : flightArrayList) {
                        String c = request.getParameter("bookclass"+f.getFlight_id());
                        bookedclasses.add(c);
                        ArrayList<String> classes = AdminFlightServlet.getClasses(f);
                        ArrayList<Float> prices = AdminFlightServlet.getPrices(f);
                        for(int j = 0; j < classes.size();j++) {
                            if(classes.get(j).equals(c)) {
                                totalprice += prices.get(j);
                                bookedprices.add(prices.get(j));

                            }
                        }
                    }
                    totalprice=totalprice*((Integer.valueOf(request.getParameter("numpassengers"))));
                    session.setAttribute("totalprice",String.valueOf(totalprice));
                    session.setAttribute("bookedclasses",bookedclasses);
                    session.setAttribute("bookedprices",bookedprices);

                }
            }
            response.sendRedirect("/bookflights.jsp");
        }

    }


    public static ArrayList<ArrayList<Flight>> getFlights(HttpSession session){
        try {
            if((session.getAttribute("flightfromfield")!=null&&session.getAttribute("flighttofield")!=null
                    &&session.getAttribute("departfield")!=null&&(session.getAttribute("tripfield").equals("oneway"))||
                    (session.getAttribute("returnfield")!=null))){
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                Statement stmt = con.createStatement();
                String search = "select * from flight_instances where depart_date='"+session.getAttribute("departfield")+"'";
                ResultSet rs = stmt.executeQuery(search);
                ArrayList<String> flightids = new ArrayList<>();
                while(rs.next()){
                    flightids.add(rs.getString("flight_id"));
                }
                search = "select * from flights";
                rs = stmt.executeQuery(search);
                ArrayList<ArrayList<Flight>> flights = new ArrayList<>();
                while(rs.next()){
                    ArrayList<Flight> tempflights = new ArrayList<>();
                    if(flightids.contains(rs.getString("flight_id"))&&rs.getString("depart_city").equals(session.getAttribute("flightfromfield"))&&rs.getString("arrive_city").equals(session.getAttribute("flighttofield"))) {
                        tempflights.add(new Flight(rs.getString("depart_city"), rs.getString("arrive_city"), rs.getString("aircraft_name"), rs.getInt("depart_hours"), rs.getInt("depart_minutes"), rs.getString("depart_AMPM"), rs.getString("depart_timezone"),
                                rs.getInt("arrive_hours"), rs.getInt("arrive_minutes"), rs.getString("arrive_AMPM"), rs.getString("arrive_timezone"), rs.getString("flight_id"), rs.getString("once"), rs.getString("weekly"), rs.getString("monthly"), rs.getBoolean("same_day"),rs.getString("until"),(String)session.getAttribute("departfield")));
                        flights.add(tempflights);
                    }
                    else if(flightids.contains(rs.getString("flight_id"))&&rs.getString("depart_city").equals(session.getAttribute("flightfromfield"))){
                        search = "select * from flights where (depart_city='"+rs.getString("arrive_city")+"') and (arrive_city='"+session.getAttribute("flighttofield")+"')";
                        Statement stmt2 = con.createStatement();
                        ResultSet rs2 = stmt2.executeQuery(search);
                        String arrday;
                        if(rs.getBoolean("same_day")){
                            arrday = LocalDate.parse((String)session.getAttribute("departfield")).toString();
                        }else {
                            arrday=LocalDate.parse((String) session.getAttribute("departfield")).plusDays(1).toString();
                        }
                        while(rs2.next()) {
                            search = "select * from flight_instances where (flight_id='"+rs2.getString("flight_id")+"') and ((depart_date='"+arrday+"') or (depart_date='"+LocalDate.parse(arrday).plusDays(1)+"'))";
                            Statement stmt3 = con.createStatement();
                            ResultSet rs3 = stmt3.executeQuery(search);
                            while(rs3.next()) {
                                tempflights=new ArrayList<>();
                                if (timeDifference(rs.getInt("arrive_hours"), rs.getInt("arrive_minutes"), rs.getString("arrive_AMPM"), arrday, rs2.getInt("depart_hours"), rs2.getInt("depart_minutes"), rs2.getString("depart_AMPM"), rs3.getString("depart_date"))) {
                                    tempflights.add(new Flight(rs.getString("depart_city"), rs.getString("arrive_city"), rs.getString("aircraft_name"), rs.getInt("depart_hours"), rs.getInt("depart_minutes"), rs.getString("depart_AMPM"), rs.getString("depart_timezone"),
                                            rs.getInt("arrive_hours"), rs.getInt("arrive_minutes"), rs.getString("arrive_AMPM"), rs.getString("arrive_timezone"), rs.getString("flight_id"), rs.getString("once"), rs.getString("weekly"), rs.getString("monthly"), rs.getBoolean("same_day"), rs.getString("until"),(String)session.getAttribute("departfield")));
                                    tempflights.add(new Flight(rs2.getString("depart_city"), rs2.getString("arrive_city"), rs2.getString("aircraft_name"), rs2.getInt("depart_hours"), rs2.getInt("depart_minutes"), rs2.getString("depart_AMPM"), rs2.getString("depart_timezone"),
                                            rs2.getInt("arrive_hours"), rs2.getInt("arrive_minutes"), rs2.getString("arrive_AMPM"), rs2.getString("arrive_timezone"), rs2.getString("flight_id"), rs2.getString("once"), rs2.getString("weekly"), rs2.getString("monthly"), rs2.getBoolean("same_day"), rs2.getString("until"),rs3.getString("depart_date")));
                                    flights.add(tempflights);
                                }
                            }
                        }
                    }
                }



                con.close();
                if(flights.size()>0) {
                    Collections.sort(flights,new PriceComparator());
                    if(session.getAttribute("flightsort")!=null&&session.getAttribute("flightsort").equals("Depart Time")){
                        Collections.sort(flights, new DepartTimeComparator());
                    }else if(session.getAttribute("flightsort")!=null&&session.getAttribute("flightsort").equals("Arrive Time")){
                        Collections.sort(flights, new ArriveTimeComparator());
                    }
                    return flights;
                }
                else{
                    session.setAttribute("flightsearcherror","No matching flights found.");
                }

            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ArrayList<Flight>> getReturnFlights(HttpSession session){
        try {
            if((session.getAttribute("flightfromfield")!=null&&session.getAttribute("flighttofield")!=null
                    &&session.getAttribute("departfield")!=null&&(session.getAttribute("tripfield").equals("oneway"))||
                    (session.getAttribute("returnfield")!=null))){
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                Statement stmt = con.createStatement();
                String search = "select * from flight_instances where depart_date='"+session.getAttribute("returnfield")+"'";
                ResultSet rs = stmt.executeQuery(search);
                ArrayList<String> flightids = new ArrayList<>();
                while(rs.next()){
                    flightids.add(rs.getString("flight_id"));
                }
                search = "select * from flights";
                rs = stmt.executeQuery(search);
                ArrayList<ArrayList<Flight>> flights = new ArrayList<>();
                while(rs.next()){
                    ArrayList<Flight> tempflights = new ArrayList<>();
                    if(flightids.contains(rs.getString("flight_id"))&&rs.getString("depart_city").equals(session.getAttribute("flighttofield"))&&rs.getString("arrive_city").equals(session.getAttribute("flightfromfield"))) {
                        tempflights.add(new Flight(rs.getString("depart_city"), rs.getString("arrive_city"), rs.getString("aircraft_name"), rs.getInt("depart_hours"), rs.getInt("depart_minutes"), rs.getString("depart_AMPM"), rs.getString("depart_timezone"),
                                rs.getInt("arrive_hours"), rs.getInt("arrive_minutes"), rs.getString("arrive_AMPM"), rs.getString("arrive_timezone"), rs.getString("flight_id"), rs.getString("once"), rs.getString("weekly"), rs.getString("monthly"), rs.getBoolean("same_day"),rs.getString("until"),(String)session.getAttribute("returnfield")));
                        flights.add(tempflights);
                    }
                    else if(flightids.contains(rs.getString("flight_id"))&&rs.getString("depart_city").equals(session.getAttribute("flighttofield"))){
                        search = "select * from flights where (depart_city='"+rs.getString("arrive_city")+"') and (arrive_city='"+session.getAttribute("flightromfield")+"')";
                        Statement stmt2 = con.createStatement();
                        ResultSet rs2 = stmt2.executeQuery(search);
                        String arrday;
                        if(rs.getBoolean("same_day")){
                            arrday = LocalDate.parse((String)session.getAttribute("returnfield")).toString();
                        }else {
                            arrday=LocalDate.parse((String) session.getAttribute("returnfield")).plusDays(1).toString();
                        }
                        while(rs2.next()) {
                            search = "select * from flight_instances where (flight_id='"+rs2.getString("flight_id")+"') and ((depart_date='"+arrday+"') or (depart_date='"+LocalDate.parse(arrday).plusDays(1)+"'))";
                            Statement stmt3 = con.createStatement();
                            ResultSet rs3 = stmt3.executeQuery(search);
                            while(rs3.next()) {
                            tempflights=new ArrayList<>();
                                if (timeDifference(rs.getInt("arrive_hours"), rs.getInt("arrive_minutes"), rs.getString("arrive_AMPM"), arrday, rs2.getInt("depart_hours"), rs2.getInt("depart_minutes"), rs2.getString("depart_AMPM"), rs3.getString("depart_date"))) {
                                    tempflights.add(new Flight(rs.getString("depart_city"), rs.getString("arrive_city"), rs.getString("aircraft_name"), rs.getInt("depart_hours"), rs.getInt("depart_minutes"), rs.getString("depart_AMPM"), rs.getString("depart_timezone"),
                                            rs.getInt("arrive_hours"), rs.getInt("arrive_minutes"), rs.getString("arrive_AMPM"), rs.getString("arrive_timezone"), rs.getString("flight_id"), rs.getString("once"), rs.getString("weekly"), rs.getString("monthly"), rs.getBoolean("same_day"), rs.getString("until"),(String)session.getAttribute("returnfield")));
                                    tempflights.add(new Flight(rs2.getString("depart_city"), rs2.getString("arrive_city"), rs2.getString("aircraft_name"), rs2.getInt("depart_hours"), rs2.getInt("depart_minutes"), rs2.getString("depart_AMPM"), rs2.getString("depart_timezone"),
                                            rs2.getInt("arrive_hours"), rs2.getInt("arrive_minutes"), rs2.getString("arrive_AMPM"), rs2.getString("arrive_timezone"), rs2.getString("flight_id"), rs2.getString("once"), rs2.getString("weekly"), rs2.getString("monthly"), rs2.getBoolean("same_day"), rs2.getString("until"),rs3.getString("depart_date")));
                                    flights.add(tempflights);
                                }
                            }
                        }
                    }
                }



                con.close();
                if(flights.size()>0) {
                    Collections.sort(flights,new PriceComparator());
                    if(session.getAttribute("flightsort")!=null&&session.getAttribute("flightsort").equals("Depart Time")){
                        Collections.sort(flights, new DepartTimeComparator());
                    }else if(session.getAttribute("flightsort")!=null&&session.getAttribute("flightsort").equals("Arrive Time")){
                        Collections.sort(flights, new ArriveTimeComparator());
                    }
                    return flights;
                }
                else{
                    session.setAttribute("flightsearcherror","No matching return flights found.");
                }

            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean timeDifference(int a_hours, int a_minutes, String a_AMPM, String a_arrive,int b_hours, int b_minutes, String b_AMPM, String b_depart ){
        LocalDateTime atime = LocalDateTime.parse(a_arrive+"T"+String.format("%02d", a_hours) + ":" + String.format("%02d", a_minutes));
        LocalDateTime btime = LocalDateTime.parse(b_depart+"T"+String.format("%02d", b_hours) + ":" + String.format("%02d",b_minutes));
        if (a_AMPM.equals("PM")) {
            atime = LocalDateTime.parse(a_arrive+"T"+String.format("%02d", a_hours+12) + ":" + String.format("%02d", a_minutes)+":00");
        }
        if (b_AMPM.equals("PM")) {
            btime = LocalDateTime.parse(b_depart+"T"+String.format("%02d", b_hours+12 )+ ":" + String.format("%02d",b_minutes)+":00");
        }
        if(!btime.isAfter(atime)||btime.minusHours(12).isAfter(atime)){
            return false;
        }else{
            return true;
        }
    }
}
