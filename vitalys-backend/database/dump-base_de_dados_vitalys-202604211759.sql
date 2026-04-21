-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: base_de_dados_vitalys
-- ------------------------------------------------------
-- Server version	9.6.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cargos`
--

DROP TABLE IF EXISTS `cargos`;
CREATE TABLE `cargos` (
                          `id` int unsigned NOT NULL AUTO_INCREMENT,
                          `cargo` varchar(100) NOT NULL,
                          `nivel_acesso` varchar(100) NOT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Data for table `cargos`
--

LOCK TABLES `cargos` WRITE;
INSERT INTO `cargos` (`cargo`, `nivel_acesso`) VALUES
                                                   ('Atendente', 'ADMIN'),
                                                   ('Médico', 'PROFISSIONAL'),
                                                   ('Fisioterapeuta', 'PROFISSIONAL'),
                                                   ('Enfermeiro', 'PROFISSIONAL'),
                                                   ('Psicólogo', 'PROFISSIONAL');
UNLOCK TABLES;

--
-- Table structure for table `paciente`
--

DROP TABLE IF EXISTS `paciente`;
CREATE TABLE `paciente` (
                            `id` int unsigned NOT NULL AUTO_INCREMENT,
                            `nome` varchar(150) NOT NULL,
                            `cpf` varchar(14) NOT NULL,
                            `email` varchar(150) NOT NULL,
                            `data_nascimento` date NOT NULL,
                            `endereco` text NOT NULL,
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `paciente` WRITE;
UNLOCK TABLES;

--
-- Table structure for table `profissionais`
--

DROP TABLE IF EXISTS `profissionais`;
CREATE TABLE `profissionais` (
                                 `id` int unsigned NOT NULL AUTO_INCREMENT,
                                 `nome` varchar(150) NOT NULL,
                                 `cpf` varchar(14) NOT NULL,
                                 `email` varchar(150) NOT NULL,
                                 `data_nascimento` date NOT NULL,
                                 `id_cargo` int unsigned NOT NULL,
                                 PRIMARY KEY (`id`),
                                 KEY `profissionais_cargos_fk` (`id_cargo`),
                                 CONSTRAINT `profissionais_cargos_fk` FOREIGN KEY (`id_cargo`) REFERENCES `cargos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `profissionais` WRITE;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
                            `id` int unsigned NOT NULL AUTO_INCREMENT,
                            `login` varchar(100) NOT NULL,
                            `senha` varchar(100) NOT NULL,
                            `id_cargo` int unsigned NOT NULL,
                            `id_profissional` int unsigned NULL,
                            `ativo` tinyint(1) NOT NULL DEFAULT 1,
                            PRIMARY KEY (`id`),
                            KEY `usuarios_cargos_fk` (`id_cargo`),
                            KEY `usuarios_profissionais_fk` (`id_profissional`),
                            CONSTRAINT `usuarios_cargos_fk` FOREIGN KEY (`id_cargo`) REFERENCES `cargos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                            CONSTRAINT `usuarios_profissionais_fk` FOREIGN KEY (`id_profissional`) REFERENCES `profissionais` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `usuarios` WRITE;
UNLOCK TABLES;

--
-- Table structure for table `atendimento`
--

DROP TABLE IF EXISTS `atendimento`;
CREATE TABLE `atendimento` (
                               `id` int unsigned NOT NULL AUTO_INCREMENT,
                               `id_paciente` int unsigned NOT NULL,
                               `id_profissional` int unsigned NOT NULL,
                               `data_e_hora_marcadas` datetime NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `atendimento_profissionais_fk` (`id_profissional`),
                               KEY `atendimento_paciente_fk` (`id_paciente`),
                               CONSTRAINT `atendimento_paciente_fk` FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
                               CONSTRAINT `atendimento_profissionais_fk` FOREIGN KEY (`id_profissional`) REFERENCES `profissionais` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `atendimento` WRITE;
UNLOCK TABLES;

--
-- Table structure for table `calendario`
--

DROP TABLE IF EXISTS `calendario`;
CREATE TABLE `calendario` (
                              `id` int unsigned NOT NULL AUTO_INCREMENT,
                              `nome` varchar(100) NOT NULL,
                              `data` date NOT NULL,
                              `tipo` varchar(20) NOT NULL,
                              `id_atendimento` int unsigned DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              KEY `calendario_atendimento_fk` (`id_atendimento`),
                              CONSTRAINT `calendario_atendimento_fk` FOREIGN KEY (`id_atendimento`) REFERENCES `atendimento` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `calendario` WRITE;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Schema gerado para o projeto Vitalys
-- 2026