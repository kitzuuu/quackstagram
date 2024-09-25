
-- TRIGGER --
-- Trigger used for like insert used by the application and transaction to verify that user has already liked the picture
DELIMITER $$

USE `quackstagram`$$

CREATE TRIGGER `before_like_insert`
BEFORE INSERT ON `likes_post`
FOR EACH ROW
BEGIN
    DECLARE already_liked INT;
    SELECT COUNT(*) INTO already_liked FROM `likes_post`
    JOIN `record_post_interaction` ON `likes_post`.`like_id` = `record_post_interaction`.`like_id`
    WHERE `record_post_interaction`.`user_id` IN (
        SELECT `user_id` FROM `record_post_interaction` WHERE `like_id` = NEW.like_id
    ) AND `likes_post`.`post_id` = NEW.post_id;

    IF already_liked > 0 THEN
        -- Prevent the insert and signal an error uses code 45000 since it is also signaled by transaction
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transaction failed';
    END IF;
END$$

DELIMITER ;

-- STORED PROCEDURE / TRANSACTION --
-- transaction/stored `likepost` procedure for likes, called by trigger `before_like_insert`
-- reason for transaction use: Frequent use case complex update method to maintain data integrity

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
END



-- FUNCTION RETURN: BOOLEAN--
-- Not implemented in the application since comments were not implemented, checks comment Length before insert
DELIMITER $$

CREATE FUNCTION CheckCommentLength(comment VARCHAR(100))
    RETURNS BOOLEAN
BEGIN
    RETURN CHAR_LENGTH(comment) <= 100;
END$$

DELIMITER;

-- TRIGGER --
-- Not implemented in the application since comments were not implemented
-- Trigger for the the check Comment length  messages signals error if comment length too long
DELIMITER $$

CREATE TRIGGER BeforeNewComment
    BEFORE INSERT ON comments
    FOR EACH ROW
BEGIN
    IF NOT CheckCommentLength(NEW.comment_content) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Error2';
    END IF;
END $$

DELIMITER ;

-- FUNCTION RETURN: JSON --
-- Local parsing of JSON file, more secure, stored procedure since it asks for user data
-- thus prevents SQL injections on application side, better for debugging
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
END


-- Trigger to check if user already follows another user, this is handled application side, however good practice
USE `quackstagram`$$
CREATE
    DEFINER=`root`@`localhost`
    TRIGGER `before_follow_insert`
    BEFORE INSERT ON `followers`
    FOR EACH ROW
BEGIN
    DECLARE existing_follow INT;
    -- Check for an existing follow relationship where the new combination is already present
    SELECT COUNT(*) INTO existing_follow
    FROM `followers`
    WHERE `user_id` = NEW.user_id AND `followed_by` = NEW.followed_by;

    IF existing_follow > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Already following';
    END IF;
    END$$

DELIMITER ;
