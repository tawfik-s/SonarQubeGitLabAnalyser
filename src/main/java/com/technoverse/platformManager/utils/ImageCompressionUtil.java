package com.technoverse.platformManager.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

public class ImageCompressionUtil {

	public String compressBase64Image(String base64Image) throws IOException {
		// Decode the Base64 string into a byte array
		String valid = base64Image.substring(22);
		System.out.println(valid);
		byte[] decodedBytes = Base64.getDecoder().decode(valid);

		// Create an input stream from the decoded byte array
		ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);

		// Compress the image using an image compression algorithm (e.g., JPEG)
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageIO.write(ImageIO.read(inputStream), "png", outputStream);

		// Get the compressed byte array
		byte[] compressedBytes = outputStream.toByteArray();

		// Encode the compressed byte array back into a Base64 string
		String compressedBase64 = Base64.getEncoder().encodeToString(compressedBytes);
		System.out.println(compressedBase64);
		// Close the streams
		inputStream.close();
		outputStream.close();

		return compressedBase64;
	}

}
