-- Above average following count
CREATE VIEW above_average_following_count AS
SELECT
    u.user_id,
    u.User_name AS username,
    COUNT(f.followed_by) AS following_count
FROM
    user u
        LEFT JOIN
    followers f ON u.user_id = f.user_id
GROUP BY
    u.user_id, u.User_name
HAVING
    COUNT(f.followed_by) > (
        SELECT
            AVG(following_count)
        FROM (
                 SELECT
                     COUNT(followed_by) AS following_count
                 FROM
                     followers
                 GROUP BY
                     user_id
             ) AS avg_following
    );

-- view to detect everyday interactions on the platform grouped by date, likes and posts per day
CREATE VIEW View_Daily_interactions AS
SELECT
    rpi.date AS action_date,
    COUNT(DISTINCT lp.like_event_id) AS total_likes,
    COUNT(DISTINCT p.post_id) AS total_posts
FROM
    record_post_interaction rpi
        LEFT JOIN
    likes_post lp ON rpi.like_id = lp.like_id
        LEFT JOIN
    post p ON rpi.user_id = p.user_id
GROUP BY
    rpi.date
ORDER BY rpi.date DESC;

-- View that uses View_Daily_interactions and selects above average days with a formula of interactions = total posts+ (0.5 likes count) of that day
-- Gave 0.5 for likes since they have less interaction weight than posts
CREATE VIEW View_Above_Average_Interactions AS
SELECT
    action_date,
    total_likes,
    total_posts,
    ((total_posts + (0.5 * total_likes)) / 2) AS interactions -- give likes a 1/2 weight since they are less important than posts made
FROM
    View_Daily_interactions
WHERE
    ((total_posts + (0.5 * total_likes)) / 2) > (
        SELECT
            AVG((total_posts + (0.5 * total_likes)) / 2) AS avg_interactions
        FROM
            View_Daily_interactions
    );

-- View to detect possible bots on the platform
CREATE VIEW possible_bots AS -- bots usualy have high following to follower ratio and 0 posts
SELECT
    u.user_id AS user_id,
    u.User_name AS username,
    COUNT(DISTINCT f.followed_by) AS follower_count,
    COUNT(DISTINCT f2.user_id) AS following_count
FROM
    (user u
        LEFT JOIN followers f ON (u.user_id = f.user_id)
        LEFT JOIN followers f2 ON (u.user_id = f2.followed_by)
        LEFT JOIN post p ON (u.user_id =p.user_id))
GROUP BY
    u.user_id, u.User_name
HAVING
    (COUNT(DISTINCT f2.user_id) / COUNT(DISTINCT f.followed_by) > 7) -- formula can be changed ?
   AND (COUNT(p.post_id) = 0); -- AND clause can be removed





