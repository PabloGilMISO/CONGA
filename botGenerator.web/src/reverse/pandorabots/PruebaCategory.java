package reverse.pandorabots;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "category")
public class PruebaCategory {
//	@JacksonXmlText(value = true)
	@JacksonXmlProperty(localName = "pattern")
	public String pattern;
//	@JacksonXmlText(value = true)
	@JacksonXmlProperty(localName = "template")
	public String template;

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@Override
	public String toString() {
		return "PruebaCategory [pattern=" + pattern + ", template=" + template + "]";
	}

}
