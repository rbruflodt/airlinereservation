package MainPackage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


/**
 * Created by Rachel on 3/13/2017.
 */
@WebServlet("/adminflightservlet")
public class AdminFlightServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        HttpSession session = request.getSession();
        ArrayList<Flight> flights = getFlights(session);
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
                LocalDate until = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get(f.getDepart_timezone()))).plusMonths(1);
                if(!request.getParameter("frequency").equals("onceradio")&&!request.getParameter("until").equals("")&&request.getParameter("until")!=null){
                    until = LocalDate.parse(request.getParameter("until"));
                }
                if(request.getParameter("frequency").equals("onceradio")){
                    if(f.getOnce()==null||!f.getOnce().equals(request.getParameter("once"))){
                        query="update flights set once='"+request.getParameter("once")+"', monthly=null, weekly=null, until=null where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                        query="delete from flight_instances where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                        LocalDate departdate=LocalDate.parse(request.getParameter("once"));
                        if(f.isSame_day()){
                            query="insert into flight_instances (flight_id,depart_date,arrive_date) values('"+f.getFlight_id()+"', '"+request.getParameter("once")+"', '" +
                                    request.getParameter("once")+"')";
                        }
                        else{
                            query="insert into flight_instances (flight_id,depart_date,arrive_date) values('"+f.getFlight_id()+"', '"+request.getParameter("once")+"', '" +
                                    departdate.plusDays(1)+"')";
                        }
                        stmt.execute(query);
                    }
                }
                else if(request.getParameter("frequency").equals("monthlyradio")){
                    if(f.getMonthly()==null||!f.getMonthly().equals(request.getParameter("monthday"))||!until.equals(f.getUntil())){

                        query="update flights set monthly='"+request.getParameter("monthday")+"', once=null, weekly=null, until='"+until+"' where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                        query="delete from flight_instances where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                        LocalDate currentdate = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get(f.getDepart_timezone())));
                        while(currentdate.compareTo(until)<=0){
                            if(currentdate.getDayOfMonth()==Integer.valueOf(request.getParameter("monthday"))){
                                if(f.isSame_day()){
                                    query="insert into flight_instances (flight_id,depart_date,arrive_date) values('"+f.getFlight_id()+"', '"+currentdate+"', '" +
                                            currentdate+"')";
                                }
                                else{
                                    query="insert into flight_instances (flight_id,depart_date,arrive_date) values('"+f.getFlight_id()+"', '"+currentdate+"', '" +
                                            currentdate.plusDays(1)+"')";
                                }
                                stmt.execute(query);
                            }
                            currentdate=currentdate.plusDays(1);
                        }
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
                    if(f.getWeekly()==null||!f.getWeekly().equals(weekly)||!until.equals(f.getUntil())){

                        query="update flights set weekly='"+weekly+"', monthly=null, once=null, until='"+until+"' where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                        query="delete from flight_instances where flight_id='"+f.getFlight_id()+"'";
                        stmt.execute(query);
                        LocalDate currentdate = LocalDate.now(ZoneId.of(ZoneId.SHORT_IDS.get(f.getDepart_timezone())));
                        while(currentdate.compareTo(until)<=0){
                            if(weekly.contains(currentdate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US).toLowerCase())){
                                if(f.isSame_day()){
                                    query="insert into flight_instances (flight_id,depart_date,arrive_date) values('"+f.getFlight_id()+"', '"+currentdate+"', '" +
                                            currentdate+"')";
                                }
                                else{
                                    query="insert into flight_instances (flight_id,depart_date,arrive_date) values('"+f.getFlight_id()+"', '"+currentdate+"', '" +
                                            currentdate.plusDays(1)+"')";
                                }
                                stmt.execute(query);
                            }
                            currentdate=currentdate.plusDays(1);
                        }
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
                ArrayList<Aircraft> aircrafts = getAircraft(session);
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
                query="insert into flights (depart_city, arrive_city, aircraft_name, flight_id, depart_AMPM, depart_timezone, arrive_AMPM, arrive_timezone, once, weekly, monthly, arrive_hours, arrive_minutes, depart_hours, depart_minutes, same_day, until)" +
                        " values ('Iowa City, IA','Iowa City, IA', '"+aircraft_name+"', 'Flight "+size+"', 'AM', 'CST', 'AM', 'CST', '', 'sunday', null, 12,0,12,0,1,null)";
                stmt.execute(query);
                query="select * from seats where aircraft_name='"+aircraft_name+"'";
                ResultSet rs =stmt.executeQuery(query);
                while(rs.next()){
                    query="insert into prices (flight_id, class, price) values ('Flight "+size+"', '"+rs.getString("class")+"', 0)";
                    Statement stmt2 = con.createStatement();
                    stmt2.execute(query);
                }
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
                                Statement stmt2 = con.createStatement();
                                stmt2.execute(query);
                            }
                        }
                        boolean same_day=false;
                        if(request.getParameter("same_day"+f.getFlight_id())!=null){
                            same_day=true;
                        }
                        if(same_day!=f.isSame_day()) {
                            query = "select * from flight_instances where flight_id='"+f.getFlight_id()+"'";
                            ResultSet rs = stmt.executeQuery(query);
                            while(rs.next()) {
                                LocalDate departdate = LocalDate.parse(rs.getString("depart_date"));

                                if (same_day) {
                                    query = "update flight_instances set depart_date='" + departdate + "', arrive_date='" + departdate + "' where (flight_id='"+f.getFlight_id()+"') and (depart_date='"+departdate+"')";
                                } else {
                                    query = "update flight_instances set depart_date='" + departdate + "', arrive_date='" + departdate.plusDays(1) + "' where (flight_id='"+f.getFlight_id()+"') and (depart_date='"+departdate+"')";
                                }
                                Statement stmt2 = con.createStatement();
                                stmt2.execute(query);
                            }}

                        query = "update flights set depart_hours='" + request.getParameter("departhours" + f.getFlight_id()) + "', depart_minutes='" + request.getParameter("departminutes" + f.getFlight_id()) + "', " +
                                "arrive_hours='" + request.getParameter("arrivehours" + f.getFlight_id()) + "', arrive_minutes='" + request.getParameter("arriveminutes" + f.getFlight_id()) + "', same_day="+same_day+
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
            con.close();
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
            String search = "select * from prices where flight_id='"+f.getFlight_id()+"' order by price";
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
            String search = "select * from prices where flight_id='"+f.getFlight_id()+"' order by price";
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
                                rs.getInt("arrive_hours"),rs.getInt("arrive_minutes"),rs.getString("arrive_AMPM"),rs.getString("arrive_timezone"),rs.getString("flight_id"),rs.getString("once"),rs.getString("weekly"),rs.getString("monthly"),rs.getBoolean("same_day"),rs.getString("until"),null));
                    }
                }
                while(rs.next());
                Collections.sort(flights);
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

    public static ArrayList<Aircraft> getAircraft(HttpSession session){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            Statement stmt = con.createStatement();
            String search = "select * from aircraft";
            ResultSet rs = stmt.executeQuery(search);
            if(!rs.next()){
                con.close();
                return new ArrayList<Aircraft>();
            }
            else{
                ArrayList<Aircraft> aircrafts= new ArrayList<>();
                do{
                    if(session.getAttribute("namefield")==null||rs.getString("name").toLowerCase().indexOf(((String)(session.getAttribute("namefield"))).toLowerCase())==0) {
                        aircrafts.add(new Aircraft(rs.getString("name"), rs.getString("type")));
                    }
                }
                while(rs.next());
                Collections.sort(aircrafts);

                con.close();
                return  aircrafts;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}