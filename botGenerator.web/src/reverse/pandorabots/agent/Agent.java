package reverse.pandorabots.agent;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ailm")
public class Agent {
//	@JacksonXmlRootElement(localName = "aiml")
//	@JacksonXmlProperty(localName = "aiml")
//	@JacksonXmlElementWrapper(useWrapping = true)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JacksonXmlProperty(localName = "category")
	public List<Category> categories;
	public List<SetFile> setFiles;
	public List<MapFile> mapFiles;

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public void addCategories(List<Category> categories) {
		this.categories.addAll(categories);
	}

	public List<SetFile> getSetFiles() {
		return setFiles;
	}

	public void setSetFiles(List<SetFile> setFiles) {
		this.setFiles = setFiles;
	}

	public void addSetFiles(List<SetFile> setFiles) {
		this.setFiles.addAll(setFiles);
	}

	public void addSetFile(SetFile setFile) {
		this.setFiles.add(setFile);
	}

	public List<MapFile> getMapFiles() {
		return mapFiles;
	}

	public void setMapFiles(List<MapFile> mapFiles) {
		this.mapFiles = mapFiles;
	}

	public void addMapFiles(List<MapFile> mapFiles) {
		this.mapFiles.addAll(mapFiles);
	}

	public void addMapFile(MapFile mapFile) {
		this.mapFiles.add(mapFile);
	}

	@Override
	public String toString() {
		return "Agent [categories=" + categories + ", setFiles=" + setFiles + ", mapFiles=" + mapFiles + "]";
	}
}
