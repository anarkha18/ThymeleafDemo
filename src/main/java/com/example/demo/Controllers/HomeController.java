package com.example.demo.Controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Repos.EmployeeRepo;
import com.example.demo.models.Employee;
import com.example.demo.services.EmployeeService;

@Controller
public class HomeController {

	public static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private EmployeeService empService;
	@Autowired
	private EmployeeRepo employeeRepo;

	@GetMapping("/addEmployee")
	public ModelAndView home(Model model) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home/index");
		modelAndView.addObject("Employee", new Employee());
		return modelAndView;
	}

	@PostMapping("/addOrSaveEmployee")
	public ModelAndView saveEmployee(@Valid @ModelAttribute("Employee") Employee newEmployee, BindingResult result,
			RedirectAttributes redirectAttributes) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home/index");
		try {
			if (result.hasErrors()) {
				logger.info("Validation errors: " + result.getAllErrors());
				modelAndView.addObject("errorMessage", "Employee not saved due to validation errors");
				modelAndView.addObject("Employee", newEmployee);
				modelAndView.addObject("hasErrors", true);

				return modelAndView;
			}
			if (newEmployee.getId() != null) {
				// If ID is present, it's an update
				Employee existingEmployee = empService.getEmployeeById(newEmployee.getId())
						.orElseThrow(() -> new IllegalArgumentException("Employee not found"));
				// Copy updated fields to the existing employee
				existingEmployee.setName(newEmployee.getName());
				existingEmployee.setPhone(newEmployee.getPhone());
				existingEmployee.setDob(newEmployee.getDob());
				existingEmployee.setGender(newEmployee.getGender());
				existingEmployee.setEmail(newEmployee.getEmail());
				existingEmployee.setPassword(newEmployee.getPassword());
				existingEmployee.setRole(newEmployee.getRole());

				empService.addOrSaveEmployee(existingEmployee);
				modelAndView.setViewName("redirect:/allEmployees");
				redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully");
				return modelAndView;
			} else {
				System.out.println(newEmployee);
				// If ID is not present, it's a new employee
				Employee myEmployee = empService.addOrSaveEmployee(newEmployee);
				modelAndView.addObject("successMessage", "Employee saved successfully");
				logger.info("saved successfullyy!!" + myEmployee);
				return modelAndView;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("Employee not saved");
		}
	}

	// per page = 3[n]
	// current page = 0 [page]
	@GetMapping("/allEmployees")
	public ModelAndView allEmployees(
			@RequestParam(name = "entriesPerPage", defaultValue = "5", required = false) Integer entriesPerPage,
			@RequestParam(name = "page", defaultValue = "0") Integer page) throws Exception {
		try {
			Pageable pageable = PageRequest.of(page, entriesPerPage);
			Page<Employee> employees = employeeRepo.findAllBy(pageable);
			ModelAndView modelAndView = new ModelAndView();
			modelAndView.setViewName("home/EmployeeTable");
			modelAndView.addObject("employees", employees);
			modelAndView.addObject("currentpage", page);
			modelAndView.addObject("totalpages", employees.getTotalPages());
			modelAndView.addObject("entriesPerPage", entriesPerPage);
			return modelAndView;

		} catch (Exception e) {
			logger.error("An error occurred while fetching all employees", e);
			throw new Exception("An error occurred while fetching all employees", e);
		}
	}

	@GetMapping("/updateEmployee/{id}")
	public ModelAndView showUpdateForm(@PathVariable("id") int id, Model model) {
		try {
			Optional<Employee> employee = empService.getEmployeeById(id);
			if (employee.isPresent()) {
				ModelAndView modelAndView = new ModelAndView("home/index");
				modelAndView.addObject("Employee", employee.get());
				return modelAndView;
			} else {
				throw new Exception("Employee not found");
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
			// Re-throw the exception to let the global handler handle it
		}
	}

	@GetMapping("/deleteEmployee/{id}")
	public ModelAndView deleteEmployee(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			Optional<Employee> employee = empService.getEmployeeById(id);
			if (employee.isPresent()) {
				empService.deleteEmp(id);
				modelAndView.setViewName("redirect:/allEmployees");
				redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully");
				return modelAndView;
			} else {
				// Employee not found
				modelAndView.setViewName("redirect:/allEmployees");
				redirectAttributes.addFlashAttribute("errorMessage", "Employee not found");
				return modelAndView;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			modelAndView.setViewName("redirect:/allEmployees");
			return modelAndView;
		}

	}

}