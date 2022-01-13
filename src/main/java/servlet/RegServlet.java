package servlet;

import models.User;
import store.ItemStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * @author ArvikV
 * @version 1.0
 * @since 13.01.2022
 */
@WebServlet(urlPatterns = "/reg")
public class RegServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        if (ItemStore.instOf().findUserByName(name) != null) {
            req.setAttribute("error", "Email занят");
            req.getRequestDispatcher("reg.jsp").forward(req, res);
        } else {
            ItemStore.instOf().addUser(new User(name, password));
            HttpSession session = req.getSession();
            session.setAttribute("user", ItemStore.instOf().findUserByName(name));
            res.sendRedirect(req.getContextPath() + "/index.html");
        }
    }
}
