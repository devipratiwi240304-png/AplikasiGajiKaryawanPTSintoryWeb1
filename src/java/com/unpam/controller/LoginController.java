package com.unpam.controller;

import com.unpam.model.Enkripsi;
import com.unpam.model.Karyawan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);

        // ================= CEK SESSION =================
        if (session.getAttribute("userName") != null) {
            response.sendRedirect("index.jsp");
            return;
        }

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        String pesan = "";

        if (userId != null) {
            if (userId.equals("")) {
                pesan = "<br><br><font color='red'>User ID harus diisi</font>";
            } else if (password == null || password.equals("")) {
                pesan = "<br><br><font color='red'>Password harus diisi</font>";
            } else {
                Karyawan karyawan = new Karyawan();

                if (karyawan.baca(userId)) {
                    // Bandingkan password langsung (tanpa MD5 karena di database plain text)
                    // Gunakan trim() untuk menghilangkan spasi di awal/akhir
                    String dbPassword = karyawan.getPassword();
                    if (dbPassword != null) {
                        dbPassword = dbPassword.trim();
                    }
                    
                    if (password.trim().equals(dbPassword)) {

                        // ===== LOGIN BERHASIL =====
                        session.setAttribute(
                                "userName",
                                karyawan.getNama().equals("") ? "No Name" : karyawan.getNama()
                        );

                        session.setMaxInactiveInterval(15 * 60);

                        response.sendRedirect("index.jsp");
                        return;

                    } else {
                        // DEBUG: tampilkan password yang di-compare
                        pesan = "<br><br><font color='red'>User ID atau password salah</font>";
                        pesan += "<br><small>Debug: Input=[" + password + "], DB=[" + dbPassword + "]</small>";
                    }
                } else {
                    pesan = "<br><br><font color='red'>User ID atau password salah</font>";
                    pesan += "<br><small>Debug: User tidak ditemukan di database. Pesan: " + karyawan.getPesan() + "</small>";
                }
            }
        }

        // ===== TAMPILKAN FORM LOGIN LANGSUNG (TANPA MainForm) =====
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link href='style.css' rel='stylesheet' type='text/css' />");
            out.println("<title>Login - Informasi Gaji Karyawan</title>");
            out.println("</head>");
            out.println("<body bgcolor='#808080'>");

            out.println("<center>");
            out.println("<table width='80%' bgcolor='#eeeeee'>");

            // HEADER
            out.println("<tr>");
            out.println("<td colspan='2' align='center'>");
            out.println("<br>");
            out.println("<h2 style='margin-bottom:0px; margin-top:0px'>Informasi Gaji Karyawan</h2>");
            out.println("<h1 style='margin-bottom:0px; margin-top:0px;'>PT Sintory</h1>");
            out.println("<h4 style='margin-bottom:0px; margin-top:0px'>Jl. Surya Kencana No. 99 Pamulang, Tangerang Selatan, Banten</h4>");
            out.println("<br></td>");
            out.println("</tr>");

            // BODY - FORM LOGIN
            out.println("<tr height='400'>");
            out.println("<td width='200' align='center' valign='top' bgcolor='#eeffee'>");
            out.println("<br><b>Menu</b><br><br>");
            out.println("<a href='.'>Home</a><br><br>");
            out.println("</td>");

            out.println("<td align='center' valign='top' bgcolor='#ffffff'>");
            out.println("<br><h2>Login</h2>");
            out.println("<form action='LoginController' method='post'>");
            out.println("<table>");
            out.println("<tr><td>User ID</td><td><input type='text' name='userId'></td></tr>");
            out.println("<tr><td>Password</td><td><input type='password' name='password'></td></tr>");
            out.println("<tr><td colspan='2' align='center'><input type='submit' value='Login'></td></tr>");
            out.println("</table></form>");
            out.println(pesan);
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
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
