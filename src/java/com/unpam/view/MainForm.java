/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.unpam.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Devi Pratiwi
 */
@WebServlet(name = "MainForm", urlPatterns = {"/MainForm"})
public class MainForm extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void tampilkan(String konten, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true); // [cite: 1427]

        // --- MENU SAMPING (Sesuai Modul Hal 25) ---
        String menu = "<br><b>Master Data</b><br>"
                + "<a href='KaryawanController'>Karyawan</a><br>"
                + "<a href='PekerjaanController'>Pekerjaan</a><br><br>"
                + "<b>Transaksi</b><br>"
                + "<a href='GajiController'>Gaji</a><br><br>"
                + "<b>Laporan</b><br>"
                + "<a href='LaporanGajiController'>Gaji</a><br><br>"
                + "<a href='LogoutController'>Logout</a><br><br>"; // [cite: 1428-1434]

        // --- TOP MENU (Sesuai Modul Hal 25) ---
        String topMenu = "<nav><ul>"
                + "<li><a href='.'>Home</a></li>"
                + "<li><a href='#'>Master Data</a>"
                + "<ul>"
                + "<li><a href='KaryawanController'>Karyawan</a></li>"
                + "<li><a href='PekerjaanController'>Pekerjaan</a></li>"
                + "</ul>"
                + "</li>"
                + "<li><a href='#'>Transaksi</a>"
                + "<ul>"
                + "<li><a href='GajiController'>Gaji</a></li>"
                + "</ul>"
                + "</li>"
                + "<li><a href='#'>Laporan</a>"
                + "<ul>"
                + "<li><a href='LaporanGajiController'>Gaji</a></li>"
                + "</ul>"
                + "</li>"
                + "<li><a href='LogoutController'>Logout</a></li>"
                + "</ul>"
                + "</nav>"; // [cite: 1437-1475]

        String userName = "";

        // Cek Session [cite: 1481]
        if (!session.isNew()) {
           // Cara Modul
try {
    userName = session.getAttribute("userName").toString();
} catch (Exception ex) {}

            // Jika User Valid [cite: 1493]
            if (userName != null && !userName.equals("")) {
                
                // Jika konten kosong, isi dengan pesan selamat datang 
                if (konten.equals("")) {
                    konten = "<br><h1>Selamat Datang</h1><h2>" + userName + "</h2>";
                }

                // Cek Session Menu [cite: 1500-1503]
                try {
                    if (session.getAttribute("menu") != null) {
                        menu = session.getAttribute("menu").toString();
                    }
                } catch (Exception ex) {
                }

                try {
                    if (session.getAttribute("topMenu") != null) {
                        topMenu = session.getAttribute("topMenu").toString();
                    }
                } catch (Exception ex) {
                }

                // --- OUTPUT HTML (Sesuai Modul Hal 26-27) ---
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<link href='style.css' rel='stylesheet' type='text/css' />");
                    out.println("<title>Informasi Gaji Karyawan</title>");
                    out.println("</head>");
                    out.println("<body bgcolor='#808080'>");

                    out.println("<center>");
                    out.println("<table width='80%' bgcolor='#eeeeee'>"); // Modul 60% atau 80% (sesuaikan visual)

                    // HEADER
                    out.println("<tr>");
                    out.println("<td colspan='2' align='center'>");
                    out.println("<br>");
                    out.println("<h2 style='margin-bottom:0px; margin-top:0px'>Informasi Gaji Karyawan</h2>");
                    out.println("<h1 style='margin-bottom:0px; margin-top:0px;'>PT Sintory</h1>");
                    out.println("<h4 style='margin-bottom:0px; margin-top:0px'>Jl. Surya Kencana No. 99 Pamulang, Tangerang Selatan, Banten</h4>");
                    out.println("<br></td>");
                    out.println("</tr>");

                    // BODY
                    out.println("<tr height='400'>");
                    // LEFT MENU
                    out.println("<td width='200' align='center' valign='top' bgcolor='#eeffee'>");
                    out.println("<div id='menu'>" + menu + "</div>");
                    out.println("</td>");

                    // CONTENT
                    out.println("<td align='center' valign='top' bgcolor='#ffffff'>");
                    out.println(topMenu);
                    out.println("<br>");
                    out.println(konten); // Menampilkan parameter konten dinamis
                    out.println("</td>");
                    out.println("</tr>");

                    // FOOTER
                    out.println("<tr>");
                    out.println("<td colspan='2' align='center' bgcolor='#eeeeff'>");
                    out.println("<small>");
                    out.println("Copyright &copy; 2017 PT Sintory<br>");
                    out.println("Jl. Surya Kencana No. 99 Pamulang, Tangerang Selatan, Banten<br>");
                    out.println("</small>");
                    out.println("</td>");
                    out.println("</tr>");

                    out.println("</table>");
                    out.println("</center>");
                    out.println("</body>");
                    out.println("</html>");
                }
            } else {
                // Jika user tidak valid/belum login
                response.sendRedirect("LoginController");
            }
        } else {
            // Jika session baru (belum login)
            response.sendRedirect("LoginController");
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
        tampilkan("",request, response);
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
    tampilkan("", request, response);
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
