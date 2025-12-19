/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.unpam.controller;

import com.unpam.model.Gaji;
import com.unpam.model.Karyawan;
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
@WebServlet(name = "GajiController", urlPatterns = {"/GajiController"})
public class GajiController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(true);

        Gaji gaji = new Gaji();
        Karyawan karyawan = new Karyawan();
        Pekerjaan pekerjaan = new Pekerjaan();

        String userName = "";

        // Ambil semua parameter
        String tombol = request.getParameter("tombol");
        String ktp = request.getParameter("ktp");
        String nama = request.getParameter("nama");
        String ruang = request.getParameter("ruang");
        String kodePekerjaan = request.getParameter("kodePekerjaan");
        String namaPekerjaan = request.getParameter("namaPekerjaan");
        String jumlahTugasParam = request.getParameter("jumlahTugas");
        String gajiBersihParam = request.getParameter("gajiBersih");
        String gajiKotorParam = request.getParameter("gajiKotor");
        String tunjanganParam = request.getParameter("tunjangan");
        String potonganParam = request.getParameter("potongan");
        String keteranganBulan = request.getParameter("keteranganBulan");

        // Set default values
        if (tombol == null) tombol = "";
        if (ktp == null) ktp = "";
        if (nama == null) nama = "";
        if (ruang == null) ruang = "";
        if (kodePekerjaan == null) kodePekerjaan = "";
        if (namaPekerjaan == null) namaPekerjaan = "";
        if (jumlahTugasParam == null) jumlahTugasParam = "0";
        if (gajiBersihParam == null) gajiBersihParam = "";
        if (gajiKotorParam == null) gajiKotorParam = "";
        if (tunjanganParam == null) tunjanganParam = "";
        if (potonganParam == null) potonganParam = "";
        if (keteranganBulan == null) keteranganBulan = "";

        int jumlahTugas = 0;
        double gajiBersih = 0;
        double gajiKotor = 0;
        double tunjangan = 0;
        double potongan = 0;

        try { jumlahTugas = Integer.parseInt(jumlahTugasParam); } catch (Exception e) {}
        try { gajiBersih = Double.parseDouble(gajiBersihParam); } catch (Exception e) {}
        try { gajiKotor = Double.parseDouble(gajiKotorParam); } catch (Exception e) {}
        try { tunjangan = Double.parseDouble(tunjanganParam); } catch (Exception e) {}
        try { potongan = Double.parseDouble(potonganParam); } catch (Exception e) {}

        String keterangan = "<br>";

        try {
            Object userObj = session.getAttribute("userName");
            if (userObj != null) userName = userObj.toString();
        } catch (Exception ex) {}

        // --- LOGIKA UTAMA ---
        if (userName != null && !userName.equals("")) {

            // === TOMBOL CARI KARYAWAN ===
            if (tombol.equals("CariKaryawan")) {
                if (!ktp.equals("")) {
                    if (karyawan.baca(ktp)) {
                        nama = karyawan.getNama();
                        ruang = String.valueOf(karyawan.getRuang());
                    } else {
                        keterangan = "<font color='red'>KTP tidak ditemukan</font>";
                    }
                } else {
                    keterangan = "<font color='red'>KTP harus diisi</font>";
                }
            }

            // === TOMBOL CARI PEKERJAAN ===
            else if (tombol.equals("CariPekerjaan")) {
                if (!kodePekerjaan.equals("")) {
                    if (pekerjaan.baca(kodePekerjaan)) {
                        namaPekerjaan = pekerjaan.getNamaPekerjaan();
                        jumlahTugas = pekerjaan.getJumlahTugas();
                        jumlahTugasParam = String.valueOf(jumlahTugas);
                    } else {
                        keterangan = "<font color='red'>Kode Pekerjaan tidak ditemukan</font>";
                    }
                } else {
                    keterangan = "<font color='red'>Kode Pekerjaan harus diisi</font>";
                }
            }

            // === TOMBOL SIMPAN ===
            else if (tombol.equals("Simpan")) {
                if (!ktp.equals("") && !kodePekerjaan.equals("")) {
                    // Hitung Total Gaji = Gaji Bersih + Gaji Kotor + Tunjangan - Potongan
                    double totalGaji = gajiBersih + gajiKotor + tunjangan - potongan;
                    
                    gaji.setKtp(ktp);
                    gaji.setKodePekerjaan(kodePekerjaan);
                    gaji.setJumlahTugas(jumlahTugas);
                    gaji.setGajiBersih(gajiBersih);
                    gaji.setGajiKotor(gajiKotor);
                    gaji.setTunjangan(tunjangan);
                    gaji.setPotongan(potongan);
                    gaji.setTotalGaji(totalGaji);
                    gaji.setKeterangan("Gaji " + keteranganBulan);

                    if (gaji.simpan()) {
                        keterangan = "<font color='green'>Data gaji berhasil disimpan</font>";
                        // Reset form
                        ktp = "";
                        nama = "";
                        ruang = "";
                        kodePekerjaan = "";
                        namaPekerjaan = "";
                        jumlahTugasParam = "0";
                        gajiBersihParam = "";
                        gajiKotorParam = "";
                        tunjanganParam = "";
                        potonganParam = "";
                        // Reset variabel double agar Total Gaji juga kosong
                        gajiBersih = 0;
                        gajiKotor = 0;
                        tunjangan = 0;
                        potongan = 0;
                    } else {
                        keterangan = "<font color='red'>Gagal menyimpan: " + gaji.getPesan() + "</font>";
                    }
                } else {
                    keterangan = "<font color='red'>KTP dan Kode Pekerjaan harus diisi</font>";
                }
            }

            // === TOMBOL HAPUS ===
            else if (tombol.equals("Hapus")) {
                if (!ktp.equals("") && !kodePekerjaan.equals("")) {
                    if (gaji.hapus(ktp, kodePekerjaan)) {
                        keterangan = "<font color='green'>Data gaji berhasil dihapus</font>";
                        ktp = "";
                        nama = "";
                        ruang = "";
                        kodePekerjaan = "";
                        namaPekerjaan = "";
                        jumlahTugasParam = "0";
                        gajiBersihParam = "";
                        gajiKotorParam = "";
                        tunjanganParam = "";
                    } else {
                        keterangan = "<font color='red'>Gagal menghapus: " + gaji.getPesan() + "</font>";
                    }
                } else {
                    keterangan = "<font color='red'>KTP dan Kode Pekerjaan harus diisi</font>";
                }
            }

            // === MEMBANGUN FORM HTML ===
            String konten = "<h2>Input Gaji Karyawan</h2>";
            konten += "<form action='GajiController' method='post'>";
            konten += "<table>";

            // KTP dengan tombol Cari dan Lihat
            konten += "<tr>";
            konten += "<td align='right'>KTP</td>";
            konten += "<td><input type='text' name='ktp' value='" + ktp + "' size='20'>";
            konten += " <input type='submit' name='tombol' value='CariKaryawan'>";
            konten += " <input type='submit' name='tombol' value='LihatKaryawan'></td>";
            konten += "</tr>";

            // Nama
            konten += "<tr>";
            konten += "<td align='right'>Nama</td>";
            konten += "<td><input type='text' name='nama' value='" + nama + "' size='30' readonly></td>";
            konten += "</tr>";

            // Ruang
            konten += "<tr>";
            konten += "<td align='right'>Ruang</td>";
            konten += "<td><input type='text' name='ruang' value='" + ruang + "' size='5' readonly></td>";
            konten += "</tr>";

            // Kode Pekerjaan dengan tombol Cari dan Lihat
            konten += "<tr>";
            konten += "<td align='right'>Kode Pekerjaan</td>";
            konten += "<td><input type='text' name='kodePekerjaan' value='" + kodePekerjaan + "' size='15'>";
            konten += " <input type='submit' name='tombol' value='CariPekerjaan'>";
            konten += " <input type='submit' name='tombol' value='LihatPekerjaan'></td>";
            konten += "</tr>";

            // Nama Pekerjaan
            konten += "<tr>";
            konten += "<td align='right'>Nama Pekerjaan</td>";
            konten += "<td><input type='text' name='namaPekerjaan' value='" + namaPekerjaan + "' size='30' readonly></td>";
            konten += "</tr>";

            // Jumlah Tugas
            konten += "<tr>";
            konten += "<td align='right'>Jumlah Tugas</td>";
            konten += "<td><input type='text' name='jumlahTugas' value='" + jumlahTugasParam + "' size='5' readonly></td>";
            konten += "</tr>";

            // Gaji Bersih
            konten += "<tr>";
            konten += "<td align='right'>Gaji Bersih</td>";
            konten += "<td><input type='text' name='gajiBersih' id='gajiBersih' value='" + gajiBersihParam + "' size='15' onkeyup='hitungTotalGaji()'></td>";
            konten += "</tr>";

            // Gaji Kotor
            konten += "<tr>";
            konten += "<td align='right'>Gaji Kotor</td>";
            konten += "<td><input type='text' name='gajiKotor' id='gajiKotor' value='" + gajiKotorParam + "' size='15' onkeyup='hitungTotalGaji()'></td>";
            konten += "</tr>";

            // Tunjangan
            konten += "<tr>";
            konten += "<td align='right'>Tunjangan</td>";
            konten += "<td><input type='text' name='tunjangan' id='tunjangan' value='" + tunjanganParam + "' size='15' onkeyup='hitungTotalGaji()'></td>";
            konten += "</tr>";

            // Potongan
            konten += "<tr>";
            konten += "<td align='right'>Potongan</td>";
            konten += "<td><input type='text' name='potongan' id='potongan' value='" + potonganParam + "' size='15' onkeyup='hitungTotalGaji()'></td>";
            konten += "</tr>";

            // Total Gaji (otomatis dihitung dari Gaji Bersih + Gaji Kotor + Tunjangan - Potongan)
            double totalGajiCalc = gajiBersih + gajiKotor + tunjangan - potongan;
            String totalGajiParam = (totalGajiCalc > 0) ? String.valueOf(totalGajiCalc) : "";
            konten += "<tr>";
            konten += "<td align='right'><b>Total Gaji</b></td>";
            konten += "<td><input type='text' name='totalGaji' id='totalGaji' value='" + totalGajiParam + "' size='15' readonly style='background-color:#e6ffe6; font-weight:bold;'></td>";
            konten += "</tr>";

            // Keterangan Bulan
            String[] namaBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", 
                                  "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
            int bulanSekarang = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1;
            konten += "<tr>";
            konten += "<td align='right'>Keterangan Bulan</td>";
            konten += "<td><select name='keteranganBulan'>";
            for (int i = 1; i <= 12; i++) {
                String selected = (i == bulanSekarang) ? " selected" : "";
                konten += "<option value='" + namaBulan[i-1] + "'" + selected + ">" + namaBulan[i-1] + "</option>";
            }
            konten += "</select></td>";
            konten += "</tr>";

            // Pesan Keterangan
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
            
            // JavaScript untuk menghitung Total Gaji otomatis
            konten += "<script>";
            konten += "function hitungTotalGaji() {";
            konten += "  var gajiBersih = parseFloat(document.getElementById('gajiBersih').value) || 0;";
            konten += "  var gajiKotor = parseFloat(document.getElementById('gajiKotor').value) || 0;";
            konten += "  var tunjangan = parseFloat(document.getElementById('tunjangan').value) || 0;";
            konten += "  var potongan = parseFloat(document.getElementById('potongan').value) || 0;";
            konten += "  var totalGaji = gajiBersih + gajiKotor + tunjangan - potongan;";
            konten += "  document.getElementById('totalGaji').value = totalGaji;";
            konten += "}";
            konten += "</script>";
            
            konten += "</form>";

            // Tampilkan menggunakan MainForm
            new MainForm().tampilkan(konten, request, response);

        } else {
            // Jika belum login
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
        return "Gaji Controller - Input Gaji Karyawan";
    }
}
