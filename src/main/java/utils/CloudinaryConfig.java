package utils;
// Import the required packages

import com.cloudinary.*;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.util.Map;

public class CloudinaryConfig {

    public static String updloadImage(File image) {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();
        Cloudinary cloudinary = new Cloudinary(dotenv.get("CLOUDINARY_URL"));
        System.out.println(cloudinary.config.cloudName);
        cloudinary.config.secure = true;
        try {
            Map up = cloudinary.uploader().upload(image, ObjectUtils.emptyMap());
            return (String) up.get("secure_url");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
}
