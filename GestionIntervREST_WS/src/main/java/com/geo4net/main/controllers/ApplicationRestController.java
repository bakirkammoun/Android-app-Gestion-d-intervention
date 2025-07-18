package com.geo4net.main.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/application")
public class ApplicationRestController {

	@Autowired
	private UserService uservice;

	@Autowired
	private InterventionService iservice;

	@RequestMapping(method = RequestMethod.POST, value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<List<User>> registerUser(@RequestParam Map<String, String> paramMap) throws UserExistsException {
		User user = new User(paramMap.get("username"), paramMap.get("password"), paramMap.get("email"));
		List<User> createdUser = uservice.createUser(user);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(createdUser, headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/{username}")
	@ResponseBody
	public ResponseEntity<User> getUserByID(@PathVariable("username") String username) throws RecordNotFoundException {
		User user = uservice.getUserById(username);
		return new ResponseEntity<>(user, new HttpHeaders(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/login/{username}/{password}")
	@ResponseBody
	public ResponseEntity<Boolean> verifyUserLogin(@PathVariable("username") String username,
												   @PathVariable("password") String password) throws RecordNotFoundException {
		Boolean isValidUser = uservice.getUserByUsernameAndPassword(username, password);
		return new ResponseEntity<>(isValidUser, new HttpHeaders(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interventions/all",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<List<Intervention>> getAllInterventions(@RequestParam(value = "username", required = false) String username) {
		List<Intervention> interventions;

		if (username != null && !username.isEmpty()) {
			// Récupère les interventions pour un utilisateur spécifique
			interventions = iservice.getAllInterventions(username);
		} else {
			// Récupère toutes les interventions pour tous les utilisateurs
			interventions = iservice.getAllInterventionsForAllUsers();
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(interventions, headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/intervention/create_or_update",
			consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<List<Intervention>> createOrUpdateIntervention(@RequestParam Map<String, String> paramMap) {
		User user = uservice.getUserById(paramMap.get("username"));
		List<Intervention> interventions = iservice.createOrUpdateIntervention(new Intervention(
				paramMap.get("psn"), paramMap.get("tech_name"), paramMap.get("sim"), paramMap.get("lvcan"),
				paramMap.get("current_location"), paramMap.get("matricule"), user));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(interventions, headers, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/intervention/delete",
			consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<Void> deleteInterventionById(@RequestParam Map<String, String> paramMap) throws RecordNotFoundException {
		iservice.deleteInterventionById(paramMap.get("psn"));
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
