package MainPackage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.time.LocalDate;
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
            if((request.getParameter("radio").equals("roundtrip")&&request.getParameter("returnfield")!=null)){
                if(LocalDate.parse(request.getParameter("returnfield")).compareTo(LocalDate.now())<0){
                    flightsearcherror="Must select future date.";
                }else {
                    session.setAttribute("tripfield", "roundtrip");
                    session.setAttribute("returnfield", request.getParameter("returnfield"));
                }
            }
            else if(!request.getParameter("radio").equals("oneway")){
                flightsearcherror="All fields required.";
            }
            session.setAttribute("flightsearcherror",flightsearcherror);
        }
        response.sendRedirect("/index.jsp");
    }


    public static ArrayList<Flight> getFlights(HttpSession session){
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
                ArrayList<Flight> flights = new ArrayList<>();
                while(rs.next()){
                    if(flightids.contains(rs.getString("flight_id"))&&rs.getString("depart_city").equals(session.getAttribute("flightfromfield"))&&rs.getString("arrive_city").equals(session.getAttribute("flighttofield"))) {
                        flights.add(new Flight(rs.getString("depart_city"), rs.getString("arrive_city"), rs.getString("aircraft_name"), rs.getInt("depart_hours"), rs.getInt("depart_minutes"), rs.getString("depart_AMPM"), rs.getString("depart_timezone"),
                                rs.getInt("arrive_hours"), rs.getInt("arrive_minutes"), rs.getString("arrive_AMPM"), rs.getString("arrive_timezone"), rs.getString("flight_id"), rs.getString("once"), rs.getString("weekly"), rs.getString("monthly"), rs.getBoolean("same_day"),rs.getString("until")));
                    }
                }

                con.close();
                if(flights.size()>0) {
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
}
