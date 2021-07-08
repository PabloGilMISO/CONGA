package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "think")
public class Think {
	public List<Set> sets;

	public Think() {}
	
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
