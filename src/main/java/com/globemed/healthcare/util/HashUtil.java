package com.globemed.healthcare.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashUtil {
	public static String sha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte b : hash) sb.append(String.format("%02x", b));
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean verifySha256(String raw, String hashed) {
		return sha256(raw).equalsIgnoreCase(hashed);
	}
} 