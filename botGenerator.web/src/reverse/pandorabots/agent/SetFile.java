package reverse.pandorabots.agent;

import java.util.ArrayList;
import java.util.List;

public class SetFile {
	public List<String> content;

	public SetFile() {
		content = new ArrayList<String>();
	}

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "SetFile [content=" + content + "]";
	}
}
