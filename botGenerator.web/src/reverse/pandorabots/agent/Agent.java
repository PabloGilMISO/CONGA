package reverse.pandorabots.agent;

import java.util.ArrayList;
import java.util.List;

import generator.Action;
import generator.Bot;
import generator.Entity;
import generator.GeneratorFactory;
import generator.Intent;
import generator.IntentLanguageInputs;
import generator.Language;
import generator.LanguageInput;
import generator.Literal;
import generator.Parameter;
import generator.ParameterReferenceToken;
import generator.SimpleInput;
import generator.TrainingPhrase;
import generator.impl.EntityImpl;

public class Agent {
	private String name;
	private List<Category> categories;
	private List<SetFile> setFiles;
	private List<MapFile> mapFiles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public void addCategories(List<Category> categories) {
		if (this.categories == null)
			this.categories = new ArrayList<Category>(categories);

		else
			this.categories.addAll(categories);
	}

	public List<SetFile> getSetFiles() {
		return setFiles;
	}

	public void setSetFiles(List<SetFile> setFiles) {
		this.setFiles = setFiles;
	}

	public void addSetFiles(List<SetFile> setFiles) {
		if (this.setFiles == null)
			this.setFiles = new ArrayList<SetFile>();

		this.setFiles.addAll(setFiles);
	}

	public void addSetFile(SetFile setFile) {
		if (this.setFiles == null)
			this.setFiles = new ArrayList<SetFile>();

		this.setFiles.add(setFile);
	}

	public List<MapFile> getMapFiles() {
		return mapFiles;
	}

	public void setMapFiles(List<MapFile> mapFiles) {
		this.mapFiles = mapFiles;
	}

	public void addMapFiles(List<MapFile> mapFiles) {
		if (this.mapFiles == null)
			this.mapFiles = new ArrayList<MapFile>();

		this.mapFiles.addAll(mapFiles);
	}

	public void addMapFile(MapFile mapFile) {
		if (this.mapFiles == null)
			this.mapFiles = new ArrayList<MapFile>();

		this.mapFiles.add(mapFile);
	}

	@Override
	public String toString() {
		String ret = "Agent [name=" + name + "\n" + "categories=\n";

		for (Category c : categories)
			ret += c + "\n";

		return ret + "setFiles=" + setFiles + ", \n" + "mapFiles=" + mapFiles + "\n" + "]";
	}

	public Bot getBot() {
		Bot bot = GeneratorFactory.eINSTANCE.createBot();
		List<String> languages = getLanguages();
		
		bot.setName(name);
		
		// GUARDADO DE ENTITIES
		// Si se han guardado idiomas de la manera esperada
		if (!languages.isEmpty())
			for (String language: languages)
				bot.getEntities().addAll(getEntities(language));
		
		// En caso de no detectar los idiomas del bot se usa inglés
		else
			bot.getEntities().addAll(getEntities("en"));

		// GUARDADO DE LENGUAJES
		for (String language: languages)
			bot.getLanguages().add(castLanguage(language));
		
		bot.getIntents().addAll(getIntents(languages));
//		saveAction(request, bot);
//		saveAction(response, bot);
//
//		for (Intent intent : getIntents()) {
//			generator.Intent botIntent = intent.getBotIntent(bot);
//			saveIntent(botIntent, bot);
//			for (Action action : intent.getBotIntentActions(bot, this)) {
//				saveAction(action, bot);
//			}
//		}
//
//		for (Intent intent : getIntents()) {
//			if (intent.getContexts().isEmpty()) {
//				bot.getFlows().add(startFlow(intent, bot));
//			}
//		}

		return bot;
	}
	
	// Devuelve la lista de lenguajes en que está escrito el bot
	public List<String> getLanguages() {
		List<String> ret = new ArrayList<String>();
		
		for (Category category: categories)
			if (category.think != null)
				for (Set set: category.think.sets)
					if (set.name.contains("lang") && !ret.contains(set.text))
						ret.add(set.text);
		
		return ret;
	}
	
	// Recoge las entities siempre y cuando se utilicen los maps de Pandorabots como entities
	public List<generator.Entity> getEntities(String language) {
		List<generator.Entity> entities = new ArrayList<generator.Entity>();
		
		for(MapFile map: mapFiles) {
			Entity entity = GeneratorFactory.eINSTANCE.createEntity();
			LanguageInput languageInput = GeneratorFactory.eINSTANCE.createLanguageInput();
			
			entity.setName(map.name);
			languageInput.setLanguage(castLanguage(language));
			
			for(String key: map.content.keySet()) {
				SimpleInput input = GeneratorFactory.eINSTANCE.createSimpleInput();
				input.setName(key);
				input.getValues().addAll(map.content.get(key));
				languageInput.getInputs().add(input);
			}
			
			entity.getInputs().add(languageInput);
			entities.add(entity);
		}
		
		return entities;
	}
	
	// Recoge los intents de Pandorabots como intents de CONGA
	public List<Intent> getIntents(List<String> languages) {
		List<Intent> intents = new ArrayList<Intent>();
		
		for (Category category: categories) {
			Intent intent = GeneratorFactory.eINSTANCE.createIntent();
			
			if (category.pattern.text == null) {
				if (!category.pattern.sets.isEmpty()) {
					IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
					TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();

					for (SetAttr set: category.pattern.sets) {
						ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
						Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
						
						parameter.setName(set.name);
						parameterRef.setParameter(parameter);
						phrase.getTokens().add(parameterRef);
					}
					
					languageInput.getInputs().add(phrase);
					intent.getInputs().add(languageInput);
				}
			}
			
			else {
				if (category.pattern.text.contains("\\*")) {
					IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
					TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();
					List<Set> sets = new ArrayList<Set>(category.template.think.sets);
					
					String[] tokens = category.pattern.text.split("\\*");				
					for (String token: tokens) {
						Literal literal = GeneratorFactory.eINSTANCE.createLiteral();					
						
						// Guardado de texto previo al parametro
						literal.setText(token);
						phrase.getTokens().add(literal);
						
						// Guardado del parametro si el formato es correcto
						if (!sets.isEmpty()) {						
							ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
							Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
							
							parameter.setName(sets.get(0).name);
							sets.remove(0);
							parameterRef.setParameter(parameter);
							phrase.getTokens().add(parameterRef);
						}
					}
					
					languageInput.getInputs().add(phrase);
					intent.getInputs().add(languageInput);
				}				

				// Creación de un intent simple
				else {
					IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
					TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();
					Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
					
					literal.setText(category.pattern.text);
					
					phrase.getTokens().add(literal);
					languageInput.getInputs().add(phrase);
					intent.getInputs().add(languageInput);
				}
				
				intents.add(intent);
			}
			
		}
		
		return intents;
	}
	
	// Función original de dialogflow.Agent
	public static Language castLanguage(String language) {
		if (language == null) {
			return Language.ENGLISH;
		}
		switch (language) {
		case "en":
			return Language.ENGLISH;
		case "es":
			return Language.SPANISH;
		case "da":
			return Language.DANISH;
		case "de":
			return Language.GERMAN;
		case "fr":
			return Language.FRENCH;
		case "hi":
			return Language.HINDI;
		case "id":
			return Language.INDONESIAN;
		case "it":
			return Language.ITALIAN;
		case "ja":
			return Language.JAPANESE;
		case "ko":
			return Language.KOREAN;
		case "nl":
			return Language.DUTCH;
		case "no":
			return Language.NORWEGIAN;
		case "pl":
			return Language.POLISH;
		case "pt":
			return Language.PORTUGUESE;
		case "ru":
			return Language.RUSIAN;
		case "sv":
			return Language.SWEDISH;
		case "th":
			return Language.THAI;
		case "tr":
			return Language.TURKISH;
		case "uk":
			return Language.UKRANIAN;
		case "zh":
			return Language.CHINESE;
		default:
			return Language.ENGLISH;
		}
	}
}
