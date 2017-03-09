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
        session.setAttribute("currenttab","ManageAircraft");
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
            }
            else{
                size = 0;
            }
            session.setAttribute("namefield","Aircraft "+String.valueOf(size+1));
            session.setAttribute("typefield","");
            query="insert into aircraft (name, type) values('Aircraft "+String.valueOf(size+1)+"','Boeing 777')";
            stmt.execute(query);
        }
        else if(request.getParameter("searchaircraft")!=null){
            session.setAttribute("typefield",request.getParameter("typefield"));
            session.setAttribute("namefield",request.getParameter("namefield"));
        }
        else {
            for (int i = 0; i < aircrafts.size(); i++) {
                if (request.getParameter(aircrafts.get(i).getName()) != null) {
                    if(request.getParameter(aircrafts.get(i).getName()).equals("Add Class")) {
                        int size;
                        if(aircrafts.get(i).getClasses()!=null){
                            size = aircrafts.get(i).getClasses().size();
                        }
                        else{
                            size = 0;
                        }
                        query = "insert into seats (class, max_seats, remaining_seats, aircraft_name, price) values ('Class " + String.valueOf(size + 1) + "', 0, 0, '" + aircrafts.get(i).getName() + "', 0)";
                        stmt.execute(query);
                        session.setAttribute("namefield",aircrafts.get(i).getName());
                        session.setAttribute("typefield","");
                    }
                    else if(request.getParameter(aircrafts.get(i).getName()).equals("Delete")){
                        query="delete from aircraft where name='"+aircrafts.get(i).getName()+"'";
                        stmt.execute(query);
                        query="delete from seats where aircraft_name='"+aircrafts.get(i).getName()+"'";
                        stmt.execute(query);
                    }
                else if(request.getParameter(aircrafts.get(i).getName()).equals("Save Changes")) {
                    String aircrafterror="";
                        if(aircrafts.get(i).getClasses()!=null) {
                            for (int j = 0; j < aircrafts.get(i).getClasses().size(); j++) {
                                if(!aircrafts.get(i).getClasses().get(j).equals(request.getParameter("class" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)))) {
                                    query = "select * from seats where (class='" + request.getParameter("class" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)) + "')" +
                                            "and (aircraft_name='" + aircrafts.get(i).getName() + "')";
                                    ResultSet rs = stmt.executeQuery(query);
                                    if (rs.next()) {
                                        aircrafterror += "Class names must be unique within the aircraft.";
                                    }
                                }
                                if(!aircrafterror.contains("Class names must be unique within the aircraft.")){
                                    query = "update seats set " +
                                            "class='" + request.getParameter("class" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)) + "', " +
                                            "max_seats=" + (request.getParameter("seats" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)) + " " +
                                            "where (class='" + aircrafts.get(i).getClasses().get(j)) + "') and (aircraft_name='" + aircrafts.get(i).getName() + "')";
                                    stmt.execute(query);
                                    session.setAttribute("namefield",aircrafts.get(i).getName());
                                    session.setAttribute("typefield","");
                                }
                            }
                        }
                        if(!aircrafts.get(i).getName().equals(request.getParameter("name" + aircrafts.get(i).getName()))) {
                            query = "select * from aircraft where name='" + request.getParameter("name" + aircrafts.get(i).getName()) + "'";
                            ResultSet rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                aircrafterror += "Aircraft names must be unique. ";
                            }
                        }
                        if(!aircrafterror.contains("Aircraft names must be unique. ")) {
                            query = "update aircraft set " +
                                    "name='" + request.getParameter("name" + aircrafts.get(i).getName()) + "', " +
                                    "type='" + request.getParameter("type" + aircrafts.get(i).getName()) + "' " +
                                    "where name='" + aircrafts.get(i).getName() + "'";
                            stmt.execute(query);
                            query = "update seats set " +
                                    "aircraft_name='"+request.getParameter("name" + aircrafts.get(i).getName())+"' " +
                                    "where aircraft_name='"+aircrafts.get(i).getName()+"'";
                            stmt.execute(query);
                            session.setAttribute("namefield",request.getParameter("name" + aircrafts.get(i).getName()));
                            session.setAttribute("typefield","");
                        }

                        session.setAttribute("aircrafterror",aircrafterror);
                    }

                }
                else {
                    if(aircrafts.get(i).getClasses()!=null) {
                        for (int j = 0; j < aircrafts.get(i).getClasses().size(); j++) {
                            if (request.getParameter("deleteclass" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)) != null) {
                                query = "delete from seats where (aircraft_name='" + aircrafts.get(i).getName() + "') and (class='" + aircrafts.get(i).getClasses().get(j) + "')";
                                stmt.execute(query);
                                session.setAttribute("namefield",aircrafts.get(i).getName());
                                session.setAttribute("typefield","");
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
                    if(session.getAttribute("namefield")==null||rs.getString("name").indexOf((String)(session.getAttribute("namefield")))==0) {
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
