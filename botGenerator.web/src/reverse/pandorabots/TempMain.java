package reverse.pandorabots;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import generator.Bot;
import reverse.pandorabots.agent.Agent;
import reverse.pandorabots.agent.Category;
import zipUtils.Unzipper;

public class TempMain {

	// Clase temporal para probar la conversión de Pandorabots a CONGA
	public static void main(String[] args) throws IOException {
		String pandorabotsPath = "C:/CONGA/pandorabots/veterinaryCenterPruebas.zip";
//		String pandorabotsPath = "C:/Users/pablo/CONGA/pandorabots/prueba.zip";
//		String pandorabotsPath = "C:/CONGA/pandorabots/prueba.zip";
		File zip = new File(pandorabotsPath);
		ReadPandorabotsAgent reader = new ReadPandorabotsAgent();
		Agent fullAgent = reader.getAgent(zip);
		
		Bot bot = fullAgent.getBot();
		
		System.out.println(fullAgent);

		// Pruebas
//		System.out.println(getPruebaAgent(zip));
	}

//	public static PruebaAgent getPruebaAgent(File zip) throws JsonParseException, JsonMappingException, IOException {
//		XmlMapper mapper = new XmlMapper();
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//				.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
//				.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
////				.configure(DeserializationFeature.WRAP_EXCEPTIONS, true);
//
//		// Descomprime el zip que contiene el bot
//		File agentFiles = new Unzipper(zip.getCanonicalPath()).unzip();
//		if (agentFiles == null)
//			return null;
//
//		// Declaración del modelo intermedio del bot y la lista de ficheros a recorrer
//		// para rellenarlo
//		PruebaAgent fullAgent = new PruebaAgent();
//		PruebaAgent tempAgent;
//		CollectionType tempCol = mapper.getTypeFactory().constructCollectionType(List.class, Category.class);
//		List<PruebaCategory> tempCats;
//		List<File> files = new ArrayList<File>();
//
//		// Se añaden los archivos del bot para posterior lectura y eliminación
//		files.add(agentFiles);
//
//		// Se van a ir eliminando elementos de la lista de archivos del bot conforme
//		// sean leídos
//		while (!files.isEmpty()) {
//			File currentFile = files.get(0);
//
//			// Si es un directorio, se mira archivo por archivo
//			if (currentFile.isDirectory()) {
//				for (File f : currentFile.listFiles()) {
//					// Lectura de ficheros de código aiml
//					if (f.getName().contains(".aiml")) {
//						// Se ignoran los ficheros de funciones auxiliares necesarios en Pandorabots
//						// únicamente
//						if (!f.getName().equals("aimlstandardlibrary.aiml") && !f.getName().equals("utils.aiml")) {
//							tempCats = mapper.readValue(f, tempCol);
//							System.out.println(tempCats);
//						}
//					}
//					// Si el zip está separado en subcarpetas
//					else if (f.isDirectory()) {
//						files.add(f);
//					}
//				}
//			}
//
//			// Si los ficheros no vienen en una estructura de carpetas
//			else {
//				// Lectura de ficheros de código aiml
//				if (currentFile.getName().contains(".aiml")) {
//					// Se ignoran los ficheros de funciones auxiliares necesarios en Pandorabots
//					// únicamente
//					if (!currentFile.getName().equals("aimlstandardlibrary.aiml")
//							&& !currentFile.getName().equals("utils.aiml")) {
//						tempCats = mapper.readValue(currentFile, tempCol);
//						System.out.println(tempCats);
//					}
//				}
//			}
//
//			// Se suprime el archivo leído de la lista de archivos a leer
//			files.remove(currentFile);
//		}
//
//		// Se devuelve el bot con su información creada en un modelo intermedio previo a
//		// CONGA
//		return fullAgent;
//	}
}