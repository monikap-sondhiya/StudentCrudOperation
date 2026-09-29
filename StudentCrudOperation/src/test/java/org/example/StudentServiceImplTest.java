package org.example;
import  org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.example.service.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void saveStudentTest() {

        Student student = new Student();
        student.setName("Monika");
        student.setSalary(50000.00);

        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.saveStudent(student);

        assertEquals("Monika", result.getName());
        assertEquals(50000, result.getSalary());

        verify(studentRepository, times(1)).save(student);

        verify(kafkaTemplate, times(1))
                .send("student-topic", "Student Created: Monika");
    }
}