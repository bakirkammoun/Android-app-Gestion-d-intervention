package com.geo4net.main.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.geo4net.main.beans.Intervention;
import com.geo4net.main.beans.User;
import com.geo4net.main.exception.RecordNotFoundException;
import com.geo4net.main.exception.UserExistsException;
import com.geo4net.main.service.InterventionService;
import com.geo4net.main.service.UserService;

@RestController
@RequestMapping("/user") // Base path for all methods in this controller
public class UserRestController {

	@Autowired
	private UserService uservice;

	@Autowired
	private InterventionService iservice;

	// Register User
	@RequestMapping(method = RequestMethod.POST, value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<List<User>> registerUser(@RequestParam Map<String, String> paramMap) throws UserExistsException {
		System.out.println("In registerUser");

		// Create a new user from the parameters
		User user = new User(paramMap.get("username"), paramMap.get("password"), paramMap.get("email"));

		// Add the user to the database
		List<User> createdUser = uservice.createUser(user);

		// Set headers and return the response
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return new ResponseEntity<>(createdUser, headers, HttpStatus.OK);
	}

	// Get all users
	@RequestMapping(method = RequestMethod.GET, value = "/all")
	@ResponseBody
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> users = uservice.getAllUsers();
		return new ResponseEntity<>(users, new HttpHeaders(), HttpStatus.OK);
	}

	// Get user by username
	@RequestMapping(method = RequestMethod.GET, value = "/{username}")
	@ResponseBody
	public ResponseEntity<User> getUserByID(@PathVariable("username") String username) throws RecordNotFoundException {
		User user = uservice.getUserById(username);
		return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.OK);
	}

	// Login user (verify username and password)
	@RequestMapping(method = RequestMethod.GET, value = "/login/{username}&{password}")
	@ResponseBody
	public ResponseEntity<Boolean> verifyUserLogin(@PathVariable("username") String username,
												   @PathVariable("password") String password) throws RecordNotFoundException {
		Boolean isValidUser = uservice.getUserByUsernameAndPassword(username, password);
		return new ResponseEntity<>(isValidUser, new HttpHeaders(), HttpStatus.OK);
	}

	// Get all interventions for a user
	@RequestMapping(method = RequestMethod.GET, value = "/interventions/all",
			consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<List<Intervention>> getAllInterventions(@RequestParam Map<String, String> paramMap) {
		String username = paramMap.get("username");
		List<Intervention> interventions = iservice.getAllInterventions(username);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return new ResponseEntity<>(interventions, headers, HttpStatus.OK);
	}

	// Create or update an intervention
	@RequestMapping(method = RequestMethod.POST, value = "/intervention/create_or_update",
			consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<List<Intervention>> createOrUpdateIntervention(@RequestParam Map<String, String> paramMap) {
		System.out.println("In createOrUpdateIntervention");

		// Get the user object based on the username
		User user = uservice.getUserById(paramMap.get("username"));
		System.out.println(user);

		// Create or update the intervention
		List<Intervention> interventions = iservice.createOrUpdateIntervention(new Intervention(
				paramMap.get("psn"), paramMap.get("tech_name"), paramMap.get("sim"), paramMap.get("lvcan"),
				paramMap.get("current_location"), paramMap.get("matricule"), user));

		// Set headers and return the response
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return new ResponseEntity<>(interventions, headers, HttpStatus.OK);
	}

	// Delete an intervention by ID
	@RequestMapping(method = RequestMethod.DELETE, value = "/intervention/delete",
			consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<Void> deleteInterventionById(@RequestParam Map<String, String> paramMap) throws RecordNotFoundException {
		iservice.deleteInterventionById(paramMap.get("psn"));
		return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No content to return after deletion
	}
}
