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
	// TODO: Revisar y completar cuando se hayan terminado funciones de captura de intents de menor complejidad
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
