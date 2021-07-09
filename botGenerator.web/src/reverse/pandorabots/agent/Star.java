package reverse.pandorabots.agent;

import java.util.UUID;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "star")
public class Star {
//	private String id;
	@JacksonXmlProperty(isAttribute = true)
	public int index;

	public Star() {
//		id = UUID.randomUUID().toString();
	}

	public Star(int index) {
//		id = UUID.randomUUID().toString();
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
//		return "Star [id=" + id + ", index=" + index + "]";
		return "Star [index=" + index + "]";
	}
}
