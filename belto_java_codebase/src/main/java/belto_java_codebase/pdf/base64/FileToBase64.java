package belto_java_codebase.pdf.base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.IOUtils;


public class FileToBase64 {
	
	private String base64;
	private byte[] byteArray;
	private String file_path;
	private String file_type;
	private String file_name;
	
	public FileToBase64(File file) throws IOException {
		convertFileToBase64(file);
		this.file_type = getFileExtension(file);
		this.file_path = file.getAbsolutePath();
		this.file_name = file.getName();
    }


    // private method to convert the file to Base64
    private void convertFileToBase64(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            // Extract the content of the file as bytes
            byte[] fileBytes = IOUtils.toByteArray(fileInputStream);
            byteArray = fileBytes;

            // Encode the bytes into a Base64 string
            base64 = Base64.getEncoder().encodeToString(fileBytes);
        }
    }
    
    // method to get the file extension
    private String getFileExtension(File file) {
        String fileName = file.getName();
        int lastDotIndex = fileName.lastIndexOf('.');
        
        // Check if a dot is present in the file name and if it is not the last character
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        } else {
            return "No file extension";
        }
    }
    
    
 // Getter methods
    public String getBase64String() {
        return base64;
    }
    
    public byte[] getBytes() {
    	return byteArray;
    }
    
    public String getFileType() {
    	return file_type;
    }
    
    public String getFilePath() {
    	return file_path;
    }
    
    public String getFileName() {
    	return file_name;
    }
	

}
