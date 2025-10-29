package com.globemed.healthcare.dao;

import com.globemed.healthcare.model.User;
import com.globemed.healthcare.util.HashUtil;
import com.globemed.healthcare.util.SqlDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {
	public Optional<User> findByUsername(String username) {
		String sql = "SELECT username, password_hash, role FROM users WHERE username = ?";
		try (Connection conn = SqlDataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(new User(
						rs.getString("username"),
						rs.getString("password_hash"),
						rs.getString("role")
					));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return Optional.empty();
	}

	public List<User> listDoctors() {
		String sql = "SELECT username, password_hash, role FROM users WHERE role = 'Doctor'";
		List<User> result = new ArrayList<>();
		try (Connection conn = SqlDataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql);
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				result.add(new User(rs.getString("username"), rs.getString("password_hash"), rs.getString("role")));
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void createUser(String username, String rawPassword, String role) {
		String hashed = HashUtil.sha256(rawPassword);
		String sql = "INSERT INTO users(username, password_hash, role) VALUES (?, ?, ?)";
		try (Connection conn = SqlDataSource.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, username);
			ps.setString(2, hashed);
			ps.setString(3, role);
			ps.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean verifyPassword(String rawPassword, String storedHash) {
		if (storedHash == null || storedHash.isEmpty()) return false;
		if (storedHash.length() == 64) {
			return HashUtil.verifySha256(rawPassword, storedHash);
		}
		return rawPassword.equals(storedHash);
	}
} 