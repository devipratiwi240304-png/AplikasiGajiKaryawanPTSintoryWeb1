/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unpam.model;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.unpam.view.PesanDialog;
/**
 *
 * @author Devi Pratiwi
 */
public class Pekerjaan {
    // 1. Perbaikan Nama Variabel (camelCase) sesuai Modul Hal 14 
    private String kodePekerjaan;
    private String namaPekerjaan;
    private int jumlahTugas;
    
    // Variabel pendukung
    private String pesan;
    private Object[][] list; // Tipe data Object[][] untuk tabel
    
    private final Koneksi koneksi = new Koneksi();
    private final PesanDialog pesanDialog = new PesanDialog();

    // ==================== GETTER & SETTER =====================
    // Sesuai Modul Halaman 15 [cite: 1056-1074]
    
    public String getKodePekerjaan() {
        return kodePekerjaan;
    }

    public void setKodePekerjaan(String kodePekerjaan) {
        this.kodePekerjaan = kodePekerjaan;
    }

    public String getNamaPekerjaan() {
        return namaPekerjaan;
    }

    public void setNamaPekerjaan(String namaPekerjaan) {
        this.namaPekerjaan = namaPekerjaan;
    }

    public int getJumlahTugas() {
        return jumlahTugas;
    }

    public void setJumlahTugas(int jumlahTugas) {
        this.jumlahTugas = jumlahTugas;
    }

    public String getPesan() {
        return pesan;
    }

    public Object[][] getList() {
        return list;
    }

    public void setList(Object[][] list) {
        this.list = list;
    }

    // ======================= SIMPAN ============================
    // Sesuai Modul Halaman 16-17 [cite: 1084-1115]
    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            int jumlahSimpan = 0;
            boolean simpan = false;
            String SQLStatemen = "";
            Statement sta;

            try {
                simpan = true;
                // Query dengan nama kolom eksplisit (termasuk gajipokok)
                SQLStatemen = "INSERT INTO tbpekerjaan (kode_pekerjaan, nama_pekerjaan, gajipokok) VALUES ('" 
                        + kodePekerjaan + "','"+ namaPekerjaan +"', 0)";

                sta = connection.createStatement();
                jumlahSimpan = sta.executeUpdate(SQLStatemen);

                if (simpan) {
                    if (jumlahSimpan < 1) {
                        adaKesalahan = true;
                        pesan = "Gagal menyimpan data pekerjaan";
                    }
                }
                
                sta.close();
                connection.close(); // Menutup koneksi agar tidak memory leak

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel tbpekerjaan\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    // ======================= BACA DATA ==========================
    // Sesuai Modul Halaman 21 [cite: 1273-1301]
    // Method ini WAJIB ada untuk menampilkan data di tabel HTML nanti
    public boolean bacaData(int mulai, int jumlah) {
        boolean adaKesalahan = false;
        Connection connection;
        list = new Object[0][0];

        if ((connection = koneksi.getConnection()) != null) {
            String SQLStatemen;
            Statement sta;
            ResultSet rset;

            try {
                SQLStatemen = "select kode_pekerjaan, nama_pekerjaan from tbpekerjaan " 
                        + " limit " + mulai + ", " + jumlah;
                
                sta = connection.createStatement();
                rset = sta.executeQuery(SQLStatemen);
                
                // Gunakan ArrayList untuk menampung data dulu
                java.util.ArrayList<Object[]> tempList = new java.util.ArrayList<>();
                
                while(rset.next()){
                    Object[] row = new Object[2];
                    row[0] = rset.getString("kode_pekerjaan");
                    row[1] = rset.getString("nama_pekerjaan");
                    tempList.add(row);
                }
                
                // Convert ArrayList ke Array
                list = new Object[tempList.size()][2];
                for (int i = 0; i < tempList.size(); i++) {
                    list[i] = tempList.get(i);
                }

                rset.close();
                sta.close();
                connection.close();

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membaca data\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }
    
    // Method hapus (Opsional/Tambahan, disesuaikan dengan variabel baru)
    public boolean hapus(String kode) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            String SQLStatement = "DELETE FROM tbpekerjaan WHERE kode_pekerjaan = '" + kode + "'";
            Statement sta;

            try {
                sta = connection.createStatement();
                int jumlahHapus = sta.executeUpdate(SQLStatement);

                if (jumlahHapus < 1) {
                    adaKesalahan = true;
                    pesan = "Data tidak ditemukan.";
                }

                sta.close();
                connection.close();

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Gagal menghapus data\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Gagal koneksi server\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean baca(String kodePekerjaan) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            String SQLStatemen = "";
            Statement sta;
            ResultSet rset;

            try {
                SQLStatemen = "SELECT kode_pekerjaan, nama_pekerjaan FROM tbpekerjaan WHERE kode_pekerjaan = '" + kodePekerjaan + "'";
                sta = connection.createStatement();
                rset = sta.executeQuery(SQLStatemen);

                if (rset.next()) {
                    this.kodePekerjaan = rset.getString("kode_pekerjaan");
                    this.namaPekerjaan = rset.getString("nama_pekerjaan");
                    this.jumlahTugas = 0; // Default value karena kolom tidak ada di database
                } else {
                    adaKesalahan = true;
                    pesan = "Data tidak ditemukan";
                }

                rset.close();
                sta.close();
                connection.close();

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membaca data\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }
}
