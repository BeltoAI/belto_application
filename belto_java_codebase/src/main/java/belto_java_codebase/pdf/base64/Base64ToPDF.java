package belto_java_codebase.pdf.base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64ToPDF {
	
	// Instance Variables 
	private String base64_string;
	private String output_path;
	private byte[] pdfData;
	private String file_name;
	
	// Constructor for Base64ToPDF class with 2 String arguments for base64 and output path
	public Base64ToPDF(String b64, String fileName) {
		
		String projectDirectory = System.getProperty("user.dir"); // Get the project directory
		// Specify the target directory
        	String targetDirectory = projectDirectory + File.separator + "target";
        	String outputFolder = targetDirectory + File.separator + "output"; // Specify the output folder (assuming it's named "output" in this example)
        
        	// Create the output folder if it doesn't exist
        	File outputFolderFile = new File(outputFolder);
        	if (!outputFolderFile.exists()) {
            	outputFolderFile.mkdir();
        	}
        
		this.output_path = outputFolder + File.separator; // Set instance value for output_path
		this.base64_string = b64; // Set instance value for base64_string
		this.pdfData = Base64.getDecoder().decode(b64); // Set instance value for pdfData by decoding Base64 string to binary data
		this.file_name = fileName; // Set instance value for file_name
		
    }
	
	public void conversion() throws IOException {
		byte[] data = this.pdfData;
		String file_path_and_name = this.output_path + this.file_name;
		writeBinaryDataToPDF(data, file_path_and_name);
		System.out.println("Conversion Success");
	}

		
	private void writeBinaryDataToPDF(byte[] pdfData, String outputFilePath) throws IOException {
		try (FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {
		    // Write the binary data to the output file
		    outputStream.write(pdfData);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

	public String getBase64_string() {
		return base64_string;
	}

	public String getOutput_path() {
		return output_path;
	}

	public byte[] getPdfData() {
		return pdfData;
	}
	
	public String getFileName() {
		return file_name;
	}
	
		
}
