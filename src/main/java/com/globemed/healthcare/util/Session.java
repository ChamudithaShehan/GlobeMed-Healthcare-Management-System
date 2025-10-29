package com.globemed.healthcare.util;

import com.globemed.healthcare.model.User;

public class Session {
	private static User currentUser;

	public static void setCurrentUser(User user) {
		currentUser = user;
	}

	public static User getCurrentUser() {
		return currentUser;
	}
} 