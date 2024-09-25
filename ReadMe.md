# BCS1510 - QuackStagram Database Integration 

26.05.2024

### PURPOSE OF PROJECT
Integrating a database for Cheapo Software Solutions (CSS) for their new social media app quackstagram to aid in a large scale release of the applicatioon and data analytics.
### Key Features
- **Normalized Database:** This package features a fully functional implementation of a normalized database for CSS' software that supports media storage and binary objects until CSS' migrates to a webs hosting service! 
-    **19 Prepared queries:** 19 prepared queries for CSS to analyze their users' behaviours and statistics
-   **Views triggers and stored procedures:** Views that aid in data analytics such as user behaviour on given days and bot account detection views. Triggers and stored procedures to enhance user data integrity
-   **Password Hashing and Error Handling:** Password hashing and error handling have been implemented as a security measure for CSS adhering to industry standards
- **Datadump file generated by LLM using dataset**: A 60,000 tuple dataset for CSS is provided to check the performance and accuracy of the database.
- **Documentation**: Documentation can be found in the report directory detailing the development process of the database and its' features
### HOW TO START THIS PROJECT
1. **Import the database:** Import the database located in the "src-->main-->java-->database-->database.sql" into your DBMS
2. **Set Connection Details:** Set connection details via ConnectionDetails.txt, insert the specified details
3. **Open the src Directory:** Navigate to the "src" directory where the Java source code is located (run with maven and JAVA sdk. 22 or further)
4. **Go to the main class:** Navigate to "src-->main-->java-->Main.java", run the main function.

#### FOR CSS DATA ANALYTICS
1. **Import the dump database:** Import the dump database `quackstagramdd.sql` found in the 20Queries Directory
2. **Test the queries using `quackstagramdd`:** Use the 20QUERIES.SQL file on quackstagramdd.sql to verify views and the 20 queries


### AUTHORS
- R.D.M Untesu (i6357705)
- Nitu Cristian (i6350367)


### Acknowledgements
- https://www.mockaroo.com/ for the datasets used in the dumps
- We would like to thank Ashish for continuous support with the project throughout the development process
