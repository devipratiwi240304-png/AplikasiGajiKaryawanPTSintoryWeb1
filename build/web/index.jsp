<%-- Document : index Created on : 19 Nov 2025, 13.18.05 Author : Devi Pratiwi --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <link href="style.css" rel="stylesheet" type="text/css" />
            <title>Informasi Gaji Karyawan</title>
        </head>

        <body bgcolor="#808080">

            <% String menu="<br><b>Master Data</b><br>" + "<a href='KaryawanController'>Karyawan</a><br>"
                + "<a href='PekerjaanController'>Pekerjaan</a><br>" + "<b>Transaksi</b><br>"
                + "<a href='GajiController'>Gaji</a><br><br>" + "<b>Laporan</b><br>"
                + "<a href='LaporanGajiController'>Gaji</a><br><br>" + "<a href='LoginController'>Login</a><br><br>" ;
                String topMenu="<nav><ul>" + "<li><a href='.'>Home</a></li>" + "<li><a href='#'>Master Data</a>"
                + "<ul>" + "<li><a href='KaryawanController'>Karyawan</a></li>"
                + "<li><a href='PekerjaanController'>Pekerjaan</a></li>" + "</ul>" + "</li>"
                + "<li><a href='#'>Transaksi</a>" + "<ul>" + "<li><a href='GajiController'>Gaji</a></li>" + "</ul>"
                + "</li>" + "<li><a href='#'>Laporan</a>" + "<ul>" + "<li><a href='LaporanGajiController'>Gaji</a></li>"
                + "</ul>" + "</li>" + "<li><a href='LoginController'>Login</a></li>" + "</ul>" + "</nav>" ; String
                konten="<br><h1>Selamat Datang</h1>" ; String userName="" ; if (!session.isNew()){ try { Object
                userObj=session.getAttribute("userName"); if(userObj !=null) { userName=userObj.toString(); } } catch
                (Exception ex) {} if (userName !=null && !userName.equals("")) { konten +="<h2>" + userName + "</h2>" ;
                try{ if(session.getAttribute("menu") !=null){ menu=session.getAttribute("menu").toString(); } } catch
                (Exception ex){} try { if(session.getAttribute("topMenu") !=null){
                topMenu=session.getAttribute("topMenu").toString(); } } catch (Exception ex){} } } %>

                <center>
                    <table width="80%" bgcolor="#eeeeee">
                        <tr>
                            <td colspan="2" align="center">
                                <br>
                                <h2 style="margin-bottom:0px; margin-top: 0px">Informasi Gaji Karyawan</h2>
                                <h1 style="margin-bottom:0px; margin-top: 0px">PT Sintory</h1>
                                <h4 style="margin-bottom:0px; margin-top: 0px">Jl. Surya Kencana No. 99 Pamulang,
                                    Tangerang Selatan, Banten</h4>
                                <br>
                            </td>
                        </tr>

                        <tr height="400">
                            <td width="200" align="center" valign="top" bgcolor="#eeeeee">
                                <br>
                                <div id='menu'>
                                    <%= menu %>
                                </div>
                            </td>
                            <td align="center" valign="top" bgcolor="#ffffff">
                                <%=topMenu%>
                                    <br>
                                    <%=konten%>
                            </td>
                        </tr>

                        <tr>
                            <td colspan="2" align="center" bgcolor="#eeeeff">
                                <small>
                                    Copyright © 2017 PT Sintory<br>
                                    Jl. Surya Kencana No. 99 Pamulang, Tangerang Selatan, Banten
                                </small>
                            </td>
                        </tr>
                    </table>
                </center>
        </body>

        </html>