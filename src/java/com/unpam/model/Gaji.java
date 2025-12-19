/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unpam.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Devi Pratiwi
 */
public class Gaji {
    
    private String ktp;
    private String kodePekerjaan;
    private int jumlahTugas;
    private double gajiBersih;
    private double gajiKotor;
    private double gaji;
    private double tunjangan;
    private double potongan;
    private double totalGaji;
    private String keterangan;
    private String pesan;
    private Object[][] list;
    
    private final Koneksi koneksi = new Koneksi();

    // ==================== GETTER & SETTER ====================
    
    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getKodePekerjaan() {
        return kodePekerjaan;
    }

    public void setKodePekerjaan(String kodePekerjaan) {
        this.kodePekerjaan = kodePekerjaan;
    }

    public int getJumlahTugas() {
        return jumlahTugas;
    }

    public void setJumlahTugas(int jumlahTugas) {
        this.jumlahTugas = jumlahTugas;
    }

    public double getGajiBersih() {
        return gajiBersih;
    }

    public void setGajiBersih(double gajiBersih) {
        this.gajiBersih = gajiBersih;
    }

    public double getGajiKotor() {
        return gajiKotor;
    }

    public void setGajiKotor(double gajiKotor) {
        this.gajiKotor = gajiKotor;
    }

    public double getGaji() {
        return gaji;
    }

    public void setGaji(double gaji) {
        this.gaji = gaji;
    }

    public double getTunjangan() {
        return tunjangan;
    }

    public void setTunjangan(double tunjangan) {
        this.tunjangan = tunjangan;
    }

    public double getPotongan() {
        return potongan;
    }

    public void setPotongan(double potongan) {
        this.potongan = potongan;
    }

    public double getTotalGaji() {
        return totalGaji;
    }

    public void setTotalGaji(double totalGaji) {
        this.totalGaji = totalGaji;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
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

    // ==================== SIMPAN ====================
    public boolean simpan() {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            String SQLStatemen = "";
            PreparedStatement preparedStatement;

            try {
                // Generate unique idgaji using "G" + timestamp
                String idgaji = "G" + System.currentTimeMillis();
                // Get current date for tanggal field
                java.sql.Date tanggal = new java.sql.Date(System.currentTimeMillis());
                
                SQLStatemen = "INSERT INTO tbgaji(idgaji, ktp, kode_pekerjaan, gajipokok, tunjangan, potongan, totalgaji, tanggal, keterangan) VALUES (?,?,?,?,?,?,?,?,?)";

                preparedStatement = connection.prepareStatement(SQLStatemen);
                preparedStatement.setString(1, idgaji);
                preparedStatement.setString(2, ktp);
                preparedStatement.setString(3, kodePekerjaan);
                preparedStatement.setDouble(4, gajiKotor);  // gajipokok
                preparedStatement.setDouble(5, tunjangan);
                preparedStatement.setDouble(6, potongan);   // potongan
                preparedStatement.setDouble(7, totalGaji);  // totalgaji = gajiBersih + gajiKotor + tunjangan - potongan
                preparedStatement.setDate(8, tanggal);      // tanggal hari ini
                preparedStatement.setString(9, keterangan); // keterangan bulan

                int jumlahSimpan = preparedStatement.executeUpdate();

                if (jumlahSimpan < 1) {
                    adaKesalahan = true;
                    pesan = "Gagal menyimpan data gaji";
                }

                preparedStatement.close();
                connection.close();

            } catch (SQLException ex) {
                adaKesalahan = true;
                pesan = "Tidak dapat membuka tabel tbgaji\n" + ex;
            }
        } else {
            adaKesalahan = true;
            pesan = "Tidak dapat melakukan koneksi ke server\n" + koneksi.getPesanKesalahan();
        }

        return !adaKesalahan;
    }

    // ==================== HAPUS ====================
    public boolean hapus(String ktp, String kodePekerjaan) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            String SQLStatemen = "DELETE FROM tbgaji WHERE ktp = ? AND kode_pekerjaan = ?";
            PreparedStatement preparedStatement;

            try {
                preparedStatement = connection.prepareStatement(SQLStatemen);
                preparedStatement.setString(1, ktp);
                preparedStatement.setString(2, kodePekerjaan);
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

    // ==================== BACA DATA ====================
    public boolean bacaData(int mulai, int jumlah) {
        boolean adaKesalahan = false;
        Connection connection;
        list = new Object[0][0];

        if ((connection = koneksi.getConnection()) != null) {
            String SQLStatemen;
            PreparedStatement preparedStatement;
            ResultSet rset;

            try {
                SQLStatemen = "SELECT ktp, kode_pekerjaan, gajipokok, tunjangan, totalgaji FROM tbgaji LIMIT " + mulai + ", " + jumlah;
                preparedStatement = connection.prepareStatement(SQLStatemen);
                rset = preparedStatement.executeQuery();
                
                java.util.ArrayList<Object[]> tempList = new java.util.ArrayList<>();
                
                while(rset.next()){
                    Object[] row = new Object[5];
                    row[0] = rset.getString("ktp");
                    row[1] = rset.getString("kode_pekerjaan");
                    row[2] = rset.getDouble("totalgaji");    // gajiBersih = totalgaji
                    row[3] = rset.getDouble("gajipokok");    // gajiKotor = gajipokok
                    row[4] = rset.getDouble("tunjangan");
                    tempList.add(row);
                }
                
                list = new Object[tempList.size()][5];
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

    // ==================== BACA SATU DATA ====================
    public boolean baca(String ktp, String kodePekerjaan) {
        boolean adaKesalahan = false;
        Connection connection;

        if ((connection = koneksi.getConnection()) != null) {
            String SQLStatemen = "";
            PreparedStatement preparedStatement;
            ResultSet rset;

            try {
                SQLStatemen = "SELECT ktp, kode_pekerjaan, gajipokok, tunjangan, totalgaji FROM tbgaji WHERE ktp = ? AND kode_pekerjaan = ?";
                preparedStatement = connection.prepareStatement(SQLStatemen);
                preparedStatement.setString(1, ktp);
                preparedStatement.setString(2, kodePekerjaan);
                rset = preparedStatement.executeQuery();

                if (rset.next()) {
                    this.ktp = rset.getString("ktp");
                    this.kodePekerjaan = rset.getString("kode_pekerjaan");
                    this.jumlahTugas = 0; // Default value
                    this.gajiBersih = rset.getDouble("totalgaji");   // totalgaji
                    this.gajiKotor = rset.getDouble("gajipokok");    // gajipokok
                    this.tunjangan = rset.getDouble("tunjangan");
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
}
