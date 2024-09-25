CREATE DATABASE  IF NOT EXISTS `quackstagram` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `quackstagram`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: quackstagram
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary view structure for view `above_average_following_count`
--

DROP TABLE IF EXISTS `above_average_following_count`;
/*!50001 DROP VIEW IF EXISTS `above_average_following_count`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `above_average_following_count` AS SELECT 
 1 AS `user_id`,
 1 AS `username`,
 1 AS `following_count`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `comment_id` int NOT NULL AUTO_INCREMENT,
  `post_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `comment_time` time DEFAULT NULL,
  `comment_content` varchar(100) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`comment_id`),
  KEY `fk_post_id` (`post_id`),
  KEY `fk_user_id` (`user_id`),
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `BeforeNewComment` BEFORE INSERT ON `comments` FOR EACH ROW BEGIN
    IF NOT CheckCommentLength(NEW.comment_content) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error2';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `followers`
--

DROP TABLE IF EXISTS `followers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `followers` (
  `action_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `followed_by` int DEFAULT NULL,
  `Date_followed` date DEFAULT NULL,
  `Time_followed` time DEFAULT NULL,
  PRIMARY KEY (`action_id`),
  KEY `user_id` (`user_id`),
  KEY `followed_by` (`followed_by`),
  KEY `Key` (`Date_followed`,`Time_followed`),
  CONSTRAINT `followers_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `followers_ibfk_2` FOREIGN KEY (`followed_by`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_follow_insert` BEFORE INSERT ON `followers` FOR EACH ROW BEGIN
    DECLARE existing_follow INT;
    -- Check for an existing follow relationship where the new combination is already present
    SELECT COUNT(*) INTO existing_follow
    FROM `quackstagram`.`followers`
    WHERE `user_id` = NEW.user_id AND `followed_by` = NEW.followed_by;

    IF existing_follow > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Already following';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `likes_post`
--

DROP TABLE IF EXISTS `likes_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `likes_post` (
  `like_event_id` int NOT NULL AUTO_INCREMENT,
  `like_id` int DEFAULT NULL,
  `post_id` int DEFAULT NULL,
  PRIMARY KEY (`like_event_id`),
  KEY `like_id` (`like_id`),
  KEY `post_id` (`post_id`),
  CONSTRAINT `likes_post_ibfk_1` FOREIGN KEY (`like_id`) REFERENCES `record_post_interaction` (`like_id`) ON DELETE CASCADE,
  CONSTRAINT `likes_post_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `post` (`post_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `before_like_insert` BEFORE INSERT ON `likes_post` FOR EACH ROW BEGIN
    -- Check if this user has already liked this post by joining with record_post_interaction
    DECLARE already_liked INT;
    SELECT COUNT(*) INTO already_liked FROM `likes_post`
                                                JOIN `record_post_interaction` ON `likes_post`.`like_id` = `record_post_interaction`.`like_id`
    WHERE `record_post_interaction`.`user_id` IN (
        SELECT `user_id` FROM `record_post_interaction` WHERE `like_id` = NEW.like_id
    ) AND `likes_post`.`post_id` = NEW.post_id;

    IF already_liked > 0 THEN
        -- Prevent the insert and signal an error
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User has already liked this post';
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `login_data`
--

DROP TABLE IF EXISTS `login_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login_data` (
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `possible_bots`
--

DROP TABLE IF EXISTS `possible_bots`;
/*!50001 DROP VIEW IF EXISTS `possible_bots`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `possible_bots` AS SELECT 
 1 AS `user_id`,
 1 AS `username`,
 1 AS `follower_count`,
 1 AS `following_count`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `post` (
  `post_id` int NOT NULL AUTO_INCREMENT,
  `post_file` longblob,
  `post_bio` varchar(30) DEFAULT NULL,
  `DATE_OF_UPLOAD` date DEFAULT NULL,
  `TIME_OF_UPLOAD` time DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  KEY `user_id` (`user_id`),
  KEY `post_file_index` (`post_file`(255)),
  KEY `Key` (`post_bio`,`DATE_OF_UPLOAD`,`TIME_OF_UPLOAD`),
  KEY `idx_post_id` (`post_id`),
  CONSTRAINT `post_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `record_post_interaction`
--

DROP TABLE IF EXISTS `record_post_interaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `record_post_interaction` (
  `like_id` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `time` time DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`like_id`),
  KEY `user_id` (`user_id`),
  KEY `Key` (`date`,`time`),
  CONSTRAINT `record_post_interaction_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `User_name` varchar(30) DEFAULT NULL,
  `bio` varchar(50) DEFAULT NULL,
  `profile_picture` longblob,
  PRIMARY KEY (`user_id`),
  KEY `Key` (`User_name`,`bio`),
  KEY `profile_picture_index` (`profile_picture`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `view_above_average_interactions`
--

DROP TABLE IF EXISTS `view_above_average_interactions`;
/*!50001 DROP VIEW IF EXISTS `view_above_average_interactions`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_above_average_interactions` AS SELECT 
 1 AS `action_date`,
 1 AS `total_likes`,
 1 AS `total_posts`,
 1 AS `interactions`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_daily_interactions`
--

DROP TABLE IF EXISTS `view_daily_interactions`;
/*!50001 DROP VIEW IF EXISTS `view_daily_interactions`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_daily_interactions` AS SELECT 
 1 AS `action_date`,
 1 AS `total_likes`,
 1 AS `total_posts`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping events for database 'quackstagram'
--

--
-- Dumping routines for database 'quackstagram'
--
/*!50003 DROP FUNCTION IF EXISTS `CheckCommentLength` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `CheckCommentLength`(comment VARCHAR(100)) RETURNS tinyint(1)
    DETERMINISTIC
BEGIN
    RETURN CHAR_LENGTH(comment) <= 100;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `GetUserProfile` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `GetUserProfile`(username_param VARCHAR(255)) RETURNS json
    DETERMINISTIC
BEGIN
    DECLARE username VARCHAR(30);
    DECLARE bio VARCHAR(50);
    DECLARE user_id INT;
    DECLARE followers_count INT;
    DECLARE posts_count INT;
    DECLARE following_count INT;

    SELECT
        u.user_name, u.bio, u.user_id,
        COUNT(DISTINCT f.followed_by) AS followers_count,
        COUNT(DISTINCT p.post_id) AS posts_count,
        (SELECT COUNT(DISTINCT followers.user_id)
         FROM followers
         WHERE followers.followed_by = u.user_id) AS following_count
    INTO
        username, bio, user_id, followers_count, posts_count, following_count
    FROM
        user u
            LEFT JOIN
        followers f ON u.user_id = f.user_id
            LEFT JOIN
        post p ON u.user_id = p.user_id
    WHERE
        u.user_name = username_param
    GROUP BY
        u.user_id;

    RETURN JSON_OBJECT(
            'username', username,
            'bio', bio,
            'user_id', user_id,
            'followers_count', followers_count,
            'posts_count', posts_count,
            'following_count', following_count
           );
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `likePost` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `likePost`(
    IN postID INT,
    IN userID INT,
    IN actionDate DATE,
    IN actionTime TIME
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        BEGIN
            ROLLBACK;
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transaction failed';
        END;

    START TRANSACTION;

    INSERT INTO record_post_interaction(date, time, user_id)
    VALUES (actionDate, actionTime, userID);

    SET @last_like_id = LAST_INSERT_ID();

    INSERT INTO likes_post(like_id, post_id)
    VALUES (@last_like_id, postID);

    COMMIT;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `above_average_following_count`
--

/*!50001 DROP VIEW IF EXISTS `above_average_following_count`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `above_average_following_count` AS select `u`.`user_id` AS `user_id`,`u`.`User_name` AS `username`,count(`f`.`followed_by`) AS `following_count` from (`user` `u` left join `followers` `f` on((`u`.`user_id` = `f`.`user_id`))) group by `u`.`user_id`,`u`.`User_name` having (count(`f`.`followed_by`) > (select avg(`avg_following`.`following_count`) from (select count(`followers`.`followed_by`) AS `following_count` from `followers` group by `followers`.`user_id`) `avg_following`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `possible_bots`
--

/*!50001 DROP VIEW IF EXISTS `possible_bots`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `possible_bots` AS select `u`.`user_id` AS `user_id`,`u`.`User_name` AS `username`,count(distinct `f`.`followed_by`) AS `follower_count`,count(distinct `f2`.`user_id`) AS `following_count` from (((`user` `u` left join `followers` `f` on((`u`.`user_id` = `f`.`user_id`))) left join `followers` `f2` on((`u`.`user_id` = `f2`.`followed_by`))) left join `post` `p` on((`u`.`user_id` = `p`.`user_id`))) group by `u`.`user_id`,`u`.`User_name` having (((count(distinct `f2`.`user_id`) / count(distinct `f`.`followed_by`)) > 7) and (count(`p`.`post_id`) = 0)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_above_average_interactions`
--

/*!50001 DROP VIEW IF EXISTS `view_above_average_interactions`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_above_average_interactions` AS select `view_daily_interactions`.`action_date` AS `action_date`,`view_daily_interactions`.`total_likes` AS `total_likes`,`view_daily_interactions`.`total_posts` AS `total_posts`,((`view_daily_interactions`.`total_posts` + (0.5 * `view_daily_interactions`.`total_likes`)) / 2) AS `interactions` from `view_daily_interactions` where (((`view_daily_interactions`.`total_posts` + (0.5 * `view_daily_interactions`.`total_likes`)) / 2) > (select avg(((`view_daily_interactions`.`total_posts` + (0.5 * `view_daily_interactions`.`total_likes`)) / 2)) AS `avg_interactions` from `view_daily_interactions`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_daily_interactions`
--

/*!50001 DROP VIEW IF EXISTS `view_daily_interactions`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_daily_interactions` AS select `rpi`.`date` AS `action_date`,count(distinct `lp`.`like_event_id`) AS `total_likes`,count(distinct `p`.`post_id`) AS `total_posts` from ((`record_post_interaction` `rpi` left join `likes_post` `lp` on((`rpi`.`like_id` = `lp`.`like_id`))) left join `post` `p` on((`rpi`.`user_id` = `p`.`user_id`))) group by `rpi`.`date` order by `rpi`.`date` desc */;
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

-- Dump completed on 2024-05-29  2:46:54
