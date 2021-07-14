package reverse.pandorabots.agent;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "category")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {
	public Pattern pattern;
	public String that;
	public Think think;
	public Template template;

	public Category() {}

	public Category(Pattern pattern, String that, Think think, Template template) {
		this.pattern = pattern;
		this.that = that;
		this.think = think;
		this.template = template;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public String getThat() {
		return that;
	}

	public void setThat(String that) {
		this.that = that;
	}

	public Think getThink() {
		return think;
	}

	public void setThink(Think think) {
		this.think = think;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	@Override
	public String toString() {
		return "Category [pattern=" + pattern + ", that=" + that + ", think=" + think + ", template=" + template + "]";
	}

//	@Override
//	public String toString() {
//		return "Category [\n"
//				+ "  pattern=" + pattern + ", \n"
//				+ "  that=" + that + ", \n"
//				+ "  think=" + think + ", \n"
//				+ "  template=" + template + "]";
//	}
	
}
