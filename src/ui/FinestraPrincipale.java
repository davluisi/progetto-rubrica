package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import db.GestoreDatabase;
import model.Persona;

public class FinestraPrincipale extends JFrame {

    private JTable tabella;
    private DefaultTableModel modelloTabella;
    private Vector<Persona> listaPersone;

    public FinestraPrincipale() {
        setTitle("Rubrica Telefonica");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); // Impedisce di staccare la barra dalla finestra

        JButton btnNuovo = new JButton("Nuovo");
        JButton btnModifica = new JButton("Modifica");
        JButton btnElimina = new JButton("Elimina");
        
        // Caricamento icone
        
        java.net.URL urlNuovo = getClass().getResource("/img/nuovo.png");
        if (urlNuovo != null) {
            btnNuovo.setIcon(new javax.swing.ImageIcon(urlNuovo));
        }

        java.net.URL urlModifica = getClass().getResource("/img/modifica.png");
        if (urlModifica != null) {
            btnModifica.setIcon(new javax.swing.ImageIcon(urlModifica));
        }

        java.net.URL urlElimina = getClass().getResource("/img/elimina.png");
        if (urlElimina != null) {
            btnElimina.setIcon(new javax.swing.ImageIcon(urlElimina));
        }

        toolBar.add(btnNuovo);
        toolBar.addSeparator();
        toolBar.add(btnModifica);
        toolBar.addSeparator();
        toolBar.add(btnElimina);

        add(toolBar, BorderLayout.SOUTH);

        // Le colonne devono mostrare solo nome, cognome e telefono come da specifiche
        String[] colonne = {"Nome", "Cognome", "Telefono"};
        modelloTabella = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rende le celle non modificabili direttamente con doppio click
            }
        };
        
        tabella = new JTable(modelloTabella);
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Seleziona una riga alla volta
        add(new JScrollPane(tabella), BorderLayout.CENTER);

        // Carichiamo i dati iniziali
        aggiornaTabella();

        // Azioni ---

        btnNuovo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EditorPersona editor = new EditorPersona(FinestraPrincipale.this, null);
                editor.setVisible(true);
            }
        });

        btnModifica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = tabella.getSelectedRow();
                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(FinestraPrincipale.this, 
                            "Seleziona una persona dalla tabella per modificarla.", 
                            "Attenzione", JOptionPane.WARNING_MESSAGE);
                } else {
                    Persona p = listaPersone.get(rigaSelezionata);
                    EditorPersona editor = new EditorPersona(FinestraPrincipale.this, p);
                    editor.setVisible(true);
                }
            }
        });

        btnElimina.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = tabella.getSelectedRow();
                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(FinestraPrincipale.this, 
                            "Seleziona una persona dalla tabella per eliminarla.", 
                            "Attenzione", JOptionPane.WARNING_MESSAGE);
                } else {
                    Persona p = listaPersone.get(rigaSelezionata);
                    int conferma = JOptionPane.showConfirmDialog(FinestraPrincipale.this, 
                            "Eliminare la persona " + p.getNome() + " " + p.getCognome() + "?", 
                            "Conferma Eliminazione", 
                            JOptionPane.YES_NO_OPTION);
                    
                    if (conferma == JOptionPane.YES_OPTION) {
                        if (GestoreDatabase.eliminaPersona(p.getId())) {
                            aggiornaTabella();
                        } else {
                            JOptionPane.showMessageDialog(FinestraPrincipale.this, 
                                    "Errore durante l'eliminazione dal database.", 
                                    "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    public void aggiornaTabella() {
        modelloTabella.setRowCount(0); // Svuota la tabella visiva
        listaPersone = GestoreDatabase.getTutteLePersone(); // Ricarica la lista dal DB
        
        for (Persona p : listaPersone) {
            Object[] riga = {p.getNome(), p.getCognome(), p.getTelefono()};
            modelloTabella.addRow(riga);
        }
    }
}