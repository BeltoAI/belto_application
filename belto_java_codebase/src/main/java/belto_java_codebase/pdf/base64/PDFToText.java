package belto_java_codebase.pdf.base64;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFToText {
	
	// Instance variables
	private String unstructured_text; // Stores all the unstructured text from pdf
	private String file_name;
	private String source_file_path;
	private Map<String, String> keyValueMap = new LinkedHashMap<>(); // Map of key value pairs from the form

	
	// Constructor for PDFToText class with 1 String argument for fileName
	public PDFToText(String fileName) {
		
		// Get the project directory
		String projectDirectory = System.getProperty("user.dir"); 
		// Set path to output folder
        String targetDirectory = projectDirectory + File.separator + "target" + File.separator + "output"; 
        
        // Set values for instance variables
        this.file_name = fileName;
        this.source_file_path = targetDirectory + File.separator + fileName;
       
	}
	
	// public method to set the value of the unstructured_text instance variable 
	public void parseText() throws IOException {
		String path = this.source_file_path;
		this.unstructured_text = readPDFText(path);
	}
	
	// public method to extract key-value pairs from the form data
	public void extractFormData() {
		String path = this.source_file_path;
		
		try (PDDocument document = PDDocument.load(new File(path))) {
            PDDocumentCatalog catalog = document.getDocumentCatalog();
            PDAcroForm acroForm = catalog.getAcroForm();

            if (acroForm != null) {
                extractFormFields(acroForm);
            } else {
                System.out.println("The PDF does not contain a form.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	// Private method to read text from the pdf 
	private String readPDFText(String pdfFilePath) throws IOException {
		try (PDDocument document = PDDocument.load(new java.io.File(pdfFilePath))) {
			// Create a PDFTextStripper object
	        PDFTextStripper textStripper = new PDFTextStripper();
	        // Get the text content from the PDF document
	       textStripper.setSortByPosition(true);
	        
	        return textStripper.getText(document);
	    }
	}
	
	// private method to extract form fields/values from the pdf
	private void extractFormFields(PDAcroForm acroForm) throws IOException {
        for (PDField field : acroForm.getFieldTree()) {
            String fieldName = field.getFullyQualifiedName();
            String fieldValue = field.getValueAsString();
            this.keyValueMap.put(fieldName, fieldValue);
        }
    }
	
	// Getter methods
	public String getUnstructuredText() {
		return unstructured_text;
	}
	
	public String getFileName() {
		return file_name;
	}
	
	public Map<String, String> getKeyValuePairs() {
		return keyValueMap;
	}
	
	

}
