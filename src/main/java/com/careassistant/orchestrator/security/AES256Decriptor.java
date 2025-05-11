package com.careassistant.orchestrator.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AES256Decriptor {

	@Value("${aes.key}")
	private String base64Key;

	private SecretKey secretKey;

	@PostConstruct
	public void init() {
		byte[] decodedKey = Base64.getDecoder().decode(base64Key);
		this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
	}

	public String decrypt(String encryptedText) throws Exception {
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
		byte[] decrypted = cipher.doFinal(decodedBytes);
		return new String(decrypted, StandardCharsets.UTF_8);
	}

}
