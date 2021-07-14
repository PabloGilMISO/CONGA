package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "template")
public class Template {
//	@JacksonXmlElementWrapper(localName = "srai", useWrapping = false)
	@JacksonXmlProperty(localName = "srai")
	public List<Srai> srais;
	@JacksonXmlText
	public String text;
	public Think think;
	public Condition condition;
	public Callapi callapi;

	public Template() {
	}

	public Template(List<Srai> srais, String text, Think think, Condition condition, Callapi callapi) {
		this.srais = srais;
		this.text = text;
		this.think = think;
		this.condition = condition;
		this.callapi = callapi;
	}

	public Template(List<Srai> srais, String text, Think think, Condition condition) {
		this.srais = srais;
		this.text = text;
		this.think = think;
		this.condition = condition;
	}

	public Template(List<Srai> srais) {
		this.srais = srais;
	}

	public Template(String text) {
		this.text = text;
	}

	public Template(Think think) {
		this.think = think;
	}

	public Template(Condition condition) {
		this.condition = condition;
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

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Think getThink() {
		return think;
	}

	public void setThink(Think think) {
		this.think = think;
	}

	public Callapi getCallapi() {
		return callapi;
	}

	public void setCallapi(Callapi callapi) {
		this.callapi = callapi;
	}

	@Override
	public String toString() {
		return "Template [srais=" + srais + ", text=" + text + ", think=" + think + ", condition=" + condition
				+ ", callapi=" + callapi + "]";
	}
}
