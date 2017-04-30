package MainPackage;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            response.sendRedirect("/index.jsp");
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
            response.sendRedirect("/index.jsp");
        }
        else if(request.getParameter("checkinticket")!=null) {
            session.setAttribute("whichcity", request.getParameter("whichcity"));
            response.sendRedirect("/passengercheckin.jsp");
        }
        else if(request.getParameter("boardingpass")!=null){
                ArrayList<String> passengerstrings = new ArrayList<>();
                ArrayList<String> flightstrings = new ArrayList<>();
                passengerstrings.add((String)session.getAttribute("ticketnum"));
                passengerstrings.add((String)session.getAttribute("lname"));
                passengerstrings.add((String) session.getAttribute("fname"));
                passengerstrings.add(request.getParameter("seatnumber"));
                passengerstrings.add(request.getParameter("numbags"));
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                Statement stmt = null;
                stmt = con.createStatement();
                String search = "select * from tickets where ticket_number ='" + session.getAttribute("ticketnum") + "'";
                ResultSet rs = stmt.executeQuery(search);
                while(rs.next()){
                    search="select * from flights where flight_id='"+rs.getString("flight_id")+"'";
                    Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(search);
                    while(rs2.next()) {
                        if(rs2.getString("depart_city").equals(session.getAttribute("whichcity"))){
                            session.setAttribute("whichflight",rs2.getString("flight_id"));
                        }
                        flightstrings.add(rs2.getString("depart_city"));
                        flightstrings.add(rs2.getString("arrive_city"));
                        flightstrings.add(String.format("%02d", rs2.getInt("depart_hours")) + ":" + String.format("%02d", rs2.getInt("depart_minutes")) + " " + rs2.getString("depart_AMPM") + " (" + rs2.getString("depart_timezone") + ")");
                        flightstrings.add(String.format("%02d", rs2.getInt("arrive_hours")) + ":" + String.format("%02d", rs2.getInt("arrive_minutes")) + " " + rs2.getString("arrive_AMPM") + " (" + rs2.getString("arrive_timezone") + ")");
                    }
                }
                con.close();

                BoardingPassMaker.makeBoardingPass(session,session.getId()+"boardingpass",passengerstrings,flightstrings);
                try {
                    Thread.sleep(10000);
                }catch(Exception e){
                    e.printStackTrace();
                }
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition",
                        "attachment;filename="+session.getId()+"boardingpass"+".pdf");
                ServletContext ctx = getServletContext();
                InputStream is = ctx.getResourceAsStream("/WEB-INF/receipts/"+session.getId()+"boardingpass"+".pdf");

                int read=0;
                byte[] bytes = new byte[1024];
                OutputStream os = response.getOutputStream();

                while((read = is.read(bytes))!= -1){
                    os.write(bytes, 0, read);
                }

                os.flush();
                os.close();

            }
            catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ServletContext sc = session.getServletContext();
            Files.deleteIfExists(Paths.get(sc.getRealPath("/WEB-INF/receipts/"+session.getId()+"boardingpass.pdf")));
            request.getRequestDispatcher("/passengercheckin.jsp").forward(request,response);
        }
        else if(request.getParameter("finishcheckin")!=null){
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://aa3zjrg5cjqq3u.c9taiotksa6k.us-east-1.rds.amazonaws.com:3306/ebdb", "team10", "team1010");
                Statement stmt = null;
                stmt = con.createStatement();
                String search = "update tickets set checked_in = '1' where (ticket_number='" + session.getAttribute("ticketnum") + "' and flight_id='"+session.getAttribute("whichflight")+"')";
                stmt.execute(search);
                con.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            response.sendRedirect("/index.jsp");
        }

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
                    if(rs.getBoolean("checked_in")){
                        temptickets.add("Checked in");
                    }
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
