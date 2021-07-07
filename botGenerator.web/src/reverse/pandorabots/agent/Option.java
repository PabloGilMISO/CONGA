package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "li")
public class Option {
	@JacksonXmlProperty(isAttribute = true)
	public String value;
	public Star star;
	@JacksonXmlProperty(localName = "srai")
	public List<String> links;
	public Condition condition;

	public Option(String value) {
		this.value = value;
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

	@Override
	public String toString() {
		return "Option [value=" + value + ", star=" + star + ", links=" + links + ", condition=" + condition + "]";
	}
}
