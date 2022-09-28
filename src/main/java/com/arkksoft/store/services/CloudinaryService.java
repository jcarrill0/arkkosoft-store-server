package com.arkksoft.store.services;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.arkksoft.store.global.Credentials;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {
    private Cloudinary cloudinary;

    public CloudinaryService() {
        cloudinary = new Cloudinary(Credentials.CLOUDINARY_URL);
    }

    @SuppressWarnings("unchecked")
    public String upload(MultipartFile multipartFile) {
        String result = "";
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
            result = uploadResult.get("secure_url").toString();
        } catch (Exception e) {
            String message = "Failed to load to Cloudinary the image file: " + multipartFile.getName();
            throw new RuntimeException(message);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> deleteFile(String id) {
        Map<String, Object> result = null;
        try {
            result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        } catch (Exception e) {
            String message = "Failed to delete to Cloudinary the image file";
            //throw new BusinessException("409", message, HttpStatus.CONFLICT);
            throw new RuntimeException(message);
        }

        return result;
    }
}
