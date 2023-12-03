package service1;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class App {

	public static void main(String[] args) throws IOException {


		File file = new File("belto_output.html");
		
		Document doc = Jsoup.parse(file, null);
		
		Elements els = doc.getElementsByTag("span");
		int cnt = 0; // counter for spans with text
		int cnt2 = 0; // counter for span containers
		for(Element el : els) {
			
			String html_markup = el.outerHtml().replaceAll(">(.*?)<", "><");
			html_markup = html_markup.replace(">", " ");
			html_markup = html_markup.replace("<", "");
			html_markup = html_markup.replace("/", "");
			html_markup = html_markup.replace("span ", "");
			String own_text = el.ownText();
			
			int childElementCount = el.childrenSize(); // Record amount of children spans
			
			// If the span is a container... we need to know which span elements it contains
			if(childElementCount > 0) {
				
				
				// Add the childElement count to the sibling counter
				// The indexes of span elements within the range of....
				// siblingCounter and (siblingCounter + childElementCount)...
				// Should be all the span elements contained inside the containing span
				
				int siblingEnd = (cnt + childElementCount - 1);
				String[] containerMarkupOnly = html_markup.split(" ");
				System.out.println("span container index: " + cnt2++);
				System.out.println("---- start ---- " + cnt);
				
			
				
				
				// Now we get all the span's inside the container 
				Elements spans = el.getElementsByTag("span");
				
				SpanContainer spanContainer = new SpanContainer((cnt2 - 1), spans.size(),containerMarkupOnly[0]);
				
				// Create an Span[] to hold all the containing span elements
				// spans.size() is accounting for all the spans, including the container
				// I will try subtractring 1 from it
				Span container[] = new Span[spans.size() - 1];
				int addCounter = 0;
				for(Element span : spans) {
					
					if(span.ownText().length() > 0) {
						String markup = span.outerHtml().replaceAll(">(.*?)<", "><");
						
						markup = markup.replace(">", " ");
						markup = markup.replace("<", "");
						markup = markup.replace("/", "");
						markup = markup.replace("span ", "");
						
						Span newSpan = new Span(cnt, markup); // New span constructor with index and markup arguments passed
						container[addCounter] = newSpan; // Add the span the the container array
						System.out.println("#" + (cnt++) + " " + newSpan.getPosition_left() + newSpan.getPosition_top() + " " + span.ownText());
						
					}
								
				}
				
				
				spanContainer.setSiblings(container);
				System.out.println("Container Count: " + spanContainer.getSiblings().length + " and markup " + spanContainer.getFont_family()  + spanContainer.getFont_size() + spanContainer.getFont_weight());
				
				System.out.println("---- end ---- " + siblingEnd);
				System.out.println();
			}
			
			
		}

	}

}
