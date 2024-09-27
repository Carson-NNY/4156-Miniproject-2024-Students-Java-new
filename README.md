# Welcome Students of 4156

This is the GitHub repository for the Individual Project for COMS 4156 Advanced Software Engineering.
The author of this repository is Guanhong Liu. 

## Viewing the Client App Repository
Please use the following link to view the repository relevant to the app: https://github.com/Carson-NNY/4156-Miniproject-2024-Students-Java-new

## Building and Running a Local Instance
In order to build and use our service you must install the following (This guide assumes
Windows but the Maven README has instructions for both Windows and Mac):
*   Maven 4.0.0: https://maven.apache.org/download.cgi Download and follow the installation instructions, be sure to set the bin as described in Maven's README as a new path variable by editing the system variables if you are on windows or by following the instructions for MacOS.
*   JDK 17: This project used JDK 17 for development so that is what we recommend you use: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
*   IntelliJ IDE: We recommend using IntelliJ but you are free to use any other IDE that you are comfortable with: https://www.jetbrains.com/idea/download/?section=windows
*   To successfully run the project, you need to first run mvn spring-boot:run -Dspring-boot.run.arguments="setup" if you are using  MacOS, or mvn spring-boot:run -D"spring-boot.run.arguments=setup" if you are on Windows, and let the project initialize the database until you see "System Setup” showing up in console.
*   If you wish to run the style checker you can with mvn checkstyle:check or mvn checkstyle:checkstyle if you wish to generate the report. The report will be generated under target folder, where you will find in a file named checkstyle-result.xml.
*   If you wish to have a bug finder to check if there are any bugs in the code or style violations not detected by the style checker, you can use pmd check -d /Users/carson/Downloads/CS\ W4156/4156-Miniproject-2024-Students-Java  -R rulesets/java/quickstart.xml -f json, to install pmd, you may checkout this link: https://pmd.github.io/

## Running Tests
* The unit test  are located under the directory 'src/test'. To run the tests , you must first set up the project by following step 5 in the "Building and Running a Local Instance" section.  
* From there, you can right-click any of the classes present in the src/test directory and click run to see the results.  To see the system-level tests, see the section "Postman Test Documentation" below.

## Endpoints testing with Postman
* You can test all the endpoints by going to :https://www.postman.com/ 
* With the url we have already deployed on Google Cloud Platform: https://miniproject-436902.ue.r.appspot.com/, you are free to test all the endpoints we have implemented with corresponding params and methods.

### GET /   /index   or /home
- Expected Input Parameters: N/A