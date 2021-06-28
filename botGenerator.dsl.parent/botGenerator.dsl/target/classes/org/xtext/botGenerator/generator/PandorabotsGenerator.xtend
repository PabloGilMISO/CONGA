package org.xtext.botGenerator.generator

import generator.Action
import generator.Bot
import generator.DefaultEntity
import generator.Entity
import generator.EntityInput
import generator.HTTPRequest
import generator.HTTPResponse
import generator.Image
import generator.Intent
import generator.IntentLanguageInputs
import generator.Language
import generator.Literal
import generator.ParameterReferenceToken
import generator.ParameterToken
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
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import zipUtils.Zip
import generator.KeyValue
import generator.Empty

class PandorabotsGenerator {
	String path;
	protected static String uri;
	Zip zip;

	def doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context, Zip zip) {
		var resourceName = resource.URI.lastSegment.substring(0, resource.URI.lastSegment.indexOf("."));
		var bot = resource.allContents.filter(Bot).toList.get(0);

		path = resourceName + "/Pandorabots"

		this.zip = zip

//		for (lang: bot.languages) {
//			var langPath = path + '/' + lang.languageAbbreviation
			
		// Creacion de fichero de propiedades .properties
//		var systemPropertiesName = langPath + "/system/" + resourceName.toLowerCase().replace(' ', '_') + ".properties"
		var systemPropertiesName = path + "/system/" + resourceName.toLowerCase().replace(' ', '_') + ".properties"
		fsa.generateFile(systemPropertiesName, systemFileFill())
		var systemPropertiesValue = fsa.readBinaryFile(systemPropertiesName)
		zip.addFileToFolder("system", resourceName.toLowerCase().replace(' ', '_') + ".properties", systemPropertiesValue)

		// Creacion de fichero UDC
//		var udcName = langPath + "/files/" + "udc.aiml"
		var udcName = path + "/files/" + "udc.aiml"
		fsa.generateFile(udcName, udcFileFill())
		var udcValue = fsa.readBinaryFile(udcName)
		zip.addFileToFolder("files", "udc.aiml", udcValue)
		
		// Creacion de ficheros de utils y funciones externas comunes entre todos los proyectos
		var congaPath = "C:/CONGA/pandorabots/"
		
		var utilsPath = Paths.get(congaPath + "utils.aiml")
		var utils = new String(Files.readAllBytes(utilsPath))
//		fsa.generateFile(langPath + "/files/utils.aiml", utils)
//		var utilsValue = fsa.readBinaryFile(langPath + "/files/utils.aiml")
		fsa.generateFile(path + "/files/utils.aiml", utils)
		var utilsValue = fsa.readBinaryFile(path + "/files/utils.aiml")
		zip.addFileToFolder("files", "utils.aiml", utilsValue)
		
		var aimlSLPath = Paths.get(congaPath + "aimlstandardlibrary.aiml")
		var aimlSL = new String(Files.readAllBytes(aimlSLPath))
//		fsa.generateFile(langPath + "/files/aimlstandardlibrary.aiml", aimlSL)
//		var aimlSLValue = fsa.readBinaryFile(langPath + "/files/aimlstandardlibrary.aiml")
		fsa.generateFile(path + "/files/aimlstandardlibrary.aiml", aimlSL)
		var aimlSLValue = fsa.readBinaryFile(path + "/files/aimlstandardlibrary.aiml")
		zip.addFileToFolder("files", "aimlstandardlibrary.aiml", aimlSLValue)
		
		// Generacion de ficheros de sustituciones automaticas vacios
//		generateEmptySubstitutions(fsa, '/' + lang.languageAbbreviation)
		generateEmptySubstitutions(fsa, "")
		
		// Obtencion de todas las entities del modelo
		var entities = resource.allContents.filter(Entity).toList
		for (Entity entity : entities) {
			// Creacion de map para la entity correspondiente
//			var entityPath = langPath + "/maps/" + entity.name + ".map"
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
//						var inputSetPath = langPath + "/sets/" + input.name + ".set"
						var inputSetPath = path + "/sets/" + input.name + ".set"
						fsa.generateFile(inputSetPath, entitySetFill(input))
						var inputSetValue = fsa.readBinaryFile(inputSetPath)
						zip.addFileToFolder("sets", input.name + ".set", inputSetValue)
					}
				}
			}
		}

		// En flows se guardan los flujos de conversacion
		for (UserInteraction transition : bot.flows) {
//			createTransitionFiles(transition, lang.languageAbbreviation, fsa, bot)
			createTransitionFiles(transition, "", fsa, bot)
		}
		
		zip.close	
//		}
	}
	
	// Genera los ficheros de sustituciones vacios para que no haya problemas de sustituciones indeseadas
	def generateEmptySubstitutions(IFileSystemAccess2 fsa, String prefix) {
		// Nombres de ficheros
		var person2Name = path + prefix + "/substitutions/" + "person2.substitution"
		var personName = path + prefix + "/substitutions/" + "person.substitution"
		var normalName = path + prefix + "/substitutions/" + "normal.substitution"
		var genderName = path + prefix + "/substitutions/" + "gender.substitution"
		var denormalName = path + prefix + "/substitutions/" + "denormal.substitution"
		
		// Generacion de archivos
		fsa.generateFile(person2Name, "[]")
		fsa.generateFile(personName, "[]")
		fsa.generateFile(normalName, 
		'''
		[
		["\/", " slash "],
		[":0", " colon 0"],
		[":1", " colon 1"],
		[":2", " colon 2"],
		[":3", " colon 3"],
		[":4", " colon 4"],
		[":5", " colon 5"],
		[":6", " colon 6"],
		[":7", " colon 7"],
		[":8", " colon 8"],
		[":9", " colon 9"]
		]
		''')
		fsa.generateFile(genderName, "[]")
		fsa.generateFile(denormalName, "[]")
		
		// Lectura de ficheros para introduccion en zip
		var person2Value = fsa.readBinaryFile(person2Name)
		var personValue = fsa.readBinaryFile(personName)
		var normalValue = fsa.readBinaryFile(normalName)
		var genderValue = fsa.readBinaryFile(genderName)
		var denormalValue = fsa.readBinaryFile(denormalName)
		
		// Introduccion de ficheros en zip
		zip.addFileToFolder("substitutions", "person2.substitution", person2Value)
		zip.addFileToFolder("substitutions", "person.substitution", personValue)
		zip.addFileToFolder("substitutions", "normal.substitution", normalValue)
		zip.addFileToFolder("substitutions", "gender.substitution", genderValue)
		zip.addFileToFolder("substitutions", "denormal.substitution", denormalValue)
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
		// CompositeInput y RegexInput no soportados
	}

	// Escribe las posibles opciones de un entity dentro de un fichero set
	def entry(SimpleInput entry) '''
		«FOR synonym : entry.values»
			["«synonym»", "«entry.name»"]«IF !isTheLast(entry.values, synonym)»,«ENDIF»
		«ENDFOR»
	'''

	// Guarda los intents durante el recorrido de los flujos de conversación
	def void createTransitionFiles(UserInteraction transition, String prefix, IFileSystemAccess2 fsa, Bot bot) {
		var intentFileName = (transition.intent.name).toLowerCase().replaceAll('[ _]', '')
		var intentFileContent = '''
		<?xml version="1.0" encoding="UTF-8"?>
		«  »<aiml>
		'''
		intentFileContent += transition.intentFile(bot, prefix, "")
		intentFileContent += '''
		</aiml>
		'''
		fsa.generateFile(path + '/' + prefix + "/files/" + intentFileName + ".aiml", intentFileContent)
		var intentValue = fsa.readBinaryFile(path + '/' + prefix + "/files/" + intentFileName + ".aiml")
		zip.addFileToFolder("files", intentFileName + ".aiml", intentValue)
	}

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
	
	// Devuelve una lista ordenada de pares <parametro, frase de peticion> de un intent concreto
	def getIntentParameterPrompts(Intent intent) {
		var ret = new ArrayList<Pair<String, String>>()
		
		for (parameter: intent.parameters)
			ret.add(new Pair(parameter.name, parameter.prompts.get(0).prompts.get(0)))
	
		return ret
	}
	
	// Extrae los nombres de parametros de una lista de pares <parametro, frase de peticion>
	def getPromptsKeys(ArrayList<Pair<String, String>> list) {
		var ret = new ArrayList<String>()
		
		for (elem: list)
			ret.add(elem.key)
			
		return ret
	}
	
	// Devuelve el siguiente par <parametro, frase de peticion> de una frase concreta contenida en un intent concreto
	def getNextParamPetition(Intent intent, TrainingPhrase phrase) {
		var entities = getPhraseEntities(phrase)
		var parameters = getIntentParameterPrompts(intent)
		
		var keys = getPromptsKeys(parameters)
		keys.removeAll(entities)		
		
		if (keys.isEmpty())
			return new Pair("", "")
		
		else
			for	(param: parameters)
				if(param.key == keys.get(0))
					return param	
	}
	
	// Generador de codigo de un intent
	// TODO: Revisar impresion de parametros etc en HttpResponse <- Necesario acceso a llamadas HTTP con callapi
	def intentFile(UserInteraction transition, Bot bot, String prefix, String that)
	'''
	«createSaveParameter(transition.intent, prefix)»
	«"  "»<!-- Intent -->
	«intentGenerator(transition, bot, prefix)»
	«createIntentInputs(transition, bot, prefix, that)»
	«createChainedParamIntents(transition, prefix)»
	«IF transition.target.outcoming.length >= 1»
		«"  "»<!-- Nested outcoming intents -->
		«FOR action: transition.target.actions»
			«IF action instanceof Text»
				«FOR language: action.inputs»
					«var List<?> responses»
					«{responses = language.getAllIntentResponses(); ""}»
					«FOR response: responses»
						«FOR outcoming: transition.target.outcoming»
							«intentFile(outcoming, bot, transition.intent.name, response.toString())»
						«ENDFOR»
					«ENDFOR»
				«ENDFOR»
			«ELSEIF action instanceof Empty»
				«FOR outcoming: transition.target.outcoming»
					«intentFile(outcoming, bot, transition.intent.name, "")»
				«ENDFOR»
			«ENDIF»
		«ENDFOR»
	«ENDIF»
	'''
	
	// Crea los intents de Pandorabots relacionados con las entradas del usuario que generalmente conllevan guardado
	// de argumentos
	def createIntentInputs(UserInteraction transition, Bot bot, String prefix, String that) '''
		«"  "»<!-- Intent inputs -->
		«FOR language: transition.intent.inputs»
	  		«FOR input: language.inputs»
	  			«IF input instanceof TrainingPhrase»
					«"  "»<category>
					«"    "»<pattern>«FOR token: input.tokens»«IF token instanceof Literal»«token.text.replace('?', ' #').replace('&', language.language.ampersandSubstitution)»«ELSEIF token instanceof ParameterReferenceToken»*«ENDIF»«ENDFOR»</pattern>
					«IF !that.isEmpty()»
						«"    "»<that>«that.replaceAll('[?.!]', ' ').replace('&', language.language.ampersandSubstitution)»</that>
					«ENDIF»
					«"    "»<template>
					«var List<String> entities»
					«{entities = getPhraseEntities(input); ""}»
					«IF !entities.isEmpty()»
						«"      "»<think>
						«FOR entity: entities»
							«"        "»<srai>
							«"          "»SAVE«(prefix + entity).toUpperCase()» <star index="«entities.indexOf(entity) + 1»"/>
							«"        "»</srai>
						«ENDFOR»
						«"      "»</think>
					«ENDIF»
					«var lang = ""»
				  	«IF language.language != Language.EMPTY»
				    	«{lang = language.language.languageAbbreviation; ""}»
				  	«ELSE»
				    	«{lang = bot.languages.get(0).languageAbbreviation; ""}»
				    «ENDIF»
					«var String nextPrompt»
					«{nextPrompt = getNextParamPetition(transition.intent, input).getValue(); ""}»
					«IF nextPrompt !== ""»
						«"      "»«nextPrompt»
					«ELSE»
						«"      "»<think>
						«"        "»<set name="pandoralang">«lang»</set>
						«"      "»</think>
						«"      "»<srai>«(prefix + transition.intent.name).toUpperCase().replaceAll('[ _]', '').toUpperCase()»</srai>
					«ENDIF»
					«"    "»</template>
					«"  "»</category>
			  	«ENDIF»
		  	«ENDFOR»
		«ENDFOR»
	'''
		
	// Generacion de codigo referente a la lectura de parametros por parte del usuario y posterior solicitud del resto
	// de parametros requeridos
	def createChainedParamIntents(UserInteraction transition, String prefix) {
		var parameters = getIntentParameters(transition.intent)
		if (parameters.isEmpty())
			return ""
		
		else {
			var ret = 
			'''
			«"  "»<!-- Chained param intents -->
			'''
			for (key: parameters.keySet) {
				var value = parameters.get(key)
				var paramConditions = generateParamConditionsRec(transition.intent, new ArrayList<String>(parameters.keySet), "  ", prefix)
				var completeKey = prefix + key
				switch (value) {
					case DefaultEntity.TEXT:
						ret += 
						'''
						«"  "»<category>
						«"    "»<pattern>*</pattern>
						«"    "»<that>«getParamPromptByName(transition.intent, key).replaceAll('[?.!]', ' ').replace('&', transition.intent.inputs.get(0).language.ampersandSubstitution)»</that>
						«"    "»<template>
						«"      "»<think>
						«"        "»<srai>SAVE«completeKey.toUpperCase()» <star/></srai>
						«"      "»</think>
						«paramConditions»
						«"    "»</template>
						«"  "»</category>
						'''
					case DefaultEntity.TIME:
						ret +=
						'''
						«"  "»<category>
						«"    "»<pattern>* colon *</pattern>
						«"    "»<that>«getParamPromptByName(transition.intent, key).replaceAll('[?.!]', ' ').replace('&', transition.intent.inputs.get(0).language.ampersandSubstitution)»</that>
						«"    "»<template>
						«"      "»<think>
						«"        "»<srai>SAVE«completeKey.toUpperCase()» <star index="1"/>:<star index="2"/></srai>
						«"      "»</think>
						«paramConditions»
						«"    "»</template>
						«"  "»</category>
						'''
					case DefaultEntity.DATE:
						ret +=
						'''
						«"  "»<category>
						«"    "»<pattern>* slash * slash *</pattern>
						«"    "»<that>«getParamPromptByName(transition.intent, key).replaceAll('[.!]', ' ').replace('&', transition.intent.inputs.get(0).language.ampersandSubstitution).replace('?', " #")»</that>
						«"    "»<template>
						«"      "»<think>
						«"        "»<srai>SAVE«completeKey.toUpperCase()» <star index="1"/>/<star index="2"/>/<star index="3"/></srai>
						«"      "»</think>
						«paramConditions»
						«"    "»</template>
						«"  "»</category>
						'''
					case DefaultEntity.NUMBER:
						ret +=
						'''
						«"  "»<category>
						«"    "»<pattern><set>number</set></pattern>
						«"    "»<that>«getParamPromptByName(transition.intent, key).replaceAll('[?.!]', ' ').replace('&', transition.intent.inputs.get(0).language.ampersandSubstitution).replace('?', " #")»</that>
						«"    "»<template>
						«"      "»<think>
						«"        "»<srai>SAVE«completeKey.toUpperCase()» <star/></srai>
						«"      "»</think>
						«paramConditions»
						«"    "»</template>
						«"  "»</category>
						'''
					default: 
						ret += 
						'''
						«"  "»<category>
						«"    "»<pattern>*</pattern>
						«"    "»<that>«getParamPromptByName(transition.intent, key).replaceAll('[?.!]', ' ').replace('&', transition.intent.inputs.get(0).language.ampersandSubstitution).replace('?', " #")»</that>
						«"    "»<template>
						«"      "»<think>
						«"        "»<srai>SAVE«completeKey.toUpperCase()» <star/></srai>
						«"      "»</think>
						«paramConditions»
						«"    "»</template>
						«"  "»</category>
						'''
				}
			}
			
			return ret
		}
	}
	
	// Genera los arboles de solicitud de parametros en cada intent de forma recursiva
	// TODO: cambiar para que acepte distintos lenguages. Hay que poner alguna condicion del lenguage en que se esten
	// pidiendo los argumentos en ese momento concreto y en base a eso imprimir el que corresponda
	def generateParamConditionsRec(Intent intent, List<String> params, String indent, String prefix) {
		if (params.isEmpty())
			return 
			'''
			«indent + "    "»<think>
			«indent + "      "»<set name="pandoralang">«intent.inputs.get(0).language.languageAbbreviation»</set>
			«indent + "    "»</think>
			«indent + "    "»<srai>«(prefix + intent.name).toUpperCase().replaceAll('[ _]', '').toUpperCase()»</srai>
			'''
		else {
			var currentParam = params.get(0)
			var newIndent = indent + "    "
			
			params.remove(currentParam)
			return
			'''
				«newIndent»<condition name="«prefix + currentParam»">
				«newIndent + "  "»<li value="unknown">«getParamPromptByName(intent, currentParam).replace('&', intent.inputs.get(0).language.ampersandSubstitution)»</li>
				«newIndent + "  "»<li>
				«generateParamConditionsRec(intent, params, newIndent, prefix)»
				«newIndent + "  "»</li>
				«newIndent»</condition>
			'''
		}
	}
	
	// Devuelve la peticion de parametro correspondiente dado el nombre del parametro
	def getParamPromptByName(Intent intent, String name) {
		var params = getIntentParameterPrompts(intent)
		for (param: params)
			if (param.key == name)
				return param.value
	}
	
	// Devuelve los parametros que se quieren recoger con un intent
	def getIntentParameters(Intent intent) {
		// <lenguaje, entity>
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
	def createSaveParameter(Intent intent, String prefix) {
		var parameters = getIntentParameters(intent)
		if (parameters.isEmpty())
			return ""
		
		else {
			var ret = "  <!-- Entity saving -->\n"
			for (key: parameters.keySet) {
				var value = parameters.get(key)
				var completeKey = prefix + key
				switch (value) {
					case DefaultEntity.TEXT:
						ret += 
						'''
						«"  "»<category>
						«"    "»<pattern>SAVE«completeKey.toUpperCase()» *</pattern>
						«"    "»<template>
						«"      "»<think><set name="«completeKey»"><star/></set></think>
						«"    "»</template>
						«"  "»</category>
						'''
					case DefaultEntity.TIME:
						ret +=
						'''
						«"  "»<category>
						«"    "»<pattern>SAVE«completeKey.toUpperCase()» * colon *</pattern>
						«"    "»<template>
						«"      "»<think>
						«"        "»<set name="«completeKey»_is_valid"><srai>ISVALIDHOUR <star index="1"/>:<star index="2"/></srai></set>
						«"      "»</think>
						«"      "»<condition name="«completeKey»_is_valid">
						«"        "»<li value="TRUE">
						«"          "»<think>
						«"            "»<set name="«completeKey»"><star index="1"/>:<star index="2"/></set>
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
						«"    "»<pattern>SAVE«completeKey.toUpperCase()» * slash * slash *</pattern>
						«"    "»<template>
						«"      "»<think>
						«"        "»<set name="«completeKey»_is_valid">
						«"          "»<srai>VALIDDATE <star index="1"/>/<star index="2"/>/<star index="3"/></srai>
						«"        "»</set>
						«"      "»</think>
						«"      "»<condition name="«completeKey»_is_valid">
						«"        "»<li value="TRUE">
						«"          "»<think><set name="«completeKey»"><star index="1"/>/<star index="2"/>/<star index="3"/></set></think>
						«"        "»</li>
						«"      "»</condition>
						«"    "»</template>
						«"  "»</category>
						'''
					case DefaultEntity.NUMBER:
						ret +=
						'''
						«"  "»<category>
						«"    "»<pattern>SAVE«completeKey.toUpperCase()» <set>number</set></pattern>
						«"    "»<template>
						«"      "»<think><set name="«completeKey»"><star/></set></think>
						«"    "»</template>
						«"  "»</category>
						'''
					default: 
						ret += 
						'''
						«"  "»<category>
						«"    "»<pattern>SAVE«completeKey.toUpperCase()» *</pattern>
						«"    "»<template>
						«"      "»<think><set name="«completeKey»"><star/></set></think>
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
	
	def getActionsByLanguage(UserInteraction transition) {
		var ret = new HashMap<String, List<String>>()
		var othersList = new ArrayList<String>()
		
		for (action: transition.target.actions) {
			if (action instanceof Text) {
				for (input: action.inputs) {
					var lang = languageAbbreviation(input.language)
					if (!ret.keySet.contains(lang)) {
						var temp = new ArrayList<String>()
						temp.add(action.name)
						ret.put(lang, temp)
					}
					
					else {
						var tempList = ret.get(lang)
						tempList.add(action.name)
						ret.put(lang, tempList)
					}
				}
			}
			
			else {
				othersList.add(action.name)
			}
		}
		
		if (!othersList.isEmpty())
			ret.put("others", othersList)
		
		return ret
	}
	
	// Generacion de codigo de un intent con una o varias respuestas
	def intentGenerator(UserInteraction transition, Bot bot, String prefix)
	'''
		«var intentName = ""»
		«"  "»<!-- Main intents -->
«««		«FOR language: transition.intent.inputs»
«««		«var lang = ""»
«««	  	«IF language.language != Language.EMPTY»
«««	    	«{lang = language.language.languageAbbreviation; ""}»
«««	  	«ELSE»
«««	    	«{lang = bot.languages.get(0).languageAbbreviation; ""}»
«««	    «ENDIF»
		«{intentName = (prefix + transition.intent.name).toUpperCase().replaceAll('[ _]', ''); ""}»
		«"  "»<category>
		«"    "»<pattern>«intentName»</pattern>
		«"    "»<template>
		«"      "»<condition name="pandoralang">
		«var flag = ""»
		«var HashMap<?, ?> langActions»
		«{langActions = getActionsByLanguage(transition); ""}»
		«FOR key: langActions.keySet»
			«IF key !== "others"»
				«"        "»<li value="«key»">
				«FOR act: (langActions.get(key) as ArrayList<String>)»
					«"          "»<srai>«(intentName + (key as String) + act).replaceAll('[ _]', '').toUpperCase()»</srai>
				«ENDFOR»
				«IF langActions.get("others") !== null && flag == ""»
					«FOR act: (langActions.get("others") as ArrayList<String>)»
						«"          "»<srai>«(intentName + act).toUpperCase().replaceAll('[ _]', '')»</srai>
					«ENDFOR»
					«{flag="x"; ""}»
				«ENDIF»
				«"        "»</li>
			«ENDIF»
		«ENDFOR»
		«IF langActions.size() == 1 && langActions.get("others") !== null»
			«"        "»<li>
			«FOR act: langActions.get("others") as List<String>»
				«"          "»<srai>«(intentName + act).toUpperCase().replaceAll('[ _]', '')»</srai>
			«ENDFOR»
			«"        "»</li>
		«ENDIF»
		«"      "»</condition>
		«"    "»</template>
		«"  "»</category>
«««	    «ENDFOR»
		«"  "»<!-- Action intents -->
		«{intentName = (prefix + transition.intent.name).toUpperCase().replaceAll('[ _]', ''); ""}»
		«FOR action: transition.target.actions»
			«IF action instanceof Text»
				«FOR language: action.inputs»
					«var lang=""»
					«IF language.language != Language.EMPTY»
						«{lang = language.language.languageAbbreviation.toUpperCase(); ""}»
					«ELSE»
						«{lang = bot.languages.get(0).languageAbbreviation.toUpperCase(); ""}»
					«ENDIF»
					«"  "»<category>
					«"    "»<pattern>«(intentName + lang + action.name).replaceAll('[ _]', '').toUpperCase()»</pattern>
					«IF language.getAllIntentResponses().length > 1»
						«"    "»<template>
						«"      "»<random>
						«FOR response: language.getAllIntentResponses()»
							«"        "»<li>«response.replaceAll("[?.!]", ' ').replace('&', language.language.ampersandSubstitution)»</li>
						«ENDFOR»
						«"      "»</random>
						«"    "»</template>
						«"  "»</category>
					«ELSE»
						«"    "»<template>«language.getAllIntentResponses().get(0).replaceAll("[?.!]", ' ').replace('&', language.language.ampersandSubstitution)»</template>
						«"  "»</category>
					«ENDIF»
				«ENDFOR»
			«ELSEIF action instanceof Empty»
				«"  "»<category>
				«"    "»<pattern>«(intentName + action.name).replaceAll('[ _]', '').toUpperCase()»</pattern>
				«"    "»<template></template>
				«"  "»</category>
			«ELSEIF action instanceof Image»
				«"  "»<category>
				«"    "»<pattern>«(intentName + action.name).toUpperCase().replaceAll('[ _]', '')»</pattern>
				«"    "»<template><image>«action.URL»</image></template>
				«"  "»</category>
			«ELSEIF action instanceof HTTPRequest»
				«"  "»<category>
				«"    "»<pattern>«(intentName + action.name).toUpperCase().replaceAll('[ _]', '')»</pattern>
				«"    "»<template>
				«"      "»<callapi response_code_var="response_«prefix + action.name»">
				«"        "»<url>«(action as HTTPRequest).getURL()»</url>
				«"        "»<method>«action.method»</method>
				«FOR header: action.headers»
					«"        "»<header><name>«header.key»</name>«(header.value as Literal).text»</header>
				«ENDFOR»
				«FOR param: action.data»
					«"        "»<query name="«(param.value as ParameterToken).parameter.name»"><get name="«(param.value as ParameterToken).parameter.name»"/></query>
				«ENDFOR»
				«"      "»</callapi>
				«"    "»</template>
				«"  "»</category>
			«ELSEIF action instanceof HTTPResponse»
				«"  "»<category>
				«"    "»<pattern>«(intentName + action.name).toUpperCase().replaceAll('[ _]', '')»</pattern>
				«"    "»<template>
				«"      "»<get name="response_«prefix + (action as HTTPResponse).HTTPRequest.name»"/>
				«"    "»</template>
				«"  "»</category>
			«ENDIF»
		«ENDFOR»
	'''

	// Devuelve la sustitucion del simbolo ampersand asociado al idioma en que este programado el bot
	def ampersandSubstitution(Language lang) {
		switch (lang) {
			case Language.ENGLISH:
				return 'and'
			case Language.SPANISH:
				return 'y'
			case Language.DANISH:
				return 'og'
			case Language.GERMAN:
				return 'und'
			case Language.FRENCH:
				return 'et'
			case Language.INDONESIAN:
				return 'dan'
			case Language.ITALIAN:
				return 'e'
			case Language.DUTCH:
				return 'en'
			case Language.NORWEGIAN:
				return 'og'
			case Language.POLISH:
				return 'i'
			case Language.PORTUGUESE:
				return 'e'
			case Language.SWEDISH:
				return 'och'
			case Language.TURKISH:
				return 've'
			case Language.UKRANIAN:
				return 'i'
			default:
				return 'and'
		}
	}
	
	// Obtencion de abreviacion del lenguage
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
	
	def static isTheLast(List<?> list, Object object) {
		if (list.indexOf(object) == list.size - 1) {
			return true;
		}
		return false;

	}
}
