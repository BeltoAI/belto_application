package service1;

public class SpanContainer {
	
	private int id;
	private int siblingCount;
	private String containerCSS;
	private String class_names;
	private Span[] siblings;
	private String font_size;
	private String font_weight;
	private String font_family;
	
	
	
	public SpanContainer(int i, int sibCnt, String markup) {
		this.id = i;
		this.siblingCount = sibCnt;
		this.siblings = new Span[sibCnt];
		parseMarkup(markup);
	}
	
	
	private void parseMarkup(String html) {
		
		String[] tokens = html.split(" ");
		for(String token : tokens) {
			if(token.contains("class=")) {
				parseClassMarkup(token); // parse class markup 
			}
			else if(token.contains("style=")) {
				parseStyleAttributes(token); // parse style markup
			}
		}
	}
	
	private void parseClassMarkup(String markup) {
		String temp = markup.replace("class=", "");
		temp = temp.replace("\"", "");
		this.class_names = temp; // set class names
	}
	
	private void parseStyleAttributes(String markup) {
		String temp = markup.replace("style=", "");
		temp = temp.replace("\"", "");
		String[] tempArray = temp.split(";");
		for(String t : tempArray) {
			if(t.contains("font-size:")) {
				this.font_size = t;
			}
			else if(t.contains("font-family:")) {
				this.font_family = t;
			}
			else if(t.contains("font-weight")) {
				this.font_weight = t;
			}
		}
	}
	
	public void setSiblings(Span[] sibs) {
		this.siblings = sibs;
	}
	
	public Span[] getSiblings() {
		return siblings;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getSiblingCount() {
		return siblingCount;
	}


	public void setSiblingCount(int siblingCount) {
		this.siblingCount = siblingCount;
	}


	public String getClass_names() {
		return class_names;
	}


	public void setClass_names(String class_names) {
		this.class_names = class_names;
	}


	public String getFont_size() {
		return font_size;
	}


	public void setFont_size(String font_size) {
		this.font_size = font_size;
	}


	public String getFont_weight() {
		return font_weight;
	}


	public void setFont_weight(String font_weight) {
		this.font_weight = font_weight;
	}


	public String getFont_family() {
		return font_family;
	}


	public void setFont_family(String font_family) {
		this.font_family = font_family;
	}
		

}
