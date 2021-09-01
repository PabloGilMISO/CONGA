package reverse.pandorabots.agent;

import java.util.ArrayList;
import java.util.List;

import generator.DefaultEntity;
import generator.Entity;
import generator.GeneratorFactory;
import generator.Intent;
import generator.IntentLanguageInputs;
import generator.LanguageInput;
import generator.Literal;
import generator.Parameter;
import generator.ParameterReferenceToken;
import generator.SimpleInput;
import generator.TrainingPhrase;

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
			}

			languageInput.getInputs().add(phrase);
			intent.getInputs().add(languageInput);
		}
	}

	// Función para gestionar los intents cuya entrada contenga fechas en el formato DD/MM/AA
	// TODO: Revisar y completar cuando se hayan terminado funciones de captura de intents de menor complejidad
	public static void addCategoryWithDate(Category category, Intent intent, List<MapFile> mapFiles) {
		IntentLanguageInputs languageInput = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
		TrainingPhrase phrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();

		String[] tokens1 = category.pattern.text.split("\\* slash \\* slash \\*");
		String[] tokens2 = category.pattern.text.split("\\*slash\\*slash\\*");
		String[] tokens = (tokens1.length == 0 || 
						  (tokens1.length > tokens2.length && tokens2.length != 0)) ? tokens1 : tokens2;

		// Caso en que el intent únicamente esté formado por la fecha
		if (tokens.length == 0) {
			ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
			Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
			
			parameter.setDefaultEntity(DefaultEntity.DATE);
			parameterRef.setParameter(parameter);
			phrase.getTokens().add(parameterRef);
		}
		
		// Caso en que el intent está formado por fecha y otros elementos
		else {
			for (String token : tokens) {
				// TODO: Caso en que haya horas en el fragmento y otros elementos o no (!!)
				if (token.contains("* colon *") || token.contains("*colon*")) {
//					String[] innerTokens1 = category.pattern.text.split("\\* slash \\* slash \\*");
//					String[] innerTokens2 = category.pattern.text.split("\\*slash\\*slash\\*");
//					String[] innerTokens = (innerTokens1.length == 0 || 
//									  	   (innerTokens1.length > innerTokens2.length && innerTokens2.length != 0)) ? 
//									  			   innerTokens1 : innerTokens2;
					ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
					Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
					
					parameter.setDefaultEntity(DefaultEntity.TIME);
					parameterRef.setParameter(parameter);
					phrase.getTokens().add(parameterRef);
				}
				
				// Caso en que haya otros parámetros en el fragmento
				if (token.contains("*")) {
					// Sets contenidos en todo el template
					List<Set> sets = getAllTemplateSets(category.template);
				
					String[] innerTokens = category.pattern.text.split("\\*");
					for (String innerToken : innerTokens) {
						Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
						ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
						Parameter parameter = GeneratorFactory.eINSTANCE.createParameter();
						
						// Guardado de texto previo al parametro
						literal.setText(token);
						phrase.getTokens().add(literal);

						// Guardado del parámetro
						parameter.setDefaultEntity(DefaultEntity.TEXT);
						parameterRef.setParameter(parameter);
						phrase.getTokens().add(parameterRef);
					}

					languageInput.getInputs().add(phrase);
					intent.getInputs().add(languageInput);
				}
				
				// Caso en que contenga sets en el pattern
				if (category.pattern.sets != null)
					intentAddSets(category.pattern.sets, intent, mapFiles, phrase);
				
				////////////////////////
				// Caso en que el fragmento no contenga otros argumentos
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
			
		}
		
		
		// Si el template contiene sets
		// TODO: Completar con sets que haya en condicionales. Hacer una función que
		// extraiga los sets que hay
		// en el think y los junte con los que hay en los condition mediante una
		// intersección
		if (category.template.think.sets != null) {
			List<Set> sets = new ArrayList<Set>(category.template.think.sets);

			

			// Caso en que contenga sets en el pattern
			// TODO: Revisar
			if (category.pattern.sets != null) {
				for (SetAttr set : category.pattern.sets) {
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
				}
			}

			languageInput.getInputs().add(phrase);
			intent.getInputs().add(languageInput);
		}

		// Si no hay sets en el template, se guarda el pattern como texto
		else {
//			Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
//			
//			literal.setText(category.pattern.text);
//			phrase.getTokens().add(literal);
//			languageInput.getInputs().add(phrase);
//			intent.getInputs().add(languageInput);
			String[] tokens = category.pattern.text.split("\\*");
			for (String token : tokens) {
				Literal literal = GeneratorFactory.eINSTANCE.createLiteral();
				ParameterReferenceToken parameterRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
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
				}
			}

			languageInput.getInputs().add(phrase);
			intent.getInputs().add(languageInput);
		}
	}

	// Función para gestionar los intents cuya entrada contenga horas en el formato HH:MM
	// TODO: Revisar y completar cuando se hayan terminado funciones de captura de intents de menor complejidad
	public static void addCategoryWithHour(Category category, Intent intent, List<MapFile> mapFiles) {
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

//							parameter.setName(sets.get(0).name);
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
						for (MapFile mapFile : mapFiles) {
							if (mapFile.name.equals(set.name)) {
								LanguageInput entityLanguageInput = GeneratorFactory.eINSTANCE
										.createLanguageInput();

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
				}
			}

			languageInput.getInputs().add(phrase);
			intent.getInputs().add(languageInput);
		}
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
						if (setsFlag == 0 && !set.stars.isEmpty()) {
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
				
				tokenIndex += 1;
			}
			
			// Caso en que contenga sets en el pattern
			if (category.pattern.sets != null)
				intentAddSets(category.pattern.sets, intent, mapFiles, phrase);

			languageInput.getInputs().add(phrase);
			intent.getInputs().add(languageInput);
		}
	}
	
	//// Funciones auxiliares para realizar tareas concretas
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
}
