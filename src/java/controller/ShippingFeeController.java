/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import static proxy.GHNProxy.GetShippingFee;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ShippingFeeController", urlPatterns = {"/calculate-shipping"})
public class ShippingFeeController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String toWardCode = request.getParameter("toWardCode");
        String toDistrictId = request.getParameter("toDistrictId");
        response.getWriter().write(GetShippingFee(toDistrictId, toWardCode));
        return; 
    }
}
