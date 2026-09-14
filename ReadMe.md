# 🦆 Quackstagram

An Instagram-style desktop app in Java Swing, backed by a MySQL database designed for a large-scale release and data analytics. Built for the *Databases* course (BCS1510) at Maastricht University, for the fictional client Cheapo Software Solutions (CSS).

## Highlights

- **Normalized relational schema:** 7 tables (users, posts, comments, likes, followers, login data and post interactions), with media stored as binary objects until CSS moves to a web hosting service
- **Analytics views:** daily interactions, above-average engagement, and likely bot accounts (high following-to-follower ratio and no posts)
- **Triggers and a stored function** that protect data integrity, such as validating comment length
- **19 analytics queries** answering business questions like the most-liked posts, mutual followers and the fastest-growing accounts
- **60,000-row test dataset** (generated with Mockaroo) to check query performance and correctness
- **Clean Java client:** refactored with the Factory, Singleton and Command design patterns, with SHA-256 password hashing and error handling
- **Documentation:** design and development reports in [`report/`](report/)

## Tech stack

Java 22 · Swing / SwingX · MySQL · JDBC · Maven

## Getting started

1. Import `src/main/java/database/database.sql` into MySQL.
2. Put your connection details in `connectionDetails.txt`: the JDBC URL, username and password, each on its own line.
3. Open the project with Maven and JDK 22 (preview features enabled), then run `src/main/java/main/Main.java`.

### Analytics queries

Import the data dump `20Queries/quackstagramdd.sql`, then run `20Queries/20QUERIES.sql` against it to check the views and queries.

## Authors

T. C. Nitu · R. D. M. Untesu

## Acknowledgements

- [Mockaroo](https://www.mockaroo.com/) for the generated datasets
- Ashish Sai, for continuous support throughout the project
