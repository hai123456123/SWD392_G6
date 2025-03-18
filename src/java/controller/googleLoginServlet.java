/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.GoogleService;
import dao.AccountDao;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author DELL
 */
public class googleLoginServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet googleLoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet googleLoginServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("/WEB-INF/Views/LoginWithGoogle.jsp").forward(request, response);
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
        HttpSession session = request.getSession();
        // Nếu cần, bạn có thể kiểm tra xem email đã đăng ký trên hệ thống chưa
        // Ví dụ:
        // if (!AccountDao.isEmailExists(email)) {
        //    session.setAttribute("error", "Email chưa được đăng ký! Vui lòng đăng ký trước.");
        //    response.sendRedirect("register.jsp");
        //    return;
        // } 
        // Tạo OTP và gửi email
        String otp = GoogleService.generateOTP();
        session.setAttribute("otp", otp);
        session.setAttribute("email", email);
        try {

            GoogleService.sendOTP(email, otp);
            // Sau khi gửi OTP thành công, chuyển đến trang nhập OTP
            request.getRequestDispatcher("/WEB-INF/Views/verifyOTP.jsp").forward(request, response);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi khi gửi email OTP.");
            request.getRequestDispatcher("/WEB-INF/Views/login.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "GoogleLoginServlet handles Google OTP login process";
    }// </editor-fold>

}
