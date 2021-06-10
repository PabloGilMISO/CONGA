package org.xtext.botGenerator.generator

import generator.Action
import generator.Bot
import generator.CompositeInput
import generator.DefaultEntity
import generator.Entity
import generator.EntityInput
import generator.EntityToken
import generator.HTTPRequest
import generator.HTTPResponse
import generator.Image
import generator.Intent
import generator.IntentLanguageInputs
import generator.Language
import generator.LanguageInput
import generator.Literal
import generator.ParameterReferenceToken
import generator.ParameterToken
import generator.RegexInput
import generator.SimpleInput
import generator.Text
import generator.TextInput
import generator.TextLanguageInput
import generator.Token
import generator.TrainingPhrase
import generator.UserInteraction
import java.nio.file.Files
import java.nio.file.Paths
import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.UUID
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import zipUtils.Zip

class PandorabotsGenerator {
	String path;
	protected static String uri;
	Zip zip;

	def doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context, Zip zip) {
		var resourceName = resource.URI.lastSegment.substring(0, resource.URI.lastSegment.indexOf("."));
		var bot = resource.allContents.filter(Bot).toList.get(0);

		path = resourceName + "/Pandorabots"

		this.zip = zip

		// Creacion de fichero de propiedades .properties
		var systemPropertiesName = path + "/system/" + resourceName.toLowerCase().replace(' ', '_') + ".properties"
		fsa.generateFile(systemPropertiesName, systemFileFill())
		var systemPropertiesValue = fsa.readBinaryFile(systemPropertiesName)
		zip.addFileToFolder("system", resourceName.toLowerCase().replace(' ', '_') + ".properties", systemPropertiesValue)

		// Creacion de fichero UDC
		var udcName = path + "/files/" + "udc.aiml"
		fsa.generateFile(udcName, udcFileFill())
		var udcValue = fsa.readBinaryFile(udcName)
		zip.addFileToFolder("files", "udc.aiml", udcValue)
		
		// Creacion de ficheros de utils y funciones externas comunes entre todos los proyectos
		var congaPath = "C:/CONGA/pandorabots/"
		
		var utilsPath = Paths.get(congaPath + "utils.aiml")
		var utils = new String(Files.readAllBytes(utilsPath))
		fsa.generateFile(path + "/files/utils.aiml", utils)
		var utilsValue = fsa.readBinaryFile(path + "/files/utils.aiml")
		zip.addFileToFolder("files", "utils.aiml", utilsValue)
		
		var aimlSLPath = Paths.get(congaPath + "aimlstandardlibrary.aiml")
		var aimlSL = new String(Files.readAllBytes(aimlSLPath))
		fsa.generateFile(path + "/files/aimlstandardlibrary.aiml", aimlSL)
		var aimlSLValue = fsa.readBinaryFile(path + "/files/aimlstandardlibrary.aiml")
		zip.addFileToFolder("files", "aimlstandardlibrary.aiml", aimlSLValue)
		
		// Obtencion de todas las entities del modelo
		var entities = resource.allContents.filter(Entity).toList
		for (Entity entity : entities) {
			// Creacion de map para la entity correspondiente
			var entityPath = path + "/maps/" + entity.name + ".map"
			
			// Generacion del archivo map asociado a la entity concreta
			fsa.generateFile(entityPath, entityMapFill(entity))
			var entityValue = fsa.readBinaryFile(entityPath)
			zip.addFileToFolder("maps", entity.name + ".map", entityValue)

			// Generacion de sets correspondientes a cada input en la carpeta sets
			for (language_input : entity.inputs) {
				for (input : language_input.inputs) {
					// Solo se contemplan SimpleInputs
					if (input instanceof SimpleInput) {
						var inputSetPath = path + "/sets/" + input.name + ".set"
						fsa.generateFile(inputSetPath, entitySetFill(input))
						var inputSetValue = fsa.readBinaryFile(inputSetPath)
						zip.addFileToFolder("sets", input.name + ".set", inputSetValue)
					}
				}
			}
		}

		//////////////////TODO: Checkpoint
		// En flows se guardan los flujos de conversacion
		for (UserInteraction transition : bot.flows) {
			createTransitionFiles(transition, "", fsa, bot)
		}
		
		zip.close
	}

	// Genera el codigo para rellenar el archivo de propiedades del bot
	def systemFileFill() '''
		[
		["name", "set_when_loaded"],
		["default-get", "unknown"],
		["default-property", "unknown"],
		["default-map", "unknown"],
		["sentence-splitters", ".!?"],
		["learn-filename", "pand_learn.aiml"],
		["max-learn-file-size", "1000000"]
		]
	'''

	// Genera el codigo para rellenar el archivo udc principal del bot
	def udcFileFill() '''
		<?xml version="1.0" encoding="UTF-8"?>
		<aiml>
		</aiml>
	'''
	
	// Rellena el set correspondiente al input concreto (entity->inputs->inputs)
	def entitySetFill(SimpleInput input) '''
		[
		«FOR synonym : input.values»
			["«synonym»"]«IF !input.values.isTheLast(synonym)»,«ENDIF»
		«ENDFOR»
		]
	'''

	// Rellena el map con todas las entities de pandorabots y su set asociado
	def entityMapFill(Entity entity) '''
		[
		«FOR input : entity.inputs»
			«FOR entry: input.inputs»
				«entry(entry)»«IF !isTheLast(input.inputs, entry)»,«ENDIF»
			«ENDFOR»
		«ENDFOR»
		]
	'''

	// Genera codigo distinto dependiendo del tipo de input
	def entry(EntityInput entry) {
		if (entry instanceof SimpleInput) {
			return entry(entry)
		}
		// TODO: No soportadas
		else if (entry instanceof CompositeInput) {
			return entry(entry)
		} 
		// TODO: No soportadas
		else if (entry instanceof RegexInput) {
			return entry(entry)
		}
	}

	// Escribe las posibles opciones de un entity dentro de un fichero set
	def entry(SimpleInput entry) '''
		«FOR synonym : entry.values»
			["«synonym»", "«entry.name»"]«IF !isTheLast(entry.values, synonym)»,«ENDIF»
		«ENDFOR»
	'''

	// TODO: No soportadas
	def entry(CompositeInput entry) '''
	'''
	
	// TODO: No soportadas
	def entry(RegexInput entry)'''
	'''

	// Guarda los intents durante el recorrido de los flujos de conversación
	def void createTransitionFiles(UserInteraction transition, String prefix, IFileSystemAccess2 fsa, Bot bot) {
		// Generacion del archivo del intent del que parte el flujo concreto
		var intentFileContent = 
		'''
		<?xml version="1.0" encoding="UTF-8"?>
		«  »<aiml>
		'''
		// Generacion de intents de Pandorabots para guardar los parametros
		intentFileContent += createSaveParameter(transition.intent)
		// Generacion de intents
		intentFileContent += transition.intentFile(prefix, bot)
		intentFileContent += "</aiml>"
		var intentFileName = (prefix + transition.intent.name).toLowerCase().replace(' ', '')
		fsa.generateFile(path + "/files/" + intentFileName + ".aiml", intentFileContent)
		var intentValue = fsa.readBinaryFile(path + "/files/" + intentFileName + ".aiml")
		zip.addFileToFolder("files", intentFileName + ".aiml", intentValue)
		// Se crea y rellena un archivo con los inputs de cada intent
//		for (IntentLanguageInputs input : transition.intent.inputs) {
//			// Rellenado y apertura del archivo con los posibles inputs
//			var intentLanValue = fsa.readBinaryFile(
//				path + '/intents/' + prefix + transition.intent.name + '_usersays_' + lan.languageAbbreviation +
//					'.json')
//			zip.addFileToFolder('intents',
//				prefix + transition.intent.name + '_usersays_' + lan.languageAbbreviation + '.json', intentLanValue)
//		}
//
//		zip.addFileToFolder('intents', prefix + transition.intent.name + '.aiml', intentValue)
//
//		// Se va recorriendo cada action y creando los intents que correspondan
//		if (transition.target !== null) {
//			var newPrefix = prefix + transition.intent.name + " - ";
//			for (UserInteraction t : transition.target.outcoming) {
//				createTransitionFiles(t, newPrefix, fsa, bot)
//			}
//		}
	}

	// Para manejo de contextos en Dialogflow
//	def contextName(UserInteraction transition, String prefix) {
//		var name = prefix + transition.intent.name + " - " + "followup"
//		name = name.replaceAll(" ", "");
//		return name
//	}
//
//	def contextName(String prefix) {
//		var name = prefix + "followup"
//		name = name.replaceAll(" ", "");
//		return name
//	}
	
	// Devuelve todas las posibles respuestas a un intent para un lenguage concreto
	def getAllIntentResponses(TextLanguageInput textAction) {
		var responses = new ArrayList<String>()
		for (TextInput input : textAction.inputs) {
			var response = ""
			for (Token token : input.tokens) {
				if (token instanceof Literal) {
					response += token.text
					if (!isTheLast(input.tokens, token))
						response += " "
				}
				
				else if (token instanceof ParameterToken)
					response += "<get name=\"" + token.parameter.name + "\"/> "
			}
			
			responses.add(response)
		}
		
		return responses
	}
	
	// Devuelve las entidades conetenidas en una frase concreta
	def getPhraseEntities(TrainingPhrase phrase) {
		var ret = new ArrayList<String>()
		
		for (token: phrase.tokens)
			if (token instanceof ParameterReferenceToken)
				ret.add(token.parameter.name)
		
		return ret
	}
	
	def getIntentParameterPrompts(Intent intent) {
		var ret = new ArrayList<Pair<String, String>>()
		
		for (parameter: intent.parameters)
			ret.add(new Pair(parameter.name, parameter.prompts.get(0).prompts.get(0)))
	
		return ret
	}
	
	def getNextParamPetition(Intent intent, TrainingPhrase phrase) {
		var entities = getPhraseEntities(phrase)
		var parameters = getIntentParameterPrompts(intent)
		var ret = new ArrayList<Pair<String, String>>()
		
		for (parameter: parameters) {
			for(entity: entities) {
				if (parameter.key != entity)
					ret.add(parameter)
				
				else 
					ret.remove(parameter)
			}
		}
		
		return ret.length > 0 ? ret.get(0) : new Pair("", "")
	}
	
	// Generador de codigo de un intent
	// TODO: 
	// 1. Mirar funcionamiento del webhook.
	// 2. Implementar recogida de parametros a enviar en HttpRequest
	// 3. Revisar impresion de parametros etc en HttpResponse <- Necesarias pruebas con callapi
	def intentFile(UserInteraction transition, String prefix, Bot bot)
	'''
	«"  "»<!-- Intent -->
	«intentGenerator(transition, bot)»
	«"  "»<!-- Intent inputs -->
	«FOR language: transition.intent.inputs»
		«var lang=""»
      	«IF language.language != Language.EMPTY»
  	    	«{lang = language.language.languageAbbreviation; ""}»
      	«ELSE»
	    	«{lang = bot.languages.get(0).languageAbbreviation; ""}»
	    «ENDIF»
  		«FOR input: language.inputs»
  			«IF input instanceof TrainingPhrase»
				«"  "»<category>
				«"    "»<pattern>«FOR token: input.tokens»«IF token instanceof Literal»«token.text.replace('?', ' #')»«ELSEIF token instanceof ParameterReferenceToken»*«ENDIF»«ENDFOR»</pattern>
				«"    "»<think>
				«var List<String> entities»
				«{entities = getPhraseEntities(input); ""}»
				«FOR entity: entities»
					«"      "»<srai>
					«"        "»SAVE«entity.toUpperCase()» <star index="«entities.indexOf(entity) + 1»"/>
					«"      "»</srai>
				«ENDFOR»
				«"    "»</think>
				«var String nextPrompt»
				«{nextPrompt = getNextParamPetition(transition.intent, input).getValue(); ""}»
				«IF nextPrompt !== ""»
				«"    "»<template>«nextPrompt»</template>
				«ELSE»
				«"    "»<template></template>
				«ENDIF»
				«"  "»</category>
		  	«ENDIF»
	  	«ENDFOR»
	«ENDFOR»
	'''
	
	// Devuelve los parametros que se quieren recoger con un intent
	def getIntentParameters(Intent intent) {
		var ret = new HashMap<String, DefaultEntity>()
		
		for (IntentLanguageInputs language: intent.inputs)
			for (input: language.inputs)
				if (input instanceof TrainingPhrase)
					for (token: (input as TrainingPhrase).tokens)
						if (token instanceof ParameterReferenceToken)
							// Si no contiene el parametros por otro input, lo introduce en el diccionario
							if (!ret.keySet.contains(token.parameter.name))
								ret.put(token.parameter.name, token.parameter.defaultEntity)
		
		return ret
	}
	
	// Genera los intents de pandorabots de tipo SAVE_PARAMETER para poder guardar los parametros de distintos
	// tipos del intent concreto
	def createSaveParameter(Intent intent) {
		var parameters = getIntentParameters(intent)
		if (parameters.isEmpty())
			return ""
		
		else {
			var ret = "  <!-- Entity saving -->\n"
			for (key: parameters.keySet) {
				var value = parameters.get(key)
				switch (value) {
					case DefaultEntity.TEXT:
						ret += 
						'''
						«"  "»<category>
						«"    "»<pattern>SAVE«key.toUpperCase()» *</pattern>
						«"    "»<template>
						«"      "»<think><set name="«key»"><star/></set></think>
						«"    "»</template>
						«"  "»</category>
						'''
					case DefaultEntity.TIME:
						ret +=
						'''
						«"  "»<category>
						«"    "»<pattern>SAVE«key.toUpperCase()» * colon *</pattern>
						«"    "»<template>
						«"      "»<think>
						«"        "»<set name="«key»_is_valid"><srai>ISVALIDHOUR <star index="1"/>:<star index="2"/></srai></set>
						«"      "»</think>
						«"      "»<condition name="«key»_is_valid">
						«"        "»<li value="TRUE">
						«"          "»<think>
						«"            "»<set name="«key»"><star index="1"/>:<star index="2"/></set>
						«"          "»</think>
						«"        "»</li>
						«"      "»</condition>
						«"    "»</template>
						«"  "»</category>
						'''
					case DefaultEntity.DATE:
						ret +=
						'''
						«"  "»<category>
						«"    "»<pattern>SAVE«key.toUpperCase()» * slash * slash *</pattern>
						«"    "»<template>
						«"      "»<think>
						«"        "»<set name="«key»_is_valid">
						«"          "»<srai>VALIDDATE <star index="1"/>/<star index="2"/>/<star index="3"/></srai>
						«"        "»</set>
						«"      "»</think>
						«"      "»<condition name="«key»_is_valid">
						«"        "»<li value="TRUE">
						«"          "»<think><set name="«key»"><star index="1"/>/<star index="2"/>/<star index="3"/></set></think>
						«"        "»</li>
						«"      "»</condition>
						«"    "»</template>
						«"  "»</category>
						'''
					case DefaultEntity.NUMBER:
						ret +=
						'''
						«"  "»<category>
						«"    "»<pattern>SAVE«key.toUpperCase()» <set>number</set></pattern>
						«"    "»<template>
						«"      "»<think><set name="«key»"><star/></set></think>
						«"    "»</template>
						«"  "»</category>
						'''
					default: 
						ret += 
						'''
						«"  "»<category>
						«"    "»<pattern>SAVE«key.toUpperCase()» *</pattern>
						«"    "»<template>
						«"      "»<think><set name="«key»"><star/></set></think>
						«"    "»</template>
						«"  "»</category>
						'''
				}
			}
			
			return ret
		}
	}
	
	// Devuelve los lenguajes de un acton
	def getActionLanguages(Action action) {
		var languageList = new ArrayList<String>()
		
		if (action instanceof Text)
			for (language: action.inputs)
				languageList.add(language.language.languageAbbreviation)
		
		return languageList
	}
	
	// Devuelve las acciones de un lenguaje concreto
	def getLanguageActions(List<Action> actions, String lang) {
		var ret = new ArrayList<TextLanguageInput>()
		
		for (action: actions)
			if (action instanceof Text)
				for (language: action.inputs)
					if (language.language.languageAbbreviation == lang)
						ret.add(language)
		
		return ret
	}
	
	// Generacion de codigo de un intent con una o varias respuestas
	def intentGenerator(UserInteraction transition, Bot bot)
	'''
		«var intentName = ""»
		«"  "»<!-- Main intents -->
		«{intentName = transition.intent.name.toUpperCase().replace(' ', ''); ""}»
		«FOR language: transition.intent.inputs»
			«var lang = ""»
		  	«IF language.language != Language.EMPTY»
		    	«{lang = language.language.languageAbbreviation; ""}»
		  	«ELSE»
		    	«{lang = bot.languages.get(0).languageAbbreviation; ""}»
		    «ENDIF»
			«"  "»<category>
			«"    "»<pattern>«intentName»</pattern>
			«"    "»<template>
			«FOR action: transition.target.actions»
				«IF action instanceof Text»
					«"      "»<srai>«intentName + lang.toUpperCase()»</srai>
				«ELSE»
					«"      "»<srai>«intentName + action.name.toUpperCase().replace(' ', '')»</srai>
				«ENDIF»
			«ENDFOR»
			«"    "»</template>
			«"  "»</category>
	    «ENDFOR»
		«"  "»<!-- Action intents -->
		«FOR action: transition.target.actions»
			«IF action instanceof Text»
				«FOR language: action.inputs»
					«var lang=""»
					«IF language.language != Language.EMPTY»
						«{lang = language.language.languageAbbreviation.toUpperCase(); ""}»
					«ELSE»
						«{lang = bot.languages.get(0).languageAbbreviation.toUpperCase(); ""}»
					«ENDIF»
					«IF language.getAllIntentResponses().length > 1»
						«"  "»<category>
						«"    "»<pattern>«(intentName + lang).toUpperCase()»</pattern>
						«"    "»<template>
						«"      "»<random>
						«FOR response: language.getAllIntentResponses()»
							«"        "»<li>«response»</li>
						«ENDFOR»
						«"      "»</random>
						«"    "»</template>
						«"  "»</category>
					«ELSE»
						«"  "»<category>
						«"    "»<pattern>«(intentName + lang).toUpperCase()»</pattern>
						«"    "»<template>«language.getAllIntentResponses().get(0)»</template>
						«"  "»</category>
					«ENDIF»
				«ENDFOR»
			«ELSEIF action instanceof Image»
				«"  "»<category>
				«"    "»<pattern>«intentName + action.name.toUpperCase().replace(' ', '')»</pattern>
				«"    "»<template><image>«action.URL»</image></template>
				«"  "»</category>
			«ELSEIF action instanceof HTTPRequest»
				«"  "»<category>
				«"    "»<pattern>«intentName + action.name.toUpperCase().replace(' ', '')»</pattern>
				«"    "»<template>
				«"      "»<callapi response_code_var="response«"_" + action.name»">
				«"        "»<url>«(action as HTTPRequest).getURL()»</url>
				«"      "»</callapi>
				«"    "»</template>
				«"  "»</category>
			«ELSEIF action instanceof HTTPResponse»
				«"  "»<category>
				«"    "»<pattern>«intentName + action.name.toUpperCase().replace(' ', '')»</pattern>
				«"    "»<template>
				«"      "»<get name="response_«(action as HTTPResponse).HTTPRequest.name»"/>
				«"    "»</template>
				«"  "»</category>
			«ENDIF»
		«ENDFOR»
	'''

//	«FOR action: transition.target.actions»
//		«IF action instanceof Text»
//			«FOR texLanguage: action.inputs»
//				«intentGenerator(transition, texLanguage, bot)»
//			«ENDFOR»
//		«ELSEIF action instanceof Image»
//			«"  "»<category>
//			«"    "»<pattern>«transition.intent.name.toUpperCase().replace(' ', '_')»</pattern>
//			«"    "»<template><image>«(action as Image).URL»</image></template>
//			«"  "»</category>
//		«ELSEIF action instanceof HTTPRequest»
//			«"  "»<category>
//			«"    "»<pattern>«(transition.intent.name + "_" + action.name).toUpperCase().replace(' ', '_')»</pattern>
//			«"    "»<template>
//			«"      "»<callapi response_code_var="response«"_" + action.name»">
//			«"        "»<url>«(action as HTTPRequest).getURL()»</url>
//			«"      "»</callapi>
//			«"    "»</template>
//			«"  "»</category>
//		«ELSEIF action instanceof HTTPResponse»
//			«"  "»<category>
//			«"    "»<pattern>«(transition.intent.name + "_" + action.name).toUpperCase().replace(' ', '_')»</pattern>
//			«"    "»<template>
//			«"      "»<get name="response_«(action as HTTPResponse).HTTPRequest.name»"/>
//			«"    "»</template>
//			«"  "»</category>
//		«ENDIF»
//	«ENDFOR»
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
	def returnText(String value) {
		if (value.isEmpty) {
			return '';
		}
		return value
	}

	def languageAbbreviation(Language lan) {
		switch (lan) {
			case Language.ENGLISH:
				return 'en'
			case Language.SPANISH:
				return 'es'
			case Language.DANISH:
				return 'da'
			case Language.GERMAN:
				return 'de'
			case Language.FRENCH:
				return 'fr'
			case Language.HINDI:
				return 'hi'
			case Language.INDONESIAN:
				return 'id'
			case Language.ITALIAN:
				return 'it'
			case Language.JAPANESE:
				return 'ja'
			case Language.KOREAN:
				return 'ko'
			case Language.DUTCH:
				return 'nl'
			case Language.NORWEGIAN:
				return 'no'
			case Language.POLISH:
				return 'pl'
			case Language.PORTUGUESE:
				return 'pt'
			case Language.RUSIAN:
				return 'ru'
			case Language.SWEDISH:
				return 'sv'
			case Language.THAI:
				return 'th'
			case Language.TURKISH:
				return 'tr'
			case Language.UKRANIAN:
				return 'uk'
			case Language.CHINESE:
				return 'zh'
			default:
				return 'en'
		}
	}

	// Generacion de codigo de configuracion de cada entity
	def entityFile(Entity entity) '''
		
		{
			"id": "«UUID.randomUUID().toString»",
			"name": "«entity.name»",
			"isOverridable": true,	  
			«IF BotGenerator.entityType(entity) === BotGenerator.REGEX»
				"isEnum": false,
				"isRegexp":true,
				"automatedExpansion": true,
				"allowFuzzyExtraction": false
			«ELSEIF BotGenerator.entityType(entity) === BotGenerator.SIMPLE»
				"isEnum": false,
				"isRegexp": false,
				"automatedExpansion": true,
				"allowFuzzyExtraction": true
			«ELSE»
				"isEnum": true,
				"isRegexp": false,
				"automatedExpansion": false,
				"allowFuzzyExtraction": false
			«ENDIF»
		}
	'''

	def entityIsSimple(Entity entity) {
	}

	// Generador de codigo para cada lenguaje en que este configurada cada entity
	def entriesFile(LanguageInput entity) '''
		[
			«FOR entry : entity.inputs»
				{
				   «entry(entry)»
				} «IF !entity.inputs.isTheLast(entry)»,«ENDIF»
			«ENDFOR»
		]
	'''

	def static isTheLast(List<?> list, Object object) {
		if (list.indexOf(object) == list.size - 1) {
			return true;
		}
		return false;

	}

	def getCompositeEntry(CompositeInput entry) {
		var ret = "";
		for (Token token : entry.expresion) {
			if (token instanceof Literal) {
				ret += token.text + " "
			} else if (token instanceof EntityToken) {
				ret += "@" + token.entity.name + ":" + token.entity.name + " "
			}
		}
		return ret;
	}
}
