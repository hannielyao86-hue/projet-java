-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mer. 04 mars 2026 à 14:13
-- Version du serveur : 9.1.0
-- Version de PHP : 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `gym_manager`
--

--
-- Structure de la table `abonnement`
--

DROP TABLE IF EXISTS `abonnement`;
CREATE TABLE IF NOT EXISTS `abonnement` (
  `id_abonnement` int NOT NULL AUTO_INCREMENT,
  `nom_abonnement` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_debut` date NOT NULL,
  `date_fin` date NOT NULL,
  `statut_abonnement` enum('actif','expire','suspendu') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'actif',
  PRIMARY KEY (`id_abonnement`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `abonnement`
--

INSERT INTO `abonnement` (`id_abonnement`, `nom_abonnement`, `date_debut`, `date_fin`, `statut_abonnement`) VALUES
(1, 'Formule Premium Annuelle', '2025-01-01', '2025-12-31', 'actif'),
(2, 'Formule Basic Mensuelle', '2025-02-01', '2025-03-31', 'actif'),
(3, 'Formule Étudiant', '2024-09-01', '2025-06-30', 'actif');

-- --------------------------------------------------------

--
-- Structure de la table `activite`
--

DROP TABLE IF EXISTS `activite`;
CREATE TABLE IF NOT EXISTS `activite` (
  `id_activite` int NOT NULL AUTO_INCREMENT,
  `nom_activite` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_activite` date NOT NULL,
  `heure_debut` time NOT NULL,
  `heure_fin` time NOT NULL,
  `id_coach` int NOT NULL,
  `id_salle` int DEFAULT NULL,
  PRIMARY KEY (`id_activite`),
  KEY `id_coach` (`id_coach`),
  KEY `id_salle` (`id_salle`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `activite`
--

INSERT INTO `activite` (`id_activite`, `nom_activite`, `date_activite`, `heure_debut`, `heure_fin`, `id_coach`, `id_salle`) VALUES
(1, 'Séance Musculation Débutant', '2025-03-10', '09:00:00', '10:30:00', 1, 1),
(2, 'Yoga du Matin', '2025-03-10', '08:00:00', '09:00:00', 2, 2),
(3, 'Cardio Intensif', '2025-03-11', '18:00:00', '19:00:00', 3, 3);

-- --------------------------------------------------------

--
-- Structure de la table `activiter`
--

DROP TABLE IF EXISTS `activiter`;
CREATE TABLE IF NOT EXISTS `activiter` (
  `id_activite` int NOT NULL AUTO_INCREMENT,
  `nom_activite` varchar(250) COLLATE utf8mb3_unicode_ci NOT NULL,
  `date_activite` date NOT NULL,
  `heure_debut` time NOT NULL,
  `heure_fin` time NOT NULL,
  `id_coah` int NOT NULL,
  `id_salle` int NOT NULL,
  PRIMARY KEY (`id_activite`),
  KEY `id_coah` (`id_coah`),
  KEY `id_salle` (`id_salle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;

-- --------------------------------------------------------

--
-- Structure de la table `coach`
--

DROP TABLE IF EXISTS `coach`;
CREATE TABLE IF NOT EXISTS `coach` (
  `id_coach` int NOT NULL AUTO_INCREMENT,
  `nom_coach` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email_coach` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `specialite` varchar(250) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id_coach`),
  UNIQUE KEY `email_coach` (`email_coach`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `coach`
--

INSERT INTO `coach` (`id_coach`, `nom_coach`, `email_coach`, `specialite`) VALUES
(1, 'Jean Dupont', 'jean.dupont@gym.com', 'Musculation'),
(2, 'Marie Martin', 'marie.martin@gym.com', 'Yoga'),
(3, 'Paul Bernard', 'paul.bernard@gym.com', 'Cardio');

-- --------------------------------------------------------

--
-- Structure de la table `inscription`
--

DROP TABLE IF EXISTS `inscription`;
CREATE TABLE IF NOT EXISTS `inscription` (
  `id_inscription` int NOT NULL AUTO_INCREMENT,
  `date_inscription` date NOT NULL DEFAULT (curdate()),
  `statut` enum('confirmee','en_attente','annulee') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'en_attente',
  `id_membre` int NOT NULL,
  `id_activite` int NOT NULL,
  PRIMARY KEY (`id_inscription`),
  UNIQUE KEY `unique_membre_activite` (`id_membre`,`id_activite`),
  KEY `id_activite` (`id_activite`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `inscription`
--

INSERT INTO `inscription` (`id_inscription`, `date_inscription`, `statut`, `id_membre`, `id_activite`) VALUES
(1, '2025-03-01', 'confirmee', 1, 1),
(2, '2025-03-02', 'confirmee', 2, 2),
(3, '2025-03-03', 'en_attente', 3, 3),
(4, '2025-03-04', 'confirmee', 4, 1);

-- --------------------------------------------------------

--
-- Structure de la table `membre`
--

DROP TABLE IF EXISTS `membre`;
CREATE TABLE IF NOT EXISTS `membre` (
  `id_membre` int NOT NULL AUTO_INCREMENT,
  `nom_membre` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email_membre` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `id_abonnement` int DEFAULT NULL,
  PRIMARY KEY (`id_membre`),
  UNIQUE KEY `email_membre` (`email_membre`),
  KEY `id_abonnement` (`id_abonnement`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Déchargement des données de la table `membre`
--

INSERT INTO `membre` (`id_membre`, `nom_membre`, `email_membre`, `phone`, `id_abonnement`) VALUES
(1, 'Alice Dubois', 'alice.dubois@email.com', '0601020304', 1),
(2, 'Bob Leroy', 'bob.leroy@email.com', '0605060708', 1),
(3, 'Clara Petit', 'clara.petit@email.com', '0609101112', 2),
(4, 'David Martin', 'david.martin@email.com', '0612131415', 3);

-- --------------------------------------------------------

--
-- Structure de la table `salle`
--

DROP TABLE IF EXISTS `salle`;
CREATE TABLE IF NOT EXISTS `salle` (
  `id_salle` int NOT NULL AUTO_INCREMENT,
  `nom_salle` varchar(250) COLLATE utf8mb4_unicode_ci NOT NULL,
  `capacite` int NOT NULL,
  PRIMARY KEY (`id_salle`)
) ;

--
-- Déchargement des données de la table `salle`
--

INSERT INTO `salle` (`id_salle`, `nom_salle`, `capacite`) VALUES
(1, 'Salle Musculation', 20),
(2, 'Salle Yoga', 15),
(3, 'Salle Cardio', 25);

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `activite`
--
ALTER TABLE `activite`
  ADD CONSTRAINT `activite_ibfk_1` FOREIGN KEY (`id_coach`) REFERENCES `coach` (`id_coach`) ON DELETE CASCADE,
  ADD CONSTRAINT `activite_ibfk_2` FOREIGN KEY (`id_salle`) REFERENCES `salle` (`id_salle`) ON DELETE SET NULL;

--
-- Contraintes pour la table `inscription`
--
ALTER TABLE `inscription`
  ADD CONSTRAINT `inscription_ibfk_1` FOREIGN KEY (`id_membre`) REFERENCES `membre` (`id_membre`) ON DELETE CASCADE,
  ADD CONSTRAINT `inscription_ibfk_2` FOREIGN KEY (`id_activite`) REFERENCES `activite` (`id_activite`) ON DELETE CASCADE;

--
-- Contraintes pour la table `membre`
--
ALTER TABLE `membre`
  ADD CONSTRAINT `membre_ibfk_1` FOREIGN KEY (`id_abonnement`) REFERENCES `abonnement` (`id_abonnement`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
