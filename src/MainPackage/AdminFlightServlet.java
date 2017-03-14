package MainPackage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Rachel on 3/13/2017.
 */
@WebServlet("/adminflightservlet")
public class AdminFlightServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        HttpSession session = request.getSession();
        ArrayList<Flight> flights = FlightsServlet.getFlights(session);
        session.removeAttribute("namefield");
        session.removeAttribute("typefield");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            Statement stmt = con.createStatement();
            String query;
            if(request.getParameter("editflight")!=null){
                ArrayList<String> classes = (ArrayList<String>) session.getAttribute("classes");
                Flight f = (Flight) session.getAttribute("flight");
                for (String c : classes){
                    query = "update prices set price="+Float.valueOf(request.getParameter(c))+" where class='"+c+"' and flight_id='"+f.getFlight_id()+"'";
                    stmt.execute(query);
                }
                if(request.getParameter("frequency").equals("onceradio")){
                    if(f.getOnce()==null||!f.getOnce().equals(request.getParameter("once"))){
                        query="update flights set once='"+request.getParameter("once")+"', monthly=null, weekly=null where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                    }
                }
                else if(request.getParameter("frequency").equals("monthlyradio")){
                    if(f.getMonthly()==null||!f.getMonthly().equals(request.getParameter("monthday"))){
                        query="update flights set monthly='"+request.getParameter("monthday")+"', once=null, weekly=null where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                    }
                }
                else{
                    String weekly = "";
                    String[] days = {"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
                    for(String d : days) {
                        if (request.getParameter(d)!=null) {
                            weekly += d;
                        }
                    }
                    if(f.getWeekly()==null||!f.getWeekly().equals(weekly)){
                        query="update flights set weekly='"+weekly+"', monthly=null, once=null where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                    }
                }
                session.removeAttribute("classes");
                session.removeAttribute("prices");
                session.removeAttribute("flight");
                response.sendRedirect("/index.jsp");
            }
            else if(request.getParameter("searchflights")!=null){
                session.setAttribute("flightidfield",request.getParameter("flightidfield"));
                session.setAttribute("aircraftnamefield",request.getParameter("aircraftnamefield"));
                session.setAttribute("fromfield",request.getParameter("fromfield"));
                session.setAttribute("tofield",request.getParameter("tofield"));
                response.sendRedirect("/index.jsp");
            }
            else if (request.getParameter("newflight") != null) {
                int size;
                String aircraft_name;
                ArrayList<Aircraft> aircrafts = ManageAircraftServlet.getAircraft(session);
                if(flights!=null){
                    size = flights.size();
                    ResultSet rs;
                    do{
                        size++;
                        query="select * from flights where flight_id='Flight "+size+"'";
                        rs=stmt.executeQuery(query);
                    }while(rs.next());
                }
                else{
                    size = 1;
                }
                if(aircrafts.size()==0){
                    aircraft_name="";
                }
                else{
                    aircraft_name=aircrafts.get(0).getName();
                }
                query="insert into flights (depart_city, arrive_city, aircraft_name, flight_id, depart_AMPM, depart_timezone, arrive_AMPM, arrive_timezone, once, weekly, monthly, arrive_hours, arrive_minutes, depart_hours, depart_minutes)" +
                        " values ('Iowa City, IA','Iowa City, IA', '"+aircraft_name+"', 'Flight "+size+"', 'AM', 'CST', 'AM', 'CST', '', 'sunday', '', 12,0,12,0)";
                stmt.execute(query);
                session.removeAttribute("aircraftnamefield");
                session.removeAttribute("fromfield");
                session.removeAttribute("tofield");
                session.setAttribute("flightidfield","Flight "+size);
                response.sendRedirect("/index.jsp");
            }
            else {
                for (Flight f : flights) {
                    if (request.getParameter("edit" + f.getFlight_id()) != null) {
                        session.setAttribute("flight", f);
                        session.setAttribute("classes", getClasses(f));
                        session.setAttribute("prices", getPrices(f));
                        response.sendRedirect("/editflight.jsp");
                    } else if (request.getParameter("save" + f.getFlight_id()) != null) {

                        query = "update flights set aircraft_name ='" + request.getParameter("aircraft" + f.getFlight_id()) + "'," +
                                "depart_city='" + request.getParameter("depart_city" + f.getFlight_id()) + "', " +
                                "depart_AMPM='" + request.getParameter("depart_AMPM" + f.getFlight_id()) + "', " +
                                "depart_timezone='" + request.getParameter("depart_timezone" + f.getFlight_id()) + "'," +
                                "arrive_city='" + request.getParameter("arrive_city" + f.getFlight_id()) + "', " +
                                "arrive_AMPM='" + request.getParameter("arrive_AMPM" + f.getFlight_id()) + "', " +
                                "arrive_timezone='" + request.getParameter("arrive_timezone" + f.getFlight_id()) + "'" +
                                "where flight_id='" + f.getFlight_id() + "'";
                        stmt.execute(query);
                        if(!f.getAircraft_name().equals(request.getParameter("aircraft"+f.getFlight_id()))){
                            query="delete from prices where flight_id='"+f.getFlight_id()+"'";
                            stmt.execute(query);
                            query="select * from seats where aircraft_name='"+request.getParameter("aircraft"+f.getFlight_id())+"'";
                            ResultSet rs =stmt.executeQuery(query);
                            while(rs.next()){
                                query="insert into prices (flight_id, class, price) values ('"+f.getFlight_id()+"', '"+rs.getString("class")+"', 0)";
                                stmt.execute(query);
                            }
                        }
                        query = "update flights set depart_hours='" + request.getParameter("departhours" + f.getFlight_id()) + "', depart_minutes='" + request.getParameter("departminutes" + f.getFlight_id()) + "', " +
                                "arrive_hours='" + request.getParameter("arrivehours" + f.getFlight_id()) + "', arrive_minutes='" + request.getParameter("arriveminutes" + f.getFlight_id()) + "'" +
                                " where flight_id='" + f.getFlight_id() + "'";
                        stmt.execute(query);
                        if (!f.getFlight_id().equals(request.getParameter("name" + f.getFlight_id()))) {
                            query = "select * from flights where flight_id='" + request.getParameter("name" + f.getFlight_id()) + "'";
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                session.setAttribute("flighterror", "Flight IDs must be unique. ");
                            } else {
                                query = "update flights set flight_id='" + request.getParameter("name" + f.getFlight_id()) + "'" +
                                        " where flight_id='" + f.getFlight_id() + "'";
                                stmt.execute(query);
                                query = "update flight_instances set flight_id='" + request.getParameter("name" + f.getFlight_id()) + "'" +
                                        " where flight_id='" + f.getFlight_id() + "'";
                                stmt.execute(query);
                                query = "update prices set flight_id='" + request.getParameter("name" + f.getFlight_id()) + "'" +
                                        " where flight_id='" + f.getFlight_id() + "'";
                                stmt.execute(query);
                                query = "update tickets set flight_id='" + request.getParameter("name" + f.getFlight_id()) + "'" +
                                        " where flight_id='" + f.getFlight_id() + "'";
                                stmt.execute(query);
                            }
                        }
                        response.sendRedirect("/index.jsp");
                    }
                    else if(request.getParameter("delete"+f.getFlight_id())!=null){
                        query="delete from flights where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                        query="delete from flight_instances where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                        query="delete from prices where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                        query="delete from tickets where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                        response.sendRedirect("/index.jsp");
                    }
                }
            }
        }

        catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public static ArrayList<String> getClasses(Flight f){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            Statement stmt = con.createStatement();
            String search = "select * from prices where flight_id='"+f.getFlight_id()+"'";
            ResultSet rs = stmt.executeQuery(search);
            if(!rs.next()){
                con.close();
                return new ArrayList<String>();
            }
            else{
                ArrayList<String> classes= new ArrayList<>();
                do{
                    classes.add(rs.getString("class"));
                }
                while(rs.next());
                con.close();
                return  classes;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static ArrayList<Float> getPrices(Flight f){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            Statement stmt = con.createStatement();
            String search = "select * from prices where flight_id='"+f.getFlight_id()+"'";
            ResultSet rs = stmt.executeQuery(search);
            if(!rs.next()){
                con.close();
                return new ArrayList<Float>();
            }
            else{
                ArrayList<Float> prices= new ArrayList<>();
                do{
                    prices.add(rs.getFloat("price"));
                }
                while(rs.next());
                con.close();
                return  prices;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}