package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "think")
public class Think {
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "set")
	public List<Set> sets;
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "srai")
	public List<Srai> srais;
	@JacksonXmlText
	public String text;

	public Think() {
	}

	public Think(List<Set> sets, List<Srai> srais, String text) {
		this.sets = sets;
		this.srais = srais;
		this.text = text;
	}

	public Think(List<Set> sets, List<Srai> srais) {
		this.sets = sets;
		this.srais = srais;
	}

//	public Think(List<Set> sets) {
//		this.sets = sets;
//	}

	public Think(List<?> things) {
		System.out.println(things);
		if (things.get(0) instanceof Set)
			this.sets = (List<Set>) things;
		
		else
			this.srais = (List<Srai>) things;
	}
//	public Think(List<Srai> srais) {
//		this.srais = srais;
//	}

	public List<Set> getSets() {
		return sets;
	}

	public void setSets(List<Set> sets) {
		this.sets = sets;
	}

	public List<Srai> getSrais() {
		return srais;
	}

	public void setSrais(List<Srai> srais) {
		this.srais = srais;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Think [sets=" + sets + ", srais=" + srais + ", text=" + text + "]";
	}
//	@Override
//	public String toString() {
//		return "Think [sets=" + sets + ", srais=" + srais + "]";
//	}

}
