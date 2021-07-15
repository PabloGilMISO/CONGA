package reverse.pandorabots.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapFile {
	public Map<String, List<String>> content;

	public MapFile() {
		super();
	}

	public MapFile(Map<String, List<String>> content) {
		this.content = content;
	}

	public Map<String, List<String>> getContent() {
		return content;
	}

	public void setContent(Map<String, List<String>> content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "MapFile [content=" + content + "]";
	}
}
