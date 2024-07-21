package com.example;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class JoinToLobbyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String JDBC_URL = "jdbc:mysql://mysql:3306/lobbyRoblox";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "password";
    //public static Integer contaRoblox = 0;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head><title>Unirse a Roblox</title>");
        out.println("</head><body>");

        out.println("<h1>Unirse a la sala de Roblox</h1>");
        out.println("<form action='join' method='post' class='form-container'>");
        out.println("<label for='jugador'>ID del Jugador: </label><br>");
        out.println("<input type='number' id='jugador' name='id_jugador'><br>");
        out.println("<input type='submit' value='Unirse'>");
        out.println("</form>");
        out.println("<br>");
        out.println("<a href='index.html' class='button'>Volver</a><br>");

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            // VAMOS A MOSTRAR UNA LISTA CON LOS JUGADORES Y SUS IDs
            stmt = conn.createStatement();
            String sql =  "SELECT j.id_jugador, j.jugador, s.sala, sl.id_lugar FROM jugador j\n" +
                "LEFT JOIN sala_lugares sl ON j.id_jugador = sl.id_jugador\n" +
                "LEFT JOIN sala s ON s.id_sala = sl.id_sala;";
            rs = stmt.executeQuery(sql);

            out.println("<h2>Lista de Jugadores</h2>");
            while (rs.next()) {
                out.println("<p>ID: " + rs.getInt("id_jugador")
                        + " - Jugador: " + rs.getString("jugador")
                        + " - Sala: " + (rs.getString("sala") != null ? rs.getString("sala") : "En el lobby")
                        + (rs.getInt("id_lugar") != 0 ? (" - Lugar: " + rs.getInt("id_lugar")) : "") + "</p>");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            out.println("<p>Ocurrió un error al conectar la base de datos... " + e + "</p> ");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        out.println("</body></html>");
        out.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        Integer jugador = Integer.parseInt(request.getParameter("id_jugador"));
        Integer lugar = 0;
        try {
            lugar = contaActual(); // Ingresa el valor actual del contador
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("<h3>¡Error al obtener el contador actual!</h3>");
        }

        Connection conn = null;
        PreparedStatement ptmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            String sSQL =  "UPDATE sala_lugares SET id_jugador = ?"
                    + " WHERE id_sala = ? AND id_lugar = ?";
            ptmt = conn.prepareStatement(sSQL);
            ptmt.setInt(1, jugador);
            ptmt.setInt(2, 1); // Sala de Roblox
            ptmt.setInt(3, lugar);
            ptmt.executeUpdate();

            response.sendRedirect("players");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("<h3>¡Error al unir al jugador!</h3>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ptmt != null) ptmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected Integer contaActual() throws ClassNotFoundException{
        Connection conn = null;
        PreparedStatement ptmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            String query = "SELECT MAX(id_lugar) AS max_id_lugar FROM sala_lugares WHERE id_sala = ? AND id_jugador IS NOT NULL";
            ptmt = conn.prepareStatement(query);
            ptmt.setInt(1, 1);
            rs = ptmt.executeQuery();
            if (rs.next()) {
                Integer robInt = rs.getInt("max_id_lugar");
                //robInt = robInt==3 ? robInt : robInt+1;
                return !rs.wasNull() ? robInt+1 : 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }
}
