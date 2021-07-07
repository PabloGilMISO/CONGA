package reverse.pandorabots.agent;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "set")
public class Set {
	@JacksonXmlProperty(isAttribute = true)
	public String name;
	public Star star;
	@JacksonXmlProperty(isAttribute = true, localName = "star")
	public String getVar;
	@JacksonXmlProperty
	public String content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Star getStar() {
		return star;
	}

	public void setStar(Star star) {
		this.star = star;
	}

	public String getGetVar() {
		return getVar;
	}

	public void setGetVar(String getVar) {
		this.getVar = getVar;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Set [name=" + name + ", star=" + star + ", getVar=" + getVar + "]";
	}
}
