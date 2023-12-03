package x.web3central.belto.service1;


public class Span {

	private int id;
	private String font_size;
	private String font_weight;
	private String font_family;
	private String class_names;
	private String position_left;
	private String position_top;
	private String text;
	
	
	// constructor
	public Span(int i, String markup) {
		this.id = i; // set span index
		parseMarkup(markup); // parse markup for class and style attributes
	}
	
	
	// private method to parse the html markup
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
	
	// private method to parse markup for class values
	private void parseClassMarkup(String markup) {
		String temp = markup.replace("class=", "");
		temp = temp.replace("\"", "");
		this.class_names = temp; // set class names
	}
	
	// private method to parse markup for style attributes 
	private void parseStyleAttributes(String markup) {
		String temp = markup.replace("style=", "");
		temp = temp.replace("\"", "");
		String[] tempArray = temp.split(";");
		for(String t : tempArray) {
			if(t.contains("left:")) {
				this.position_left = t; // set position left
			}
			else if(t.contains("top:")) {
				this.position_top = t; // set position top
			}
		}
	}

	public int getId() {
		return id;
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

	public String getClass_name() {
		return class_names;
	}

	public void setClass_name(String class_name) {
		this.class_names = class_name;
	}

	public String getPosition_left() {
		return position_left;
	}

	public void setPosition_left(String position_left) {
		this.position_left = position_left;
	}

	public String getPosition_top() {
		return position_top;
	}

	public void setPosition_top(String position_top) {
		this.position_top = position_top;
	}

	
}
