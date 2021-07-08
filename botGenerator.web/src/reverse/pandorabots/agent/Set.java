package reverse.pandorabots.agent;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "set")
public class Set {
	@JacksonXmlProperty(isAttribute = true)
	public String name;
	public Star star;
	public Get getVar;
	@JacksonXmlText
	public String content;

	public Set() {}
	
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

	public Get getGetVar() {
		return getVar;
	}

	public void setGetVar(Get getVar) {
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
