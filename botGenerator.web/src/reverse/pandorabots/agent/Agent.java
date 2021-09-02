package reverse.pandorabots.agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import generator.Action;
import generator.Bot;
import generator.BotInteraction;
import generator.DefaultEntity;
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
import generator.Text;
import generator.TextInput;
import generator.TextLanguageInput;
import generator.TrainingPhrase;
import generator.UserInteraction;
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
			for (String language : languages)
				bot.getEntities().addAll(getEntities(language));

		// En caso de no detectar los idiomas del bot se usa ingl�s
		else
			bot.getEntities().addAll(getEntities("en"));

		// GUARDADO DE LENGUAJES
		for (String language : languages)
			bot.getLanguages().add(castLanguage(language));

		// GUARDADO DE INTENTS B�SICOS
		bot.getIntents().addAll(getIntents());
		
		// GUARDADO DE FLUJOS EN INTENTS
//		bot.getFlows().addAll(getFlows(null));
		
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

	// Devuelve la lista de lenguajes en que est� escrito el bot
	public List<String> getLanguages() {
		List<String> ret = new ArrayList<String>();

		for (Category category : categories)
			if (category.think != null)
				for (Set set : category.think.sets)
					if (set.name.contains("lang") && !ret.contains(set.text))
						ret.add(set.text);

		return ret;
	}

	// Recoge las entities siempre y cuando se utilicen los maps de Pandorabots como
	// entities
	public List<generator.Entity> getEntities(String language) {
		List<generator.Entity> entities = new ArrayList<generator.Entity>();

		for (MapFile map : mapFiles) {
			Entity entity = GeneratorFactory.eINSTANCE.createEntity();
			LanguageInput languageInput = GeneratorFactory.eINSTANCE.createLanguageInput();

			entity.setName(map.name);
			languageInput.setLanguage(castLanguage(language));

			for (String key : map.content.keySet()) {
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
	public List<Intent> getIntents() {
		List<Intent> intents = new ArrayList<Intent>();

		for (Category category : categories) {
			Intent intent = GeneratorFactory.eINSTANCE.createIntent();

			// Caso en que la categor�a s�lo contenga sets de tipo <set>example</set>
			if (category.pattern.text == null)
				AgentIntentsGetter.addCategoryWithOnlyPatternSets(category, intent);

			// Caso en que contenga texto y pueda o no contener sets
			else {
				// TODO: Gestionar que pueda tener fechas y horas simult�neamente etc
				
				// Caso en que contenga fechas
				if (category.pattern.text.contains("* slash * slash *")
						|| category.pattern.text.contains("*slash*slash*"))
					AgentIntentsGetter.addCategoryWithDate(category, intent, mapFiles);

				// Caso en que contenga horas
				else if (category.pattern.text.contains("* colon *") || 
						 category.pattern.text.contains("*colon*"))
					AgentIntentsGetter.addCategoryWithHour(category, intent, mapFiles);
				
				// Caso base: intents que contengan texto y puedan o no contener par�metros
				else
					AgentIntentsGetter.addCategoryBasic(category, intent, mapFiles);
			}
			
			intents.add(intent);
		}
		return intents;
	}

	// Procesa los datos de Agent para extraer los flujos de conversaci�n
	public List<UserInteraction> getFlows(List<Intent> botIntents) {
		List<UserInteraction> ret = new ArrayList<UserInteraction>();
		List<Intent> intents = botIntents == null ? getIntents() : botIntents;

		for (Category category: categories) {
			if (category.that != null) {
				// TODO: El intent del that es el origen, no el target
				Intent targetIntent = findIntentByThat(intents, category.that);
				if (targetIntent != null) {
					UserInteraction flow = GeneratorFactory.eINSTANCE.createUserInteraction();
					Intent intent = findIntentByPattern(intents, category.pattern.text);
					BotInteraction target = GeneratorFactory.eINSTANCE.createBotInteraction();
					Text targetText = GeneratorFactory.eINSTANCE.createText();
					TextLanguageInput targetLanguage = GeneratorFactory.eINSTANCE.createTextLanguageInput();
					TextInput targetTextInput = GeneratorFactory.eINSTANCE.createTextInput();
					Literal targetLiteral = GeneratorFactory.eINSTANCE.createLiteral();
					
					if (intent != null)
						flow.setIntent(intent);
					
					targetLiteral.setText(getIntentsText(intents).get(targetIntent));
					targetTextInput.getTokens().add(targetLiteral);
					targetLanguage.setLanguage(castLanguage("en"));
					targetLanguage.getInputs().add(targetTextInput);
					targetText.getInputs().add(targetLanguage);
					target.getActions().add(targetText);
					flow.setTarget(target);
					flow.setIntent(intent);
					ret.add(flow);
				}
			}
		}

		return ret;
	}

	// Devuelve el intent cuyo texto coincida con el that
	public Intent findIntentByThat(List<Intent> intents, String that) {
//		Map<Intent, String> intentMap = getIntentsText(intents);
//
//		for (Intent intent : intentMap.keySet()) {
//			String intentPhrase = intentMap.get(intent).replaceAll("[?.!<>#]", " "); 
//			if (intentPhrase.equals(that))
//				return intent;
//		}
		
		for (Category category: categories) {
			List<String> categoryResponses = getCategoryResponses(category);
			
			if (categoryResponses.contains(that)) {
				Intent intent = findIntentByPattern(intents, category.pattern.text);
				return intent;
			}
		}

		return null;
	}

	// Devuelve el intent que coincide con un texto concreto
	public Intent findIntentByPattern(List<Intent> intents, String text) {
		Map<Intent, String> intentMap = getIntentsText(intents);

		for (Intent intent : intentMap.keySet()) {
			String val = intentMap.get(intent);
			if (intentMap.get(intent).contains("*")) {
				List<String> auxList = List.of(intentMap.get(intent).split("\\*"));
				
				if (auxList.isEmpty()) {
					if (intentMap.get(intent).equals(text)) {
						return intent;
					}
				}
				
				else {
					String intentText = intentMap.get(intent).split("\\*")[0];
					
					if (text.contains("*") && !List.of(text.split("\\*")).isEmpty()) {
						String textClean = text.split("\\*")[0];
						
						if (intentText.equals(textClean))
							return intent;
					}
				}
			}
		}
		
		return null;
	}

	// Devuelve los intents con su frase de entrada tal como ha sido le�da de la
	// categor�a
	public Map<Intent, String> getIntentsText(List<Intent> intents) {
		Map<Intent, String> ret = new HashMap<Intent, String>();

		for (Intent intent : intents) {
			String phraseStr = "";

			for (var token : ((TrainingPhrase) intent.getInputs().get(0).getInputs().get(0)).getTokens()) {
				if (token instanceof Literal)
					phraseStr += ((Literal) token).getText();

				else if (token instanceof ParameterReferenceToken) {
					// Si no es una entity, entonces se guardaba como una *
					if (((ParameterReferenceToken) token).getParameter().getName() == null)
						phraseStr += "*";
				}
			}

			ret.put(intent, phraseStr);
		}

		return ret;
	}

	// Devuelve todas las posibles respuestas de una categor�a
	public List<String> getCategoryResponses(Category category) {
		List<String> responses = new ArrayList<String>();
		
		if (category.template.text != null) {
			String clearText = category.template.text;
			clearText = clearText.replace("\n", "").replace("  ", "").replaceAll("[?.!<>#]", " ");
			responses.add(clearText);
		}
		
		if (category.template.condition != null) {
			List<String> conditionResponses = getConditionResponsesRC(category.template.condition, new ArrayList<String>());
			responses.addAll(conditionResponses);
		}
		
		return responses;
	}
	
	// Funcion recursiva para obtener todas las posibles respuestas de un intent con conditions
	public List<String> getConditionResponsesRC(Condition condition, List<String> responses) {
		List<Condition> conditionsLeft = new ArrayList<Condition>();
		for (Option option: condition.options) {
			if (option.text != null)
				responses.add(option.text);

			if (option.condition != null)
				conditionsLeft.add(option.condition);
		}
		
		if (!conditionsLeft.isEmpty())
			for (Condition c: conditionsLeft)
				return getConditionResponsesRC(c, responses);

		return responses;
	}
	
	// Funci�n original de dialogflow.Agent
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
