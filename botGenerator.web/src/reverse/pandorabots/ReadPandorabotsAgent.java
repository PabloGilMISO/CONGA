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

import reverse.dialogflow.agent.Agent;
import reverse.dialogflow.agent.entities.Entity;
import reverse.dialogflow.agent.entities.Entry;
import reverse.dialogflow.agent.entities.EntryLanguage;
import reverse.dialogflow.agent.intents.Intent;
import reverse.dialogflow.agent.intents.TrainingPhrase;
import reverse.dialogflow.agent.intents.UserSaysLanguage;
import zipUtils.Unzipper;

// Clase para leer la información de un bot de Pandorabots
// TODO: En este momento es la clase para leer un bot de Dialogflow
public class ReadPandorabotsAgent {
//	private ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//			.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
//			.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
	XmlMapper mapper = new XmlMapper();

	// Devuelve el agente, esto es: el bot creado con las clases intermedias al modelo final que es CONGA
	public Agent getAgent(File zip) throws IOException {
		// Descomprime el zip que contiene el bot 
		// TODO: (pandorabots devuelve un zip con todos los elementos juntos 
		// aunque sean de distinto tipo metidos juntos en una misma carpeta)
		File agentFiles = new Unzipper(zip.getCanonicalPath()).unzip();
		if (agentFiles == null) {
			return null;
		}
		// Declaración de variables para guardar las distintas partes del bot
		String agentName = agentFiles.getName();
		Agent agent = null;
		List<Intent> intents = new ArrayList<>();
		List<Entity> entities = new ArrayList<>();
		List<File> files = new ArrayList<File>();
		
		// Se añaden los archivos del bot para posterior lectura y eliminación
		files.add(agentFiles);

		// Se van a ir eliminando elementos de la lista de archivos del bot
		boolean agentCreated = false;
		while (agentCreated == false && !files.isEmpty()) {
			// Se coge el primer archivo de la lista
			File currentFile = files.get(0);
			// Si es un directorio, se mira archivo por archivo
			if (currentFile.isDirectory()) {
				for (File f : currentFile.listFiles()) {
					// Se lee el archivo principal del bot que contiene sus propiedades en la clase Agent
					if (f.getName().equals("agent.json")) {
						agent = mapper.readValue(f, Agent.class);
						agent.setName(agentName);
						agentCreated = true;
					} 
					// Se leen los intents que se encuentran en su carpeta
					else if (f.getName().equals("intents")) {
						intents = getIntets(f);
					} 
					// Se leen las entities que se encuentran en su carpeta
					else if (f.getName().equals("entities")) {
						entities = getEntities(f);
					} 
					// Si hay algún subdirectorio, se añade a la lista de forma recursiva
					else if (f.isDirectory()) {
						files.add(f);
					}
				}
			}
			// No se leen archivos que no estén contenidos en directorios
			files.remove(currentFile);
		}
		// Si se ha leido algo, entonces se establece la información del bot
		if (agentCreated) {
			agent.setEntities(entities);
			agent.setIntents(intents);
		}
		
		// Se devuelve el bot con su información creada en un modelo intermedio previo a CONGA
		return agent;
	}

	// Lee los intents de la carpeta de intents en un diccionario
	private List<Intent> getIntets(File intentsFolder) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Intent> intents = new HashMap<>();
		// Se recorren todos los archivos de la carpeta de intents
		for (File intentFile : intentsFolder.listFiles()) {
			// Se obtiene el nombre del intent quitando la extensión al archivo del intent que se está leyendo
			String[] nameSplit = intentFile.getName().replace(".json", "").split("_");
			
			// Se separan los intents de acciones de los de inputs del usuario
			// Lectura de intents de inputs del usuario
			if (nameSplit.length >= 3 && nameSplit[nameSplit.length - 2].equals("usersays")) {
				String language = nameSplit[nameSplit.length - 1];
				String name = intentFile.getName().replace("_usersays_" + language + ".json", "");
				
				// Se utiliza el mapper para transformar el json en un objeto del modelo intermedio de dialogflow
				// En primer lugar, se leen las frases de inputs del usuario
				List<TrainingPhrase> phrases = mapper.readValue(intentFile,
						mapper.getTypeFactory().constructCollectionType(List.class, TrainingPhrase.class));
				
				// Después se se crea correctamente el elemento del modelo intermedio correspondiente a los inputs del usuario
				UserSaysLanguage userSaysLanguage = new UserSaysLanguage();
				userSaysLanguage.setLanguage(language);
				userSaysLanguage.setPhrases(phrases);
				
				// Se añade al intent que corresponda de la lista de intents el contenido leído
				Intent intent = intents.get(name);
				if (intent == null) {
					intent = new Intent();
					intent.setName(name);
					intents.put(name, intent);
				}
				intent.addUsersays(userSaysLanguage);
			} 
			
			// Lectura de intents de acciones del bot como respuesta a inputs del usuario
			else {
				String name = intentFile.getName().replace(".json", "");
				// Se utiliza el mapper para leer la información del archivo principal del intent en el modelo intermedio
				Intent intent = mapper.readValue(intentFile, Intent.class);
				Intent intentAux = intents.get(name);
				if (intentAux != null) {
					intent.setUsersays(intentAux.getUsersays());
				}
				
				// Se añade la información del intent a la lista de intents
				intents.put(name, intent);
			}
		}
		
		// Se devuelven todos los intents leídos en forma de lista
		return new ArrayList<>(intents.values());
	}

	// Devuelve la lista de entidades
	private List<Entity> getEntities(File entityFolder) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Entity> entities = new HashMap<>();
		for (File entityFile : entityFolder.listFiles()) {
			String[] nameSplit = entityFile.getName().replace(".json", "").split("_");
			if (nameSplit.length >= 3 && nameSplit[nameSplit.length - 2].equals("entries")) {
				String language = nameSplit[nameSplit.length - 1];
				String name = entityFile.getName().replace("_entries_" + language + ".json", "");
				List<Entry> entries = mapper.readValue(entityFile,
						mapper.getTypeFactory().constructCollectionLikeType(List.class, Entry.class));
				EntryLanguage entryLanguage = new EntryLanguage();
				entryLanguage.setLanguage(language);
				entryLanguage.setEntries(entries);
				Entity entity = entities.get(name);
				if (entity == null) {
					entity = new Entity();
					entity.setName(name);
					entities.put(name, entity);
				}
				entity.addEntry(entryLanguage);

			} else {
				String name = entityFile.getName().replace(".json", "");
				Entity entity = mapper.readValue(entityFile, Entity.class);
				Entity entityAux = entities.get(name);
				if (entityAux != null) {
					entity.setEntriesLanguage(entityAux.getEntriesLanguage());
				}
				entities.put(name, entity);
			}
		}
		return new ArrayList<>(entities.values());
	}
}
