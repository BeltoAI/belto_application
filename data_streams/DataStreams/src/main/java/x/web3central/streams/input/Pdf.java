package x.web3central.streams.input;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.text.PDFTextStripper;

public class Pdf {

	private String file_name;
	private String file_owner;
	private int page_count;
	private Map<String, String> meta_data;
	private boolean hasFormFields;
	
	
	public Pdf(String fileName, String fileOwner, Map<String, String> metaData) {
		this.file_name = fileName;
		this.meta_data = metaData;
		this.file_owner = fileOwner;
	}
	
	public Pdf(String fileName, Map<String, String> metaData) {
		this.file_name = fileName;
		this.meta_data = metaData;
		this.file_owner = "unknown";
	}
	
	public PDDocument createPdfDocument(InputStream inputStream) throws IOException {
		 try (PDDocument document = PDDocument.load(inputStream)) {
	            return document;
	        }
		
	}
	
	
	 public String extractTextFromPDF(PDDocument document) throws IOException {
	        PDFTextStripper textStripper = new PDFTextStripper();
	        textStripper.setSortByPosition(true);
	        return textStripper.getText(document);
	        
	  }
	 

	    // Public method to extract form fields/values from the PDF
	    public Map<String, String> extractFormFields(PDDocument document) throws IOException {
	    		Map<String, String> keyVals = new LinkedHashMap<>();
	            PDDocumentCatalog catalog = document.getDocumentCatalog();
	            PDAcroForm acroForm = catalog.getAcroForm();

	            if (acroForm != null) {
	            	this.hasFormFields = true;
	                keyVals = extractFormFields(acroForm);
	               
	            } else {
	            	this.hasFormFields = false;
	                System.out.println("The PDF does not contain a form.");
	              
	            }
	      
			return keyVals;
	    }
	    
	    
		// private method to extract form fields/values from the pdf
		private Map<String, String> extractFormFields(PDAcroForm acroForm) throws IOException {
			Map<String, String> keyVals = new LinkedHashMap<>();
	        for (PDField field : acroForm.getFieldTree()) {
	            String fieldName = field.getFullyQualifiedName();
	            String fieldValue = field.getValueAsString();
	            keyVals.put(fieldName, fieldValue);
	        }
	        
	        return keyVals;
	    }
	        

		public String getFile_name() {
			return file_name;
		}

		public String getFile_owner() {
			return file_owner;
		}

		public int getPage_count() {
			return page_count;
		}

		public Map<String, String> getMeta_data() {
			return meta_data;
		}

		public boolean isAForm() {
			return hasFormFields;
		}
	    
	    
	    
}
