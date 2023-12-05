package belto_java_codebase.pdf.base64;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFToText {

	// Instance variables
	private String unstructured_text;
	private String file_name;
	private String source_file_path;

	// Constructor for PDFToText class with 1 String argument for fileName
	public PDFToText(String fileName) {
		
		// Get the project directory
		String projectDirectory = System.getProperty("user.dir"); 
		// Set path to output folder
        	String targetDirectory = projectDirectory + File.separator + "target" + File.separator + "output"; 
        
        	this.file_name = fileName; // Set value for file_name 
        	this.source_file_path = targetDirectory + File.separator + fileName;  // Set value for source_file_path
       
	}

	// public method to set the value of the unstructured_text instance variable 
	public void parseText() throws IOException {
		String path = this.source_file_path;
		this.unstructured_text = readPDFText(path);
	}

	// private method to extract the text from a pdf file
	private String readPDFText(String pdfFilePath) throws IOException {
		try (PDDocument document = PDDocument.load(new java.io.File(pdfFilePath))) {
			// Create a PDFTextStripper object
	        	PDFTextStripper textStripper = new PDFTextStripper();
	        	// Get the text content from the PDF document
	        	return textStripper.getText(document);
	    }
	}

	// Getter methods
	public String getUnstructuredText() {
		return unstructured_text;
	}
	
	public String getFileName() {
		return file_name;
	}
	


}
