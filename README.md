# Introduction

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

## Running Tests
* The unit test  are located under the directory 'src/test'. To run the tests , you must first set up the project by following step 5 in the "Building and Running a Local Instance" section.  
* From there, you can right-click any of the classes present in the src/test directory and click run to see the results.  To see the system-level tests, see the section "Postman Test Documentation" below.

## Endpoints testing with Postman
* You can test all the endpoints by going to :https://www.postman.com/ 
* With the url we have already deployed on Google Cloud Platform: https://miniproject-436902.ue.r.appspot.com/, you are free to test all the endpoints we have implemented with corresponding params and methods.

### GET /   /index   or /home
- Expected Input Parameters: N/A
- Expected Output: Introduction to the service and provided Postman link
- Upon Success: The string returned: Welcome, in order to make an API call direct your browser or Postman to an endpoint This can be done using the following format:  http:127.0.0.1:8080/endpoint?arg=value
- Upon Failure: N/A

### GET /retrieveDept
- Expected Input Parameters: deptCode(String)
- Expected Output: A JSON object containing the following fields:
  - departmentChair (String): The name of the department chair
  - deptCode (String): The department code
  - numberOfMajors (int): The number of majors in the department
  - courseSelection (list):
    - enrolledStudentCount (int): The number of students enrolled in the course
    - courseLocation (String): The location of the course
    - instructorName (String): The name of the instructor
    - courseTimeSlot (String): The time slot of the course
    - courseFull (boolean): Whether the course is full
    - capacity (int): The capacity of the course
- Searches within on the database for the specified department code.
- Upon Success: HTTP 200 Status Code is returned along with the department contents in a JSON structure.
- Upon Failure: 
  - HTTP 404 Status Code with "Department Not Found." If the specified document does not exist.
  - HTTP 500 Status Code if an error occurs on the Server side.

### GET /retrieveCourse
- Expected Input Parameters:
    - deptCode (String): The department code.
    - courseCode (String): The course code.
- Expected Output: A JSON object containing the following fields:
    - enrolledStudentCount (int): The number of students enrolled in the course.
    - courseLocation (String): The location of the course.
    - instructorName (String): The name of the instructor teaching the course.
    - courseTimeSlot (String): The time slot when the course.
    - courseFull (boolean): Indicates whether the course has reached its capacity.
    - capacity (int): The total capacity of the course.
- This endpoint searches the database for a course within a specific department based on the provided deptCode and courseCode. It returns the information about the course if found.
- Upon Success: An HTTP 200 Status Code is returned, along with the course details in a JSON structure.
- Upon Failure:
    - HTTP 404 Status Code with "Department Not Found" if the specified department code does not exist.
    - HTTP 404 Status Code with "Course Not Found" if the course not found
    - HTTP 500 Status Code if an error occurs on the server side.

### GET /retrieveCourses
- Expected Input Parameters:
    - courseCode (String): The course code.
- Expected Output: A JSON object containing the following:
    - courseCode (String): The course code.
    - A list of string representations of courses from all departments with the following format for each course:
        - "Instructor: ...; Location: ...; Time: ..."
- This endpoint displays the string representation of the requested courses from all departments or displays the appropriate error message in response to the request.
- Upon Success: An HTTP 200 Status Code is returned, along with a JSON object containing the course details from all departments.
- Upon Failure:
    - HTTP 404 Status Code with "Courses Not Found" if no course is found.
    - HTTP 500 Status Code if an error occurs on the server side.

### GET /isCourseFull
- Expected Input Parameters:
    - deptCode (String): The department code.
    - courseCode (String): The course code.
- Expected Output: A JSON object containing a boolean value indicating whether the course has reached its enrollment capacity.
- This endpoint checks if the course has at least reached its enrollment capacity in the specified department based on the provided deptCode and courseCode.
- Upon Success: An HTTP 200 Status Code is returned, along with a boolean value (`true` if the course is full, `false` otherwise).
- Upon Failure:
    - HTTP 404 Status Code with "Course Not Found" if the course does not exist in the specified department.
    - HTTP 500 Status Code if an error occurs on the server side.

### GET /getMajorCountFromDept
- Expected Input Parameters:
    - deptCode (String): The department code.
- Expected Output: A JSON object containing the following:
    - numberOfMajors (int): The number of majors in the specified department.
- This endpoint retrieves the number of majors in the specified department based on the provided deptCode.
- Upon Success: An HTTP 200 Status Code is returned, along with the number of majors in a JSON structure.
- Upon Failure:
    - HTTP 404 Status Code with "Department Not Found" if the specified department code does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

### GET /idDeptChair
- Expected Input Parameters:
    - deptCode (String): The department code.
- Expected Output: A JSON object containing the following:
    - departmentChair (String): The name of the department chair for the specified department.
- This endpoint displays the department chair for the specified department based on the provided deptCode.
- Upon Success: An HTTP 200 Status Code is returned, along with the department chair of the specified department in a JSON structure.
- Upon Failure:
    - HTTP 404 Status Code with "Department Not Found" if the specified department code does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

### GET /findCourseLocation
- Expected Input Parameters:
    - deptCode (String): The department code.
    - courseCode (String): The course code.
- Expected Output: A JSON object containing the following:
    - location (String): The location of the specified course.
- This endpoint displays the location of the specified course based on the provided deptCode and courseCode.
- Upon Success: An HTTP 200 Status Code is returned, along with the location of the course in a JSON structure.
- Upon Failure:
    - HTTP 404 Status Code with "Course Not Found" if the specified course does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

### GET /findCourseInstructor
- Expected Input Parameters:
    - deptCode (String): The department code.
    - courseCode (String): The course code.
- Expected Output: A JSON object containing the following:
    - instructor (String): The name of the instructor for the specified course.
- This endpoint displays the instructor of the specified course based on the provided deptCode and courseCode.
- Upon Success: An HTTP 200 Status Code is returned, along with the course instructor in a JSON structure.
- Upon Failure:
    - HTTP 404 Status Code with "Course Not Found" if the specified course does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

### GET /findCourseTime
- Expected Input Parameters:
    - deptCode (String): The department code.
    - courseCode (String): The course code.
- Expected Output: A JSON object containing the following:
    - courseTime (String): The time slot when the specified course meets.
- This endpoint displays the time the specified course meets at, based on the provided deptCode and courseCode.
- Upon Success: An HTTP 200 Status Code is returned, along with the course timeslot in a JSON structure.
- Upon Failure:
    - HTTP 404 Status Code with "Course Not Found" if the specified course does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

### PATCH /addMajorToDept
- Expected Input Parameters:
    - deptCode (String): The department code.
- Expected Output: A JSON object containing the updated department details:
    - departmentChair (String): The name of the department chair.
    - deptCode (String): The department code.
    - numberOfMajors (int): The updated number of majors in the department.
    - courseSelection (list): A list of courses with the following fields for each course:
        - enrolledStudentCount (int): The number of students enrolled in the course.
        - courseLocation (String): The location of the course.
        - instructorName (String): The name of the instructor teaching the course.
        - courseTimeSlot (String): The time slot for the course.
        - courseFull (boolean): Indicates whether the course has reached its capacity.
        - capacity (int): The total capacity of the course.
- This endpoint attempts to add a student to the specified department. 
- Upon Success: An HTTP 200 Status Code is returned, along with the updated department details in a JSON structure.
- Upon Failure:
    - HTTP 404 Status Code with "Department Not Found" if the specified department code does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

### PATCH /removeMajorFromDept
- Expected Input Parameters:
    - deptCode (String): The department code.
- Expected Output: A JSON structure containing the updated department details:
    - departmentChair (String): The name of the department chair.
    - deptCode (String): The department code.
    - numberOfMajors (int): The updated number of majors in the department after removing a student.
    - courseSelection (list): A list of courses with the following fields for each course:
        - enrolledStudentCount (int): The number of students enrolled in the course.
        - courseLocation (String): The location of the course.
        - instructorName (String): The name of the instructor teaching the course.
        - courseTimeSlot (String): The time slot for the course.
        - courseFull (boolean): Indicates whether the course has reached its capacity.
        - capacity (int): The total capacity of the course.
- This endpoint attempts to remove a student from the specified department. It updates the number of majors in the department and returns the updated department details.
- Upon Success: An HTTP 200 Status Code is returned, along with the updated department details in a JSON structure.
- Upon Failure:
    - HTTP 404 Status Code with "Department Not Found" if the specified department code does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

### PATCH /dropStudentFromCourse
- Expected Input Parameters:
    - deptCode (String): The department code.
    - courseCode (String): The course code within the department.
- Expected Output: A JSON structure containing the following:
    - message (String): A message indicating whether the student was successfully dropped from the course.
- This endpoint attempts to drop a student from the specified course within the given department.
- Upon Success: An HTTP 200 Status Code is returned with a message "Student has been dropped" in a JSON structure if the operation is successful.
- Upon Failure:
    - HTTP 400 Status Code with "Student has not been dropped" if the operation fails.
    - HTTP 404 Status Code with "Course Not Found" if the specified course does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

### PATCH /setEnrollmentCount
- Expected Input Parameters:
    - deptCode (String): The department code.
    - courseCode (String): The course code within the department.
    - count (int): The new enrollment count for the specified course.
- Expected Output: A JSON structure or message indicating whether the enrollment count was successfully updated.
- This endpoint updates the enrollment count for a specified course within a department. The course is identified by the provided deptCode and courseCode, and the count is updated to the provided value.
- Upon Success: An HTTP 200 Status Code is returned with the message "Attributed was updated successfully." in a JSON structure.
- Upon Failure:
    - HTTP 400 Status Code with "Invalid count" if the provided enrollment count is less than 0.
    - HTTP 404 Status Code with "Course Not Found" if the specified course does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

### PATCH /enrollStudentInCourse
- Expected Input Parameters:
    - deptCode (String): The department code.
    - courseCode (String): The course code for enrolling the student.
- Expected Output: A JSON structure containing the following:
    - message (String): A message indicating whether the student was successfully enrolled or if the capacity was exceeded.
- This endpoint handles enrolling a student in the specified course based on the provided deptCode and courseCode.
- Upon Success: An HTTP 200 Status Code is returned with the message "Student has been enrolled successfully" in a JSON structure.
- Upon Failure:
    - HTTP 409 Status Code with "Student has not been enrolled due to exceeding capacity" if the course capacity is exceeded.
    - HTTP 404 Status Code with "Course Not Found" if the specified course does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

### PATCH /changeCourseTime
- Expected Input Parameters:
    - deptCode (String): The department code containing the course.
    - courseCode (String): The course code for which the time is being changed.
    - time (String): The new time for the course.
- Expected Output: A JSON structure containing the updated course details:
    - course: A JSON object representing the updated course with the following fields:
        - enrolledStudentCount (int): The number of students enrolled in the course.
        - courseLocation (String): The location of the course.
        - instructorName (String): The name of the instructor teaching the course.
        - courseTimeSlot (String): The updated time slot for the course.
        - courseFull (boolean): Indicates whether the course is full.
        - capacity (int): The total capacity of the course.
- This endpoint handles changing the time of a course based on the provided deptCode, courseCode, and new time.
- Upon Success: An HTTP 200 Status Code is returned with the updated course details in a JSON structure.
- Upon Failure:
    - HTTP 404 Status Code with "Course Not Found" if the specified course does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

### PATCH /changeCourseTeacher
- Expected Input Parameters:
    - deptCode (String): The department code containing the course.
    - courseCode (String): The course code for which the instructor is being changed.
    - teacher (String): The new instructor for the course.
- Expected Output: A JSON structure containing the updated course details:
    - course: A JSON object representing the updated course with the following fields:
        - enrolledStudentCount (int): The number of students enrolled in the course.
        - courseLocation (String): The location of the course.
        - instructorName (String): The updated instructor name for the course.
        - courseTimeSlot (String): The time slot for the course.
        - courseFull (boolean): Indicates whether the course is full.
        - capacity (int): The total capacity of the course.
- This endpoint handles changing the instructor of a course based on the provided deptCode, courseCode, and new teacher.
- Upon Success: An HTTP 200 Status Code is returned with the updated course details in a JSON structure.
- Upon Failure:
    - HTTP 404 Status Code with "Course Not Found" if the specified course does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

### PATCH /changeCourseLocation
- Expected Input Parameters:
    - deptCode (String): The department code containing the course.
    - courseCode (String): The course code for which the location is being changed.
    - location (String): The new location for the course.
- Expected Output: A JSON structure containing the updated course details:
    - course: A JSON object representing the updated course with the following fields:
        - enrolledStudentCount (int): The number of students enrolled in the course.
        - courseLocation (String): The updated location for the course.
        - instructorName (String): The name of the instructor teaching the course.
        - courseTimeSlot (String): The time slot for the course.
        - courseFull (boolean): Indicates whether the course is full.
        - capacity (int): The total capacity of the course.
- This endpoint handles changing the location of a course based on the provided deptCode, courseCode, and new location.
- Upon Success: An HTTP 200 Status Code is returned with the updated course details in a JSON structure.
- Upon Failure:
    - HTTP 404 Status Code with "Course Not Found" if the specified course does not exist.
    - HTTP 500 Status Code if an error occurs on the server side.

## Branch Coverage Reporting
- We used JaCoCo to perform branch analysis to see the branch coverage of the relevant code within the code base. Image below demonstrates the output.
    - follow the steps below to generate the report:
        - mvn clean test
        - mvn jacoco:report
        - open target/site/jacoco/index.html

## Static Code Analysis
- We used PMD to perform static analysis on our codebase
  - Command: pmd check -d </usr/src>  -R rulesets/java/quickstart.xml -f json, to install pmd, you may checkout this link: https://pmd.github.io/

## Continuous Integration Report
- This repository using GitHub Actions to perform continous integration, to view the latest results go to the following link: https://github.com/Carson-NNY/4156-Miniproject-2024-Students-Java-new/actions/workflows/maven.yml
- Under 'X workflow runs' click on the latest workflow run to read the logs

## Tools used 
- Maven Package Manager
- Spring Boot
- GitHub Actions CI 
  - This is enabled via the "Actions" tab on GitHub.
- Checkstyle
  - We use Checkstyle for code reporting. Note that Checkstyle does NOT get run as part of the CI pipeline.
- PMD
  - PMD is used to perform static code analysis.
- JUnit
  - We run JUnit tests as part of the CI pipeline.
- JaCoCo
  - We use JaCoCo to check branch coverage
- Postman
  - We use Postman to test the all the endpoints mentioned above.
- Google Cloud Platform
  - We use Google Cloud Platform for the deployment.
- IntelliJ
  - We use IntelliJ as our IDE for development.