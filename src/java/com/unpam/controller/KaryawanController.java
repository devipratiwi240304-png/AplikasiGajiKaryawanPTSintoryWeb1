/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.unpam.controller;

import com.unpam.model.Karyawan;
import com.unpam.view.MainForm;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 * @author Devi Pratiwi
 */
@WebServlet(name = "KaryawanController", urlPatterns = {"/KaryawanController"})
public class KaryawanController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);
        
        Karyawan karyawan = new Karyawan();
        String userName = "";
        
        // Ambil parameter
        String tombol = request.getParameter("tombol");
        String ktp = request.getParameter("ktp");
        String nama = request.getParameter("nama");
        String alamat = request.getParameter("alamat");
        String password = request.getParameter("password");
        String ruangParam = request.getParameter("ruang");
        
        // Set default values
        if (tombol == null) tombol = "";
        if (ktp == null) ktp = "";
        if (nama == null) nama = "";
        if (alamat == null) alamat = "";
        if (password == null) password = "";
        if (ruangParam == null) ruangParam = "0";
        
        int ruang = 0;
        try { ruang = Integer.parseInt(ruangParam); } catch (Exception e) {}
        
        String keterangan = "<br>";
        
        try {
            Object userObj = session.getAttribute("userName");
            if (userObj != null) userName = userObj.toString();
        } catch (Exception ex) {}
        
        if (userName != null && !userName.equals("")) {
            
            // === TOMBOL CARI ===
            if (tombol.equals("Cari")) {
                if (!ktp.equals("")) {
                    if (karyawan.baca(ktp)) {
                        nama = karyawan.getNama();
                        alamat = karyawan.getAlamat();
                        password = karyawan.getPassword();
                        ruang = karyawan.getRuang();
                        ruangParam = String.valueOf(ruang);
                    } else {
                        keterangan = "<font color='red'>Data tidak ditemukan</font>";
                    }
                } else {
                    keterangan = "<font color='red'>KTP harus diisi</font>";
                }
            }
            
            // === TOMBOL SIMPAN ===
            else if (tombol.equals("Simpan")) {
                if (!ktp.equals("") && !nama.equals("")) {
                    karyawan.setKtp(ktp);
                    karyawan.setNama(nama);
                    karyawan.setAlamat(alamat);
                    karyawan.setPassword(password);
                    karyawan.setRuang(ruang);
                    
                    if (karyawan.simpan()) {
                        keterangan = "<font color='green'>Data berhasil disimpan</font>";
                        // Reset form
                        ktp = "";
                        nama = "";
                        alamat = "";
                        password = "";
                        ruangParam = "0";
                    } else {
                        keterangan = "<font color='red'>Gagal menyimpan: " + karyawan.getPesan() + "</font>";
                    }
                } else {
                    keterangan = "<font color='red'>KTP dan Nama harus diisi</font>";
                }
            }
            
            // === TOMBOL HAPUS ===
            else if (tombol.equals("Hapus")) {
                if (!ktp.equals("")) {
                    if (karyawan.hapus(ktp)) {
                        keterangan = "<font color='green'>Data berhasil dihapus</font>";
                        // Reset form
                        ktp = "";
                        nama = "";
                        alamat = "";
                        password = "";
                        ruangParam = "0";
                    } else {
                        keterangan = "<font color='red'>Gagal menghapus: " + karyawan.getPesan() + "</font>";
                    }
                } else {
                    keterangan = "<font color='red'>KTP harus diisi</font>";
                }
            }
            
            // === MEMBANGUN FORM HTML ===
            String konten = "<h2>Data Karyawan</h2>";
            konten += "<form action='KaryawanController' method='post'>";
            konten += "<table>";
            
            // KTP
            konten += "<tr>";
            konten += "<td align='right'>KTP</td>";
            konten += "<td><input type='text' name='ktp' value='" + ktp + "' size='20'>";
            konten += " <input type='submit' name='tombol' value='Cari'></td>";
            konten += "</tr>";
            
            // Nama
            konten += "<tr>";
            konten += "<td align='right'>Nama</td>";
            konten += "<td><input type='text' name='nama' value='" + nama + "' size='30'></td>";
            konten += "</tr>";
            
            // Alamat
            konten += "<tr>";
            konten += "<td align='right'>Alamat</td>";
            konten += "<td><input type='text' name='alamat' value='" + alamat + "' size='40'></td>";
            konten += "</tr>";
            
            // Password
            konten += "<tr>";
            konten += "<td align='right'>Password</td>";
            konten += "<td><input type='password' name='password' value='" + password + "' size='20'></td>";
            konten += "</tr>";
            
            // Ruang
            konten += "<tr>";
            konten += "<td align='right'>Ruang</td>";
            konten += "<td><input type='text' name='ruang' value='" + ruangParam + "' size='5'></td>";
            konten += "</tr>";
            
            // Keterangan
            konten += "<tr>";
            konten += "<td colspan='2' align='center'><b>" + keterangan + "</b></td>";
            konten += "</tr>";
            
            // Tombol Simpan dan Hapus
            konten += "<tr>";
            konten += "<td colspan='2' align='center'>";
            konten += "<input type='submit' name='tombol' value='Simpan'> ";
            konten += "<input type='submit' name='tombol' value='Hapus'>";
            konten += "</td>";
            konten += "</tr>";
            
            konten += "</table>";
            konten += "</form>";
            
            // Tampilkan daftar karyawan
            if (karyawan.bacaData(0, 10)) {
                Object[][] list = karyawan.getList();
                if (list != null && list.length > 0) {
                    konten += "<br><h3>Daftar Karyawan</h3>";
                    konten += "<table border='1' cellpadding='5'>";
                    konten += "<tr><th>No</th><th>KTP</th><th>Nama</th></tr>";
                    for (int i = 0; i < list.length; i++) {
                        konten += "<tr>";
                        konten += "<td>" + (i + 1) + "</td>";
                        konten += "<td>" + (list[i].length > 0 ? list[i][0] : "") + "</td>";
                        konten += "<td>" + (list[i].length > 1 ? list[i][1] : "") + "</td>";
                        konten += "</tr>";
                    }
                    konten += "</table>";
                }
            }
            
            // Tampilkan menggunakan MainForm
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
        return "Karyawan Controller";
    }
}
