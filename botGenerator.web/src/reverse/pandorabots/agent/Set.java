package reverse.pandorabots.agent;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JsonDeserialize(using = DeSerializer.class)
@JacksonXmlRootElement(localName = "set")
public class Set {
	@JacksonXmlProperty(isAttribute = true, localName = "name")
	public String name;
//	@JacksonXmlProperty(localName = "star")
//	@JacksonXmlElementWrapper(localName = "star")
//	@JacksonXmlElementWrapper(useWrapping = false)
//	@JacksonXmlProperty(localName = "star")
//	@JsonSetter(nulls = Nulls.AS_EMPTY)
//	public List<Star> stars;
//	@JacksonXmlElementWrapper(useWrapping = false)
//	@JacksonXmlProperty(localName = "srai")
//	public List<Srai> srais;
	public Get getVar;
	@JacksonXmlText
	public String text;

	public Set() {
	}

//	public Set(String name, List<Star> stars, List<reverse.pandorabots.agent.Srai> srais, Get getVar, String text) {
//		this.name = name;
//		this.stars = stars;
//		this.srais = srais;
//		this.getVar = getVar;
//		this.text = text;
//	}

//	public Set(String name, List<Star> stars, Get getVar, String text) {
//		this.name = name;
//		this.stars = stars;
//		this.getVar = getVar;
//		this.text = text;
//	}

	public Set(String name, String text) {
		this.name = name;
		this.text = text;
	}

//	public Set(String name, List<Star> stars) {
//		this.name = name;
//		this.stars = stars;
//	}

//	public Set(String name, List<Srai> srais) {
//		this.name = name;
//		this.srais = srais;
//	}

//	public Set(String name, List<?> sraiOrStar) {
//		this.name = name;
//		if (sraiOrStar.get(0) instanceof Star)
//			this.stars = (List<Star>) sraiOrStar;
//		
//		else
//			this.srais = (List<Srai>) sraiOrStar;
//	}
	
	public Set(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public List<Star> getStars() {
//		return stars;
//	}
//
//	public void setStars(List<Star> stars) {
//		this.stars = stars;
//	}

	public Get getGetVar() {
		return getVar;
	}

	public void setGetVar(Get getVar) {
		this.getVar = getVar;
	}

//	public List<Srai> getSrais() {
//		return srais;
//	}
//
//	public void setSrais(List<Srai> srais) {
//		this.srais = srais;
//	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "Set [name=" + name + ", getVar=" + getVar + ", text=" + text + "]";
	}

//	@Override
//	public String toString() {
//		return "Set [name=" + name + ", stars=" + stars + ", srais=" + srais + ", getVar=" + getVar + ", text=" + text
//				+ "]";
//	}
	
	
}

class DeSerializer extends StdDeserializer<Set> {

	protected DeSerializer() {
		super(Set.class);
	}

	@Override
	public Set deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		// use p.getText() and p.nextToken to navigate through the xml and construct
		// Person object
//		if(p.getCurrentValue() instanceof List)
//			for (var elems: (p.getCurrentValue()))
				
		return new Set("nombre", "\n" + p.getText() + "\n");
	}
}