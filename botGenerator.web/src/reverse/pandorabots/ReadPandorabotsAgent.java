package reverse.pandorabots;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import reverse.pandorabots.agent.*;
import zipUtils.Unzipper;

// WIP: Clase para leer la información de un bot de Pandorabots
public class ReadPandorabotsAgent {
	XmlMapper mapper = new XmlMapper();

	// Devuelve el agente, esto es: el bot creado con las clases intermedias al
	// modelo final que es CONGA
	public Agent getAgent(File zip) throws IOException {
		// Descomprime el zip que contiene el bot
		File agentFiles = new Unzipper(zip.getCanonicalPath()).unzip();
		if (agentFiles == null) {
			return null;
		}
		// Declaración del modelo intermedio del bot y la lista de ficheros a recorrer
		// para rellenarlo
		Agent fullAgent = new Agent();
		Agent tempAgent;
		List<File> files = new ArrayList<File>();

		// Se añaden los archivos del bot para posterior lectura y eliminación
		files.add(agentFiles);

		// Se van a ir eliminando elementos de la lista de archivos del bot conforme
		// sean leídos
		while (!files.isEmpty()) {
			File currentFile = files.get(0);

			// Si es un directorio, se mira archivo por archivo
			if (currentFile.isDirectory()) {
				for (File f : currentFile.listFiles()) {
					// Lectura de ficheros de código aiml
					if (f.getName().contains(".aiml")) {
						// Se ignoran los ficheros de funciones auxiliares necesarios en Pandorabots
						// únicamente
						if (!f.getName().equals("aimlstandardlibrary.aiml") && !f.getName().equals("utils.aiml")) {
							tempAgent = mapper.readValue(f, Agent.class);
							fullAgent.addCategories(tempAgent.categories);
						}
					}
					// Lectura de sets
					else if (f.getName().contains(".set")) {
						SetFile tempSetFile = mapper.readValue(f, SetFile.class);
						fullAgent.addSetFile(tempSetFile);
					}
					// Lectura de maps
					else if (f.getName().contains(".map")) {
						MapFile tempMapFile = mapper.readValue(f, MapFile.class);
						fullAgent.addMapFile(tempMapFile);
					}
					// Si el zip está separado en subcarpetas
					else if (f.isDirectory()) {
						files.add(f);
					}
				}
			}

			// Si los ficheros no vienen en una estructura de carpetas
			else {
				// Lectura de ficheros de código aiml
				if (currentFile.getName().contains(".aiml")) {
					// Se ignoran los ficheros de funciones auxiliares necesarios en Pandorabots
					// únicamente
					if (!currentFile.getName().equals("aimlstandardlibrary.aiml")
							&& !currentFile.getName().equals("utils.aiml")) {
						tempAgent = mapper.readValue(currentFile, Agent.class);
						fullAgent.addCategories(tempAgent.categories);
					}
				}
				// Lectura de sets
				else if (currentFile.getName().contains(".set")) {
					SetFile tempSetFile = mapper.readValue(currentFile, SetFile.class);
					fullAgent.addSetFile(tempSetFile);
				}

				// Lectura de maps
				else if (currentFile.getName().contains(".map")) {
					MapFile tempMapFile = mapper.readValue(currentFile, MapFile.class);
					fullAgent.addMapFile(tempMapFile);
				}
			}

			// Se suprime el archivo leído de la lista de archivos a leer
			files.remove(currentFile);
		}

		// Se devuelve el bot con su información creada en un modelo intermedio previo a
		// CONGA
		return fullAgent;
	}
}
