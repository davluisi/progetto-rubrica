-- Creazione del database
CREATE DATABASE IF NOT EXISTS rubrica_db;
USE rubrica_db;

-- Tabella per la gestione degli utenti
CREATE TABLE IF NOT EXISTS utenti (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Tabella per la gestione delle persone
CREATE TABLE IF NOT EXISTS persone (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    indirizzo VARCHAR(255),
    telefono VARCHAR(50),
    eta INT
);

-- Utente di default
INSERT INTO utenti (username, password) VALUES ('admin', 'admin');