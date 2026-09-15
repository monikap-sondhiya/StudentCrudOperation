package org.example.service;
import org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.example.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Create Student
    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    // Get All Students
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get Student By ID
    @Override
    public Student getStudentById(Long id) {

        Optional<Student> student = studentRepository.findById(id);

        if (student.isPresent()) {
            return student.get();
        }

        throw new RuntimeException("Student not found with id: " + id);
    }

    // Update Student
    @Override
    public Student updateStudent(Long id, Student student) {

        Student existingStudent = getStudentById(id);

        existingStudent.setName(student.getName());
        existingStudent.setSalary(student.getSalary());

        return studentRepository.save(existingStudent);
    }

    // Delete Student
    @Override
    public void deleteStudent(Long id) {

        Student existingStudent = getStudentById(id);

        studentRepository.delete(existingStudent);
    }
}

