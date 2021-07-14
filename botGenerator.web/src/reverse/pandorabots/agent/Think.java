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

	public Think() {
	}

	public Think(List<Set> sets, List<Srai> srais) {
		this.sets = sets;
		this.srais = srais;
	}

	public Think(List<Set> sets) {
		this.sets = sets;
	}

	public List<Set> getSets() {
		return sets;
	}

	public void setSets(List<Set> sets) {
		this.sets = sets;
	}

	@Override
	public String toString() {
		return "Think [sets=" + sets + ", srais=" + srais + "]";
	}
}
