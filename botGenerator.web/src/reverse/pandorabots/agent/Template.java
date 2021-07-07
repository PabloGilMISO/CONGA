package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "template")
public class Template {
	@JacksonXmlProperty(localName = "srai")
	public List<String> links;
	@JacksonXmlProperty
	public String text;
	@JacksonXmlProperty(localName = "condition")
	public Condition condition;
	
	public Template(String text) {
		this.text = text;
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

	@Override
	public String toString() {
		return "Template [links=" + links + ", text=" + text + ", condition=" + condition + "]";
	}
}
