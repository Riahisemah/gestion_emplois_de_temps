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

-- --------------------------------------------------------

--
-- Table structure for table `enseignants`
--

CREATE TABLE `enseignants` (
  `nom` varchar(100) NOT NULL,
  `matricule` varchar(50) NOT NULL,
  `contact` varchar(50) DEFAULT NULL
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
('Wassim Ayari', 'ENS015', '66289034');

-- --------------------------------------------------------

--
-- Table structure for table `seances`
--

CREATE TABLE `seances` (
  `id` int(11) NOT NULL,
  `classe` varchar(50) NOT NULL,
  `matiere` varchar(50) NOT NULL,
  `jour` varchar(20) NOT NULL,
  `heure` varchar(33) DEFAULT NULL,
  `enseignant` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `seances`
--

INSERT INTO `seances` (`id`, `classe`, `matiere`, `jour`, `heure`, `enseignant`) VALUES
(18, '6eme', 'test', 'LUNDI', '1ere H', 'ENS002');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `enseignants`
--
ALTER TABLE `enseignants`
  ADD UNIQUE KEY `matricule` (`matricule`);

--
-- Indexes for table `seances`
--
ALTER TABLE `seances`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_enseignant` (`enseignant`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `seances`
--
ALTER TABLE `seances`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `seances`
--
ALTER TABLE `seances`
  ADD CONSTRAINT `fk_enseignant` FOREIGN KEY (`enseignant`) REFERENCES `enseignants` (`matricule`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
