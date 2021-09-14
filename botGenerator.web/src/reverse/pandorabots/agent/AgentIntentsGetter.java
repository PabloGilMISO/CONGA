package reverse.pandorabots.agent;

import java.util.ArrayList;
import java.util.List;

import generator.BotInteraction;
import generator.DefaultEntity;
import generator.Entity;
import generator.GeneratorFactory;
import generator.HTTPRequest;
import generator.Intent;
import generator.IntentLanguageInputs;
import generator.KeyValue;
import generator.Language;
import generator.LanguageInput;
import generator.Literal;
import generator.Method;
import generator.Parameter;
import generator.ParameterReferenceToken;
import generator.SimpleInput;
import generator.Text;
import generator.TextInput;
import generator.TextLanguageInput;
import generator.TrainingPhrase;
import generator.UserInteraction;

public class AgentIntentsGetter {
	//// Funciones para gestionar las distintas formas que pueden tomar los intents
	// Función para gestionar los intents que únicamente contengan argumentos de tipo <set>sample</set>
	public static void addCategoryWithOnlyPatternSets(Category category, Intent intent) {
		if (!category.pattern.sets.isEmpty()) {
			IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
			TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();

			for (SetAttr set : category.pattern.sets) {
				ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
				Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();

				parameter.setName(set.name);
				parameterRef.setParameter(parameter);
				phrase.getTokens().add(parameterRef);
				intent.getParameters().add(parameter);
			}

			languageInput.getInputs().add(phrase);
			intent.getInputs().add(languageInput);
		}
	}

	// Función para gestionar los intents cuya entrada contenga fechas en el formato DD/MM/AA
	public static void addCategoryWithDate(Category category, Intent intent, List<MapFile> mapFiles) {
		IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
		TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();

		String[] tokens1 = category.pattern.text.split("\\* slash \\* slash \\*");
		String[] tokens2 = category.pattern.text.split("\\*slash\\*slash\\*");
		
		List<String> tokens;
		if (tokens1.length > tokens2.length && tokens2.length != 0 || tokens1.length == 0) {
			tokens = List.of(tokens1);
		}
		
		else if (tokens1.length == tokens2.length && tokens2.length == 1)
			tokens = List.of(tokens1[0].length() < tokens2[0].length() ? tokens1 : tokens2);
		
		else
			tokens = List.of(tokens2);
		
		// Caso en que el intent únicamente esté formado por la fecha
		if (tokens.size() == 0) {
			ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
			Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
			
			parameter.setDefaultEntity(DefaultEntity.DATE);
			parameterRef.setParameter(parameter);
			phrase.getTokens().add(parameterRef);
			intent.getParameters().add(parameter);
		}
		
		// Caso en que el intent está formado por fecha y otros elementos
		else {
			int dateOccurrences1 = countOccurrences(category.pattern.text, "* slash * slash *");
			int dateOccurrences2 = countOccurrences(category.pattern.text, "*slash*slash*");
			int dateCount = dateOccurrences1 > dateOccurrences2 ? dateOccurrences1 : dateOccurrences2;
			ParameterReferenceToken dateParameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
			Parameter dateParameter = GeneratorFactory.eINSTANCE.createParameter();
			
//			literal.setText(tokens.get(0));
//			phrase.getTokens().add(literal);
//			tokens.remove(0);

			for (String token : tokens) {
				// Caso en que haya horas en el fragmento y otros elementos o no
				if (token.contains("* colon *") || token.contains("*colon*")) {
					String[] innerTokens1 = token.split("\\* colon \\*");
					String[] innerTokens2 = token.split("\\*colon\\*");

					List<String> innerTokens; 
					if (innerTokens1.length > innerTokens2.length && innerTokens2.length != 0)
						innerTokens = List.of(innerTokens1);
					
					else if (innerTokens1.length == innerTokens2.length && innerTokens2.length == 1)
						innerTokens = List.of(innerTokens1[0].length() < innerTokens2[0].length() ? innerTokens1 : innerTokens2);
					
					else
						innerTokens = List.of(innerTokens2);
					
					// Caso en que el fragmento únicamente contenga hora
					if (innerTokens.size() == 0) {
						ParameterReferenceToken innerParameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
						Parameter innerParameter = GeneratorFactory.eINSTANCE.createParameter();
						
						innerParameter.setDefaultEntity(DefaultEntity.TIME);
						innerParameterRef.setParameter(innerParameter);
						phrase.getTokens().add(innerParameterRef);
						intent.getParameters().add(innerParameter);
					}
					
					// Caso en que el fragmento contenga más información además de la hora
					else {
						int timeOccurrences1 = countOccurrences(category.pattern.text, "* colon *");
						int timeOccurrences2 = countOccurrences(category.pattern.text, "*colon*");
						int timeCount = timeOccurrences1 > timeOccurrences2 ? timeOccurrences1 : timeOccurrences2;
						
						for (String innerToken : innerTokens) {
							ParameterReferenceToken hourParameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
							Parameter hourParameter = GeneratorFactory.eINSTANCE.createParameter();
							
							// Caso en que haya otros parámetros en el fragmento
							if (innerToken.contains("*")) {
								String[] innerTokensStar = innerToken.split("\\*");
								for (String innerToken2 : innerTokensStar) {
									Literal innerLiteral = GeneratorFactory.eINSTANCE.createLiteral();
									ParameterReferenceToken innerParameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
									Parameter innerParameter = GeneratorFactory.eINSTANCE.createParameter();
									
									// Guardado de texto previo al parametro
									innerLiteral.setText(innerToken2);
									phrase.getTokens().add(innerLiteral);

									// Guardado del parámetro
									innerParameter.setDefaultEntity(DefaultEntity.TEXT);
									innerParameterRef.setParameter(innerParameter);
									phrase.getTokens().add(innerParameterRef);
									intent.getParameters().add(innerParameter);
								}
							}
							
							// Caso en que el fragmento sólo contenga texto
							else {
								Literal innerLiteral = GeneratorFactory.eINSTANCE.createLiteral();
								
								// Guardado de texto previo al parametro
								innerLiteral.setText(innerToken);
								phrase.getTokens().add(innerLiteral);
							}
							
							// Control de adición de parámetros de tipo hora
							if (timeCount > 0) {
								hourParameter.setDefaultEntity(DefaultEntity.TIME);
								hourParameterRef.setParameter(hourParameter);
								phrase.getTokens().add(hourParameterRef);
								intent.getParameters().add(hourParameter);
								timeCount -= 1;
							}
						}
					}
				}
				
				// Caso en que haya otros parámetros en el fragmento
				else if (token.contains("*")) {				
					String[] innerTokens = category.pattern.text.split("\\*");
					for (String innerToken : innerTokens) {
						Literal innerLiteral = GeneratorFactory.eINSTANCE.createLiteral();
						ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
						Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
						
						// Guardado de texto previo al parametro
						innerLiteral.setText(innerToken);
						phrase.getTokens().add(innerLiteral);

						// Guardado del parámetro
						parameter.setDefaultEntity(DefaultEntity.TEXT);
						parameterRef.setParameter(parameter);
						phrase.getTokens().add(parameterRef);
						intent.getParameters().add(parameter);
					}
				}
				
				// Caso en que el fragmento sólo contenga texto
				else {
					Literal innerLiteral = GeneratorFactory.eINSTANCE.createLiteral();
					
					// Guardado de texto previo al parametro
					innerLiteral.setText(token);
					phrase.getTokens().add(innerLiteral);
				}
				
				// Control de adición de parámetros de tipo fecha
				if (dateCount > 0) {
					dateParameter.setDefaultEntity(DefaultEntity.DATE);
					dateParameterRef.setParameter(dateParameter);
					phrase.getTokens().add(dateParameterRef);
					intent.getParameters().add(dateParameter);
					dateCount -= 1;
				}
			}
			
			// Caso en que contenga sets en el pattern
			if (category.pattern.sets != null)
				intentAddSets(category.pattern.sets, intent, mapFiles, phrase);
		}
		
		languageInput.getInputs().add(phrase);
		intent.getInputs().add(languageInput);
	}

	// Función para gestionar los intents cuya entrada contenga horas en el formato HH:MM
	public static void addCategoryWithHour(Category category, Intent intent, List<MapFile> mapFiles) {
		IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
		TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();
		String[] tokens1 = category.pattern.text.split("\\* colon \\*");
		String[] tokens2 = category.pattern.text.split("\\*colon\\*");

		List<String> tokens; 
		if (tokens1.length > tokens2.length && tokens2.length != 0 || tokens1.length == 0)
			tokens = List.of(tokens1);
		
		else if (tokens1.length == tokens2.length && tokens2.length == 1)
			tokens = List.of(tokens1[0].length() < tokens2[0].length() ? tokens1 : tokens2);
		
		else
			tokens = List.of(tokens2);
		
		// Caso en que el fragmento únicamente contenga hora
		if (tokens.size() == 0) {
			ParameterReferenceToken innerParameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
			Parameter innerParameter = GeneratorFactory.eINSTANCE.createParameter();
			
			innerParameter.setDefaultEntity(DefaultEntity.TIME);
			innerParameterRef.setParameter(innerParameter);
			phrase.getTokens().add(innerParameterRef);
			intent.getParameters().add(innerParameter);
		}
		
		// Caso en que el fragmento contenga más información además de la hora
		else {
			int timeOccurrences1 = countOccurrences(category.pattern.text, "* colon *");
			int timeOccurrences2 = countOccurrences(category.pattern.text, "*colon*");
			int timeCount = timeOccurrences1 > timeOccurrences2 ? timeOccurrences1 : timeOccurrences2;
			
			for (String token : tokens) {
				ParameterReferenceToken hourParameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
				Parameter hourParameter = GeneratorFactory.eINSTANCE.createParameter();
				
				// Caso en que haya otros parámetros en el fragmento
				if (token.contains("*")) {
					String[] innerTokensStar = token.split("\\*");
					for (String innerToken2 : innerTokensStar) {
						Literal innerLiteral = GeneratorFactory.eINSTANCE.createLiteral();
						ParameterReferenceToken innerParameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
						Parameter innerParameter = GeneratorFactory.eINSTANCE.createParameter();
						
						// Guardado de texto previo al parametro
						innerLiteral.setText(innerToken2);
						phrase.getTokens().add(innerLiteral);

						// Guardado del parámetro
						innerParameter.setDefaultEntity(DefaultEntity.TEXT);
						innerParameterRef.setParameter(innerParameter);
						phrase.getTokens().add(innerParameterRef);
						intent.getParameters().add(innerParameter);
					}
				}
				
				// Caso en que el fragmento sólo contenga texto
				else {
					Literal innerLiteral = GeneratorFactory.eINSTANCE.createLiteral();
					
					// Guardado de texto previo al parametro
					innerLiteral.setText(token);
					phrase.getTokens().add(innerLiteral);
				}
				
				// Control de adición de parámetros de tipo hora
				if (timeCount > 0) {
					hourParameter.setDefaultEntity(DefaultEntity.TIME);
					hourParameterRef.setParameter(hourParameter);
					phrase.getTokens().add(hourParameterRef);
					intent.getParameters().add(hourParameter);
					timeCount -= 1;
				}
			}
		}
		
		languageInput.getInputs().add(phrase);
		intent.getInputs().add(languageInput);
	}

	// Función para gestionar los intents básicos, es decir, aquellos que están formados por texto y parámetros o sets
	public static void addCategoryBasic(Category category, Intent intent, List<MapFile> mapFiles) {
		// Caso en que el intent contenga sólo texto
		if (!category.pattern.text.contains("*")) {
			IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
			TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();
			Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
			
			literal.setText(category.pattern.text);
			phrase.getTokens().add(literal);

			// Caso en que contenga sets en el pattern
			if (category.pattern.sets != null)
				intentAddSets(category.pattern.sets, intent, mapFiles, phrase);

			languageInput.getInputs().add(phrase);
			intent.getInputs().add(languageInput);
		}
		
		// Caso en que contenga *s
		else {
			IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
			TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();

			// Sets contenidos en todo el template
			List<Set> sets = getAllTemplateSets(category.template);
			
			String[] tokens = category.pattern.text.split("\\*");
			
			if (tokens.length == 0) {
				Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
				literal.setText("*");
				phrase.getTokens().add(literal);
				intent.setFallbackIntent(true);
			}
			
			int tokenIndex = 1;
			for (String token : tokens) {
				Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
				ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
				Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
				int setsFlag = 0;
				
				// Guardado de texto previo al parametro
				literal.setText(token);
				phrase.getTokens().add(literal);

				// Guardado del parámetro
				// Si el template tiene sets, entonces se busca el nombre del parámetro y se asocia a este
				if (!sets.isEmpty()) {
					for (Set set: sets) {
						if (setsFlag == 0 && set.stars != null && !set.stars.isEmpty()) {
							for (Star star: set.stars) {
								if (star.index == tokenIndex || tokens.length == 1) {
									parameter.setName(set.name);
									setsFlag = 1;
									break;
								}
							}
						} else {
							setsFlag = 0;
							break;
						}
					}
				}
				
				parameter.setDefaultEntity(DefaultEntity.TEXT);
				parameterRef.setParameter(parameter);
				phrase.getTokens().add(parameterRef);
				intent.getParameters().add(parameter);
				
				tokenIndex += 1;
			}
			
			// Caso en que contenga sets en el pattern
			if (category.pattern.sets != null)
				intentAddSets(category.pattern.sets, intent, mapFiles, phrase);

			languageInput.getInputs().add(phrase);
			intent.getInputs().add(languageInput);
		}
	}
	
	//// Funciones para gestionar los flujos de conversación
	// Función para gestionar los flujos de conversación directos, a nivel 1
	public static List<UserInteraction> getMainFlows(Category category, Intent intent, List<Intent> intents) {
		List<UserInteraction> ret = new ArrayList<UserInteraction>();
		List<TextLanguageInput> responses = getAllIntentDirectResponses(category, intents);
		UserInteraction flow = GeneratorFactory.eINSTANCE.createUserInteraction();
		BotInteraction target = GeneratorFactory.eINSTANCE.createBotInteraction();
		
		// Caso en que el target no tenga responses
		if (responses.isEmpty())
			return null;
		
		for (TextLanguageInput response: responses) {
			Text targetText = GeneratorFactory.eINSTANCE.createText();
			
			targetText.getInputs().add(response);
			target.getActions().add(targetText);
		}
		
		// Caso en que tenga una llamada de tipo HTTP (callapi)
		if (category.template.callapi != null)
			addCallapi(category.template.callapi, target);

		flow.setTarget(target);
		flow.setIntent(intent);
		ret.add(flow);
		
		return ret;
	}
	
	// Función para gestionar los flujos de conversación indirectos.
	public static void getOutcomingsFlows(List<UserInteraction> currentFlows, List<UserInteraction> flows) {
		List<UserInteraction> futureFlows = new ArrayList<UserInteraction>();
		
		// DEBUG
//		List<List<?>> debug = new ArrayList<List<?>>();
		
		for (UserInteraction cFlow: currentFlows) {
			// Comparación de intents y targets
			for (UserInteraction flow: flows) {
				List<?> tokens1 = ((Text) cFlow.getTarget().getActions().get(0)).getInputs().get(0).getInputs().get(0).getTokens();
				List<?> tokens2 = ((TrainingPhrase) flow.getIntent().getInputs().get(0).getInputs().get(0)).getTokens();
				
				if (equalTokens(tokens1, tokens2)) {
					cFlow.getTarget().getOutcoming().add(flow);
					futureFlows.add(flow);
					
					// DEBUG
//					debug.add(tokens2);
					
					break;
				}
			}
		}
		
		if (!futureFlows.isEmpty())
			getOutcomingsFlows(futureFlows, flows);
	}
	
	// Función para añadir llamadas HTTP
	public static void addCallapi(Callapi callapi, BotInteraction target) {
		HTTPRequest httpRequest = GeneratorFactory.eINSTANCE.createHTTPRequest();
		
		if (callapi.url != null)
			httpRequest.setURL(callapi.url);
		
		if (callapi.response_code_var != null)
			httpRequest.setName(callapi.response_code_var);
		
		if (callapi.method != null)
			httpRequest.setMethod(castMethod(callapi.method));
		
		// El método por defecto en Pandorabots si no se indica es GET
		else
			httpRequest.setMethod(Method.GET);
		
		if (callapi.queryParams != null) {
			for (CallapiQuery param: callapi.queryParams) {

				// Caso en que el parámetro tenga texto
				if (param.text != null) {
					KeyValue keyValue = GeneratorFactory.eINSTANCE.createKeyValue();
					Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
					
					literal.setText(param.text);

					keyValue.setKey(param.name + "_text");
					keyValue.setValue(literal);
					httpRequest.getData().add(keyValue);
				}
				
				// Caso en que el parámetro tenga llamadas a variables guardadas
				if (param.gets != null) {
					for (Get get: param.gets) {
						KeyValue keyValue = GeneratorFactory.eINSTANCE.createKeyValue();
						ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
						Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
						
						parameter.setName(get.name);
						parameter.setDefaultEntity(DefaultEntity.TEXT);
						parameterRef.setParameter(parameter);
						
						keyValue.setKey(param.name + "_param_" + get.name);
						keyValue.setValue(parameterRef);
						httpRequest.getData().add(keyValue);
					}
				}
				
			}
		}
		
		if (callapi.header != null)
			httpRequest.setHeader("header", callapi.header.name.text);
		
		target.getActions().add(httpRequest);
	}
	
	//// Funciones auxiliares para realizar tareas concretas
	// Función que comprueba si un TextLanguageInput es igual a otro
	public static boolean equalTextInputs(Text input1, Text input2) {
		List<TextLanguageInput> internalInputs1 = input1.getInputs();
		List<TextLanguageInput> internalInputs2 = input2.getInputs();
		
		if (internalInputs1.size() == internalInputs2.size()) {
			for (int i = 0; i < internalInputs1.size(); i++) {
				List<?> tokens1 = internalInputs1.get(i).getInputs().get(0).getTokens();
				List<?> tokens2 = internalInputs2.get(i).getInputs().get(0).getTokens();
				
				return equalTokens(tokens1, tokens2);
			}
			
			return true;
		}
		
		else
			return false;
	}
	
	// Función para comprobar que dos listas de tokens son iguales
	public static boolean equalTokens(List<?> tokens1, List<?> tokens2) {
		if (tokens1.size() == tokens2.size()) {
			for (int j = 0; j < tokens1.size(); j++) {
				var token1 = tokens1.get(j);
				var token2 = tokens2.get(j);
				
				// Caso en que los tokens sean literales
				if (token1 instanceof Literal &&
					token2 instanceof Literal) {
					if (!((Literal) token1).getText().equals(((Literal) token2).getText()))
						return false;
				}
				
				// Caso en que los tokens sean parámetros
				else if (token1 instanceof ParameterReferenceToken && 
						 token2 instanceof ParameterReferenceToken) {
					continue;
				}
				
				else {
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
		
	// Función que comprueba si un HTTPRequest es igual a otro
	// TODO: Mejorar comparación si es conveniente
	public static boolean equalHTTPRequest(HTTPRequest http1, HTTPRequest http2) {
		if (http1.getName().equals(http2.getName()))
			return true;
		
		else
			return false;
	}
	
	// Castea el String con el nombre del método de llamada HTTP a la enumeración definida
	public static Method castMethod(String methodName) {
		if (methodName.toUpperCase().equals("POST"))
			return Method.POST;
		
		else if (methodName.toUpperCase().equals("GET"))
			return Method.GET;
		
		else
			return Method.POST;
	}
	// Devuelve todas las posibles respuestas de una categoría
	public static List<TextLanguageInput> getAllIntentDirectResponses(Category category, List<Intent> intents) {
		List<TextLanguageInput> ret = new ArrayList<TextLanguageInput>();
	
		// Caso en que el template tenga texto
		if (category.template.text != null && !category.template.text.isBlank()) {
			TextLanguageInput languageInput = GeneratorFactory.eINSTANCE.createTextLanguageInput();
			TextInput textInput = GeneratorFactory.eINSTANCE.createTextInput();
			Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
			
			literal.setText(category.template.text);
			textInput.getTokens().add(literal);
			languageInput.setLanguage(Language.ENGLISH);
			languageInput.getInputs().add(textInput);
			ret.add(languageInput);
		}
		
		// Caso en que el template tenga srais
		if (category.template.srais != null) {
			ret.addAll(addResponseSrais(category.template.srais, intents));
		}
		
		// Caso en que el template tenga conditions
		if (category.template.condition != null) {
			ret.addAll(getConditionResponsesREC(category.template.condition, intents, new ArrayList<TextLanguageInput>()));
		}
		
		// Caso en el que el template tenga un think con srais
		if (category.template.think != null && category.template.think.srais != null) {
			ret.addAll(addResponseSrais(category.template.think.srais, intents));
		}
		
		return ret;
	}
	
	// Añade las respuestas de un intent formadas por srais en forma de lista
	public static List<TextLanguageInput> addResponseSrais(List<Srai> srais, List<Intent> intents) {
		List<TextLanguageInput> ret = new ArrayList<TextLanguageInput>();
		
		for (Srai srai: srais) {
			TextLanguageInput languageInput = GeneratorFactory.eINSTANCE.createTextLanguageInput();
			TextInput textInput = GeneratorFactory.eINSTANCE.createTextInput();
			Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
			
			literal.setText(srai.text);
			textInput.getTokens().add(literal);

			// Si el srai tiene referencias a argumentos del pattern, 
			// se busca qué parámetros son en el intent que sea y se añaden
			if (srai.stars != null) {
				int starsFound = srai.stars.size();
				
				for (Intent intent: intents) {
					var firstToken = intent.getInputs().get(0).getInputs().get(0);
					if (firstToken instanceof Literal && ((Literal) firstToken).getText().equals(srai.text)) {
						List<Parameter> parameters = new ArrayList<Parameter>();
						
						// Se recogen los parámetros del intent en cuestión
						for (var token: intent.getInputs().get(0).getInputs()) {
							if (token instanceof ParameterReferenceToken) {
								parameters.add(((ParameterReferenceToken) token).getParameter());
								starsFound -= 1;
							}
						}
						
						// Caso en que sólo haya un parámetro
						if (srai.stars.size() == 1 && !parameters.isEmpty()) {
							ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
							
							parameterRef.setParameter(parameters.get(0));
							textInput.getTokens().add(parameterRef);
							starsFound -= 1;
							break;
						}

						// Caso en que haya más de un parámetro (fechas y horas no soportadas)
						else {
							// Se hace el matching de los parámetros con sus índices 
							// o en caso de error se añade un parámetro genérico
							for (Star star: srai.stars) {
								ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
								
								try {
									parameterRef.setParameter(parameters.get(star.index - 1));
								} catch(Exception e) {
									Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();

									parameter.setDefaultEntity(DefaultEntity.TEXT);
									parameterRef.setParameter(parameter);
									textInput.getTokens().add(parameterRef);
								}
								
								textInput.getTokens().add(parameterRef);
								starsFound -= 1;
								break;
							}
						}
					}
				}
				
				// En caso de no encontrar el intent al que se hace referencia en la lista de intents,
				// se añaden las stars como parámetros
				if (starsFound > 0) {
					for (int i = 0; i < starsFound; i++) {
						ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
						Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
						
						parameter.setDefaultEntity(DefaultEntity.TEXT);
						parameterRef.setParameter(parameter);
						textInput.getTokens().add(parameterRef);
					}
				}
			}

			languageInput.setLanguage(Language.ENGLISH);
			languageInput.getInputs().add(textInput);
			ret.add(languageInput);
		}
		
		return ret;
	}
	
	// Recoge todas las posibles respuestas de un condicional y sus posibles árboles de condicionales
	// y las devuelve en forma de lista
	public static List<TextLanguageInput> getConditionResponsesREC(Condition condition, List<Intent> intents, List<TextLanguageInput> list) {
		for (Option option: condition.options) {
			// Caso en que la opción tenga texto
			if (option.text != null) {
				TextLanguageInput languageInput = GeneratorFactory.eINSTANCE.createTextLanguageInput();
				TextInput textInput = GeneratorFactory.eINSTANCE.createTextInput();
				Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
				
				literal.setText(option.text);
				textInput.getTokens().add(literal);
				languageInput.setLanguage(Language.ENGLISH);
				languageInput.getInputs().add(textInput);
				list.add(languageInput);
			}
			
			// Caso en que la opción tenga srais
			if (option.srais != null) {
				for (Srai srai: option.srais) {
					TextLanguageInput languageInput = GeneratorFactory.eINSTANCE.createTextLanguageInput();
					TextInput textInput = GeneratorFactory.eINSTANCE.createTextInput();
					Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
					
					literal.setText(srai.text);
					textInput.getTokens().add(literal);

					// Si el srai tiene referencias a argumentos del pattern, 
					// se busca qué parámetros son en el intent que sea y se añaden
					if (srai.stars != null) {
						int starsFound = srai.stars.size();
						
						for (Intent intent: intents) {
							var firstToken = intent.getInputs().get(0).getInputs().get(0);
							if (firstToken instanceof Literal && ((Literal) firstToken).getText().equals(srai.text)) {
								List<Parameter> parameters = new ArrayList<Parameter>();
								
								// Se recogen los parámetros del intent en cuestión
								for (var token: intent.getInputs().get(0).getInputs()) {
									if (token instanceof ParameterReferenceToken) {
										parameters.add(((ParameterReferenceToken) token).getParameter());
									}
								}
								
								// Caso en que sólo haya un parámetro
								if (srai.stars.size() == 1 && !parameters.isEmpty()) {
									ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
									
									parameterRef.setParameter(parameters.get(0));
									textInput.getTokens().add(parameterRef);
									break;
								}

								// Caso en que haya más de un parámetro (fechas y horas no soportadas)
								else {
									// Se hace el matching de los parámetros con sus índices 
									// o en caso de error se añade un parámetro genérico
									for (Star star: srai.stars) {
										ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
										
										try {
											parameterRef.setParameter(parameters.get(star.index - 1));
										} catch(Exception e) {
											Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();

											parameter.setDefaultEntity(DefaultEntity.TEXT);
											parameterRef.setParameter(parameter);
											textInput.getTokens().add(parameterRef);
										}
										
										textInput.getTokens().add(parameterRef);
										break;
									}
								}
							}
						}
						
						// En caso de no encontrar el intent al que se hace referencia en la lista de intents,
						// se añaden las stars como parámetros
						if (starsFound > 0) {
							for (int i = 0; i < starsFound; i++) {
								ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
								Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
								
								parameter.setDefaultEntity(DefaultEntity.TEXT);
								parameterRef.setParameter(parameter);
								textInput.getTokens().add(parameterRef);
							}
						}
					}

					languageInput.setLanguage(Language.ENGLISH);
					languageInput.getInputs().add(textInput);
					list.add(languageInput);
				}
			}
			
			// Caso en el que la opción tenga un think con srais
			if (option.think != null && option.think.srais != null) {
				list.addAll(addResponseSrais(option.think.srais, intents));
			}
			
			// Caso en que la opción contenga a su vez otro condicional
			if (option.condition != null) {
				list.addAll(getConditionResponsesREC(option.condition, intents, list));
			}
		}
		
		return list;
	}
	
	// Dada una lista no-vacía de sets del patrón de una categoría, añade dichos sets a la frase en forma de parámetros
	public static void intentAddSets(List<SetAttr> sets, Intent intent, List<MapFile> mapFiles, TrainingPhrase phrase) {
		for (SetAttr set : sets) {
			ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
			Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
			Entity entity = GeneratorFactory.eINSTANCE.createEntity();

			if (mapFiles != null) {
				for (MapFile mapFile : mapFiles) {
					if (mapFile.name.equals(set.name)) {
						LanguageInput entityLanguageInput = GeneratorFactory.eINSTANCE.createLanguageInput();
						
						for (String key : mapFile.content.keySet()) {
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
			intent.getParameters().add(parameter);
		}
	}
	
	// Devuelve todos los sets que hay en el template a las distintas profundidades posibles
	public static List<Set> getAllTemplateSets(Template template) {
		List<Set> ret = new ArrayList<Set>();
		
		if (template.think != null && template.think.sets != null)
			ret.addAll(template.think.sets);
		
		if (template.condition != null)
			for (Option li: template.condition.options)
				if (li.think != null && !li.think.sets.isEmpty())
					ret.addAll(li.think.sets);
			
		return ret;
	}
	
	//// Funciones necesarias pero ajenas a la tarea que se está tratando
	// Cuenta el numero de apariciones de un substring en un string
	// Credit: https://stackoverflow.com/questions/767759/occurrences-of-substring-in-a-string
	public static int countOccurrences(String source, String substring) {
		int lastIndex = 0;
		int count = 0;

		while(lastIndex != -1){
		    lastIndex = source.indexOf(substring, lastIndex);

		    if(lastIndex != -1) {
		        count ++;
		        lastIndex += substring.length();
		    }
		}
		
		return count;
	}
}
