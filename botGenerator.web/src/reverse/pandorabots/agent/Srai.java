package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "srai")
public class Srai {
	@JacksonXmlText
	public String text;
	@JacksonXmlElementWrapper(localName = "star")
	@JsonSetter(nulls=Nulls.AS_EMPTY)
	public List<Star> stars;
	
	public Srai() {}
	
	public Srai(String text, List<Star> stars) {
		this.text = text;
		this.stars = stars;
	}
	
	public Srai(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Star> getStars() {
		return stars;
	}

	public void setStars(List<Star> stars) {
		this.stars = stars;
	}
}
