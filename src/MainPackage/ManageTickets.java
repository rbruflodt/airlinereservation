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
 * Created by Lei Xiang on 4/22/2017.
 */
@WebServlet("/managetickets")
public class ManageTickets extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        HttpSession session = request.getSession();
        if (request.getParameter("searchtickets") != null) {
            session.setAttribute("ticketnum", request.getParameter("ticketnum"));
            session.setAttribute("lname", request.getParameter("lname"));
            session.setAttribute("fname", request.getParameter("fname"));
            //response.sendRedirect("/index.jsp");
            String ticketsearcherror = "";
        }
        if (request.getParameter("cancelticketnumber") != null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                Statement stmt = null;
                stmt = con.createStatement();
                String search = "delete from tickets where (ticket_number='" + session.getAttribute("ticketnum") + "')";
                stmt.execute(search);
                con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(request.getParameter("checkinticket")!=null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                Statement stmt = null;
                stmt = con.createStatement();
                String search = "update tickets set checked_in = '1' where ticket_number='" + session.getAttribute("ticketnum") + "'";
                stmt.execute(search);
                con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect("/index.jsp");
    }

    public static ArrayList<ArrayList<String>> getTickets(HttpSession session){
        try{
            if(session.getAttribute("ticketnum")!=null|| (session.getAttribute("lname")!=null && session.getAttribute("fname")!=null)) {
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                Statement stmt = con.createStatement();
                String search = "select * from tickets where (ticket_number='" + session.getAttribute("ticketnum") + "' and last_name='" + session.getAttribute("lname") + "' and first_name ='" + session.getAttribute("fname") + "')";
                ResultSet rs = stmt.executeQuery(search);
                ArrayList<ArrayList<String>> tickets = new ArrayList<>();
                while (rs.next()) {
                    ArrayList<String> temptickets = new ArrayList<>();
                    search = "select * from flights where flight_id='" + rs.getString("flight_id") + "'";
                    Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(search);
                    rs2.next();
                    temptickets.add(String.valueOf(rs.getInt("ticket_number")));
                    temptickets.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                    temptickets.add(rs2.getString("depart_city"));
                    temptickets.add(rs.getString("depart_date"));
                    temptickets.add(String.format("%02d", rs2.getInt("depart_hours")) + ":" + String.format("%02d", rs2.getInt("depart_minutes")) + " " + rs2.getString("depart_AMPM") + " (" + rs2.getString("depart_timezone") + ")");
                    temptickets.add(rs2.getString("arrive_city"));
                    temptickets.add(rs.getString("arrive_date"));
                    temptickets.add(String.format("%02d", rs2.getInt("arrive_hours")) + ":" + String.format("%02d", rs2.getInt("arrive_minutes")) + " " + rs2.getString("arrive_AMPM") + " (" + rs2.getString("arrive_timezone") + ")");
                    tickets.add(temptickets);
                }
                tickets.sort(new TicketDepartTimeComparator());
                con.close();
                return tickets;
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }
}
