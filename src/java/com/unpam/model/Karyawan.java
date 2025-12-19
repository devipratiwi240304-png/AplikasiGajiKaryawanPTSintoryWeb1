/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unpam.model; 

import com.unpam.view.PesanDialog; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Devi Pratiwi
 */
public class Karyawan {
    private String ktp;
    private String nama;
    private String alamat;
    private String password;
    private int ruang;
    private String pesan;
    
    private Object[][] list;
    
    private final Koneksi koneksi = new Koneksi();
    private final PesanDialog pesanDialog = new PesanDialog(); 

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getRuang() {
        return ruang;
    }

    public void setRuang(int ruang) {
        this.ruang = ruang;
    }

    public String getPesan() {
        return pesan;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object[][] getList() {
        return list;
    }

    public void setList(Object[][] list) {
        this.list = list;
    }

    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            int jumlahSimpan = 0;
            String SQLStatemen = "";
            PreparedStatement preparedStatement;
            ResultSet rset;

            try {
                // Cek apakah KTP sudah ada di database
                String cekSQL = "SELECT COUNT(*) FROM tbkaryawan WHERE ktp = ?";
                preparedStatement = connection.prepareStatement(cekSQL);
                preparedStatement.setString(1, ktp);
                rset = preparedStatement.executeQuery();
                
                boolean dataAda = false;
                if (rset.next()) {
                    dataAda = rset.getInt(1) > 0;
                }
                rset.close();
                preparedStatement.close();
                
                // Jika data sudah ada, gunakan UPDATE. Jika belum, gunakan INSERT.
                if (dataAda) {
                    SQLStatemen = "UPDATE tbkaryawan SET nama = ?, password = ? WHERE ktp = ?";
                    preparedStatement = connection.prepareStatement(SQLStatemen);
                    preparedStatement.setString(1, nama);
                    preparedStatement.setString(2, password);
                    preparedStatement.setString(3, ktp);
                } else {
                    SQLStatemen = "INSERT INTO tbkaryawan(ktp, nama, password) VALUES (?,?,?)";
                    preparedStatement = connection.prepareStatement(SQLStatemen);
                    preparedStatement.setString(1, ktp);
                    preparedStatement.setString(2, nama);
                    preparedStatement.setString(3, password);
                }

                jumlahSimpan = preparedStatement.executeUpdate();

                if (jumlahSimpan < 1) {
                    adaKesalahan = true;
                    pesan = "Gagal menyimpan data karyawan";
                }

                preparedStatement.close();
                connection.close();

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel tbkaryawan\n" + ex + "\n" + SQLStatemen;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    // --- METHOD BACA DATA (PERBAIKAN 3: DITAMBAHKAN SESUAI HALAMAN 21) ---
    // [cite: 1237-1269]
    public boolean bacaData(int mulai, int jumlah) {
        boolean adaKesalahan = false;
        Connection connection;
        list = new Object[0][0];

        if ((connection = koneksi.getConnection()) != null) {
            String SQLStatemen;
            PreparedStatement preparedStatement;
            ResultSet rset;

            try {
                SQLStatemen = "select ktp, nama from tbkaryawan limit " + mulai + ", " + jumlah;
                preparedStatement = connection.prepareStatement(SQLStatemen);
                rset = preparedStatement.executeQuery();
                
                // Gunakan ArrayList untuk menampung data dulu
                java.util.ArrayList<Object[]> tempList = new java.util.ArrayList<>();
                
                while(rset.next()){
                    Object[] row = new Object[2];
                    row[0] = rset.getString("ktp");
                    row[1] = rset.getString("nama");
                    tempList.add(row);
                }
                
                // Convert ArrayList ke Array
                list = new Object[tempList.size()][2];
                for (int i = 0; i < tempList.size(); i++) {
                    list[i] = tempList.get(i);
                }
                
                rset.close();
                preparedStatement.close();
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

    public boolean hapus(String ktp) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            PreparedStatement preparedStatement;

            try {
                // LANGKAH 1: Hapus data gaji terkait dulu (untuk menghindari foreign key error)
                String hapusGajiSQL = "DELETE FROM tbgaji WHERE ktp = ?";
                preparedStatement = connection.prepareStatement(hapusGajiSQL);
                preparedStatement.setString(1, ktp);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                
                // LANGKAH 2: Baru hapus data karyawan
                String hapusKaryawanSQL = "DELETE FROM tbkaryawan WHERE ktp = ?";
                preparedStatement = connection.prepareStatement(hapusKaryawanSQL);
                preparedStatement.setString(1, ktp);
                int jumlahHapus = preparedStatement.executeUpdate();

                if (jumlahHapus < 1) {
                    adaKesalahan = true;
                    pesan = "Data tidak ditemukan atau gagal dihapus";
                }

                preparedStatement.close();
                connection.close();

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat menghapus data\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    public boolean baca(String userId) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            String SQLStatemen = "";
            PreparedStatement preparedStatement;
            ResultSet rset;

            try {
                // Hanya ambil kolom yang diperlukan untuk login (tanpa ruang)
                SQLStatemen = "SELECT ktp, nama, password FROM tbkaryawan WHERE ktp = ?";
                preparedStatement = connection.prepareStatement(SQLStatemen);
                preparedStatement.setString(1, userId);
                rset = preparedStatement.executeQuery();

                if (rset.next()) {
                    this.ktp = rset.getString("ktp");
                    this.nama = rset.getString("nama");
                    this.ruang = 0; // Default karena kolom tidak ada di database
                    this.password = rset.getString("password");
                } else {
                    adaKesalahan = true;
                    pesan = "Data tidak ditemukan";
                }

                rset.close();
                preparedStatement.close();
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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}