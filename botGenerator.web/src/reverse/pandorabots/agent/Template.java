package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "template")
public class Template {
	@JacksonXmlElementWrapper(localName = "srai", useWrapping = false)
	public List<String> links;
	@JacksonXmlText
	public String text;
	public Think think;
	public Condition condition;
	
	public Template() {}
	public Template(List<String> links, String text, Think think, Condition condition) {
		this.links = links;
		this.text = text;
		this.think = think;
		this.condition = condition;
	}
	
	public Template(List<String> links) {
		this.links = links;
	}
	
	public Template(String text) {
		this.text = text;
	}
	
	public Template(Think think) {
		this.think = think;
	}
	
	public Template(Condition condition) {
		this.condition = condition;
	}
	
	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Think getThink() {
		return think;
	}

	public void setThink(Think think) {
		this.think = think;
	}

	@Override
	public String toString() {
		return "Template [links=" + links + ", text=" + text + ", think=" + think + ", condition=" + condition + "]";
	}

//	@Override
//	public String toString() {
//		return "Template [\n"
//				+ "  links=" + links + ", \n"
//				+ "  text=" + text + ", \n"
//				+ "  think=" + think + ", \n"
//				+ "  condition=" + condition + "]";
//	}
}
