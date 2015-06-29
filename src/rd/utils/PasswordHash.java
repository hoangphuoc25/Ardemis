package rd.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordHash {

	private static Logger logger = LoggerFactory.getLogger(PasswordHash.class);

	private static final String ALGORITHMMD5 = "MD5";

	public static String makePasswordHash(String password){
		return md5Encrypt(password);
	}

	private static String md5Encrypt(String data)  {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance(ALGORITHMMD5);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error on password hash.", e);
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
}
