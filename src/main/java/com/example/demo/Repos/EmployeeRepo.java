package com.example.demo.Repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
	// Pageable interface has info abt pagination- per page and current page
	Page<Employee> findAllBy(Pageable pageable);
}
