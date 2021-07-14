package reverse.pandorabots.agent;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class SetAttr {
	@JacksonXmlText
	public String name;

	public SetAttr() {
	}

	public SetAttr(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "SetAttr [name=" + name + "]";
	}
}
