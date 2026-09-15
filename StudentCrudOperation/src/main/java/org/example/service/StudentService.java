package org.example.service;
import org.example.entity.Student;
import java.util.List;

public interface StudentService {

    // Create Student
    Student saveStudent(Student student);

    // Get all Students
    List<Student> getAllStudents();

    // Get Student by ID
    Student getStudentById(Long id);

    // Update Student
    Student updateStudent(Long id, Student student);

    // Delete Student
    void deleteStudent(Long id);
}

