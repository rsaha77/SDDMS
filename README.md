Spring boot ref guide: https://spring.io/guides/gs/serving-web-content/    
Spring security ref guide: http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/

# Setup Instructions
* Open git bash
* Download the project by using the following command: <code>git clone git@172.26.147.37:stm/sample.git</code>
* Navigate to the project directory where you cloned the project. Run <code>mvn clean install eclipse:clean eclipse:eclipse</code> to setup project files.
* Import the project in eclipse and check the source code
* We encourage you to explore both the Server setup options below to start & use the sample application

# Starting the application using Spring Boot with embedded server
* Use <code>mvn spring-boot:run</code> to run the application in an embedded server 
* Check http://localhost:8080/sample in browser and you should be able to see the login page of the application

# Starting the application using Tomcat Server in eclipse
* Select Window -> Preferences and type 'server' in type filter text box
* Select Runtime Environment inside Server section -> Add -> Select Apache Tomcat 7 -> Click next -> Under Tomcat installation directory select the directory where you downloaded Tomcat server
 -> Click finish
* The Server runtime environment should now show Apache Tomcat v7.0 as an available server runtime environment
* Select Window -> Show view -> Other -> Type "Server"  -> Select Server
* Open server view -> Click "create a new server" -> Select Tomcat v7.0 server -> Click next -> On "Add and Remove" screen select "sample"-> Click Add -> Click Finish
* Your server view should now have a Tomcat server 
* Right click on the server -> Select "Start"
* After start the server, also access the page at: http://localhost:8080/sample and you should be able to see the login page of the application

# Login credentials
* To login to account with Admin privileges use : User id : admin , Password : admin123
* To login to account with Employee privileges use : User Id : user1 , Password : user123

# Features
The bookstore application maintains book store related information for a book distributor : book maintenance and
employee maintenance. As the purpose of this application is to showcase the technology we use, we will have simple
assumption for this business

* All the books will be available in all the stores
* Each book has exactly 1 publisher, and belongs to 1 series. It can, however, have multiple authors.
* Each employee work in one and only one store.

## Operations supported by Employee role
* View a list of books
* Search the books by their title, author name, publisher and series.

## Operations supported by Admin role
### Book maintenance
* View a list of book and search the books similar to employee role.
* Add new books and assign it to a series.

### Office maintenance
* View a list of offices, each office has a name and a unique id
* Add/remove offices. Office can only be removed if no employees is currently assigned to that office.

### Employee maintenance
* View a list of employees by offices
* CRUD action to employee. CRUD includes all information of employees, including changing password.
* Employee are assigned to office when added, and can be transferred to another office in the future.