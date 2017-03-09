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
            ArrayList<Aircraft> aircrafts = getAircraft();
        if(request.getParameter("newaircraft")!=null){
            int size;
            if(aircrafts!=null){
                size = aircrafts.size();
            }
            else{
                size = 0;
            }
            query="insert into aircraft (name, type) values('Aircraft "+String.valueOf(size+1)+"','Boeing 777')";
            stmt.execute(query);
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
                    }
                    else if(request.getParameter(aircrafts.get(i).getName()).equals("Delete")){
                        query="delete from aircraft where name='"+aircrafts.get(i).getName()+"'";
                        stmt.execute(query);
                        query="delete from seats where aircraft_name='"+aircrafts.get(i).getName()+"'";
                        stmt.execute(query);
                    }
                else if(request.getParameter(aircrafts.get(i).getName()).equals("Save Changes")) {
                        query = "update aircraft set " +
                                "name='" + request.getParameter("name" + aircrafts.get(i).getName()) + "', " +
                                "type='" + request.getParameter("type" + aircrafts.get(i).getName()) + "' " +
                                "where name='" + aircrafts.get(i).getName() + "'";
                        stmt.execute(query);
                        if(aircrafts.get(i).getClasses()!=null) {
                            for (int j = 0; j < aircrafts.get(i).getClasses().size(); j++) {
                                query = "update seats set " +
                                        "class='" + request.getParameter("class" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)) + "', " +
                                        "max_seats=" + (request.getParameter("seats" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)) + ", " +
                                        "aircraft_name='" + request.getParameter("name" + aircrafts.get(i).getName()) + "' " +
                                        "where (class='" + aircrafts.get(i).getClasses().get(j)) + "') and (aircraft_name='" + aircrafts.get(i).getName() + "')";
                                stmt.execute(query);
                            }
                        }
                    }

                }
                else {
                    if(aircrafts.get(i).getClasses()!=null) {
                        for (int j = 0; j < aircrafts.get(i).getClasses().size(); j++) {
                            if (request.getParameter("deleteclass" + aircrafts.get(i).getName() + aircrafts.get(i).getClasses().get(j)) != null) {
                                query = "delete from seats where (aircraft_name='" + aircrafts.get(i).getName() + "') and (class='" + aircrafts.get(i).getClasses().get(j) + "')";
                                stmt.execute(query);
                            }
                        }
                    }
                }
            }
        }
            }catch(ClassNotFoundException e){
                e.printStackTrace();
            } catch(SQLException e){
                e.printStackTrace();
            }
        response.getOutputStream().println("<script>document.getElementById(</script>");
            response.sendRedirect("/index.jsp");
        }

    public static ArrayList<Aircraft> getAircraft(){
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
                    if(rs.getString("name").contains("Aircraft ")){
                        aircrafts.add(0,new Aircraft(rs.getString("name"), rs.getString("type")));
                    }
                    else {
                        aircrafts.add(new Aircraft(rs.getString("name"), rs.getString("type")));
                    }
                }
                while(rs.next());
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
