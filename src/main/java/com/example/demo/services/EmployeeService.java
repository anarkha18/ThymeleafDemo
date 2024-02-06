package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repos.EmployeeRepo;
import com.example.demo.models.Employee;

@Service
public class EmployeeService {

	public static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private EmployeeRepo empRepo;

	public Employee addOrSaveEmployee(Employee newEmployee) {
		return empRepo.save(newEmployee);
	}

	public List<Employee> getAllEmployees() {
		List<Employee> allEmployees = empRepo.findAll();
		return allEmployees;
	}

	public Optional<Employee> getEmployeeById(int id) {
		Optional<Employee> myEmployee = empRepo.findById(id);
		return myEmployee;
	}

	public void deleteEmp(int id) {
		empRepo.deleteById(id);
		logger.info("deleted!");
	}

}
