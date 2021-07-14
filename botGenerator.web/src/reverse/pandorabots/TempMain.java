package reverse.pandorabots;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import reverse.pandorabots.agent.Agent;
import reverse.pandorabots.agent.Category;
import reverse.pandorabots.agent.MapFile;
import reverse.pandorabots.agent.Set;
import reverse.pandorabots.agent.SetFile;
import reverse.pandorabots.agent.Srai;
import zipUtils.Unzipper;

public class TempMain {

	// Clase temporal para probar la conversi�n de Pandorabots a CONGA
	public static void main(String[] args) throws IOException {
		String pandorabotsPath = "C:/CONGA/pandorabots/veterinaryCenterPruebas.zip";
//		String pandorabotsPath = "C:/Users/pablo/CONGA/pandorabots/prueba.zip";
//		String pandorabotsPath = "C:/CONGA/pandorabots/prueba.zip";
		File zip = new File(pandorabotsPath);
		ReadPandorabotsAgent reader = new ReadPandorabotsAgent();
		Agent fullAgent = reader.getAgent(zip);
		
		System.out.println(fullAgent);

		// Pruebas
//		System.out.println(getPruebaAgent(zip));
	}

	public static PruebaAgent getPruebaAgent(File zip) throws JsonParseException, JsonMappingException, IOException {
//		JacksonXmlModule module = new JacksonXmlModule();
//		module.setDefaultUseWrapper(false);
		XmlMapper mapper = new XmlMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
				.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
//				.configure(DeserializationFeature.WRAP_EXCEPTIONS, true);

//		SimpleModule module = new SimpleModule("configModule", com.fasterxml.jackson.core.Version.unknownVersion());
//		module.addDeserializer(Set.class, new DeSerializer());
//		module.setDeserializerModifier(new BeanDeserializerModifier()

		
		
		//	    {
//	      @Override public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer)
//	      {
//	        if (beanDesc.getBeanClass() == Set.class)
//	          return new UserEventDeserializer(deserializer);
//	        return deserializer;
//	      }
//	    });
//		mapper.registerModule(module);
		
//		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
//				.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

//		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

//		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
//				DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
//				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
//						DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);

		// Descomprime el zip que contiene el bot
		File agentFiles = new Unzipper(zip.getCanonicalPath()).unzip();
		if (agentFiles == null)
			return null;

		// Declaraci�n del modelo intermedio del bot y la lista de ficheros a recorrer
		// para rellenarlo
		PruebaAgent fullAgent = new PruebaAgent();
		PruebaAgent tempAgent;
		CollectionType tempCol = mapper.getTypeFactory().constructCollectionType(List.class, Category.class);
		List<PruebaCategory> tempCats;
		List<File> files = new ArrayList<File>();

		// Se a�aden los archivos del bot para posterior lectura y eliminaci�n
		files.add(agentFiles);

		// Se van a ir eliminando elementos de la lista de archivos del bot conforme
		// sean le�dos
		while (!files.isEmpty()) {
			File currentFile = files.get(0);

			// Si es un directorio, se mira archivo por archivo
			if (currentFile.isDirectory()) {
				for (File f : currentFile.listFiles()) {
					// Lectura de ficheros de c�digo aiml
					if (f.getName().contains(".aiml")) {
						// Se ignoran los ficheros de funciones auxiliares necesarios en Pandorabots
						// �nicamente
						if (!f.getName().equals("aimlstandardlibrary.aiml") && !f.getName().equals("utils.aiml")) {
//							tempAgent = mapper.readValue(f, PruebaAgent.class);
//							if (tempAgent.categories != null)
//								fullAgent.addCategories(tempAgent.categories);
							tempCats = mapper.readValue(f, tempCol);
							System.out.println(tempCats);
						}
					}
					// Si el zip est� separado en subcarpetas
					else if (f.isDirectory()) {
						files.add(f);
					}
				}
			}

			// Si los ficheros no vienen en una estructura de carpetas
			else {
				// Lectura de ficheros de c�digo aiml
				if (currentFile.getName().contains(".aiml")) {
					// Se ignoran los ficheros de funciones auxiliares necesarios en Pandorabots
					// �nicamente
					if (!currentFile.getName().equals("aimlstandardlibrary.aiml")
							&& !currentFile.getName().equals("utils.aiml")) {
//						tempAgent = mapper.readValue(currentFile, PruebaAgent.class);
//						fullAgent.addCategories(tempAgent.categories);
						tempCats = mapper.readValue(currentFile, tempCol);
						System.out.println(tempCats);
					}
				}
			}

			// Se suprime el archivo le�do de la lista de archivos a leer
			files.remove(currentFile);
		}

		// Se devuelve el bot con su informaci�n creada en un modelo intermedio previo a
		// CONGA
		return fullAgent;
	}
}

//class UnEscapedSerializaer extends JsonDeserializer<Set> {
//
//	@Override
//	public Set deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//		String s = jp.getValueAsString();
//		return new Set(s);
//
//	}
//}

//class DeSerializer extends StdDeserializer<Set> {
//
//	protected DeSerializer() {
//		super(Set.class);
//	}
//
//	@Override
//	public Set deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
//		// use p.getText() and p.nextToken to navigate through the xml and construct
//		// Person object
//		return new Set("nombre", p.getText());
//	}
//}