package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import model.Persona;

public class GestoreDatabase {

    // Metodo per ottenere la connessione leggendo il file properties
    private static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("credenziali_database.properties")) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Impossibile trovare o leggere il file credenziali_database.properties");
            e.printStackTrace();
            return null;
        }

        String ip = props.getProperty("ip-server-mysql");
        String porta = props.getProperty("porta");
        String user = props.getProperty("username");
        String pass = props.getProperty("password");

        // URL di connessione per JDBC
        String url = "jdbc:mysql://" + ip + ":" + porta + "/rubrica_db";
        
        return DriverManager.getConnection(url, user, pass);
    }

    // Login
    public static boolean eseguiLogin(String username, String password) {
        String sql = "SELECT * FROM utenti WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Username e password sono corretti
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Legge tutte le persone e le restituisce in un Vector
    public static Vector<Persona> getTutteLePersone() {
        Vector<Persona> listaPersone = new Vector<>();
        String sql = "SELECT * FROM persone";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Persona p = new Persona(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cognome"),
                    rs.getString("indirizzo"),
                    rs.getString("telefono"),
                    rs.getInt("eta")
                );
                listaPersone.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPersone;
    }

    // Inserisce una nuova persona
    public static boolean inserisciPersona(Persona p) {
        String sql = "INSERT INTO persone (nome, cognome, indirizzo, telefono, eta) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCognome());
            ps.setString(3, p.getIndirizzo());
            ps.setString(4, p.getTelefono());
            ps.setInt(5, p.getEta());
            
            int righeInserite = ps.executeUpdate();
            return righeInserite > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Aggiorna una persona esistente
    public static boolean aggiornaPersona(Persona p) {
        String sql = "UPDATE persone SET nome=?, cognome=?, indirizzo=?, telefono=?, eta=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCognome());
            ps.setString(3, p.getIndirizzo());
            ps.setString(4, p.getTelefono());
            ps.setInt(5, p.getEta());
            ps.setInt(6, p.getId());
            
            int righeAggiornate = ps.executeUpdate();
            return righeAggiornate > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Elimina una persona tramite il suo ID
    public static boolean eliminaPersona(int id) {
        String sql = "DELETE FROM persone WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            int righeEliminate = ps.executeUpdate();
            return righeEliminate > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}