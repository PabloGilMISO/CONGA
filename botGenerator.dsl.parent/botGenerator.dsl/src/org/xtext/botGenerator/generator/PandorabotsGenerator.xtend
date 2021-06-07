package org.xtext.botGenerator.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import generator.Bot
import generator.HTTPRequest
import generator.Token
import generator.Literal
import generator.EntityToken
import generator.UserInteraction
import generator.DefaultEntity
import generator.Parameter
import generator.Text
import generator.Image
import generator.ParameterReferenceToken
import generator.ParameterToken
import generator.TextInput
import generator.Language
import generator.IntentLanguageInputs
import generator.TextLanguageInput
import generator.LanguageInput
import generator.EntityInput
import generator.RegexInput
import generator.SimpleInput
import generator.CompositeInput
import generator.TrainingPhrase
import generator.Entity
import java.util.List
import java.util.UUID
import zipUtils.Zip
import java.nio.file.Files
import java.nio.file.Paths
import org.eclipse.core.resources.ResourcesPlugin
import java.util.ArrayList
import generator.impl.IntentImpl
import generator.impl.IntentLanguageInputsImpl
import generator.impl.TrainingPhraseImpl
import generator.Intent
import java.util.HashMap

class PandorabotsGenerator {
	String path;
	protected static String uri;
	Zip zip;

	def doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context, Zip zip) {
		var resourceName = resource.URI.lastSegment.substring(0, resource.URI.lastSegment.indexOf("."));
		var bot = resource.allContents.filter(Bot).toList.get(0);

		path = resourceName + "/Pandorabots"

		this.zip = zip;

		// Creacion de fichero de propiedades .properties
		var systemPropertiesName = path + "/system/" + resourceName.toLowerCase().replace(' ', '_') + ".properties"
		fsa.generateFile(systemPropertiesName, systemFileFill())
		var systemPropertiesValue = fsa.readBinaryFile(systemPropertiesName)
		zip.addFileToFolder("system", resourceName.toLowerCase().replace(' ', '_') + ".properties", systemPropertiesValue)

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
		
		// TODO: Generacion del archivo udc.aiml
		////////////////////////////////////////
		
		zip.close
	}

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

	// Llena el set correspondiente al input concreto (entity->inputs->inputs)
	def entitySetFill(SimpleInput input) '''
		[
		�FOR synonym : input.values�
			["�synonym�"]�IF !input.values.isTheLast(synonym)�,�ENDIF�
		�ENDFOR�
		]
	'''

	// Llena el map con todas las entities de pandorabots y su set asociado
	def entityMapFill(Entity entity) '''
		[
		�FOR input : entity.inputs�
			�FOR entry: input.inputs�
				�entry(entry)�
			�ENDFOR�
		�ENDFOR�
		]
	'''

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
		�FOR synonym : entry.values�
			["�synonym�", "�entry.name�"]�IF !entry.values.isTheLast(synonym)�,�ENDIF�
		�ENDFOR�
	'''

	// TODO: No soportadas
	def entry(CompositeInput entry) '''
		"value": "�entry.compositeEntry�",
		"synonyms": [
			"�entry.compositeEntry�"
		]
	'''
	// TODO: No soportadas
	def entry(RegexInput entry)'''
		"value": "�entry.expresion�",
		"synonyms": [
			"�entry.expresion�"
		]
	'''

	// Guarda los intents durante el recorrido de los flujos de conversaci�n
	def void createTransitionFiles(UserInteraction transition, String prefix, IFileSystemAccess2 fsa, Bot bot) {
		// Generacion del archivo del intent del que parte el flujo concreto
		var intentFileContent = 
		'''
		<?xml version="1.0" encoding="UTF-8"?>
		�  �<aiml>
		'''
		// Generacion de intents de Pandorabots para guardar los parametros
		intentFileContent += createSaveParameter(transition.intent)
		// Generacion de intents
		intentFileContent += transition.intentFile(prefix, bot)
		intentFileContent += "</aiml>"
		fsa.generateFile(path + "/files/" + prefix + transition.intent.name + ".aiml", intentFileContent)
		var intentValue = fsa.readBinaryFile(path + "/files/" + prefix + transition.intent.name + ".aiml")
		zip.addFileToFolder("files", prefix + transition.intent.name + ".aiml", intentValue)
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
	
	// Devuelve todas las posibles respuestas a un intent
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
	
	// Generador de codigo de un intent
	// TODO: 
	// 1. Mirar funcionamiento del webhook.
	def intentFile(UserInteraction transition, String prefix, Bot bot)
	'''
	�"    "�<!-- Intent -->
	�FOR action: transition.target.actions�
		�IF action instanceof Text�
			�FOR texLanguage: action.inputs�
				�intentGenerator(transition, texLanguage, bot)�
			�ENDFOR�
		�ELSEIF action instanceof Image�
			�"    "�<category>
	  		�"      "�<pattern>�transition.intent.name.toUpperCase().replace(' ', '_')�</pattern>
	  		�"      "�<template><image>�(action as Image).URL�</image></template>
	  		�"    "�</category>
		�ENDIF��IF !isTheLast(transition.target.actions, action)�,�ENDIF�
	  �ENDFOR�
	�"    "�<!-- Intent inputs -->
	�FOR language: transition.intent.inputs�
		�var lang=""�
      	�IF language.language != Language.EMPTY�
  	    	�{lang = language.language.languageAbbreviation; ""}�
      	�ELSE�
	    	�{lang = bot.languages.get(0).languageAbbreviation; ""}�
	    �ENDIF�
  		�FOR input: language.inputs�
  			�IF input instanceof TrainingPhrase�
				�"    "�<category>
				�"      "�<pattern>�FOR token: input.tokens��IF token instanceof Literal��token.text��ELSEIF token instanceof ParameterReferenceToken�*�ENDIF��ENDFOR�</pattern>
				�"      "�<template><srai>�(transition.intent.name.toUpperCase().replace(' ', '_') + "_" + lang).toUpperCase()��FOR token: input.tokens��IF token instanceof ParameterReferenceToken� <star/>�ENDIF��ENDFOR�</srai></template>
				�"    "�</category>
		  	�ENDIF�
	  	�ENDFOR�
	�ENDFOR�
	'''
	
	// Devuelve los parametros que se quieren recoger con un intent
	def getIntentParameters(Intent intent) {
		var ret = new HashMap<String, ParameterReferenceToken>()
		
		for (IntentLanguageInputs language: intent.inputs) {
			for (input: language.inputs) {
				if (input instanceof TrainingPhrase) {
					for (token: (input as TrainingPhrase).tokens) {
						if (token instanceof ParameterReferenceToken) {
							// Si no contiene el parametros por otro input, lo introduce en el diccionario
							if (!ret.keySet.contains(token.parameter.name)) {
								ret.put(token.parameter.name, token)
							}
						}
					}
				}
			}
		}
		
		return ret
	}
	
	// Genera los intents de pandorabots de tipo SAVE_PARAMETER para poder guardar los parametros de distintos
	// tipos del intent concreto
	def createSaveParameter(Intent intent) {
		var parameters = getIntentParameters(intent)
		if (parameters.isEmpty())
			return ""
		
		else {
			var ret = ""
			for (key: parameters.keySet) {
				var value = parameters.get(key)
				if (value.parameter.entity === null) {
					switch (value.parameter.defaultEntity) {
						case DefaultEntity.TEXT:
							ret += 
							'''
							�    �<category>
							�      �<pattern>SAVE_�value.parameter.name.toUpperCase()� *</pattern>
							�      �<template>
							�        �<think><set name="�value.parameter.name�"</set></think>
							�      �</template>
							�    �<category>
							'''
						case DefaultEntity.TIME:
							ret +=
							'''
							�    �<category>
							�      �<pattern>SAVE_�value.parameter.name.toUpperCase()� * colon *</pattern>
							�      �<template>
							�        �<think>
							�          �<set name="�value.parameter.name�_is_valid"><srai>ISVALIDHOUR <star index="1"/> colon <star index="2"/></srai></set>
							�        �</think>
							�        �<condition name="�value.parameter.name�_is_valid">
							�          �<li value="TRUE">
							�            �<think>
							�              �<set name="�value.parameter.name�"><star index="1"/>:<star index="2"/></set>
							�            �</think>
							�          �</li>
							�        �</condition>
							�      �</template>
							�    �</category>
							'''
						case DefaultEntity.DATE:
							ret +=
							'''
							�    �<category>
							�      �<pattern>SAVE_�value.parameter.name� * slash * slash *</pattern>
							�      �<template>
							�        �<think>
							�          �<set name="�value.parameter.name�_is_valid"><srai>VALIDDATE <star index="1"/>/<star index="2"/>/<star index="3"/></srai></set>
							�        �</think>
							�        �<condition name="�value.parameter.name�_is_valid">
							�          �<li value="TRUE"><think><set name="�value.parameter.name�"><star index="1"/>/<star index="2"/>/<star index="3"/></set></think></li>
							�        �</condition>
							�      �</template>
							�    �</category>
							'''
						case DefaultEntity.NUMBER:
							ret +=
							'''
							�    �<category>
							�      �<pattern>SAVE_�value.parameter.name� <set>number</set></pattern>
							�      �<template>
							�        �<think><set name="�value.parameter.name�"><star/></set></think>
							�      �</template>
							�    �</category>
							'''
					}
				} else {
					ret +=
					'''
					<category>
					�  �<pattern>SAVESAVE_�value.parameter.name� *</pattern>
					�  �<template>
					�    �<think>
					�      �<set name="�value.parameter.name�_temp"><map name="animals"><star/></map></set>
					�    �</think>
					�    �<condition name="�value.parameter.name�_temp">
					�      �<li value="unknown"></li>
					�      �<li><set name="�value.parameter.name�"><get name="�value.parameter.name�_temp"/></set></li>
					�    �</condition>
					�  �</template>
					</category>
					'''
				}
			}
		}
	}
	
	// Generacion de codigo de un intent con una o varias respuestas
	def intentGenerator(UserInteraction transition, TextLanguageInput textAction, Bot bot)
	'''
		�var lang=""�
		�IF textAction.language != Language.EMPTY�
			�{lang = textAction.language.languageAbbreviation.toUpperCase(); ""}�
		�ELSE�
			�{lang = bot.languages.get(0).languageAbbreviation.toUpperCase(); ""}�
		�ENDIF�
		�IF textAction.getAllIntentResponses().length > 1�
			�"    "�<category>
			�"      "�<pattern>�(transition.intent.name.toUpperCase().replace(' ', '_') + "_" + lang).toUpperCase()�</pattern>
			�"      "�<template>
			�"        "�<random>
			�FOR response: textAction.getAllIntentResponses()�
				�"          "�<li>�response�</li>
			�ENDFOR�
			�"        "�</random>
			�"      "�</template>
			�"    "�</category>
		�ELSE�
			�"    "�<category>
			�"      "�<pattern>�(transition.intent.name.toUpperCase().replace(' ', '_') + "_" + lang).toUpperCase()�</pattern>
			�"      "�<template>�textAction.getAllIntentResponses().get(0)�</template>
			�"    "�</category>
		�ENDIF�
	'''
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// Generacion de codigo de los inputs de cada intent
//	def usersayFile(IntentLanguageInputs intent) '''
//		[
//		�FOR phrase : intent.inputs�
//			�IF phrase instanceof TrainingPhrase�
//				{
//				  "id": "�UUID.randomUUID().toString�",
//				  "data": [
//				�FOR token: phrase.tokens�
//					�IF token instanceof Literal�
//						{
//						  "text": "�token.text�",
//						  "userDefined": false
//						},
//					�ELSEIF token instanceof ParameterReferenceToken�
//						{
//						  "text": "�(token as ParameterReferenceToken).textReference�",
//						  "alias": "�(token as ParameterReferenceToken).parameter.name�",
//						  "meta": "�(token as ParameterReferenceToken).parameter.paramType�",
//						  "userDefined": true
//						},
//					�ENDIF�
//					{
//						"text": " ",
//						"userDefined": false
//					}�IF !phrase.tokens.isTheLast(token)�,�ENDIF�
//				�ENDFOR�
//				],
//				"isTemplate": false,
//				"count": 0,
//				"updated": 0
//				}�IF !isTheLast(intent.inputs, phrase)�,�ENDIF�
//			�ENDIF�
//		 �ENDFOR�
//		 ]
//	'''
	
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
			"id": "�UUID.randomUUID().toString�",
			"name": "�entity.name�",
			"isOverridable": true,	  
			�IF BotGenerator.entityType(entity) === BotGenerator.REGEX�
				"isEnum": false,
				"isRegexp":true,
				"automatedExpansion": true,
				"allowFuzzyExtraction": false
			�ELSEIF BotGenerator.entityType(entity) === BotGenerator.SIMPLE�
				"isEnum": false,
				"isRegexp": false,
				"automatedExpansion": true,
				"allowFuzzyExtraction": true
			�ELSE�
				"isEnum": true,
				"isRegexp": false,
				"automatedExpansion": false,
				"allowFuzzyExtraction": false
			�ENDIF�
		}
	'''

	def entityIsSimple(Entity entity) {
	}

	// Generador de codigo para cada lenguaje en que este configurada cada entity
	def entriesFile(LanguageInput entity) '''
		[
			�FOR entry : entity.inputs�
				{
				   �entry(entry)�
				} �IF !entity.inputs.isTheLast(entry)�,�ENDIF�
			�ENDFOR�
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
