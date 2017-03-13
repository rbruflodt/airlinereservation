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
        for(Flight f : flights){
            if(request.getParameter("edit"+f.getFlight_id())!=null){
                session.setAttribute("flight",f);
                session.setAttribute("classes",getClasses(f));
                session.setAttribute("prices",getPrices(f));
                response.sendRedirect("/editflight.jsp");
            }
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