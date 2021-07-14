package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "pattern")
public class Pattern {
	@JacksonXmlText
	public String text;
//	@JacksonXmlText
//	@JacksonXmlElementWrapper(useWrapping = false)
//	public List<String> texts;
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "set")
	public List<SetAttr> sets;

	public Pattern() {
	}

//	public Pattern(String text, List<String> texts, List<SetAttr> sets) {
//		this.text = text;
//		this.texts = texts;
//		this.sets = sets;
//	}

	public Pattern(String text, List<SetAttr> sets) {
		this.text = text;
		this.sets = sets;
	}

	public Pattern(String text) {
		this.text = text;
	}

//	public Pattern(List<?> sets) {
//		try {
//			this.sets = (List<SetAttr>) sets;
//		} catch (Exception e) {
//			this.texts = (List<String>) sets;
//		}
//	}

//	public Pattern(List<SetAttr> sets) {
//		this.sets = sets;
//	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<SetAttr> getSets() {
		return sets;
	}

	public void setSets(List<SetAttr> sets) {
		this.sets = sets;
	}

	@Override
	public String toString() {
		return "Pattern [text=" + text + ", sets=" + sets + "]";
	}

//	public List<String> getTexts() {
//		return texts;
//	}
//
//	public void setTexts(List<String> texts) {
//		this.texts = texts;
//	}

//	@Override
//	public String toString() {
//		return "Pattern [text=" + text + ", texts=" + texts + ", sets=" + sets + "]";
//	}
}
