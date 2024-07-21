package com.example;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class PlayerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JDBC_URL = "jdbc:mysql://mysql:3306/lobbyRoblox";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "password";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>¡Bienvenido!</h1>");
        out.println("<h2>Lista de Lugares de Roblox:</h2>");

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try{
            // Paso 1: Conectar a la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            // Paso 2: Ejecutar consulta SQL
            stmt = conn.createStatement();
            String sql =  "SELECT s.sala, sl.id_lugar, j.jugador FROM sala_lugares sl\n" +
            "JOIN sala s ON s.id_sala = sl.id_sala\n" +
            "LEFT JOIN jugador j ON j.id_jugador = sl.id_jugador;";
            rs = stmt.executeQuery(sql);

            // Paso 3: Mostrar resultados
            String lastSala = ""; // Variable para almacenar el último nombre de la sala impreso
            while (rs.next()) {
                String currentSala = rs.getString("sala");
                if (!currentSala.equals(lastSala)) {
                    out.println("-- SALA: " + currentSala + " --");
                    lastSala = currentSala;
                }
                out.println("<p>Lugar " + rs.getInt("id_lugar") + ": "
                        + (rs.getString("jugador") != null ? rs.getString("jugador") : "Disponible") + "</p>");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            out.println("<p>Ocurrió un error al conectar la base de datos... " + e + "</p> ");
        } finally {
            // Paso 4: Cerrar conexiones
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        out.println("<a href='index.html' class='button'>Volver al Inicio</a>");
        out.println("</body></html>");
    }

}
