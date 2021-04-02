-- MySQL dump 10.13  Distrib 8.0.21, for Linux (x86_64)
--
-- Host: localhost    Database: cool_surveys
-- ------------------------------------------------------
-- Server version       8.0.21

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
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer` (
                          `answer_id` int NOT NULL,
                          `question_id` int NOT NULL,
                          `user_id` int NOT NULL,
                          `questionnaire_id` int NOT NULL,
                          `answer` varchar(512) DEFAULT NULL,
                          PRIMARY KEY (`answer_id`,`question_id`,`questionnaire_id`),
                          KEY `answer/user_idx` (`user_id`),
                          KEY `answer/question_idx` (`questionnaire_id`,`question_id`),
                          CONSTRAINT `answer/question` FOREIGN KEY (`questionnaire_id`, `question_id`) REFERENCES `question` (`questionnaire_id`, `question_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                          CONSTRAINT `answer/user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bad_words`
--

DROP TABLE IF EXISTS `bad_words`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bad_words` (
                             `word` varchar(32) NOT NULL,
                             PRIMARY KEY (`word`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `credentials`
--

DROP TABLE IF EXISTS `credentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credentials` (
                               `user_id` int NOT NULL AUTO_INCREMENT,
                               `password_hash` char(64) NOT NULL,
                               `username` varchar(20) NOT NULL,
                               `mail` varchar(64) NOT NULL,
                               `admin` tinyint NOT NULL DEFAULT '0',
                               PRIMARY KEY (`user_id`),
                               UNIQUE KEY `user_id_UNIQUE` (`user_id`),
                               UNIQUE KEY `username_UNIQUE` (`username`),
                               UNIQUE KEY `mail_UNIQUE` (`mail`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `option`
--

DROP TABLE IF EXISTS `option`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `option` (
                          `option_id` int NOT NULL,
                          `question_id` int NOT NULL,
                          `questionnaire_id` int NOT NULL,
                          `text` varchar(50) NOT NULL,
                          PRIMARY KEY (`option_id`,`question_id`,`questionnaire_id`),
                          KEY `option/question_idx` (`questionnaire_id`,`question_id`),
                          CONSTRAINT `option/question` FOREIGN KEY (`questionnaire_id`, `question_id`) REFERENCES `question` (`questionnaire_id`, `question_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
                            `question_id` int NOT NULL AUTO_INCREMENT,
                            `questionnaire_id` int NOT NULL,
                            `question` varchar(512) NOT NULL,
                            PRIMARY KEY (`question_id`,`questionnaire_id`),
                            KEY `questionnaire_idx` (`questionnaire_id`),
                            CONSTRAINT `questionnaire/question` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaire` (`q_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `questionnaire`
--

DROP TABLE IF EXISTS `questionnaire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questionnaire` (
                                 `q_id` int NOT NULL AUTO_INCREMENT,
                                 `name` varchar(32) NOT NULL,
                                 `photo` blob NOT NULL COMMENT 'Photo_path non è unica in quanto si potrebbe riproporre un questionario',
                                 `date` datetime NOT NULL,
                                 PRIMARY KEY (`q_id`),
                                 UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `submission`
--

DROP TABLE IF EXISTS `submission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `submission` (
                              `user_id` int NOT NULL,
                              `questionnaire_id` int NOT NULL,
                              `submitted` tinyint NOT NULL,
                              `age` int DEFAULT NULL,
                              `sex` tinyint DEFAULT NULL,
                              `expertise_level` int DEFAULT NULL,
                              PRIMARY KEY (`user_id`,`questionnaire_id`),
                              KEY `submission/user_idx` (`user_id`),
                              KEY `submission/questionnaire_idx` (`questionnaire_id`),
                              CONSTRAINT `submission/questionnaire` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaire` (`q_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                              CONSTRAINT `submission/user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
                        `user_id` int NOT NULL,
                        `points` int DEFAULT '0',
                        `blocked_until` datetime DEFAULT CURRENT_TIMESTAMP,
                        PRIMARY KEY (`user_id`),
                        CONSTRAINT `user/credentials` FOREIGN KEY (`user_id`) REFERENCES `credentials` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-02 11:39:04