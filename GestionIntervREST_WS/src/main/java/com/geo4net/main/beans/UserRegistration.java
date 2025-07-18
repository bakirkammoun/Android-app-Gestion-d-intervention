package com.geo4net.main.beans;

import java.util.ArrayList;
import java.util.List;

public class UserRegistration {
	
	private static  List<User> userRecords = new ArrayList<User>();
	static {
		userRecords.add(new User("bakir", "kammoun123", "bakir@gmail.com"));
		userRecords.add(new User("hassan", "dtert1456", "hassan@gmail.com"));

	}
	


	public static void addUser(User u) {
		userRecords.add(u);
	}

	public static List<User> getUserRecords() {
		for (User u: userRecords) {
			u.toString();
		}
		return userRecords;
	} 
	
	public static User getUserById(String username) {
		for (User u : getUserRecords()) {
			if (u.getUsername().equals(username)) 
				return u;
				
			
		}
		return null;
	}

}
