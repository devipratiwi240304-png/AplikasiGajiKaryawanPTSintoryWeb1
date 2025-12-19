/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.unpam.controller;

import com.unpam.model.Koneksi;
import com.unpam.view.MainForm;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Devi Pratiwi
 */
@WebServlet(name = "LaporanGajiController", urlPatterns = {"/LaporanGajiController"})
public class LaporanGajiController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        String userName = "";
        try {
            Object userObj = session.getAttribute("userName");
            if (userObj != null) userName = userObj.toString();
        } catch (Exception ex) {}

        if (userName != null && !userName.equals("")) {
            String tombol = request.getParameter("tombol");
            String cetak = request.getParameter("cetak");
            String ktp = request.getParameter("ktp");
            String ruang = request.getParameter("ruang");
            
            if (tombol == null) tombol = "";
            if (cetak == null) cetak = "";
            if (ktp == null) ktp = "";
            if (ruang == null) ruang = "";
            
            String keterangan = "";
            
            // --- LOGIKA MENCETAK LAPORAN ---
            if (tombol.equals("Cetak")) {
                try {
                    // 1. Koneksi Database
                    Koneksi koneksi = new Koneksi();
                    Connection connection = koneksi.getConnection();
                    
                    if (connection != null) {
                        // 2. Build SQL query - ambil semua kolom dari tbgaji
                        // Menggunakan SELECT * untuk menghindari error kolom tidak ditemukan
                        String sql = "SELECT * FROM tbgaji WHERE 1=1";
                        
                        if (!ktp.equals("")) {
                            sql += " AND ktp LIKE ?";
                        }
                        
                        PreparedStatement ps = connection.prepareStatement(sql);
                        
                        int paramIndex = 1;
                        if (!ktp.equals("")) {
                            ps.setString(paramIndex++, "%" + ktp + "%");
                        }
                        
                        ResultSet rs = ps.executeQuery();
                        
                        // 3. Ambil metadata untuk tahu kolom apa saja yang ada
                        java.sql.ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();
                        
                        // 4. Generate HTML Report
                        response.setContentType("text/html;charset=UTF-8");
                        PrintWriter out = response.getWriter();
                        
                        out.println("<!DOCTYPE html>");
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<title>Laporan Gaji Karyawan - PT Sintory</title>");
                        out.println("<style>");
                        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
                        out.println("h1, h2, h3 { text-align: center; margin: 5px; }");
                        out.println("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
                        out.println("th, td { border: 1px solid black; padding: 8px; text-align: left; }");
                        out.println("th { background-color: #f2f2f2; }");
                        out.println(".header { text-align: center; margin-bottom: 30px; }");
                        out.println(".filter-info { margin: 10px 0; }");
                        out.println("@media print { .no-print { display: none; } }");
                        out.println("</style>");
                        out.println("</head>");
                        out.println("<body>");
                        
                        // Header
                        out.println("<div class='header'>");
                        out.println("<h2>LAPORAN GAJI KARYAWAN</h2>");
                        out.println("<h1>PT SINTORY</h1>");
                        out.println("<h3>Jl. Surya Kencana No. 99 Pamulang, Tangerang Selatan, Banten</h3>");
                        out.println("</div>");
                        
                        // Filter info
                        out.println("<div class='filter-info'>");
                        if (!ktp.equals("")) {
                            out.println("<p><b>Filter KTP:</b> " + ktp + "</p>");
                        }
                        if (!ruang.equals("")) {
                            out.println("<p><b>Filter Ruang:</b> " + ruang + "</p>");
                        }
                        out.println("</div>");
                        
                        // Table Header
                        out.println("<table>");
                        out.println("<tr>");
                        out.println("<th>No</th>");
                        for (int i = 1; i <= columnCount; i++) {
                            out.println("<th>" + metaData.getColumnName(i) + "</th>");
                        }
                        out.println("</tr>");
                        
                        // Table Data
                        int no = 1;
                        while (rs.next()) {
                            out.println("<tr>");
                            out.println("<td>" + no++ + "</td>");
                            for (int i = 1; i <= columnCount; i++) {
                                Object value = rs.getObject(i);
                                out.println("<td>" + (value != null ? value.toString() : "") + "</td>");
                            }
                            out.println("</tr>");
                        }
                        
                        out.println("</table>");
                        
                        if (no == 1) {
                            out.println("<p style='text-align: center; color: red;'><b>Tidak ada data gaji</b></p>");
                        }
                        
                        // Print button
                        out.println("<br><div class='no-print' style='text-align: center;'>");
                        out.println("<button onclick='window.print()'>Cetak Laporan</button> ");
                        out.println("<button onclick='history.back()'>Kembali</button>");
                        out.println("</div>");
                        
                        // Footer
                        out.println("<br><p style='text-align: center;'><small>Dicetak pada: " + new java.util.Date() + "</small></p>");
                        
                        out.println("</body>");
                        out.println("</html>");
                        
                        rs.close();
                        ps.close();
                        connection.close();
                        return;
                        
                    } else {
                        keterangan = "Gagal koneksi ke database";
                    }
                    
                } catch (Exception ex) {
                    keterangan = "Gagal mencetak: " + ex.getMessage();
                    ex.printStackTrace();
                }
            }

            // --- TAMPILAN FORM ---
            response.setContentType("text/html;charset=UTF-8");
            
            String konten = "<h2>Mencetak Gaji</h2>";
            konten += "<form action='LaporanGajiController' method='post'>";
            konten += "<table>";
            
            // Input KTP
            konten += "<tr><td>KTP</td><td>";
            if (ktp.equals("")) {
                konten += "<input type='radio' name='pilihktp' checked onclick=\"document.getElementsByName('ktp')[0].value=''\">Semua ";
                konten += "<input type='radio' name='pilihktp' onclick=\"document.getElementsByName('ktp')[0].focus()\">";
            } else {
                konten += "<input type='radio' name='pilihktp' onclick=\"document.getElementsByName('ktp')[0].value=''\">Semua ";
                konten += "<input type='radio' name='pilihktp' checked onclick=\"document.getElementsByName('ktp')[0].focus()\">";
            }
            konten += "<input type='text' name='ktp' value='" + ktp + "'>";
            konten += "</td></tr>";
            
            // Input Ruang
            konten += "<tr><td>Ruang</td><td>";
            if (ruang.equals("")) {
                konten += "<input type='radio' name='pilihruang' checked onclick=\"document.getElementsByName('ruang')[0].value=''\">Semua ";
                konten += "<input type='radio' name='pilihruang' onclick=\"document.getElementsByName('ruang')[0].focus()\">";
            } else {
                konten += "<input type='radio' name='pilihruang' onclick=\"document.getElementsByName('ruang')[0].value=''\">Semua ";
                konten += "<input type='radio' name='pilihruang' checked onclick=\"document.getElementsByName('ruang')[0].focus()\">";
            }
            konten += "<input type='text' name='ruang' value='" + ruang + "'>";
            konten += "</td></tr>";
            
            // Pilihan Format Cetak
            konten += "<tr><td>Format Laporan</td><td>";
            konten += "<select name='cetak'>";
            konten += "<option>HTML (Bisa di-print ke PDF)</option>";
            konten += "</select>";
            konten += "</td></tr>";
            
            konten += "<tr><td colspan='2' align='center'>";
            konten += "<input type='submit' name='tombol' value='Cetak'>";
            konten += "</td></tr>";
            
            // Menampilkan Pesan Error (Jika ada)
            if (!keterangan.equals("")) {
                konten += "<tr><td colspan='2'><font color='red'><b>" + keterangan + "</b></font></td></tr>";
            }
            
            konten += "</table></form>";
            
            // Tampilkan ke MainForm
            new MainForm().tampilkan(konten, request, response);
            
        } else {
            response.sendRedirect("LoginController");
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

    @Override
    public String getServletInfo() {
        return "Laporan Gaji Controller";
    }
}
