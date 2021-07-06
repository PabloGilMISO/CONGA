package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Condition {
	@JacksonXmlProperty(isAttribute = true)
	public String name;
	@JacksonXmlProperty(localName = "li")
	public List<Option> options;

	public Condition(String varName) {
		this.name = varName;
	}

	public String getVarName() {
		return name;
	}

	public void setVarName(String varName) {
		this.name = varName;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	@Override
	public String toString() {
		return "Condition [name=" + name + ", options=" + options + "]";
	}
}
