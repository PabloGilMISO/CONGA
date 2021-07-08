package reverse.pandorabots.agent;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "star")
public class Star {
	@JacksonXmlProperty(isAttribute = true)
	public int index;
	@JacksonXmlText(value = true)
	public String text;

	public Star() {}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Star [index=" + index + ", text=" + text + "]";
	}
}
