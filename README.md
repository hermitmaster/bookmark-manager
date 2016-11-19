# Quick Access Bookmark

### Installation

Quick Access Bookmark requires Tomcat 8, Java 8, and MySQL 5+ to run.


Install the dependencies and devDependencies and start the server.
* Check out the project
* Open mysql on your host and run init_schema.sql
* Edit application.yaml in the project to point to your database
* Build the app with the Maven command mvn clean package
* Add the resultant .war file to the webapps directory on your Tomcat server
* Navigate to $TOMCAT_HOME/bin and run the command./startup.sh to start the server
