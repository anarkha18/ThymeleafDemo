package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotBlank(message = "Name cannot be blank")
	@Size(min = 3, message = "Name must be at least 3 characters")
	private String name;

	@NotBlank(message = "Phone cannot be blank")
	@Pattern(regexp = "\\d{7}", message = "Phone must have 7 digits and contain only numbers")
	private String phone;

	@NotBlank(message = "Date of Birth cannot be blank")
	private String dob;

	@NotBlank(message = "Gender cannot be blank")
	private String gender;

	@NotBlank(message = "Email cannot be blank")
	@Email(message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password cannot be blank")
	@Size(min = 3, message = "Password must be at least 3 characters")
	private String password;

	@NotBlank(message = "Role cannot be blank")
	private String role;

}
