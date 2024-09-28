package dev.coms4156.project.individualproject;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * the tests ensures the functionality of the RouteController.
 */
@SpringBootTest
@ContextConfiguration
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class RouteControllerTest {
  @Autowired
  WebApplicationContext wac;

  private MockMvc mockMvc;

  @MockBean
  private MyFileDatabase myFileDatabase;

  /**
   * set up the MockMvc environment.
   */
  @BeforeEach
  public  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
  }


  @Test
  public void retrieveDepartmentTest() throws Exception {
    String deptCode = "IEOR";

    // expected result  will depend on the Database data initialized in IndividualProjectApplication
    mockMvc.perform(get("/retrieveDept")
        .param("deptCode", deptCode))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.courseSelection.size()", is(8)));

    mockMvc.perform(get("/retrieveDept")
        .param("deptCode", " dept code to be not found"))
        .andExpect(status().isNotFound());
  }

  @Test
  void retrieveDepartmentNotFoundTest() throws Exception {

    final MyFileDatabase savedDatabase = IndividualProjectApplication.myFileDatabase;

    // case1: when value returned by getDepartmentMapping() is null
    IndividualProjectApplication.myFileDatabase = myFileDatabase;
    given(myFileDatabase.getDepartmentMapping()).willReturn(null);

    mockMvc.perform(get("/retrieveDept")
            .param("deptCode", "IEOR"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Department Not Found"));

    // case2: when the department mapping is empty
    given(myFileDatabase.getDepartmentMapping()).willReturn(new HashMap<>());
    mockMvc.perform(get("/retrieveDept")
            .param("deptCode", "IEOR"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Department Not Found"));
    IndividualProjectApplication.myFileDatabase = savedDatabase;
  }

  @Test
  public void retrieveCourseTest() throws Exception {
    String deptCode = "IEOR";
    String courseCode = "2500";

    mockMvc.perform(get("/retrieveCourse")
        .param("deptCode", deptCode)
        .param("courseCode", courseCode))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.enrolledStudentCount", is(52)));

    mockMvc.perform(get("/retrieveCourse")
        .param("deptCode", "deptCode code to be not found")
        .param("courseCode", "999999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void retrieveCourseTestFailedMethods() throws Exception {

    final MyFileDatabase savedDatabase = IndividualProjectApplication.myFileDatabase;

    // case1: when value returned by getDepartmentMapping() is null
    IndividualProjectApplication.myFileDatabase = myFileDatabase;
    given(myFileDatabase.getDepartmentMapping()).willReturn(null);

    mockMvc.perform(get("/retrieveCourse")
            .param("deptCode", "IEOR")
            .param("courseCode", "2500"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Department Not Found"));

    // case1: when the department mapping is empty
    given(myFileDatabase.getDepartmentMapping()).willReturn(new HashMap<>());
    mockMvc.perform(get("/retrieveCourses")
            .param("deptCode", "IEOR")
            .param("courseCode", "2500"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("No department exists"));
    IndividualProjectApplication.myFileDatabase = savedDatabase;
  }

  @Test
  void retrieveCoursesTest() throws Exception  {
    String courseCode = "2500";
    String expectedStr = "\nInstructor: Uday Menon; Location: 627 MUDD; Time: 11:40-12:55";

    // case1: only one course across departments, and also
    // test the string representation of the course
    mockMvc.perform(get("/retrieveCourses")
        .param("courseCode", courseCode))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$", aMapWithSize(1)))
        .andExpect(jsonPath("$['2500'][0]", equalTo(expectedStr)));

    // case2: multiple courses across departments
    courseCode = "1201";
    String expectedStr2 =  "\nInstructor: David G Vallancourt; Location: 301 PUP; Time: 4:10-5:25";
    String expectedStr3 = "\nInstructor: Eric Raymer; Location: 428 PUP; Time: 2:40-3:55";
    mockMvc.perform(get("/retrieveCourses")
            .param("courseCode", courseCode))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$['1201']", hasSize(2)))
        .andExpect(jsonPath("$['1201'][0]", equalTo(expectedStr2)))
        .andExpect(jsonPath("$['1201'][1]", equalTo(expectedStr3)));
  }

  @Test
  void retrieveCoursesTestNotFound() throws Exception {
    String invalidCourseCode = "@@22222";

    // case: no course found with the given course code
    mockMvc.perform(get("/retrieveCourses")
            .param("courseCode", invalidCourseCode))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Courses Not Found"));
  }

  @Test
  void retrieveCoursesTestWithNullDepartment() throws Exception {
    final MyFileDatabase savedDatabase = IndividualProjectApplication.myFileDatabase;

    // case1: when the department mapping is null
    IndividualProjectApplication.myFileDatabase = myFileDatabase;
    given(myFileDatabase.getDepartmentMapping()).willReturn(null);
    mockMvc.perform(get("/retrieveCourses")
            .param("courseCode", "2500"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("No department exists"));

    // case1: when the department mapping is empty
    given(myFileDatabase.getDepartmentMapping()).willReturn(new HashMap<>());
    mockMvc.perform(get("/retrieveCourses")
            .param("courseCode", "2500"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("No department exists"));
    IndividualProjectApplication.myFileDatabase = savedDatabase;
  }


  @Test
  void isCourseFullTest() throws Exception {
    String deptCode = "CHEM";
    String courseCode = "1403";

    mockMvc.perform(get("/isCourseFull")
        .param("deptCode", deptCode)
        .param("courseCode", courseCode))
        .andExpect(status().isOk())
        .andExpect(content().string("false"));

    // case: not found the course
    courseCode = "140333";
    mockMvc.perform(get("/isCourseFull")
        .param("deptCode", deptCode)
        .param("courseCode", courseCode))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Course Not Found"));
  }

  @Test
  public void getMajorCtFromDeptTest() throws Exception {
    String deptCode = "IEOR";

    mockMvc.perform(get("/getMajorCountFromDept")
        .param("deptCode", deptCode))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numberOfMajors", is(67)));

    deptCode = "IEORRRR";

    mockMvc.perform(get("/getMajorCountFromDept")
            .param("deptCode", deptCode))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Department Not Found"));
  }


  @Test
  public void identifyDeptChairTest() throws Exception {
    String deptCode = "CHEM";

    mockMvc.perform(get("/idDeptChair")
        .param("deptCode", deptCode))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.departmentChair", is("Laura J. Kaufman")));

    deptCode = "CHEMMMM";

    mockMvc.perform(get("/idDeptChair")
        .param("deptCode", deptCode))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Department Not Found"));
  }


  @Test
  public void findCourseLocationTest() throws Exception {
    String deptCode = "CHEM";
    String courseCode = "1403";

    mockMvc.perform(get("/findCourseLocation")
        .param("deptCode", deptCode)
        .param("courseCode", courseCode))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.location", is("309 HAV")));

    mockMvc.perform(get("/findCourseLocation")
        .param("deptCode", "dept code to be not found")
        .param("courseCode", "999999"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Course Not Found"));
  }

  @Test
  public void findCourseInstructorTest() throws Exception {
    String deptCode = "CHEM";
    String courseCode = "1403";

    mockMvc.perform(get("/findCourseInstructor")
        .param("deptCode", deptCode)
        .param("courseCode", courseCode))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.instructor", is("Ruben M Savizky")));

    mockMvc.perform(get("/findCourseInstructor")
        .param("deptCode", "dept code to be not found")
        .param("courseCode", "999999"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Course Not Found"));
  }

  @Test
  public void findCourseTimeTest() throws Exception {
    String deptCode = "CHEM";
    String courseCode = "1403";

    mockMvc.perform(get("/findCourseTime")
        .param("deptCode", deptCode)
        .param("courseCode", courseCode))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.courseTime", is("6:10-7:25")));

    mockMvc.perform(get("/findCourseTime")
        .param("deptCode", "dept code to be not found")
        .param("courseCode", "999999"))
        .andExpect(status().isNotFound())
        .andExpect(content().string("Course Not Found"));
  }

  @Test
  public void addMajorToDeptTest() throws Exception {
    String deptCode = "CHEM";

    mockMvc.perform(patch("/addMajorToDept")
        .param("deptCode", deptCode))
        .andExpect(status().isOk());


  }

  @Test
  public void removeMajorFromDeptTest() throws Exception {
    String deptCode = "CHEM";

    mockMvc.perform(patch("/removeMajorFromDept")
        .param("deptCode", deptCode))
        .andExpect(status().isOk());
  }

  @Test
  public void dropStudentTest() throws Exception {
    String deptCode = "CHEM";
    String courseCode = "1403";

    mockMvc.perform(patch("/dropStudentFromCourse")
        .param("deptCode", deptCode)
        .param("courseCode", courseCode))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Student has been dropped.")));
  }

  @Test
  public void setEnrollmentCountTest() throws Exception {
    String deptCode = "ECON";
    String courseCode = "1105";
    String enrollmentCount = "110";

    mockMvc.perform(patch("/setEnrollmentCount")
        .param("deptCode", deptCode)
        .param("courseCode", courseCode)
        .param("count", enrollmentCount))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Attributed was updated successfully.")));

    // check negative enrollment count or invalid input
    enrollmentCount = "-1";
    mockMvc.perform(patch("/setEnrollmentCount")
        .param("deptCode", deptCode)
        .param("courseCode", courseCode)
        .param("count", enrollmentCount))
        .andExpect(status().isBadRequest());
  }

  @Test
  void enrollStudentInCourseTest() throws Exception {
    String deptCode = "ECON";
    String courseCode = "1105";
    String enrollmentCount = "110";

    mockMvc.perform(patch("/setEnrollmentCount")
            .param("deptCode", deptCode)
            .param("courseCode", courseCode)
            .param("count", enrollmentCount))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Attributed was updated successfully.")));

    mockMvc.perform(patch("/enrollStudentInCourse")
            .param("deptCode", deptCode)
            .param("courseCode", courseCode))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Student has been enrolled successfully.")));

    // check if the actual enrolled student count is updated
    mockMvc.perform(get("/retrieveCourse")
            .param("deptCode", deptCode)
            .param("courseCode", courseCode))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.enrolledStudentCount", is(111)));
  }

  @Test
  void enrollStudentInCourseTestFailed() throws Exception {
    // first set the enrollmentCount to 210 (which is the capacity of the course)
    String deptCode = "ECON";
    String courseCode = "1105";
    String enrollmentCount = "210";
    mockMvc.perform(patch("/setEnrollmentCount")
            .param("deptCode", deptCode)
            .param("courseCode", courseCode)
            .param("count", enrollmentCount))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Attributed was updated successfully.")));

    mockMvc.perform(patch("/enrollStudentInCourse")
            .param("deptCode", deptCode)
            .param("courseCode", courseCode))
        .andExpect(status().isConflict())
        .andExpect(content()
            .string(containsString("Student has not been enrolled due to exceeding capacity.")));
  }

  @Test
  void enrollStudentInCourseTestNotFound() throws Exception {
    String deptCode = "ECON";
    String courseCode = "9999999";

    // case: course not found
    mockMvc.perform(patch("/enrollStudentInCourse")
            .param("deptCode", deptCode)
            .param("courseCode", courseCode))
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Course Not Found")));

    deptCode = "ECONNNN";
    courseCode = "1105";
    mockMvc.perform(patch("/enrollStudentInCourse")
            .param("deptCode", deptCode)
            .param("courseCode", courseCode))
        .andExpect(status().isNotFound())
        .andExpect(content().string(containsString("Course Not Found")));
  }

  @Test
  void changeCourseTimeTest() throws Exception {
    String deptCode = "ECON";
    String courseCode = "1105";
    String newTime = "11:40-12:55";

    mockMvc.perform(patch("/changeCourseTime")
        .param("deptCode", deptCode)
        .param("courseCode", courseCode)
        .param("time", newTime))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.course.courseTimeSlot", is(newTime)));
  }

  @Test
  void changeCourseTeacherTest() throws Exception {
    String deptCode = "ECON";
    String courseCode = "1105";
    String newTeacher = "Tamrat Gashaw";

    mockMvc.perform(patch("/changeCourseTeacher")
        .param("deptCode", deptCode)
        .param("courseCode", courseCode)
        .param("teacher", newTeacher))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.course.instructorName", is(newTeacher)));

  }

  @Test
  void changeCourseLocationTest() throws Exception {
    String deptCode = "ECON";
    String courseCode = "1105";
    String newLocation = "301 URIS";

    mockMvc.perform(patch("/changeCourseLocation")
        .param("deptCode", deptCode)
        .param("courseCode", courseCode)
        .param("location", newLocation))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.course.courseLocation", is(newLocation)));
  }

}

