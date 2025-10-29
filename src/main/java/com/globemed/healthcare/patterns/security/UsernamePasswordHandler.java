package com.globemed.healthcare.patterns.security;

import com.globemed.healthcare.dao.UserDao;
import com.globemed.healthcare.model.User;

import java.util.Optional;

public class UsernamePasswordHandler extends AuthHandler {

	private final UserDao userDao = new UserDao();

	@Override
	public boolean handleRequest(String username, String password) {
		Optional<User> userOpt = userDao.findByUsername(username);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			if (userDao.verifyPassword(password, user.getPasswordHash())) {
				System.out.println("Username and password are correct.");
				if (next != null) {
					return next.handleRequest(username, password);
				}
				return true;
			}
		}

		System.out.println("Authentication failed: Invalid username or password.");
		return false;
	}
}
