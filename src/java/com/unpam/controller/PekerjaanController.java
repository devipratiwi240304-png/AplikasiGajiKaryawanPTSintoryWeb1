/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.unpam.controller;

import com.unpam.model.Pekerjaan;
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
@WebServlet(name = "PekerjaanController", urlPatterns = {"/PekerjaanController"})
public class PekerjaanController extends HttpServlet {

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
    
    // Pastikan response.setContentType ada di sini, sesuai instruksi modul.
    response.setContentType("text/html;charset=UTF-8"); 

    HttpSession session = request.getSession(true);
    Pekerjaan pekerjaan = new Pekerjaan();
    String userName = "";
    
    String tombol = request.getParameter("tombol");
    String kodePekerjaan = request.getParameter("kodePekerjaan");
    String namaPekerjaan = request.getParameter("namaPekerjaan");
    String jumlahTugas = request.getParameter("jumlahTugas");
    String mulaiParameter = request.getParameter("mulai");
    String jumlahParameter = request.getParameter("jumlah");
    String kodePekerjaanDipilih = request.getParameter("kodePekerjaanDipilih");

    if (tombol == null) tombol="";
    if (kodePekerjaan == null) kodePekerjaan="";
    if (namaPekerjaan == null) namaPekerjaan="";
    if (jumlahTugas == null) jumlahTugas = "2";
    if (kodePekerjaanDipilih == null) kodePekerjaanDipilih="";

    int mulai = 0, jumlah = 10;

    try{
        mulai = Integer.parseInt(mulaiParameter);
    } catch (NumberFormatException ex) {}

    try{
        jumlah = Integer.parseInt(jumlahParameter);
    } catch (NumberFormatException ex) {}

    String keterangan="<br>";

    try {
        Object userObj = session.getAttribute("userName");
        if (userObj != null) userName = userObj.toString();
    } catch (Exception ex) {}

    // --- LOGIKA UTAMA ---
    if (userName != null && !userName.equals("")) {
        
        // LOGIKA TOMBOL SIMPAN
        if (tombol.equals("Simpan")){
            if (!kodePekerjaan.equals("")) {
                pekerjaan.setKodePekerjaan (kodePekerjaan);
                pekerjaan.setNamaPekerjaan (namaPekerjaan);
                pekerjaan.setJumlahTugas (Integer.parseInt(jumlahTugas));
                
                if (pekerjaan.simpan()){
                    kodePekerjaan="";
                    namaPekerjaan="";
                    jumlahTugas="2";
                    keterangan = "Sudah tersimpan";
                } else {
                    keterangan = "Gagal menyimpan:\n"+pekerjaan.getPesan();
                }
            } else {
                keterangan = "Gagal menyimpan, kode pekerjaan tidak boleh kosong";
            }
        
        // LOGIKA TOMBOL HAPUS
        } else if (tombol.equals("Hapus")) {
            if (!kodePekerjaan.equals("")) {
                if (pekerjaan.hapus (kodePekerjaan)) {
                    kodePekerjaan="";
                    namaPekerjaan="";
                    jumlahTugas="2";
                    keterangan = "Data sudah dihapus";
                } else {
                    keterangan = "Kode pekerjaan tersebut tidak ada, atau ada kesalahan:\n"+pekerjaan.getPesan();
                }
            } else {
                keterangan="Kode pekerjaan masih kosong";
            }
        
        } else if (tombol.equals("Cari")) {
            if (!kodePekerjaan.equals("")) {
                // Pastikan method 'baca' sudah ada di Pekerjaan.java
                if (pekerjaan.baca (kodePekerjaan)) { 
                    kodePekerjaan = pekerjaan.getKodePekerjaan();
                    namaPekerjaan = pekerjaan.getNamaPekerjaan();
                    jumlahTugas = Integer.toString(pekerjaan.getJumlahTugas());
                    keterangan="<br>";
                } else {
                    keterangan = "Kode pekerjaan tersebut tidak ada";
                }
            } else {
                keterangan = "Kode pekerjaan masih kosong";
            }
        
        } else if (tombol.equals("Pilih")){
            kodePekerjaan = kodePekerjaanDipilih;
            namaPekerjaan="";
            jumlahTugas="2";
            if (!kodePekerjaanDipilih.equals("")) {
                if (pekerjaan.baca (kodePekerjaanDipilih)) {
                    kodePekerjaan = pekerjaan.getKodePekerjaan();
                    namaPekerjaan = pekerjaan.getNamaPekerjaan();
                    jumlahTugas = Integer.toString (pekerjaan.getJumlahTugas());
                    keterangan="<br>";
                } else {
                    keterangan = "Kode pekerjaan tersebut tidak ada";
                }
            } else {
                keterangan = "Tidak ada yang dipilih";
            }
        }

        // --- TAMPILAN TABEL / LIHAT DATA ---
        String kontenLihat = "";
        if (tombol.equals("Lihat") || tombol.equals("Sebelumnya") || tombol.equals("Berikutnya") || tombol.equals("Tampilkan")){
            kontenLihat += "<br>";
            kontenLihat += "<td colspan='2' align='center'>";
            kontenLihat += "<table>";
            
            if (tombol.equals("Sebelumnya")) {
                mulai -= jumlah;
                if (mulai < 0) mulai = 0;
            }
            if (tombol.equals("Berikutnya")){
                mulai += jumlah;
            }

            Object[][] listPekerjaan = null;
            if (pekerjaan.bacaData (mulai,jumlah)) {
                listPekerjaan = (Object[][]) pekerjaan.getList();
                keterangan = pekerjaan.getPesan();
            }

            if (listPekerjaan != null) {
                for (int i=0; i<listPekerjaan.length; i++){
                    kontenLihat += "<tr>";
                    kontenLihat += "<td>";
                    if (i==0){
                        kontenLihat += "<input type='radio' checked name='kodePekerjaanDipilih' value='" +listPekerjaan[i][0].toString()+"'>";
                    } else {
                        kontenLihat += "<input type='radio' name='kodePekerjaanDipilih' value='"+listPekerjaan[i][0].toString()+"'>";
                    }
                    kontenLihat += "</td>";
                    kontenLihat += "<td>" + listPekerjaan [i][0].toString() + "</td>"; // Kode
                    kontenLihat += "<td>" + listPekerjaan [i][1].toString() + "</td>"; // Nama
                    kontenLihat += "</tr>";
                }
            }
            kontenLihat += "</table>";
            kontenLihat += "</td>";
            kontenLihat += "</tr>";

            // Tombol Navigasi Tabel
            kontenLihat += "<tr>";
            kontenLihat += "<td colspan='2' align='center'>";
            kontenLihat += "<table>";
            kontenLihat += "<tr>";
            kontenLihat += "<td align='center'><input type='submit' name='tombol' value='Sebelumnya' style='width: 100px'></td>";
            kontenLihat += "<td align='center'><input type='submit' name='tombol' value='Pilih' style='width: 60px'></td>";
            kontenLihat += "<td align='center'><input type='submit' name='tombol' value='Berikutnya' style='width: 100px'></td>";
            kontenLihat += "</tr>";
            kontenLihat += "<tr>";
            kontenLihat += "<td align='center'>Mulai <input type='text' name='mulai' value='"+mulai+"' style='width: 40px'></td>";
            kontenLihat += "<td>Jumlah:";
            kontenLihat += "<select name='jumlah'>";
            for (int i=1; i<=10; i++) {
                if (jumlah == i*10){
                    kontenLihat += "<option selected value="+i*10+">"+i*10+"</option>";
                } else {
                    kontenLihat += "<option value="+i*10+">"+i*10+"</option>";
                }
            }
            kontenLihat += "</select>";
            kontenLihat += "</td>";
            kontenLihat += "<td align='center'><input type='submit' name='tombol' value='Tampilkan' style='width: 90px'></td>";
            kontenLihat += "</tr>";
            kontenLihat += "</table>";
            kontenLihat += "</td>";
            kontenLihat += "</tr>";
        }
        String konten = "<h2>Master Data Pekerjaan</h2>";
        konten += "<form action='PekerjaanController' method='post'>";
        konten += "<table>";
        konten += "<tr>";
        konten += "<td align='right'>Kode Pekerjaan</td>";
        konten += "<td align='left'><input type='text' value='"+kodePekerjaan+"' name='kodePekerjaan' maxlength='15' size='15'><input type='submit' name='tombol' value='Cari'></td>";
        konten += "</tr>";
        konten += "<tr>";
        konten += "<td align='right'>Nama Pekerjaan</td>";
        konten += "<td align='left'><input type='text' value='"+namaPekerjaan+"' name='namaPekerjaan' maxlength='30' size='30'></td>";
        konten += "</tr>";
        konten += "<tr>";
        konten += "<td align='right'>Jumlah Tugas</td>";
        konten += "<td align='left'>";
        konten += "<select name='jumlahTugas'>";
        for (int i=2; i<=6; i++) {
            if (i == Integer.parseInt(jumlahTugas)) {
                konten += "<option selected value="+i+">"+i+"</option>";
            } else {
                konten += "<option value="+i+">"+i+"</option>";
            }
        }
        konten += "</select>";
        konten += "</td>";
        konten += "</tr>";
        konten += "<tr>";
        konten += "<td colspan='2'><b>"+(keterangan != null ? keterangan.replaceAll("\n", "<br>").replaceAll(":", ",") : "")+"</b></td>";
        konten += "</tr>";
        konten += "<tr>";
        konten += "<td colspan='2' align='center'>";
        konten += "<table>";
        konten += "<tr>";
        konten += "<td align='center'><input type='submit' name='tombol' value='Simpan' style='width: 100px'></td>";
        konten += "<td align='center'><input type='submit' name='tombol' value='Hapus' style='width: 100px'></td>";
        konten += "<td align='center'><input type='submit' name='tombol' value='Lihat' style='width: 100px'></td>";
        konten += "</tr>";
        konten += "</table>";
        konten += "</td>";
        konten += "</tr>";
        konten += kontenLihat;
        konten += "</table>";
        konten += "</form>";
        new MainForm().tampilkan (konten, request, response);
    } else {
        response.sendRedirect(".");
    }
} // <-- Ini adalah kurung kurawal penutup untuk processRequest

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
        processRequest(request, response);
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
        processRequest(request, response);
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
