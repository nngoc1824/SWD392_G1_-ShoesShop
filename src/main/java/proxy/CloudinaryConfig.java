package proxy;
// Import the required packages

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

public class CloudinaryConfig {

    public static String updloadImage(Part imagePart) throws Exception {
        File tempFile = null;
        tempFile = File.createTempFile("upload_", ".jpg");
        try (InputStream input = imagePart.getInputStream();
             FileOutputStream output = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        System.out.println(cloudinary.config.cloudName);
        cloudinary.config.secure = true;
        try {
            Map up = cloudinary.uploader().upload(tempFile, ObjectUtils.emptyMap());
            return (String) up.get("secure_url");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
}
