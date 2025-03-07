package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;


@Service
public class Md5HashGenerator {

	public String CalculateHash(String filepath) throws NoSuchAlgorithmException, IOException
	{
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		try (InputStream is = Files.newInputStream(Paths.get(filepath));
		     DigestInputStream dis = new DigestInputStream(is, md)) 
		{
			while (dis.read() != -1) {
			}
		}
		byte[] digest = md.digest();
		
		return toHex(digest); 
		
	}
	
	public String toHex(byte[] bytes) {
	    BigInteger bi = new BigInteger(1, bytes);
	    return String.format("%0" + (bytes.length << 1) + "x", bi); 

	}
	
}
