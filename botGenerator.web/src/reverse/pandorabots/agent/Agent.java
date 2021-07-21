package reverse.pandorabots.agent;

import java.util.ArrayList;
import java.util.List;

import generator.Action;
import generator.Bot;
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
			for (String language : languages)
				bot.getEntities().addAll(getEntities(language));

		// En caso de no detectar los idiomas del bot se usa inglés
		else
			bot.getEntities().addAll(getEntities("en"));

		// GUARDADO DE LENGUAJES
		for (String language : languages)
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
	public List<Intent> getIntents(List<String> languages) {
		List<Intent> intents = new ArrayList<Intent>();

		for (Category category : categories) {
			Intent intent = GeneratorFactory.eINSTANCE.createIntent();

			// Caso en que la categoría sólo contenga sets
			if (category.pattern.text == null) {
				// Si el pattern del intent no tiene texto ni sets
				if (!category.pattern.sets.isEmpty()) {
					IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
					TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();

					for (SetAttr set : category.pattern.sets) {
						ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE
								.createParameterReferenceToken();
						Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();

						parameter.setName(set.name);
						parameterRef.setParameter(parameter);
						phrase.getTokens().add(parameterRef);
					}

					languageInput.getInputs().add(phrase);
					intent.getInputs().add(languageInput);
				}

				// TODO: Caso de intent con pattern vacío
			}

			// Caso en que contenga texto y pueda o no contener sets
			else {
				// Caso en que contenga fechas
				if (category.pattern.text.contains("* slash * slash *")
						|| category.pattern.text.contains("*slash*slash*")) {
					IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
					TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();

					// Si el template contiene sets
					if (category.template.think.sets != null) {
						List<Set> sets = new ArrayList<Set>(category.template.think.sets);

						String[] tokens = category.pattern.text.split("\\* slash \\* slash \\*");
						for (String token : tokens) {
							Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
							ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE
									.createParameterReferenceToken();
							Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();

							// Caso en que además de fechas haya otros argumentos
							if (category.pattern.text.contains("*")) {
								String[] innerTokens = category.pattern.text.split("\\*");
								for (String innerToken : innerTokens) {
									Literal innerLiteral = GeneratorFactory.eINSTANCE.createLiteral();
									ParameterReferenceToken innerParameterRef = GeneratorFactory.eINSTANCE
											.createParameterReferenceToken();
									Parameter innerParameter = GeneratorFactory.eINSTANCE.createParameter();

									// Guardado de texto previo al parametro
									innerLiteral.setText(innerToken);
									phrase.getTokens().add(innerLiteral);

									// Si hay sets disponibles, se asocia el nombre del parametro al set que haya
									// disponible
									if (!sets.isEmpty()) {
										innerParameter.setName(sets.get(0).name);
										sets.remove(0);
									}
									innerParameter.setDefaultEntity(DefaultEntity.TEXT);
									innerParameterRef.setParameter(innerParameter);
									phrase.getTokens().add(innerParameterRef);
								}
							}

							else {
								// Guardado de texto previo al parametro
								literal.setText(token);
								phrase.getTokens().add(literal);
							}

							// Si hay sets disponibles, se asocia el nombre del parametro al set que haya
							// disponible
							if (!sets.isEmpty()) {
								parameter.setName(sets.get(0).name);
								sets.remove(0);
							}

							parameter.setDefaultEntity(DefaultEntity.DATE);
							parameterRef.setParameter(parameter);
							phrase.getTokens().add(parameterRef);
						}

						// Caso en que contenga sets en el pattern
						if (category.pattern.sets != null) {
							for (SetAttr set : category.pattern.sets) {
								ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE
										.createParameterReferenceToken();
								Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
								Entity entity = GeneratorFactory.eINSTANCE.createEntity();
								
								if (mapFiles != null) {
									for (MapFile mapFile: mapFiles) {
										if (mapFile.name.equals(set.name)) {
											LanguageInput entityLanguageInput = GeneratorFactory.eINSTANCE.createLanguageInput();
											entityLanguageInput.setLanguage(castLanguage(languages.get(0)));
											for (String key: mapFile.content.keySet()) {
												SimpleInput attrVal = GeneratorFactory.eINSTANCE.createSimpleInput();
												
												attrVal.setName(key);
												attrVal.getValues().addAll(mapFile.content.get(key));
												entityLanguageInput.getInputs().add(attrVal);
											}
											
											entity.getInputs().add(entityLanguageInput);
										}
									}
								}
								
								parameter.setEntity(entity);
								entity.setName(set.name);
								parameter.setName(set.name);
								parameter.setDefaultEntity(DefaultEntity.TEXT);
								parameterRef.setParameter(parameter);
								phrase.getTokens().add(parameterRef);
							}
						}
						
						languageInput.getInputs().add(phrase);
						intent.getInputs().add(languageInput);
					}

					// Si no hay sets en el template
					else {
						String[] tokens = category.pattern.text.split("\\* slash \\* slash \\*");
						for (String token : tokens) {
							Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
							ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE
									.createParameterReferenceToken();
							Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();

							// Caso en que además de fechas haya otros argumentos
							if (category.pattern.text.contains("*")) {
								String[] innerTokens = category.pattern.text.split("\\*");
								for (String innerToken : innerTokens) {
									Literal innerLiteral = GeneratorFactory.eINSTANCE.createLiteral();
									ParameterReferenceToken innerParameterRef = GeneratorFactory.eINSTANCE
											.createParameterReferenceToken();
									Parameter innerParameter = GeneratorFactory.eINSTANCE.createParameter();

									// Guardado de texto previo al parametro
									innerLiteral.setText(innerToken);
									phrase.getTokens().add(innerLiteral);

									innerParameter.setDefaultEntity(DefaultEntity.TEXT);
									innerParameterRef.setParameter(innerParameter);
									phrase.getTokens().add(innerParameterRef);
								}
							}

							else {
								// Guardado de texto previo al parametro
								literal.setText(token);
								phrase.getTokens().add(literal);
							}

							// Guardado del parametro
							parameter.setDefaultEntity(DefaultEntity.DATE);
							parameterRef.setParameter(parameter);
							phrase.getTokens().add(parameterRef);
						}

						languageInput.getInputs().add(phrase);
						intent.getInputs().add(languageInput);
					}
				}

				// Caso en que contenga horas
				else if (category.pattern.text.contains("* colon *") || category.pattern.text.contains("*colon*")) {
					IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
					TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();

					// Si el template contiene sets
					if (category.template.think.sets != null) {
						List<Set> sets = new ArrayList<Set>(category.template.think.sets);

						String[] tokens = category.pattern.text.split("\\* colon \\*");
						for (String token : tokens) {
							Literal literal = GeneratorFactory.eINSTANCE.createLiteral();

							// Guardado de texto previo al parametro
							literal.setText(token);
							phrase.getTokens().add(literal);

							// Guardado del parametro si el formato es correcto
							if (!sets.isEmpty()) {
								// Caso en que no se guarden los parametros directamente
								if (sets.get(0).srais.isEmpty()) {
									ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE
											.createParameterReferenceToken();
									Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();

									// Caso en que además de guardar horas guarde otros parámetros
									// TODO: Probar
									if (category.pattern.text.contains("*")) {
										String[] innerTokens = category.pattern.text.split("\\*");
										for (String innerToken : innerTokens) {
											Literal innerLiteral = GeneratorFactory.eINSTANCE.createLiteral();
											ParameterReferenceToken innerParameterRef = GeneratorFactory.eINSTANCE
													.createParameterReferenceToken();
											Parameter innerParameter = GeneratorFactory.eINSTANCE.createParameter();

											// Guardado de texto previo al parametro
											innerLiteral.setText(innerToken);
											parameter.setDefaultEntity(DefaultEntity.TEXT);
											phrase.getTokens().add(innerLiteral);

//										parameter.setName(sets.get(0).name);
											innerParameterRef.setParameter(innerParameter);
											phrase.getTokens().add(innerParameterRef);
										}
									}

									parameter.setName(sets.get(0).name);
									parameter.setDefaultEntity(DefaultEntity.TIME);
									sets.remove(0);
									parameterRef.setParameter(parameter);
									phrase.getTokens().add(parameterRef);
								}
							}
						}

						languageInput.getInputs().add(phrase);
						intent.getInputs().add(languageInput);
					}

					// Caso en que el template no contiene sets
					else {
						String[] tokens = category.pattern.text.split("\\*");
						for (String token : tokens) {
							Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
							ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE
									.createParameterReferenceToken();
							Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();

							// Guardado de texto previo al parametro
							literal.setText(token);
							phrase.getTokens().add(literal);

							parameter.setDefaultEntity(DefaultEntity.TEXT);
							parameterRef.setParameter(parameter);
							phrase.getTokens().add(parameterRef);
						}

						// Caso en que contenga sets en el pattern
						if (category.pattern.sets != null) {
							for (SetAttr set : category.pattern.sets) {
								ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE
										.createParameterReferenceToken();
								Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
								Entity entity = GeneratorFactory.eINSTANCE.createEntity();
								
								if (mapFiles != null) {
									for (MapFile mapFile: mapFiles) {
										if (mapFile.name.equals(set.name)) {
											LanguageInput entityLanguageInput = GeneratorFactory.eINSTANCE.createLanguageInput();
											entityLanguageInput.setLanguage(castLanguage(languages.get(0)));
											for (String key: mapFile.content.keySet()) {
												SimpleInput attrVal = GeneratorFactory.eINSTANCE.createSimpleInput();
												
												attrVal.setName(key);
												attrVal.getValues().addAll(mapFile.content.get(key));
												entityLanguageInput.getInputs().add(attrVal);
											}
											
											entity.getInputs().add(entityLanguageInput);
										}
									}
								}
								
								parameter.setEntity(entity);
								entity.setName(set.name);
								parameter.setName(set.name);
								parameter.setDefaultEntity(DefaultEntity.TEXT);
								parameterRef.setParameter(parameter);
								phrase.getTokens().add(parameterRef);
							}
						}
						
						languageInput.getInputs().add(phrase);
						intent.getInputs().add(languageInput);
					}
				}

				// Caso en que no contenga fechas ni horas
				else {
					// Caso en que contenga *s
					if (category.pattern.text.contains("*")) {
						IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
						TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();

						// Si el template contiene sets
						if (category.template.think.sets != null) {
							List<Set> sets = new ArrayList<Set>(category.template.think.sets);

							String[] tokens = category.pattern.text.split("\\*");
							for (String token : tokens) {
								Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
								ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE
										.createParameterReferenceToken();
								Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();

								// Guardado de texto previo al parametro
								literal.setText(token);
								phrase.getTokens().add(literal);

								// Guardado del parametro si el formato es correcto
								if (!sets.isEmpty()) {
									parameter.setName(sets.get(0).name);
									sets.remove(0);
								}

								parameter.setDefaultEntity(DefaultEntity.TEXT);
								parameterRef.setParameter(parameter);
								phrase.getTokens().add(parameterRef);
							}

							// Caso en que contenga sets en el pattern
							if (category.pattern.sets != null) {
								for (SetAttr set : category.pattern.sets) {
									ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE
											.createParameterReferenceToken();
									Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
									Entity entity = GeneratorFactory.eINSTANCE.createEntity();
									
									if (mapFiles != null) {
										for (MapFile mapFile: mapFiles) {
											if (mapFile.name.equals(set.name)) {
												LanguageInput entityLanguageInput = GeneratorFactory.eINSTANCE.createLanguageInput();
												entityLanguageInput.setLanguage(castLanguage(languages.get(0)));
												for (String key: mapFile.content.keySet()) {
													SimpleInput attrVal = GeneratorFactory.eINSTANCE.createSimpleInput();
													
													attrVal.setName(key);
													attrVal.getValues().addAll(mapFile.content.get(key));
													entityLanguageInput.getInputs().add(attrVal);
												}
												
												entity.getInputs().add(entityLanguageInput);
											}
										}
									}
									
									parameter.setEntity(entity);
									entity.setName(set.name);
									parameter.setName(set.name);
									parameter.setDefaultEntity(DefaultEntity.TEXT);
									parameterRef.setParameter(parameter);
									phrase.getTokens().add(parameterRef);
								}
							}
							
							languageInput.getInputs().add(phrase);
							intent.getInputs().add(languageInput);
						}

						// Si el template no contiene sets
						else {
							String[] tokens = category.pattern.text.split("\\*");
							for (String token : tokens) {
								Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
								ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE
										.createParameterReferenceToken();
								Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();

								// Guardado de texto previo al parametro
								literal.setText(token);
								phrase.getTokens().add(literal);

								parameter.setDefaultEntity(DefaultEntity.TEXT);
								parameterRef.setParameter(parameter);
								phrase.getTokens().add(parameterRef);
							}

							// Caso en que contenga sets en el pattern
							if (category.pattern.sets != null) {
								for (SetAttr set : category.pattern.sets) {
									ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE
											.createParameterReferenceToken();
									Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
									Entity entity = GeneratorFactory.eINSTANCE.createEntity();
									
									if (mapFiles != null) {
										for (MapFile mapFile: mapFiles) {
											if (mapFile.name.equals(set.name)) {
												LanguageInput entityLanguageInput = GeneratorFactory.eINSTANCE.createLanguageInput();
												entityLanguageInput.setLanguage(castLanguage(languages.get(0)));
												for (String key: mapFile.content.keySet()) {
													SimpleInput attrVal = GeneratorFactory.eINSTANCE.createSimpleInput();
													
													attrVal.setName(key);
													attrVal.getValues().addAll(mapFile.content.get(key));
													entityLanguageInput.getInputs().add(attrVal);
												}
												
												entity.getInputs().add(entityLanguageInput);
											}
										}
									}
									
									parameter.setEntity(entity);
									entity.setName(set.name);
									parameter.setName(set.name);
									parameter.setDefaultEntity(DefaultEntity.TEXT);
									parameterRef.setParameter(parameter);
									phrase.getTokens().add(parameterRef);
								}
							}
							
							languageInput.getInputs().add(phrase);
							intent.getInputs().add(languageInput);
						}

					}

					// Caso en que el intent no contenga *s
					else {
						IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
						TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();
						Literal literal = GeneratorFactory.eINSTANCE.createLiteral();

						literal.setText(category.pattern.text);
						phrase.getTokens().add(literal);

						// Caso en que contenga sets en el pattern
						if (category.pattern.sets != null) {
							for (SetAttr set : category.pattern.sets) {
								ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE
										.createParameterReferenceToken();
								Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
								Entity entity = GeneratorFactory.eINSTANCE.createEntity();
								
								if (mapFiles != null) {
									for (MapFile mapFile: mapFiles) {
										if (mapFile.name.equals(set.name)) {
											LanguageInput entityLanguageInput = GeneratorFactory.eINSTANCE.createLanguageInput();
											entityLanguageInput.setLanguage(castLanguage("en"));
											for (String key: mapFile.content.keySet()) {
												SimpleInput attrVal = GeneratorFactory.eINSTANCE.createSimpleInput();
												
												attrVal.setName(key);
												attrVal.getValues().addAll(mapFile.content.get(key));
												entityLanguageInput.getInputs().add(attrVal);
											}
											
											entity.getInputs().add(entityLanguageInput);
										}
									}
								}
								
								parameter.setEntity(entity);
								entity.setName(set.name);
								parameter.setName(set.name);
								parameter.setDefaultEntity(DefaultEntity.TEXT);
								parameterRef.setParameter(parameter);
								phrase.getTokens().add(parameterRef);
							}
						}

						languageInput.getInputs().add(phrase);
						intent.getInputs().add(languageInput);
					}
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
