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
                          CONSTRAINT `answer/question` FOREIGN KEY (`question_id`) REFERENCES `question` (`question_id`) ON UPDATE CASCADE,
                          CONSTRAINT `answer/user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=134 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `check_bad_words` BEFORE INSERT ON `answer` FOR EACH ROW BEGIN
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
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `delete_points` AFTER DELETE ON `answer` FOR EACH ROW BEGIN
    update `user` set `points` = `points` - 1
    where `user`.`user_id` = OLD.`user_id`;
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
) ENGINE=InnoDB AUTO_INCREMENT=179 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credentials`
--

LOCK TABLES `credentials` WRITE;
/*!40000 ALTER TABLE `credentials` DISABLE KEYS */;
INSERT INTO `credentials` VALUES (176,'4Bn//AdaSHNaWUmf2pPA0mMy6hia/8ieG1VF7fWd0R3ebIsa02AplQRdW8zVXk9dwAGITR+E2ROytZhrn7MmDQ==:nCbQGVZ7BlJoRFbbXd6JMA==','tom','tom@tom',1),(177,'geo6mjWMO1FQA0qIOlMRdlqvQNuJUEFxTUI4JM4ktd///kiQs88R5SEhYRyRBmCgAHswPyhgYD5Qa+Z0bE3Tvg==:1fmtZe0VCO2PyTKJvReQOA==','tom1','tom1@tom1',0),(178,'cr8WlzdjqdUsQvqnw50+Bn2VKekuBn34qarvCu/qocTCc4IrufaPa7MCuDTH7OJ/1drzZQw+7CX60R+Bq3ntew==:KY3wNGbkIQLqbm5huP0RkQ==','tomtom','tomtom@tomtom',0);
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
INSERT INTO `log` VALUES (176,'2021-07-12 10:35:45'),(176,'2021-07-12 10:36:38'),(176,'2021-07-12 10:38:09'),(176,'2021-07-12 10:38:48'),(176,'2021-07-12 10:40:48'),(176,'2021-07-12 10:43:33'),(176,'2021-07-12 11:03:17'),(176,'2021-07-12 11:05:13'),(176,'2021-07-12 11:21:40'),(176,'2021-07-12 11:23:55'),(176,'2021-07-12 11:27:02'),(177,'2021-07-12 10:37:14'),(177,'2021-07-12 10:38:34'),(177,'2021-07-12 10:39:20'),(177,'2021-07-12 10:42:48'),(178,'2021-07-12 10:39:34'),(178,'2021-07-12 10:41:13'),(178,'2021-07-12 10:57:26'),(178,'2021-07-12 11:03:39'),(178,'2021-07-12 11:05:33'),(178,'2021-07-12 11:17:30'),(178,'2021-07-12 11:22:02'),(178,'2021-07-12 11:24:15'),(178,'2021-07-12 11:27:21');
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
                            CONSTRAINT `questionnaire/question` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaire` (`q_id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=298 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `delete_answers` BEFORE DELETE ON `question` FOR EACH ROW BEGIN
    DELETE from `answer` WHERE `answer`.`question_id` = OLD.`question_id`;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

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
) ENGINE=InnoDB AUTO_INCREMENT=222 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `delete_questions_submissions` BEFORE DELETE ON `questionnaire` FOR EACH ROW BEGIN
    DELETE from `question` where `question`.`questionnaire_id` = OLD.`q_id`;
    DELETE from `submission` where `submission`.`questionnaire_id` = OLD.`q_id`;
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
                              CONSTRAINT `submission/questionnaire` FOREIGN KEY (`questionnaire_id`) REFERENCES `questionnaire` (`q_id`) ON UPDATE CASCADE,
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
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `check_valid_submission` BEFORE UPDATE ON `submission` FOR EACH ROW BEGIN
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
INSERT INTO `user` VALUES (176,0,'2021-07-12 10:35:08'),(177,0,'2021-07-12 10:37:12'),(178,0,'2021-07-12 10:39:32');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `user_submission_points`
--

DROP TABLE IF EXISTS `user_submission_points`;
/*!50001 DROP VIEW IF EXISTS `user_submission_points`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `user_submission_points` AS SELECT
                                                     1 AS `username`,
                                                     1 AS `questionnaire_id`,
                                                     1 AS `age`,
                                                     1 AS `sex`,
                                                     1 AS `expertise_level`,
                                                     1 AS `NumberOfQuestions`,
                                                     1 AS `points`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `user_submission_points`
--

/*!50001 DROP VIEW IF EXISTS `user_submission_points`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
    /*!50013 DEFINER=`root`@`%` SQL SECURITY DEFINER */
    /*!50001 VIEW `user_submission_points` AS select `credentials`.`username` AS `username`,`submission`.`questionnaire_id` AS `questionnaire_id`,`submission`.`age` AS `age`,`submission`.`sex` AS `sex`,`submission`.`expertise_level` AS `expertise_level`,count(0) AS `NumberOfQuestions`,`user`.`points` AS `points` from (((`credentials` join `user` on((`credentials`.`user_id` = `user`.`user_id`))) join `submission` on((`credentials`.`user_id` = `submission`.`user_id`))) join `question` `q` on((`submission`.`questionnaire_id` = `q`.`questionnaire_id`))) where (`submission`.`submitted` = true) group by `credentials`.`username`,`submission`.`questionnaire_id`,`submission`.`age`,`submission`.`sex`,`submission`.`expertise_level` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-07-12 11:31:57
