package MainPackage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Rachel on 2/16/2017.
 */
@WebServlet("/manageraccountsservlet")
public class ManagerAccountsServlet  extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        HttpSession session = request.getSession(true);
            if(request.getParameter("newmanager")!=null){
                session.setAttribute("enteredinfo",new User());
                session.setAttribute("newmanager",true);
                response.sendRedirect("/newaccount.jsp");
            }

    }
}
