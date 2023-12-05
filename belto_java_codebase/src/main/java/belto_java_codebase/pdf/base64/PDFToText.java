package belto_java_codebase.pdf.base64;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFToText {
	
	private String unstructured_text;
	private String file_name;
	private String source_file_path;
	
	public PDFToText(String fileName) {
		
		// Get the project directory
		String projectDirectory = System.getProperty("user.dir"); 
		// Set path to output folder
        String targetDirectory = projectDirectory + File.separator + "target" + File.separator + "output"; 
        
        
        this.file_name = fileName;
        this.source_file_path = targetDirectory + File.separator + fileName;
       
	}
	
	public void parseText() throws IOException {
		String path = this.source_file_path;
		this.unstructured_text = readPDFText(path);
	}
	
	private String readPDFText(String pdfFilePath) throws IOException {
		try (PDDocument document = PDDocument.load(new java.io.File(pdfFilePath))) {
			// Create a PDFTextStripper object
	        PDFTextStripper textStripper = new PDFTextStripper();
	        // Get the text content from the PDF document
	        return textStripper.getText(document);
	    }
	}
	
	public String getUnstructuredText() {
		return unstructured_text;
	}
	
	public String getFileName() {
		return file_name;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		PDFToText pdf2txt = new PDFToText("javasample.pdf");
		pdf2txt.parseText();
		String txt = pdf2txt.getUnstructuredText();
		System.out.println(txt);
	}
	 
	 

	

}
