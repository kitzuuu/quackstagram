use quackstagramdd; -- use this database, data dumo version, non-functional as does not include LONGBLOB for picture files

-- Question 1, select more than ? followers --
Use quackstagramdd;
SELECT u.user_id, u.User_name, COUNT(f.followed_by) AS follower_count
FROM user u
JOIN followers f ON u.user_id = f.user_id
GROUP BY u.user_id, u.User_name
HAVING COUNT(f.followed_by) > ?;

-- Question 2, Show the total number of posts made by each user. (You will have to decide how this is done, via userid) --
Use quackstagramdd;
SELECT u.user_name, COUNT(p.post_id) AS `Number of posts`
FROM user u
LEFT JOIN post p ON u.user_id = p.user_id
GROUP BY  u.user_name
ORDER BY COUNT(p.post_id) DESC;

-- Question 3, Find all comments made on a particular user’s post. --
Use quackstagramdd;
SELECT c.comment_id, c.comment_content, c.comment_time, p.post_id, u.user_id AS post_user_id, u.User_name AS post_user_name
FROM comments c
JOIN post p ON c.post_id = p.post_id
JOIN user u ON p.user_id = u.user_id
WHERE u.User_name = ? ; -- user name no comments implemented

-- Question 4, Display the top X most liked posts. --
Use quackstagramdd;

SELECT *
FROM post p
         JOIN (
    SELECT lp.post_id, COUNT(lp.post_id) AS like_count
    FROM likes_post lp
    GROUP BY lp.post_id
    ORDER BY COUNT(lp.post_id) DESC
)
    AS `Select Most Liked Posts` ON p.post_id = `Select Most Liked Posts`.post_id
ORDER BY `Select Most Liked Posts`.like_count DESC
LIMIT ?; -- insert top, where ? = int

-- Question 5, Count the number of posts each user has liked. --
Use quackstagramdd;

SELECT u.user_id,  u.user_name, COUNT(like_id) AS `Number_of_likes_from_user`
from record_post_interaction rpi  RIGHT JOIN user u
ON rpi.user_id = u.user_id
GROUP BY u.user_id
ORDER BY `Number_of_likes_from_user` DESC;

-- Question 6, List all users who haven’t made a post yet. --
Use quackstagramdd;

SELECT u.user_id, u.user_name
FROM user u
WHERE u.user_id NOT IN(
SELECT u.user_id
FROM user u join post p
ON u.user_id=p.user_id
);

-- Question 7, List users who follow each other.
Use quackstagramdd;

SELECT a.user_id, a.followed_by
FROM followers a
WHERE a.followed_by IN (
SELECT b.user_id
FROM followers b
WHERE a.user_id = b.followed_by 
);

-- Question 8, Show the user with the highest number of posts. --
Use quackstagramdd;

SELECT get_posts.posts_count, username
FROM(
        SELECT
            COUNT(DISTINCT p.post_id) AS posts_count, u.user_name AS username
        FROM
            user u
                LEFT JOIN
            post p ON u.user_id = p.user_id
        WHERE
            u.user_name = u.user_name
        GROUP BY
            u.user_id
        ORDER BY posts_count DESC
    ) AS get_posts
LIMIT 1; -- GET ONLY THE FIRST ONE SINCE THE count is already sorted desc

-- Question 9, List the top X users with the most followers. --
Use quackstagramdd;

SELECT user_name, Followers_count
FROM (
         SELECT u.User_name, COUNT(f.user_id) AS Followers_Count
         FROM user u JOIN followers f ON u.user_id = f.user_id
         GROUP BY u.user_id, u.User_name
         ORDER BY Followers_Count DESC
     )
         AS Top_FOLLOWED
LIMIT ?; -- INser desired X val

-- Question 10, Find posts that have been liked by all users. --
         use quackstagram;
SELECT p.post_id, p.post_file, GROUP_CONCAT(DISTINCT u.user_name SEPARATOR ' , ') AS usernames -- in case of error 1308, change  memory alloc in your DBMS
FROM post p
         JOIN likes_post lp ON p.post_id = lp.post_id
         JOIN record_post_interaction rpi ON lp.like_id = rpi.like_id
         JOIN user u ON rpi.user_id = u.user_id
GROUP BY p.post_id, p.post_file
HAVING COUNT(DISTINCT rpi.user_id) = (SELECT COUNT(*) FROM user);


-- Question 11, Display the most active user (based on posts, comments 7, and likes). --
Use quackstagramdd;

SELECT u.user_id, (postCount.post_count + countL.like_count) AS `interaction_count`
FROM user u
LEFT JOIN (
SELECT
user_id, COUNT(*) AS post_count
FROM post
GROUP BY user_id
) AS postCount ON u.user_id = postCount.user_id -- Subquery select post count
LEFT JOIN (
SELECT user_id, COUNT(*) AS like_count
FROM record_post_interaction
GROUP BY user_id
) AS countL ON u.user_id = countL.user_id -- count amount of likes by each user
ORDER BY interaction_count DESC
LIMIT 1; -- omit likes as per question specification, not implemented in the application

-- Question 12, Find the average number of likes per post for each user. --
Use quackstagramdd;

use quackstagramdd;
SELECT u.user_name, AVG(lop.`likes of post`)
FROM user u LEFT JOIN
(
SELECT p.user_id, p.post_id, l.`Likes of post`
FROM post p RIGHT JOIN(
SELECT post_id, COUNT(like_Event_id) as `Likes of post`
FROM likes_post
group by post_id -- query to count the number of likes per post
) l
ON l.post_id=p.post_id ) lop -- QUERY GET USER id with post_id and likes of post
ON u.user_id = lop.user_id
GROUP BY u.user_id
ORDER BY AVG(lop.`likes of post`) DESC
;

-- Question 13, Show posts that have more comments than likes. --
Use quackstagramdd;

SELECT DISTINCT comm.post_id, comm.`number_of_comments`, likes.likesPost
FROM likes_post lp JOIN
(SELECT post_id, COUNT(comment_id) AS `number_of_comments`
from comments
GROUP by post_id) AS comm
JOIN
(SELECT post_id, COUNT(like_Event_id) AS likesPost
FROM likes_post
GROUP BY post_id) as likes
WHERE comm.`number_of_comments`>likes.likesPost;

-- Use this query to put comments, use for previous comments also --
INSERT INTO comments (comment_id, post_id, user_id, comment_time, comment_content, date)
VALUES
    (DEFAULT, 1, 18, DEFAULT, 'hello', DEFAULT),
    (DEFAULT, 1, 19, DEFAULT, 'thisprojecttookmealot', DEFAULT),
    (DEFAULT, 1, 21, DEFAULT, 'ofdays', DEFAULT);
    
-- hypothetical insert query not implemented due to no comments implementation

-- Question 14, List the users who have liked every post of a specific user. --
use quackstagram;
SELECT DISTINCT u.user_name AS `person that liked`
FROM user u JOIN  record_post_interaction rpi JOIN likes_post lp
WHERE
rpi.like_id = lp.like_id
AND u.user_id=rpi.user_id
AND lp.post_id IN(
SELECT idu.post_id
FROM
(SELECT p.post_id, u.user_name
FROM post P JOIN user u
WHERE p.user_id=u.user_id) idu -- postid username query
WHERE idu.user_name = ? -- select post of specific user name
);

-- Question 15, Display the most popular post of each user (based on likes). --

use quackstagramdd;
SELECT u.user_name, p.post_id, p.post_file, p.post_bio, nlp.`Number of likes`
FROM user u
         JOIN (
    SELECT lp.post_id, COUNT(lp.like_event_id) AS `Number of likes`
    FROM likes_post lp
    GROUP BY lp.post_id
) nlp ON nlp.post_id = (
    SELECT p.post_id
    FROM post p
             JOIN likes_post lp ON p.post_id = lp.post_id
    WHERE p.user_id = u.user_id
    GROUP BY p.post_id
    ORDER BY COUNT(lp.like_event_id) DESC
    LIMIT 1
)
         JOIN post p ON p.post_id = nlp.post_id
ORDER BY nlp.`Number of likes` DESC; -- must have at least 2 posts

-- Question 16, Find the user(s) with the highest ratio of followers to following. --
Use quackstagramdd;

SELECT
    u.user_id,
    u.User_name,
    IFNULL(follower_counts.total_followers, 0) AS followers,
    IFNULL(following_counts.total_following, 0) AS following,
    IF(following_counts.total_following = 0, 'Infinity', -- Handle division by zero
       follower_counts.total_followers / following_counts.total_following) AS ratio
FROM
`user` u
LEFT JOIN (
SELECT
f.user_id,
COUNT(*) AS total_followers
FROM
`followers` f
GROUP BY
f.user_id
) follower_counts ON u.user_id = follower_counts.user_id -- Subquery to count how many followers each user has
LEFT JOIN (
SELECT
f.followed_by,
COUNT(*) AS total_following
FROM
`followers` f
GROUP BY
f.followed_by
) following_counts ON u.user_id = following_counts.followed_by     -- Subquery to count how many users each user is following
ORDER BY
    ratio DESC
LIMIT ?; -- 'Highest insert here an INT'

-- Question 17, Show the month with the highest number of posts made. --
use quackstagramdd;
SELECT
YEAR(DATE_OF_UPLOAD) AS Year,
MONTH(DATE_OF_UPLOAD) AS Month,
COUNT(*) AS NumberOfPosts
FROM
post
GROUP BY
YEAR(DATE_OF_UPLOAD), MONTH(DATE_OF_UPLOAD)
ORDER BY
NumberOfPosts DESC
LIMIT 1;

-- Question 18, Identify users who have not interacted with a specific user’s posts. --
Use quackstagramdd;

SELECT u.user_name AS `person that hasn't interacted`
FROM user u
WHERE NOT EXISTS (
SELECT 1
FROM post p
JOIN likes_post lp ON p.post_id = lp.post_id
JOIN record_post_interaction rpi ON lp.like_id = rpi.like_id
WHERE p.user_id = (
SELECT user_id
FROM user
WHERE user_name = ?
) AND rpi.user_id = u.user_id
)

-- Question 19, Display the user with the greatest increase in followers in the last X days. --
-- idk


-- Question 20, Find users who are followed by more than X% of the platform users. --
Use quackstagramdd;

SET @X = ?; -- for this dataset maximum is 23%, FORMAT @X = ?, where ? = INT

-- Calculate the total number of users on the platform
SET @total_users = (SELECT COUNT(*) FROM `user`);

-- Calculate the threshold number of followers
SET @threshold = @total_users * (@X / 100);

SELECT u.user_name, COUNT(f.followed_by) AS followers_count
FROM `user` u
JOIN `followers` f ON u.user_id = f.user_id
GROUP BY u.user_id
HAVING COUNT(f.followed_by) > @threshold;


