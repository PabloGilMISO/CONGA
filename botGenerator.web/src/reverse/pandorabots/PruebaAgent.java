package reverse.pandorabots;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "ailm")
public class PruebaAgent {
//	@JacksonXmlElementWrapper(useWrapping = false)
//	@JacksonXmlProperty(localName = "category")
//	@JacksonXmlProperty(localName = "category")
	@JacksonXmlElementWrapper(localName = "category")
	public List<PruebaCategory> categories;

	public List<PruebaCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<PruebaCategory> categories) {
		this.categories = categories;
	}

	public void addCategories(List<PruebaCategory> categories) {
		if (this.categories == null)
			this.categories = new ArrayList<PruebaCategory>(categories);
		
		else
			this.categories.addAll(categories);
	}

	@Override
	public String toString() {
		return "PruebaAgent [categories=" + categories + "]";
	}

}
