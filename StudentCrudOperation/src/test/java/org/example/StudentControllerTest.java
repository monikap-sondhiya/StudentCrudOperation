package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.StudentController;
import org.example.entity.Student;
import org.example.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;


    // =========================
    // CREATE STUDENT
    // =========================

    @Test
    void saveStudentTest() throws Exception {

        Student student = new Student();
        student.setId(1L);
        student.setName("Monika");
        student.setSalary(50000.0);

        when(studentService.saveStudent(any(Student.class)))
                .thenReturn(student);

        mockMvc.perform(
                        post("/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(student))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Monika"))
                .andExpect(jsonPath("$.salary").value(50000));

        verify(studentService, times(1))
                .saveStudent(any(Student.class));
    }


    // =========================
    // GET ALL STUDENTS
    // =========================

    @Test
    void getAllStudentsTest() throws Exception {

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Monika");
        student1.setSalary(50000.0);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Rahul");
        student2.setSalary(60000.0);

        List<Student> students =
                Arrays.asList(student1, student2);

        when(studentService.getAllStudents())
                .thenReturn(students);

        mockMvc.perform(
                        get("/students")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Monika"))
                .andExpect(jsonPath("$[1].name").value("Rahul"));

        verify(studentService, times(1))
                .getAllStudents();
    }


    // =========================
    // GET STUDENT BY ID
    // =========================

    @Test
    void getStudentByIdTest() throws Exception {

        Student student = new Student();
        student.setId(1L);
        student.setName("Monika");
        student.setSalary(50000.0);

        when(studentService.getStudentById(1L))
                .thenReturn(student);

        mockMvc.perform(
                        get("/students/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Monika"))
                .andExpect(jsonPath("$.salary").value(50000));

        verify(studentService, times(1))
                .getStudentById(1L);
    }


    // =========================
    // UPDATE STUDENT
    // =========================

    @Test
    void updateStudentTest() throws Exception {

        Student student = new Student();
        student.setId(1L);
        student.setName("Monika");
        student.setSalary(60000.0);

        when(studentService.updateStudent(
                eq(1L),
                any(Student.class)
        )).thenReturn(student);

        mockMvc.perform(
                        put("/students/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(student))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Monika"))
                .andExpect(jsonPath("$.salary").value(60000.0));

        verify(studentService, times(1))
                .updateStudent(eq(1L), any(Student.class));
    }


    // =========================
    // DELETE STUDENT
    // =========================

    @Test
    void deleteStudentTest() throws Exception {

        doNothing()
                .when(studentService)
                .deleteStudent(1L);

        mockMvc.perform(
                        delete("/students/1")
                )
                .andExpect(status().isOk())
                .andExpect(
                        content().string("Student deleted successfully")
                );

        verify(studentService, times(1))
                .deleteStudent(1L);
    }
}
