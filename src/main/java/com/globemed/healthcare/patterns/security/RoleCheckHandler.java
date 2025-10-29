package com.globemed.healthcare.patterns.security;

import com.globemed.healthcare.dao.UserDao;
import com.globemed.healthcare.model.User;

import java.util.Optional;

public class RoleCheckHandler extends AuthHandler {

	private final UserDao userDao = new UserDao();

	@Override
	public boolean handleRequest(String username, String password) {
		Optional<User> userOpt = userDao.findByUsername(username);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			if (user.getRole() != null && !user.getRole().trim().isEmpty()) {
				System.out.println("User has a valid role: " + user.getRole());
				if (next != null) {
					return next.handleRequest(username, password);
				}
				return true;
			}
		}

		System.out.println("Authentication failed: User does not have a valid role.");
		return false;
	}
}
