package MainPackage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Rachel on 2/16/2017.
 */
@WebServlet("/flightsservlet")
public class FlightsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        response.sendRedirect("/index.jsp");
    }


    public static ArrayList<Flight> getFlights(HttpSession session) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            Statement stmt = con.createStatement();
            String search = "select * from flights";
            if(session.getAttribute("fromfield")!=null&&!session.getAttribute("fromfield").equals("")){
                search = "select * from flights where depart_city='"+session.getAttribute("fromfield")+"'";
            }
            if(session.getAttribute("tofield")!=null&&!session.getAttribute("tofield").equals("")){
                if(search.equals("select * from flights")) {
                    search = "select * from flights where arrive_city='" + session.getAttribute("tofield") + "'";
                }
                else{
                    search+=" and arrive_city='"+session.getAttribute("tofield")+"'";
                }
            }
            ResultSet rs = stmt.executeQuery(search);
            if(!rs.next()){
                con.close();
                return new ArrayList<Flight>();
            }
            else{
                ArrayList<Flight> flights= new ArrayList<>();
                do{
                    if((session.getAttribute("flightidfield")==null||rs.getString("flight_id").toLowerCase().indexOf(((String)(session.getAttribute("flightidfield"))).toLowerCase())==0)&&(session.getAttribute("aircraftnamefield")==null||rs.getString("aircraft_name").toLowerCase().indexOf(((String)(session.getAttribute("aircraftnamefield"))).toLowerCase())==0)) {
                        flights.add(new Flight(rs.getString("depart_city"), rs.getString("arrive_city"),rs.getString("aircraft_name"),rs.getInt("depart_hours"),rs.getInt("depart_minutes"),rs.getString("depart_AMPM"),rs.getString("depart_timezone"),
                                rs.getInt("arrive_hours"),rs.getInt("arrive_minutes"),rs.getString("arrive_AMPM"),rs.getString("arrive_timezone"),rs.getString("flight_id"),rs.getString("once"),rs.getString("weekly"),rs.getString("monthly")));
                    }
                }
                while(rs.next());
                //Collections.sort(flights);
                con.close();
                return  flights;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
