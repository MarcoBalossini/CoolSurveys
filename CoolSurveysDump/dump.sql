Enter password:
-- MySQL dump 10.13  Distrib 8.0.21, for Linux (x86_64)
--
-- Host: localhost    Database: cool_surveys
-- ------------------------------------------------------
-- Server version	8.0.21

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
                          `answer_id` int NOT NULL AUTO_INCREMENT,
                          `question_id` int NOT NULL,
                          `user_id` int NOT NULL,
                          `answer` varchar(512) DEFAULT NULL,
                          PRIMARY KEY (`answer_id`),
                          KEY `answer/user_idx` (`user_id`),
                          KEY `answer/question_idx` (`question_id`),
                          CONSTRAINT `answer/question` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                          CONSTRAINT `answer/user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `answer_BEFORE_INSERT` BEFORE INSERT ON `answer` FOR EACH ROW BEGIN
    IF EXISTS (SELECT word FROM bad_words WHERE new.answer LIKE concat('%', concat(word, '%'))) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = "Bad Word Found";
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

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
-- Dumping data for table `bad_words`
--

LOCK TABLES `bad_words` WRITE;
/*!40000 ALTER TABLE `bad_words` DISABLE KEYS */;
INSERT INTO `bad_words` VALUES ('bitch'),('fuck'),('shit');
/*!40000 ALTER TABLE `bad_words` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credentials`
--

DROP TABLE IF EXISTS `credentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `credentials` (
                               `user_id` int NOT NULL AUTO_INCREMENT,
                               `password_hash` char(128) NOT NULL,
                               `username` varchar(20) NOT NULL,
                               `mail` varchar(64) NOT NULL,
                               `admin` tinyint NOT NULL DEFAULT '0',
                               PRIMARY KEY (`user_id`),
                               UNIQUE KEY `user_id_UNIQUE` (`user_id`),
                               UNIQUE KEY `username_UNIQUE` (`username`),
                               UNIQUE KEY `mail_UNIQUE` (`mail`)
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credentials`
--

LOCK TABLES `credentials` WRITE;
/*!40000 ALTER TABLE `credentials` DISABLE KEYS */;
INSERT INTO `credentials` VALUES (173,'1JJgChJvh54d4Ue2Gt1uCV2B6a8PgUs+n7/uRVBGLuiS0FuNJoPUCu3wNA/RW2fpk7iGZJByezOiW7vYoJJAXg==:hHOLwtMR/A9sqm+rSqPZyQ==','tom','tom@tom',0),(174,'UZ6rruUd7eLZ933Y0XnQYI+RcT5CPuKYM1k7ZoAR7DTyVyKFXUPOl51ZcDV0SCy0vKRgb/LSP4h4MCAAFN42xg==:Ul3VGQuwpgJYbIZHSgsSNA==','gaia','gaia@gaia',0),(175,'pn7SzqEGRmng9kXqz5lkQvzXngS22r0Gl8FCgNH3Ipd7ImVXP5Dt8A2oWqaR53lwiEAKjG1IXdSl6IoUyezm5A==:7AG0i8SzD77Tb+F4s0OfDw==','jkl','jkl@jkl',0);
/*!40000 ALTER TABLE `credentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log` (
                       `user_id` int NOT NULL,
                       `date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       PRIMARY KEY (`user_id`,`date`),
                       CONSTRAINT `log/user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
INSERT INTO `log` VALUES (173,'2021-06-20 10:39:44'),(173,'2021-06-20 10:45:22'),(173,'2021-06-23 09:36:29'),(173,'2021-06-23 09:37:48'),(173,'2021-06-23 09:44:53'),(173,'2021-06-23 09:45:09'),(173,'2021-06-23 14:28:47'),(173,'2021-06-23 14:47:29'),(173,'2021-06-24 09:35:05'),(173,'2021-06-24 09:47:53'),(173,'2021-06-24 09:50:45'),(173,'2021-06-24 09:51:54'),(173,'2021-06-24 09:59:41'),(173,'2021-06-25 10:36:50'),(173,'2021-06-25 10:37:53'),(173,'2021-06-25 10:47:42'),(174,'2021-06-20 10:51:26'),(174,'2021-06-21 07:46:06'),(174,'2021-06-21 07:48:10'),(174,'2021-06-23 09:37:34'),(174,'2021-06-24 10:00:50'),(174,'2021-06-25 10:40:40'),(175,'2021-06-24 10:01:03');
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `options`
--

DROP TABLE IF EXISTS `options`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `options` (
                           `option_id` int NOT NULL AUTO_INCREMENT,
                           `question_id` int NOT NULL,
                           `text` varchar(50) NOT NULL,
                           PRIMARY KEY (`option_id`),
                           KEY `option/question_idx` (`question_id`),
                           CONSTRAINT `option/question` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `options`
--

LOCK TABLES `options` WRITE;
/*!40000 ALTER TABLE `options` DISABLE KEYS */;
/*!40000 ALTER TABLE `options` ENABLE KEYS */;
UNLOCK TABLES;

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
                            PRIMARY KEY (`question_id`),
                            KEY `questionnaire_idx` (`questionnaire_id`),
                            CONSTRAINT `questionnaire/question` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaire` (`q_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=290 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questionnaire`
--

DROP TABLE IF EXISTS `questionnaire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questionnaire` (
                                 `q_id` int NOT NULL AUTO_INCREMENT,
                                 `name` varchar(32) NOT NULL,
                                 `photo` longblob NOT NULL COMMENT 'Photo_path non è unica in quanto si potrebbe riproporre un questionario',
                                 `date` date NOT NULL,
                                 PRIMARY KEY (`q_id`),
                                 UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questionnaire`
--

LOCK TABLES `questionnaire` WRITE;
/*!40000 ALTER TABLE `questionnaire` DISABLE KEYS */;
/*!40000 ALTER TABLE `questionnaire` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `delete_points` BEFORE DELETE ON `questionnaire` FOR EACH ROW BEGIN
    UPDATE `user` SET `points` = `points` - (SELECT count(*) FROM `question` WHERE `question`.`questionnaire_id` = OLD.`q_id`)
        - (SELECT
                   CASE WHEN `age` IS NOT NULL THEN 2 ELSE 0 END +
                   CASE WHEN `sex` IS NOT NULL THEN 2 ELSE 0 END +
                   CASE WHEN `expertise_level` IS NOT NULL THEN 2 ELSE 0 END
           FROM `submission`
           WHERE `questionnaire_id` = OLD.`q_id` and `user`.`user_id` = `submission`.`user_id`)
    WHERE `user_id` in (SELECT `user_id` FROM `submission` WHERE `questionnaire_id` = OLD.`q_id` AND `submitted` = TRUE);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `questionnaire_id` int NOT NULL,
                           `review` varchar(256) NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `reviews/questionnaire_idx` (`questionnaire_id`),
                           CONSTRAINT `reviews/questionnaire` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaire` (`q_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `submission`
--

DROP TABLE IF EXISTS `submission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `submission` (
                              `user_id` int NOT NULL,
                              `questionnaire_id` int NOT NULL,
                              `age` int DEFAULT NULL,
                              `sex` tinyint DEFAULT NULL COMMENT 'male (0) / female (1)',
                              `expertise_level` int DEFAULT NULL,
                              `submitted` tinyint NOT NULL DEFAULT '0',
                              PRIMARY KEY (`user_id`,`questionnaire_id`),
                              KEY `submission/user_idx` (`user_id`),
                              KEY `submission/questionnaire_idx` (`questionnaire_id`),
                              CONSTRAINT `submission/questionnaire` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaire` (`q_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                              CONSTRAINT `submission/user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `submission`
--

LOCK TABLES `submission` WRITE;
/*!40000 ALTER TABLE `submission` DISABLE KEYS */;
/*!40000 ALTER TABLE `submission` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `checks` BEFORE UPDATE ON `submission` FOR EACH ROW BEGIN
    IF(OLD.`submitted` = true) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Cannot update. Already submitted';
    END IF;
    IF(NEW.`submitted` = false) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'You must submit to update';
    END IF;
    IF ((SELECT count(*) FROM `answer` as A JOIN `question` as Q
         WHERE A.`user_id` = NEW.`user_id` AND A.`question_id` = Q.`question_id` AND Q.`questionnaire_id` = NEW.`questionnaire_id`) <>
        (SELECT count(*) FROM `question` WHERE `questionnaire_id` = NEW.`questionnaire_id`)) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'User did not answer all mandatory questions';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `compute_points` AFTER UPDATE ON `submission` FOR EACH ROW BEGIN
    UPDATE `user` SET `points` = `points` +
                                 ((SELECT count(*) FROM `question` WHERE `questionnaire_id` = NEW.`questionnaire_id`) +
                                  IF(NEW.`age` is not NULL, 2, 0) + IF(NEW.`sex` is not NULL, 2, 0) + IF(NEW.`expertise_level` is not NULL, 2, 0))
    WHERE `user_id` = NEW.`user_id`;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

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

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (173,0,'2021-06-20 10:39:41'),(174,0,'2021-06-20 10:51:23'),(175,0,'2021-06-23 09:43:16');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-25 13:33:55
