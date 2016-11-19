# Quick Access Bookmark

## Installation
Quick Access Bookmark requires Tomcat 8, Java 8, and MySQL 5.x to run.
The following instructions assume that:
* You have Tomcat 8 installed on your host/server
* You have Java 8 installed on your host/server
* You have MySQL 5.x running on your host/server
* You have a MySQL user with sufficient priveleges to create a new database

### Instructions
* Clone the project
* Edit application.yaml in the project to add your MySQL username and password
* Build the app with the Maven command `mvn package`
* Add `$PROJECT_PATH/target/quickAccessBookmark.war` to the `$TOMCAT_HOME/webapps` directory on your Tomcat server
* Navigate to $TOMCAT_HOME/bin and run the command `./startup.sh` to start the server
* At first run the application will create and initialize the database.
* The default username is `admin` and the default password is `password`

These instructions may not work for your particular environment. This is simply a basic installation guideline.
It is possible to create the database yourself in advance and create a MySQL user with restricted priveleges for
security purposes (recommended!). You'll need to be familiar with JDBC connection strings and MySQL to perform this
type of (advanced) installation.
