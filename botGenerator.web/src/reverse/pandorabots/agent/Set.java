package reverse.pandorabots.agent;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Set {
	@JacksonXmlProperty(isAttribute = true)
	public String name;
	public Star star;
	@JacksonXmlProperty(isAttribute = true, localName = "star")
	public String getVar;

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

	@Override
	public String toString() {
		return "Set [name=" + name + ", star=" + star + ", getVar=" + getVar + "]";
	}
}
