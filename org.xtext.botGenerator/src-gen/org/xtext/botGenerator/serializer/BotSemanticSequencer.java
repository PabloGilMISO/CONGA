/*
 * generated by Xtext 2.20.0
 */
package org.xtext.botGenerator.serializer;

import com.google.inject.Inject;
import generator.Bot;
import generator.BotInteraction;
import generator.Composite;
import generator.CompositeInput;
import generator.CompositeLanguageInput;
import generator.EntityToken;
import generator.GeneratorPackage;
import generator.HTTPRequest;
import generator.HTTPRequestToke;
import generator.HTTPResponse;
import generator.Image;
import generator.Intent;
import generator.IntentLanguageInputs;
import generator.KeyValue;
import generator.Literal;
import generator.ParameterReferenceToken;
import generator.ParameterToken;
import generator.PromptLanguage;
import generator.Simple;
import generator.SimpleInput;
import generator.SimpleLanguageInput;
import generator.Text;
import generator.TextInput;
import generator.TextLanguageInput;
import generator.TrainingPhrase;
import generator.UserInteraction;
import java.util.Set;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.Action;
import org.eclipse.xtext.Parameter;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.serializer.ISerializationContext;
import org.eclipse.xtext.serializer.acceptor.SequenceFeeder;
import org.eclipse.xtext.serializer.sequencer.AbstractDelegatingSemanticSequencer;
import org.eclipse.xtext.serializer.sequencer.ITransientValueService.ValueTransient;
import org.xtext.botGenerator.services.BotGrammarAccess;

@SuppressWarnings("all")
public class BotSemanticSequencer extends AbstractDelegatingSemanticSequencer {

	@Inject
	private BotGrammarAccess grammarAccess;
	
	@Override
	public void sequence(ISerializationContext context, EObject semanticObject) {
		EPackage epackage = semanticObject.eClass().getEPackage();
		ParserRule rule = context.getParserRule();
		Action action = context.getAssignedAction();
		Set<Parameter> parameters = context.getEnabledBooleanParameters();
		if (epackage == GeneratorPackage.eINSTANCE)
			switch (semanticObject.eClass().getClassifierID()) {
			case GeneratorPackage.BOT:
				sequence_Bot(context, (Bot) semanticObject); 
				return; 
			case GeneratorPackage.BOT_INTERACTION:
				if (rule == grammarAccess.getState2Rule()) {
					sequence_State2(context, (BotInteraction) semanticObject); 
					return; 
				}
				else if (rule == grammarAccess.getStateRule()) {
					sequence_State(context, (BotInteraction) semanticObject); 
					return; 
				}
				else break;
			case GeneratorPackage.COMPOSITE:
				sequence_Composite(context, (Composite) semanticObject); 
				return; 
			case GeneratorPackage.COMPOSITE_INPUT:
				sequence_CompositeInput(context, (CompositeInput) semanticObject); 
				return; 
			case GeneratorPackage.COMPOSITE_LANGUAGE_INPUT:
				sequence_CompositeLanguageInput(context, (CompositeLanguageInput) semanticObject); 
				return; 
			case GeneratorPackage.ENTITY_TOKEN:
				sequence_EntityToken(context, (EntityToken) semanticObject); 
				return; 
			case GeneratorPackage.HTTP_REQUEST:
				sequence_HTTPRequest(context, (HTTPRequest) semanticObject); 
				return; 
			case GeneratorPackage.HTTP_REQUEST_TOKE:
				sequence_HTTPRequestToken(context, (HTTPRequestToke) semanticObject); 
				return; 
			case GeneratorPackage.HTTP_RESPONSE:
				sequence_HTTPResponse(context, (HTTPResponse) semanticObject); 
				return; 
			case GeneratorPackage.IMAGE:
				sequence_Image(context, (Image) semanticObject); 
				return; 
			case GeneratorPackage.INTENT:
				sequence_Intent(context, (Intent) semanticObject); 
				return; 
			case GeneratorPackage.INTENT_LANGUAGE_INPUTS:
				sequence_IntentLanguageInputs(context, (IntentLanguageInputs) semanticObject); 
				return; 
			case GeneratorPackage.KEY_VALUE:
				if (rule == grammarAccess.getDataRule()) {
					sequence_Data(context, (KeyValue) semanticObject); 
					return; 
				}
				else if (rule == grammarAccess.getKeyValueRule()) {
					sequence_KeyValue(context, (KeyValue) semanticObject); 
					return; 
				}
				else break;
			case GeneratorPackage.LITERAL:
				sequence_Literal(context, (Literal) semanticObject); 
				return; 
			case GeneratorPackage.PARAMETER:
				sequence_Parameter(context, (generator.Parameter) semanticObject); 
				return; 
			case GeneratorPackage.PARAMETER_REFERENCE_TOKEN:
				sequence_ParameterRefenceToken(context, (ParameterReferenceToken) semanticObject); 
				return; 
			case GeneratorPackage.PARAMETER_TOKEN:
				sequence_ParameterToken(context, (ParameterToken) semanticObject); 
				return; 
			case GeneratorPackage.PROMPT_LANGUAGE:
				sequence_PromptLanguage(context, (PromptLanguage) semanticObject); 
				return; 
			case GeneratorPackage.SIMPLE:
				sequence_Simple(context, (Simple) semanticObject); 
				return; 
			case GeneratorPackage.SIMPLE_INPUT:
				sequence_SimpleInput(context, (SimpleInput) semanticObject); 
				return; 
			case GeneratorPackage.SIMPLE_LANGUAGE_INPUT:
				sequence_SimpleLanguageInput(context, (SimpleLanguageInput) semanticObject); 
				return; 
			case GeneratorPackage.TEXT:
				sequence_Text(context, (Text) semanticObject); 
				return; 
			case GeneratorPackage.TEXT_INPUT:
				if (rule == grammarAccess.getTextInputHttpResponseRule()) {
					sequence_TextInputHttpResponse(context, (TextInput) semanticObject); 
					return; 
				}
				else if (rule == grammarAccess.getTextInputTextRule()) {
					sequence_TextInputText(context, (TextInput) semanticObject); 
					return; 
				}
				else break;
			case GeneratorPackage.TEXT_LANGUAGE_INPUT:
				if (rule == grammarAccess.getTextLanguageInputHttpResponseRule()) {
					sequence_TextLanguageInputHttpResponse(context, (TextLanguageInput) semanticObject); 
					return; 
				}
				else if (rule == grammarAccess.getTextLanguageInputRule()) {
					sequence_TextLanguageInput(context, (TextLanguageInput) semanticObject); 
					return; 
				}
				else break;
			case GeneratorPackage.TRAINING_PHRASE:
				sequence_TrainingPhrase(context, (TrainingPhrase) semanticObject); 
				return; 
			case GeneratorPackage.USER_INTERACTION:
				sequence_Transition(context, (UserInteraction) semanticObject); 
				return; 
			}
		if (errorAcceptor != null)
			errorAcceptor.accept(diagnosticProvider.createInvalidContextOrTypeDiagnostic(semanticObject, context));
	}
	
	/**
	 * Contexts:
	 *     Bot returns Bot
	 *
	 * Constraint:
	 *     (
	 *         name=EString 
	 *         languages+=Language 
	 *         languages+=Language* 
	 *         intents+=Intent 
	 *         intents+=Intent* 
	 *         (entities+=Entity entities+=Entity*)? 
	 *         (actions+=Action actions+=Action*)? 
	 *         flows+=Transition+
	 *     )
	 */
	protected void sequence_Bot(ISerializationContext context, Bot semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     CompositeInput returns CompositeInput
	 *
	 * Constraint:
	 *     (tokens+=Literal | tokens+=EntityToken)+
	 */
	protected void sequence_CompositeInput(ISerializationContext context, CompositeInput semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     CompositeLanguageInput returns CompositeLanguageInput
	 *
	 * Constraint:
	 *     (language=Language? (inputs+=CompositeInput inputs+=CompositeInput*)?)
	 */
	protected void sequence_CompositeLanguageInput(ISerializationContext context, CompositeLanguageInput semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Entity returns Composite
	 *     Composite returns Composite
	 *
	 * Constraint:
	 *     (name=EString inputs+=CompositeLanguageInput+)
	 */
	protected void sequence_Composite(ISerializationContext context, Composite semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Data returns KeyValue
	 *
	 * Constraint:
	 *     (key=EString (value=Literal | value=ParameterToken))
	 */
	protected void sequence_Data(ISerializationContext context, KeyValue semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Token returns EntityToken
	 *     EntityToken returns EntityToken
	 *
	 * Constraint:
	 *     entity=[Entity|EString]
	 */
	protected void sequence_EntityToken(ISerializationContext context, EntityToken semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, GeneratorPackage.Literals.ENTITY_TOKEN__ENTITY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, GeneratorPackage.Literals.ENTITY_TOKEN__ENTITY));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getEntityTokenAccess().getEntityEntityEStringParserRuleCall_1_0_1(), semanticObject.eGet(GeneratorPackage.Literals.ENTITY_TOKEN__ENTITY, false));
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     HTTPRequestToken returns HTTPRequestToke
	 *
	 * Constraint:
	 *     (type=HTTPReturnType dataKey=EString?)
	 */
	protected void sequence_HTTPRequestToken(ISerializationContext context, HTTPRequestToke semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Action returns HTTPRequest
	 *     HTTPRequest returns HTTPRequest
	 *
	 * Constraint:
	 *     (
	 *         method=Method 
	 *         name=EString 
	 *         URL=EString 
	 *         basicAuth=KeyValue? 
	 *         (headers+=KeyValue headers+=KeyValue*)? 
	 *         (data+=Data data+=Data* dataType=DataType)?
	 *     )
	 */
	protected void sequence_HTTPRequest(ISerializationContext context, HTTPRequest semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Action returns HTTPResponse
	 *     HTTPResponse returns HTTPResponse
	 *
	 * Constraint:
	 *     (name=EString HTTPRequest=[HTTPRequest|EString] inputs+=TextLanguageInputHttpResponse+)
	 */
	protected void sequence_HTTPResponse(ISerializationContext context, HTTPResponse semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Action returns Image
	 *     Image returns Image
	 *
	 * Constraint:
	 *     (name=EString URL=EString)
	 */
	protected void sequence_Image(ISerializationContext context, Image semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, GeneratorPackage.Literals.ELEMENT__NAME) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, GeneratorPackage.Literals.ELEMENT__NAME));
			if (transientValues.isValueTransient(semanticObject, GeneratorPackage.Literals.IMAGE__URL) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, GeneratorPackage.Literals.IMAGE__URL));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getImageAccess().getNameEStringParserRuleCall_2_0(), semanticObject.getName());
		feeder.accept(grammarAccess.getImageAccess().getURLEStringParserRuleCall_6_0(), semanticObject.getURL());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     IntentLanguageInputs returns IntentLanguageInputs
	 *
	 * Constraint:
	 *     (language=Language? inputs+=TrainingPhrase inputs+=TrainingPhrase*)
	 */
	protected void sequence_IntentLanguageInputs(ISerializationContext context, IntentLanguageInputs semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Intent returns Intent
	 *
	 * Constraint:
	 *     (name=EString fallbackIntent?='Fallback'? inputs+=IntentLanguageInputs* (parameters+=Parameter parameters+=Parameter*)?)
	 */
	protected void sequence_Intent(ISerializationContext context, Intent semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     KeyValue returns KeyValue
	 *
	 * Constraint:
	 *     (key=EString value=Literal)
	 */
	protected void sequence_KeyValue(ISerializationContext context, KeyValue semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, GeneratorPackage.Literals.KEY_VALUE__KEY) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, GeneratorPackage.Literals.KEY_VALUE__KEY));
			if (transientValues.isValueTransient(semanticObject, GeneratorPackage.Literals.KEY_VALUE__VALUE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, GeneratorPackage.Literals.KEY_VALUE__VALUE));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getKeyValueAccess().getKeyEStringParserRuleCall_0_0(), semanticObject.getKey());
		feeder.accept(grammarAccess.getKeyValueAccess().getValueLiteralParserRuleCall_2_0(), semanticObject.getValue());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Token returns Literal
	 *     Literal returns Literal
	 *
	 * Constraint:
	 *     text=EString
	 */
	protected void sequence_Literal(ISerializationContext context, Literal semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, GeneratorPackage.Literals.LITERAL__TEXT) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, GeneratorPackage.Literals.LITERAL__TEXT));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getLiteralAccess().getTextEStringParserRuleCall_0(), semanticObject.getText());
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Token returns ParameterReferenceToken
	 *     ParameterRefenceToken returns ParameterReferenceToken
	 *
	 * Constraint:
	 *     (textReference=EString parameter=[Parameter|EString])
	 */
	protected void sequence_ParameterRefenceToken(ISerializationContext context, ParameterReferenceToken semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, GeneratorPackage.Literals.PARAMETER_REFERENCE_TOKEN__TEXT_REFERENCE) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, GeneratorPackage.Literals.PARAMETER_REFERENCE_TOKEN__TEXT_REFERENCE));
			if (transientValues.isValueTransient(semanticObject, GeneratorPackage.Literals.PARAMETER_REFERENCE_TOKEN__PARAMETER) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, GeneratorPackage.Literals.PARAMETER_REFERENCE_TOKEN__PARAMETER));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getParameterRefenceTokenAccess().getTextReferenceEStringParserRuleCall_1_0(), semanticObject.getTextReference());
		feeder.accept(grammarAccess.getParameterRefenceTokenAccess().getParameterParameterEStringParserRuleCall_4_0_1(), semanticObject.eGet(GeneratorPackage.Literals.PARAMETER_REFERENCE_TOKEN__PARAMETER, false));
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Token returns ParameterToken
	 *     ParameterToken returns ParameterToken
	 *
	 * Constraint:
	 *     parameter=[Parameter|EString]
	 */
	protected void sequence_ParameterToken(ISerializationContext context, ParameterToken semanticObject) {
		if (errorAcceptor != null) {
			if (transientValues.isValueTransient(semanticObject, GeneratorPackage.Literals.PARAMETER_TOKEN__PARAMETER) == ValueTransient.YES)
				errorAcceptor.accept(diagnosticProvider.createFeatureValueMissing(semanticObject, GeneratorPackage.Literals.PARAMETER_TOKEN__PARAMETER));
		}
		SequenceFeeder feeder = createSequencerFeeder(context, semanticObject);
		feeder.accept(grammarAccess.getParameterTokenAccess().getParameterParameterEStringParserRuleCall_1_0_1(), semanticObject.eGet(GeneratorPackage.Literals.PARAMETER_TOKEN__PARAMETER, false));
		feeder.finish();
	}
	
	
	/**
	 * Contexts:
	 *     Parameter returns Parameter
	 *
	 * Constraint:
	 *     (name=EString (entity=[Entity|EString] | defaultEntity=DefaultEntity) required?='required'? prompts+=PromptLanguage* isList?='isList'?)
	 */
	protected void sequence_Parameter(ISerializationContext context, generator.Parameter semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     PromptLanguage returns PromptLanguage
	 *
	 * Constraint:
	 *     (language=Language? prompts+=EString prompts+=EString*)
	 */
	protected void sequence_PromptLanguage(ISerializationContext context, PromptLanguage semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     SimpleInput returns SimpleInput
	 *
	 * Constraint:
	 *     (name=EString (values+=EString values+=EString*)?)
	 */
	protected void sequence_SimpleInput(ISerializationContext context, SimpleInput semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     SimpleLanguageInput returns SimpleLanguageInput
	 *
	 * Constraint:
	 *     (language=Language? inputs+=SimpleInput inputs+=SimpleInput*)
	 */
	protected void sequence_SimpleLanguageInput(ISerializationContext context, SimpleLanguageInput semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Entity returns Simple
	 *     Simple returns Simple
	 *
	 * Constraint:
	 *     (name=EString inputs+=SimpleLanguageInput+)
	 */
	protected void sequence_Simple(ISerializationContext context, Simple semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     State2 returns BotInteraction
	 *
	 * Constraint:
	 *     (actions+=[Action|EString] actions+=[Action|EString]* outcoming+=Transition+)
	 */
	protected void sequence_State2(ISerializationContext context, BotInteraction semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     State returns BotInteraction
	 *
	 * Constraint:
	 *     (actions+=[Action|EString] actions+=[Action|EString]* outcoming+=Transition?)
	 */
	protected void sequence_State(ISerializationContext context, BotInteraction semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     TextInputHttpResponse returns TextInput
	 *
	 * Constraint:
	 *     (tokens+=Literal | tokens+=ParameterToken | tokens+=HTTPRequestToken)+
	 */
	protected void sequence_TextInputHttpResponse(ISerializationContext context, TextInput semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     TextInputText returns TextInput
	 *
	 * Constraint:
	 *     (tokens+=Literal | tokens+=ParameterToken)+
	 */
	protected void sequence_TextInputText(ISerializationContext context, TextInput semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     TextLanguageInputHttpResponse returns TextLanguageInput
	 *
	 * Constraint:
	 *     (language=Language? inputs+=TextInputHttpResponse inputs+=TextInputHttpResponse*)
	 */
	protected void sequence_TextLanguageInputHttpResponse(ISerializationContext context, TextLanguageInput semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     TextLanguageInput returns TextLanguageInput
	 *
	 * Constraint:
	 *     (language=Language? inputs+=TextInputText inputs+=TextInputText*)
	 */
	protected void sequence_TextLanguageInput(ISerializationContext context, TextLanguageInput semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Action returns Text
	 *     Text returns Text
	 *
	 * Constraint:
	 *     (name=EString inputs+=TextLanguageInput+)
	 */
	protected void sequence_Text(ISerializationContext context, Text semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     TrainingPhrase returns TrainingPhrase
	 *
	 * Constraint:
	 *     (tokens+=Literal | tokens+=ParameterRefenceToken)+
	 */
	protected void sequence_TrainingPhrase(ISerializationContext context, TrainingPhrase semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
	/**
	 * Contexts:
	 *     Transition returns UserInteraction
	 *
	 * Constraint:
	 *     (intent=[Intent|EString] (target=State | target=State2)?)
	 */
	protected void sequence_Transition(ISerializationContext context, UserInteraction semanticObject) {
		genericSequencer.createSequence(context, semanticObject);
	}
	
	
}
