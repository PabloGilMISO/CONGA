package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "li")
public class Option {
	@JacksonXmlProperty(isAttribute = true)
	public String value;
	public Star star;
	@JacksonXmlText
	public String text;
	@JacksonXmlProperty(localName = "srai")
	public List<String> links;
	public Condition condition;

	public Option() {
	}

	public Option(String value, Star star, String text, List<String> links, Condition condition) {
		this.value = value;
		this.star = star;
		this.text = text;
		this.links = links;
		this.condition = condition;
	}

	public Option(String value, String text) {
		super();
		this.value = value;
		this.text = text;
	}

	public Option(String value, Star star) {
		super();
		this.value = value;
		this.star = star;
	}

	public Option(String value, List<String> links) {
		super();
		this.value = value;
		this.links = links;
	}

	public Option(String value, Condition condition) {
		super();
		this.value = value;
		this.condition = condition;
	}

	public Option(String value, String text, Condition condition) {
		super();
		this.value = value;
		this.text = text;
		this.condition = condition;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Star getStar() {
		return star;
	}

	public void setStar(Star star) {
		this.star = star;
	}

	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Option [value=" + value + ", star=" + star + ", text=" + text + ", links=" + links + ", condition="
				+ condition + "]";
	}
}
