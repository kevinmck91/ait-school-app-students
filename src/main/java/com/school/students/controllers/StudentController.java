package com.school.students.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.school.students.dtos.Student;
import com.school.students.exceptions.StudentAlreadyExistsException;
import com.school.students.exceptions.StudentNotFoundException;
import com.school.students.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class StudentController {

	@Autowired
	private StudentRepository studentRepository;

	@GetMapping("students/")
	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	@GetMapping("students/{studentNumber}/")
	public Optional<Student> getStudentByStudentNumber(@PathVariable String studentNumber) {
		
		Optional<Student> foundStudent = studentRepository.findByStudentNumber(studentNumber);

		if (foundStudent.isPresent())
			return foundStudent;
		else
			throw new StudentNotFoundException("Unable to find Student Number: " + studentNumber);
	}

	@PostMapping("students/")
	public ResponseEntity createStudent(@RequestBody Student newStudent) {

		// Check to see if the Student number already exists
		Optional<Student> foundStudent = studentRepository.findByStudentNumber(newStudent.getStudentNumber());

		if (!foundStudent.isPresent())		// student does not exist
		{
			studentRepository.save(newStudent);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(newStudent.getId()).toUri();
			return ResponseEntity.created(location).build();
		} 
		else 
		{
			throw new StudentAlreadyExistsException("The student number already exists : " + newStudent.getStudentNumber());
		}

	}

}
