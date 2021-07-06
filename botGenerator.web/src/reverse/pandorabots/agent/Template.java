package reverse.pandorabots.agent;

import java.util.List;

public class Template {
	public List<String> links;
	public String text;
	public Condition condition;
	
	public Template(String text) {
		this.text = text;
	}

	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
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

	@Override
	public String toString() {
		return "Template [links=" + links + ", text=" + text + ", condition=" + condition + "]";
	}
}
