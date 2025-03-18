/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AccountDao;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import model.User;
import service.AccountService;

/**
 *
 * @author DELL
 */
public class LoginServlet extends HttpServlet {

    public AccountService accountService;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

    @Override
    public void init() throws ServletException {
        accountService = new AccountService();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       request.getRequestDispatcher("/WEB-INF/Views/login.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
HttpSession session = request.getSession();
        AccountDao ad = new AccountDao();

        // Kiểm tra xem email có tồn tại trong hệ thống không
        Optional<User> u1 = ad.getAccountByEmail(email);

        if (!u1.isPresent()) {
            // Không tìm thấy tài khoản với email đã nhập
            request.setAttribute("mess", "Tài khoản của bạn chưa tồn tại!");
            request.getRequestDispatcher("/WEB-INF/Views/login.jsp").forward(request, response);
        } else {
            User u = u1.get();

            // Kiểm tra mật khẩu (Nếu mã hóa, cần so sánh bằng BCrypt hoặc tương tự)
            if (!u.getPass().equals(password)) {
                request.setAttribute("mess", "Bạn nhập không đúng mật khẩu");
                request.getRequestDispatcher("/WEB-INF/Views/login.jsp").forward(request, response);
            } else {
                // Đăng nhập thành công
                
                session.setAttribute("acc", u);

                switch (u.getRoleId()) {
                    case 1:
                        response.sendRedirect("/WEB-INF/Views/homeAdmin1.jsp");
                        break;
                    case 3:
                        response.sendRedirect("/WEB-INF/Views/homeStaff.jsp");
                        break;
                    case 4:
                        request.setAttribute("mess", "Tài khoản của bạn đã bị khóa do vi phạm quy định của Web");
                        request.getRequestDispatcher("/WEB-INF/Views/login.jsp").forward(request, response);
                        break;
                    default:
                        response.sendRedirect("home");
                        break;
                }
            }
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
