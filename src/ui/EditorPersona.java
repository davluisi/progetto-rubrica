package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import db.GestoreDatabase;
import model.Persona;

public class EditorPersona extends JDialog {

    private JTextField txtNome, txtCognome, txtIndirizzo, txtTelefono, txtEta;
    private Persona personaDaModificare;
    private FinestraPrincipale parent;

    // Costruttore che accetta il parent (per aggiornare la tabella) e la persona (null se nuova)
    public EditorPersona(FinestraPrincipale parent, Persona p) {
        super(parent, true); // Rende la finestra modale
        this.parent = parent;
        this.personaDaModificare = p;

        setTitle(p == null ? "Nuova Persona" : "Modifica Persona");
        setSize(350, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Pannello centrale
        JPanel panelCampi = new JPanel(new GridLayout(5, 2, 10, 10));
        panelCampi.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelCampi.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        panelCampi.add(txtNome);

        panelCampi.add(new JLabel("Cognome:"));
        txtCognome = new JTextField();
        panelCampi.add(txtCognome);

        panelCampi.add(new JLabel("Indirizzo:"));
        txtIndirizzo = new JTextField();
        panelCampi.add(txtIndirizzo);

        panelCampi.add(new JLabel("Telefono:"));
        txtTelefono = new JTextField();
        panelCampi.add(txtTelefono);

        panelCampi.add(new JLabel("Età:"));
        txtEta = new JTextField();
        panelCampi.add(txtEta);

        // Se è una modifica, pre-compiliamo i campi
        if (p != null) {
            txtNome.setText(p.getNome());
            txtCognome.setText(p.getCognome());
            txtIndirizzo.setText(p.getIndirizzo());
            txtTelefono.setText(p.getTelefono());
            txtEta.setText(String.valueOf(p.getEta()));
        }

        add(panelCampi, BorderLayout.CENTER);

        // Bottoni
        JPanel panelBottoni = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalva = new JButton("Salva");
        JButton btnAnnulla = new JButton("Annulla");

        panelBottoni.add(btnSalva);
        panelBottoni.add(btnAnnulla);
        add(panelBottoni, BorderLayout.SOUTH);

        // Azioni bottoni
        btnAnnulla.addActionListener(e -> dispose()); // Chiude la finestra senza fare nulla

        btnSalva.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvaDati();
            }
        });
    }

    private void salvaDati() {
        if (txtNome.getText().trim().isEmpty() || txtCognome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e Cognome sono obbligatori!", "Errore Dati", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int eta = 0;
        try {
            if (!txtEta.getText().trim().isEmpty()) {
                eta = Integer.parseInt(txtEta.getText().trim());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "L'età deve essere un numero valido!", "Errore Dati", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Creiamo l'oggetto o lo aggiorniamo
        boolean successo;
        if (personaDaModificare == null) {
            Persona nuovaPersona = new Persona(
                    txtNome.getText(), txtCognome.getText(), 
                    txtIndirizzo.getText(), txtTelefono.getText(), eta);
            successo = GestoreDatabase.inserisciPersona(nuovaPersona);
        } else {
            // Modifica persona esistente
            personaDaModificare.setNome(txtNome.getText());
            personaDaModificare.setCognome(txtCognome.getText());
            personaDaModificare.setIndirizzo(txtIndirizzo.getText());
            personaDaModificare.setTelefono(txtTelefono.getText());
            personaDaModificare.setEta(eta);
            successo = GestoreDatabase.aggiornaPersona(personaDaModificare);
        }

        if (successo) {
            parent.aggiornaTabella();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Errore durante il salvataggio nel database.", "Errore DB", JOptionPane.ERROR_MESSAGE);
        }
    }
}