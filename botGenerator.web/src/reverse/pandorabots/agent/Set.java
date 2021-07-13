package reverse.pandorabots.agent;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.annotation.DomHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

//@JsonDeserialize(using = DeSerializer.class)
@JacksonXmlRootElement(localName = "set")
//@XmlAccessorType(XmlAccessType.FIELD)
public class Set {
	@JacksonXmlProperty(isAttribute = true, localName = "name")
	public String name;
////	@JacksonXmlProperty(localName = "star")
////	@JacksonXmlElementWrapper(localName = "star")
////	@JsonSetter(nulls = Nulls.AS_EMPTY)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "star")
	public List<Star> stars;
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "srai")
//	@XmlAnyElement(InnerXmlHandler.class)
//	@XmlElement(name="srai")
	public List<Srai> srais;
	public Get getVar;
	@JacksonXmlText
	public String text;

	public Set() {
	}

	public Set(String name, List<Star> stars, List<Srai> srais, Get getVar, String text) {
		super();
		this.name = name;
		this.stars = stars;
		this.srais = srais;
		this.getVar = getVar;
		this.text = text;
	}

	public Set(String name, String text) {
		this.name = name;
		this.text = text;
	}

	public Set(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Star> getStars() {
		return stars;
	}

	public void setStars(List<Star> stars) {
		this.stars = stars;
	}

	public Get getGetVar() {
		return getVar;
	}

	public void setGetVar(Get getVar) {
		this.getVar = getVar;
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
		return "Set [name=" + name + ", stars=" + stars + ", srais=" + srais + ", getVar=" + getVar + ", text=" + text
				+ "]";
	}

}

//class InnerXmlHandler implements DomHandler<String, StreamResult> {
//
//    private static final String START_TAG = "<srai>";
//    private static final String END_TAG = "</srai>";
//
//    private StringWriter xmlWriter = new StringWriter();
//
//    public StreamResult createUnmarshaller(ValidationEventHandler errorHandler) {
//        return new StreamResult(xmlWriter);
//    }
//
//    public String getElement(StreamResult rt) {
//        String xml = rt.getWriter().toString();
//        int beginIndex = xml.indexOf(START_TAG) + START_TAG.length();
//        int endIndex = xml.lastIndexOf(END_TAG);
//        return xml.substring(beginIndex, endIndex);
//    }
//
//    public Source marshal(String n, ValidationEventHandler errorHandler) {
//        try {
//            String xml = START_TAG + n.trim() + END_TAG;
//            StringReader xmlReader = new StringReader(xml);
//            return new StreamSource(xmlReader);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//}

//class DeSerializer extends StdDeserializer<Set> {
//
//	protected DeSerializer() {
//		super(Set.class);
//	}
//
//	@Override
//	public Set deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//		JsonNode node = p.getCodec().readTree(p);
//		String name = node.get("name").toString();
//		String value = node.toString();
//
////		int i = 0;
////		var currentVal = node.get(i);
////		while (currentVal != null) {
////			System.out.println("\n....\n" + currentVal + "\n....\n");
////		}
//
//		// use p.getText() and p.nextToken to navigate through the xml and construct
//		// Person object
////		if(p.getCurrentValue() instanceof List)
////			for (var elems: (p.getCurrentValue()))
//
////		return new Set(name, "\n" + p.getText() + "\n");
//		return new Set(name, "\n----\n" + value + "\n----\n");
//	}
//}