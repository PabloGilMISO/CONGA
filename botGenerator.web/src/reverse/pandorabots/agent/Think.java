package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "think")
public class Think {
//	@JacksonXmlElementWrapper(localName = "set", useWrapping = false)
//	@JacksonXmlProperty(localName = "set")
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "set")
	public List<Set> sets;

	public Think() {
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
		return "Think [sets=" + sets + "]";
	}
}
