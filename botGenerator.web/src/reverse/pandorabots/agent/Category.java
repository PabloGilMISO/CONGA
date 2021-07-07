package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "category")
public class Category {
	@JacksonXmlText(value = true)
	public String pattern;
	@JacksonXmlText(value = true)
	public String that;
	@JacksonXmlProperty(localName = "think")
	public List<Set> think;

	public Category(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getThat() {
		return that;
	}

	public void setThat(String that) {
		this.that = that;
	}

	public List<Set> getThink() {
		return think;
	}

	public void setThink(List<Set> think) {
		this.think = think;
	}

	@Override
	public String toString() {
		return "Category [pattern=" + pattern + ", that=" + that + ", think=" + think + "]";
	}
}
