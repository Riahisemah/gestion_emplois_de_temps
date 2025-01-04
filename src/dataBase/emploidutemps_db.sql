-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 04, 2025 at 12:23 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `emploidutemps_db`
--
-- Création de la base de données
CREATE DATABASE IF NOT EXISTS emplois_de_temps;
USE emplois_de_temps;
-- --------------------------------------------------------

--
-- Table structure for table `enseignants`
--

CREATE TABLE `enseignants` (
  `nom` varchar(100) NOT NULL,
  `matricule` varchar(50) NOT NULL,
  `contact` varchar(50) DEFAULT NULL,
  UNIQUE KEY `matricule` (`matricule`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `enseignants`
--

INSERT INTO `enseignants` (`nom`, `matricule`, `contact`) VALUES
('Mouna Belhaj', 'ENS002', '66891234'),
('Rami Kacem', 'ENS003', '66231456'),
('Nour Amri', 'ENS004', '66789012'),
('Hatem Karoui', 'ENS005', '66014567'),
('Sami Jlassi', 'ENS006', '66323445'),
('Amal Mabrouk', 'ENS007', '66990123'),
('Yassin Karray', 'ENS008', '66501239'),
('Faten Trabelsi', 'ENS009', '66789456'),
('Nizar Chedli', 'ENS010', '66123478'),
('Hiba Jomaa', 'ENS011', '66678901'),
('Amin Hentati', 'ENS012', '66450123'),
('Salwa Chérif', 'ENS013', '66056789'),
('Karim Gharbi', 'ENS014', '66809123'),
('Wassim Ayari', 'ENS015', '66289034'),
('Sofia Ben Ali', 'ENS016', '66123456'),
('Omar Zribi', 'ENS017', '66876543'),
('Leila Khlifi', 'ENS018', '66789011'),
('Walid Bouaziz', 'ENS019', '66234567'),
('Nadia Ghribi', 'ENS020', '66345678'),
('Hedi Jaziri', 'ENS021', '66456789'),
('Rania Khlifi', 'ENS022', '66567890'),
('Youssef Mbarek', 'ENS023', '66678901'),
('Sabrine Karray', 'ENS024', '66789012'),
('Khaled Saidi', 'ENS025', '66890123'),
('Meriem Bouaziz', 'ENS026', '66901234'),
('Amine Gharbi', 'ENS027', '66012345'),
('Hana Jomaa', 'ENS028', '66123456'),
('Samiha Trabelsi', 'ENS029', '66234567'),
('Firas Kacem', 'ENS030', '66345678');

-- --------------------------------------------------------

--
-- Table structure for table `seances`
--

CREATE TABLE `seances` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `classe` varchar(50) NOT NULL,
  `matiere` varchar(50) NOT NULL,
  `jour` varchar(20) NOT NULL,
  `heure` varchar(33) DEFAULT NULL,
  `enseignant` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_enseignant` (`enseignant`),
  CONSTRAINT `fk_enseignant` FOREIGN KEY (`enseignant`) REFERENCES `enseignants` (`matricule`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `seances`
--

INSERT INTO `seances` (`id`, `classe`, `matiere`, `jour`, `heure`, `enseignant`) VALUES
(1, '6eme', 'Mathématiques', 'LUNDI', '1ere H', 'ENS002'),
(2, '5eme', 'Physique', 'LUNDI', '2eme H', 'ENS003'),
(3, '4eme', 'Chimie', 'MARDI', '3eme H', 'ENS004'),
(4, '3eme', 'Biologie', 'MARDI', '4eme H', 'ENS005'),
(5, '2eme', 'Histoire', 'MERCREDI', '1ere et 2eme H', 'ENS006'),
(6, '1ere', 'Géographie', 'MERCREDI', '3eme et 4eme H', 'ENS007'),
(7, '6eme', 'Informatique', 'JEUDI', '1ere H', 'ENS008'),
(8, '5eme', 'Anglais', 'JEUDI', '2eme H', 'ENS009'),
(9, '4eme', 'Français', 'VENDREDI', '3eme H', 'ENS010'),
(10, '3eme', 'Arts', 'VENDREDI', '4eme H', 'ENS011'),
(11, '2eme', 'Éducation Physique', 'LUNDI', '1ere et 2eme H', 'ENS012'),
(12, '1ere', 'Philosophie', 'MARDI', '3eme et 4eme H', 'ENS013'),
(13, '6eme', 'Musique', 'MERCREDI', '1ere H', 'ENS014'),
(14, '5eme', 'Sociologie', 'JEUDI', '2eme H', 'ENS015'),
(15, '4eme', 'Psychologie', 'VENDREDI', '3eme H', 'ENS016'),
(16, '3eme', 'Économie', 'LUNDI', '4eme H', 'ENS017'),
(17, '2eme', 'Droit', 'MARDI', '1ere et 2eme H', 'ENS018'),
(18, '1ere', 'Gestion', 'MERCREDI', '3eme et 4eme H', 'ENS019'),
(19, '6eme', 'Statistiques', 'JEUDI', '1ere H', 'ENS020'),
(20, '5eme', 'Marketing', 'VENDREDI', '2eme H', 'ENS021'),
(21, '4eme', 'Entrepreneuriat', 'LUNDI', '3eme H', 'ENS022'),
(22, '3eme', 'Développement Durable', 'MARDI', '4eme H', 'ENS023'),
(23, '2eme', 'Éthique', 'MERCREDI', '1ere et 2eme H', 'ENS024'),
(24, '1ere', 'Innovation', 'JEUDI', '3eme et 4eme H', 'ENS025'),
(25, '6eme', 'Leadership', 'VENDREDI', '1ere H', 'ENS026'),
(26, '5eme', 'Communication', 'LUNDI', '2eme H', 'ENS027'),
(27, '4eme', 'Négociation', 'MARDI', '3eme H', 'ENS028'),
(28, '3eme', 'Gestion de Projet', 'MERCREDI', '4eme H', 'ENS029'),
(29, '2eme', 'Analyse de Données', 'JEUDI', '1ere et 2eme H', 'ENS030');

COMMIT;