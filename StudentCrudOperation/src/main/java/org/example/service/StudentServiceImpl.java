package org.example.service;
import org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "student-topic";

    public StudentServiceImpl(StudentRepository studentRepository,
                              KafkaTemplate<String, String> kafkaTemplate) {
        this.studentRepository = studentRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    // Create Student
    @Override
    public Student saveStudent(Student student) {

        // Save student in database
        Student savedStudent = studentRepository.save(student);
        System.out.println(savedStudent);
        // Send message to Kafka
        String message = "Student Created: " + savedStudent.getName();

        kafkaTemplate.send(TOPIC, message);

        return savedStudent;
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

        Student updatedStudent = studentRepository.save(existingStudent);

        // Send message to Kafka
        String message = "Student Updated: " + updatedStudent.getName();

        kafkaTemplate.send(TOPIC, message);

        return updatedStudent;
    }

    // Delete Student
    @Override
    public void deleteStudent(Long id) {

        Student existingStudent = getStudentById(id);

        studentRepository.delete(existingStudent);

        // Send message to Kafka
        String message = "Student Deleted: " + existingStudent.getName();

        kafkaTemplate.send(TOPIC, message);
    }
}

