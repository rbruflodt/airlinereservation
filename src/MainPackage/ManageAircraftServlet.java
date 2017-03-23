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
 * Created by Rachel on 3/5/2017.
 */
@WebServlet("/manageaircraftservlet")
public class ManageAircraftServlet  extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        HttpSession session = request.getSession(true);
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            Statement stmt = con.createStatement();
            String query;
            ArrayList<Aircraft> aircrafts = getAircraft(session);
        if(request.getParameter("newaircraft")!=null){
            int size;
            if(aircrafts!=null){
                size = aircrafts.size();
                ResultSet rs;
                do{
                    size++;
                    query="select * from aircraft where name='Aircraft "+String.valueOf(size)+"'";
                    rs=stmt.executeQuery(query);
                }while(rs.next());
            }
            else{
                size = 1;
            }
            session.setAttribute("namefield","Aircraft "+String.valueOf(size));
            session.setAttribute("typefield","");
            query="insert into aircraft (name, type) values('Aircraft "+String.valueOf(size)+"','Boeing 777')";
            stmt.execute(query);
            con.close();
        }
        else if(request.getParameter("searchaircraft")!=null){
            session.setAttribute("typefield",request.getParameter("typefield"));
            session.setAttribute("namefield",request.getParameter("namefield"));
            con.close();
        }
        else {
            for (int i = 0; i < aircrafts.size(); i++) {

                    if (request.getParameter(aircrafts.get(i).getName()) != null&&request.getParameter(aircrafts.get(i).getName()).equals("Delete")) {
                        query = "delete from aircraft where name='" + aircrafts.get(i).getName() + "'";
                        stmt.execute(query);
                        query = "delete from seats where aircraft_name='" + aircrafts.get(i).getName() + "'";
                        stmt.execute(query);
                    } else {
                        String aircrafterror = "";
                        if (aircrafts.get(i).getClasses() != null) {
                            for (int j = 0; j < aircrafts.get(i).getClasses().size(); j++) {
                                if (!aircrafts.get(i).getClasses().get(j).equals(request.getParameter("class" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)))) {
                                    query = "select * from seats where (class='" + request.getParameter("class" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)) + "')" +
                                            "and (aircraft_name='" + aircrafts.get(i).getName() + "')";
                                    ResultSet rs = stmt.executeQuery(query);
                                    if (rs.next()) {
                                        aircrafterror += "Class names must be unique within the aircraft.";
                                    }
                                }
                                if (!aircrafterror.contains("Class names must be unique within the aircraft.")) {
                                    query = "update seats set " +
                                            "class='" + request.getParameter("class" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)) + "', " +
                                            "max_seats=" + (request.getParameter("seats" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)) + " " +
                                            "where (class='" + aircrafts.get(i).getClasses().get(j)) + "') and (aircraft_name='" + aircrafts.get(i).getName() + "')";
                                    stmt.execute(query);
                                    query = "select * from flights where aircraft_name='" + aircrafts.get(i).getName() + "'";
                                    ResultSet rs = stmt.executeQuery(query);
                                    if (rs.next()) {
                                        do {
                                            query = "update prices set " +
                                                    "class='" + request.getParameter("class" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)) +
                                                    "' where class='" + aircrafts.get(i).getClasses().get(j) + "'";
                                            Statement stmt2 = con.createStatement();
                                            stmt2.execute(query);
                                        } while (rs.next());
                                    }
                                }
                            }
                        }
                        if (request.getParameter(aircrafts.get(i).getName()) != null&&!aircrafts.get(i).getName().equals(request.getParameter("name" + aircrafts.get(i).getName()))) {
                            query = "select * from aircraft where name='" + request.getParameter("name" + aircrafts.get(i).getName()) + "'";
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                aircrafterror += "Aircraft names must be unique. ";
                            }
                        }
                        if (!aircrafterror.contains("Aircraft names must be unique. ")) {
                            query = "update aircraft set " +
                                    "name='" + request.getParameter("name" + aircrafts.get(i).getName()) + "', " +
                                    "type='" + request.getParameter("type" + aircrafts.get(i).getName()) + "' " +
                                    "where name='" + aircrafts.get(i).getName() + "'";
                            stmt.execute(query);
                            query = "update seats set " +
                                    "aircraft_name='" + request.getParameter("name" + aircrafts.get(i).getName()) + "' " +
                                    "where aircraft_name='" + aircrafts.get(i).getName() + "'";
                            stmt.execute(query);
                        }

                        session.setAttribute("aircrafterror", aircrafterror);
                        if (request.getParameter(aircrafts.get(i).getName()) != null&&request.getParameter(aircrafts.get(i).getName()).equals("Add Class")) {
                            int size;
                            if (aircrafts.get(i).getClasses() != null) {
                                size = aircrafts.get(i).getClasses().size();
                                ResultSet rs;
                                do {
                                    size++;
                                    query = "select * from seats where aircraft_name='" + aircrafts.get(i).getName() + "' and class='Class " + size + "'";
                                    rs = stmt.executeQuery(query);

                                } while (rs.next());
                            } else {
                                size = 1;
                            }
                            query = "insert into seats (class, max_seats, aircraft_name) values ('Class " + String.valueOf(size) + "', 0, '" + aircrafts.get(i).getName() + "')";
                            stmt.execute(query);
                            query = "select * from flights where aircraft_name='" + aircrafts.get(i).getName() + "'";
                            ResultSet rs = stmt.executeQuery(query);
                            while (rs.next()) {
                                query = "insert into prices (flight_id, class, price) values ('" + rs.getString("flight_id") + "', 'Class " + size + "', 0)";
                                Statement stmt2 = con.createStatement();
                                stmt2.execute(query);
                            }
                        }
                    }


                        if (aircrafts.get(i).getClasses() != null) {
                            for (int j = 0; j < aircrafts.get(i).getClasses().size(); j++) {
                                if (request.getParameter("deleteclass" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)) != null) {
                                    query = "delete from seats where (aircraft_name='" + aircrafts.get(i).getName() + "') and (class='" + aircrafts.get(i).getClasses().get(j) + "')";
                                    stmt.execute(query);
                                    query = "select * from flights where aircraft_name='" + aircrafts.get(i).getName() + "'";
                                    ResultSet rs = stmt.executeQuery(query);
                                    while (rs.next()) {
                                        query = "delete from prices where (flight_id='" + rs.getString("flight_id") + "') and (class='" + aircrafts.get(i).getClasses().get(j) + "')";
                                        Statement stmt2 = con.createStatement();
                                        stmt2.execute(query);
                                    }
                                }
                            }
                        }


            }
            con.close();
        }
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            } catch(SQLException e){
                e.printStackTrace();
            }

            response.sendRedirect("/index.jsp");
        }

    public static String getAircraftType(String aircraftName){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            Statement stmt = con.createStatement();
            String search = "select * from aircraft where name='"+aircraftName+"'";
            ResultSet rs = stmt.executeQuery(search);
            if(rs.next()){
                return rs.getString("type");
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    public static ArrayList<Aircraft> getAircraft(HttpSession session){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
            Statement stmt = con.createStatement();
            String search = "select * from aircraft";
            if(session.getAttribute("typefield")!=null&&!session.getAttribute("typefield").equals("")){
                search = "select * from aircraft where type='"+session.getAttribute("typefield")+"'";
            }
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
                for(int i = 0; i < aircrafts.size();i++){
                    search="select * from seats where aircraft_name='"+aircrafts.get(i).getName()+"'";
                    rs=stmt.executeQuery(search);
                    if(rs.next()){
                        ArrayList<String> classes = new ArrayList<>();
                        ArrayList<Integer> seats = new ArrayList<>();
                        do{
                            classes.add(rs.getString("class"));
                            seats.add(rs.getInt("max_seats"));
                        }while(rs.next());
                        aircrafts.get(i).setClasses(classes);
                        aircrafts.get(i).setSeats(seats);
                    }
                }
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
