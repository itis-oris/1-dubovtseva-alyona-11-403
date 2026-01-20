package boardgames.service;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileService {
    private final String uploadPath;

    public FileService(String uploadPath) {
        this.uploadPath = uploadPath;
        new File(uploadPath + File.separator + "profiles").mkdirs();
        new File(uploadPath + File.separator + "games").mkdirs();
    }

    public String saveProfileImage(Part filePart) throws IOException {
        return saveImage(filePart, "profiles");
    }

    public String saveGameImage(Part filePart) throws IOException {
        return saveImage(filePart, "games");
    }

    private String saveImage(Part filePart, String folder) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        String originalFileName = getFileName(filePart);
        String fileExtension = "";

        if (originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID() + fileExtension;
        String filePath = uploadPath + File.separator + folder + File.separator + fileName;

        filePart.write(filePath);

        return folder + "/" + fileName;
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }

    public void deleteImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(uploadPath + File.separator + imagePath);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}