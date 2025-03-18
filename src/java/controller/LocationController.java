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
import static proxy.GHNProxy.fetchFromGHN;

/**
 *
 * @author Admin
 */
@WebServlet(name = "LocationController", urlPatterns = {"/LocationController"})
public class LocationController extends HttpServlet {
    private static final String API_PROVINCE = "https://online-gateway.ghn.vn/shiip/public-api/master-data/province";
    private static final String API_DISTRICT = "https://online-gateway.ghn.vn/shiip/public-api/master-data/district";
    private static final String API_WARD = "https://online-gateway.ghn.vn/shiip/public-api/master-data/ward";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String provinceId = request.getParameter("provinceId");
        String districtId = request.getParameter("districtId");

        String jsonResponse = "";

        if ("provinces".equals(action)) {
            jsonResponse = fetchFromGHN(API_PROVINCE);
        } else if ("districts".equals(action) && provinceId != null) {
            jsonResponse = fetchFromGHN(API_DISTRICT, "{\"province_id\":" + provinceId + "}");
        } else if ("wards".equals(action) && districtId != null) {
            jsonResponse = fetchFromGHN(API_WARD, "{\"district_id\":" + districtId + "}");
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }

}
