package ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import db.GestoreDatabase;

public class FinestraLogin extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public FinestraLogin() {
        // Impostazioni base della finestra
        setTitle("Login - Rubrica");
        setSize(300, 160);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la finestra nello schermo
        setResizable(false);

        // Creazione del pannello con un layout a griglia (3 righe, 2 colonne)
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Creazione ed inserimento dei componenti
        panel.add(new JLabel("Utente:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);

        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        // Spazio vuoto per allineare il bottone a destra
        panel.add(new JLabel("")); 
        
        JButton btnLogin = new JButton("LOGIN");
        panel.add(btnLogin);

        // Aggiunta dell'azione al bottone Login
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                // Chiamata al database per verificare le credenziali
                if (GestoreDatabase.eseguiLogin(username, password)) {
                    // Se il login è corretto, chiudiamo questa finestra e apriamo la principale
                    dispose(); 
                    
                    // Apriamo la finestra principale
                    FinestraPrincipale mainFrame = new FinestraPrincipale();
                    mainFrame.setVisible(true);
                } else {
                    // Messaggio di errore
                    JOptionPane.showMessageDialog(FinestraLogin.this, 
                            "Login errato! Controlla username e password.", 
                            "Errore di Accesso", 
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Aggiunta del pannello alla finestra
        add(panel);
    }
}