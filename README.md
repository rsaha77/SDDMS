Spring boot ref guide: https://spring.io/guides/gs/serving-web-content/    
Spring security ref guide: http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/

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
