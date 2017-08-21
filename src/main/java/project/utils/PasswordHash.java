package project.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHash implements PasswordEncoder {
	private static final String ALGORITHM_MD5 = "MD5";

	private static String md5Encrypt(String data)  {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(ALGORITHM_MD5);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error on password hash.", e);
		}
		byte[] dat = data.getBytes();
		md.update(dat);
		return byteToString(md.digest());
	}

	private static String byteToString(byte[] digest) {
		final StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < digest.length; i++) {
			final int n = digest[i] & 0xFF;
			if (n < 16)
				buf.append("0");
			buf.append(Integer.toString(n, 16));
		}
		return buf.toString();
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return md5Encrypt(rawPassword.toString());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return md5Encrypt(rawPassword.toString()).equals(encodedPassword);
	}
}
