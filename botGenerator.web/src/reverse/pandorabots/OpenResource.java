package reverse.pandorabots;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.xtext.botGenerator.BotStandaloneSetup;

import com.google.inject.Injector;

import generator.Bot;
import generator.GeneratorFactory;
import generator.Intent;
import generator.IntentLanguageInputs;
import generator.Language;
import generator.Literal;
import generator.TrainingPhrase;
import generator.UserInteraction;
import reverse.pandorabots.agent.Agent;

public class OpenResource {

//	@Inject
//	private static IResourceValidator validator;
	public static void main(String[] args) throws IOException {
		XtextSerialize.init();
//		Bot bot = createBotExamlpe();
		
		String pandorabotsPath = "C:/CONGA/pandorabots/veterinaryCenterPruebas";
		File zip = new File(pandorabotsPath + ".zip");		
		ReadPandorabotsAgent reader = new ReadPandorabotsAgent();
		Agent fullAgent = reader.getAgent(zip);
		
		Bot bot = fullAgent.getBot();
		
		Injector injector = new BotStandaloneSetup().createInjectorAndDoEMFRegistration();
		IResourceValidator validator = injector.getInstance(IResourceValidator.class);
		Resource resource = XtextSerialize.SaveDSLBot(bot, "/CONGA/BotExameple.bot");
		
		List<Issue> issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
		
		System.out.println(issues);
		
	}
	
	
	public static Bot createBotExamlpe() {
		Bot ret = GeneratorFactory.eINSTANCE.createBot();
		ret.setName("Example");
		ret.getLanguages().add(Language.ENGLISH);
		ret.getLanguages().add(Language.SPANISH);
		
		Intent intent = GeneratorFactory.eINSTANCE.createIntent();
		intent.setName("Greetings");
		IntentLanguageInputs inputs = GeneratorFactory.eINSTANCE.createIntentLanguageInputs();
		inputs.setLanguage(Language.ENGLISH);
		
		TrainingPhrase phrase1 = GeneratorFactory.eINSTANCE.createTrainingPhrase();
		Literal lit1= GeneratorFactory.eINSTANCE.createLiteral();
		lit1.setText("Hello");
		phrase1.getTokens().add(lit1);
		
		TrainingPhrase phrase2 = GeneratorFactory.eINSTANCE.createTrainingPhrase();
		Literal lit2= GeneratorFactory.eINSTANCE.createLiteral();
		lit2.setText("Hi");
		phrase2.getTokens().add(lit2);
		
		TrainingPhrase phrase3 = GeneratorFactory.eINSTANCE.createTrainingPhrase();
		Literal lit3= GeneratorFactory.eINSTANCE.createLiteral();
		lit3.setText("Hey");
		phrase3.getTokens().add(lit3);
	
		inputs.getInputs().add(phrase1);
		inputs.getInputs().add(phrase2);
		inputs.getInputs().add(phrase3);
		
		intent.getInputs().add(inputs);
		
		ret.getIntents().add(intent);
		UserInteraction ui = GeneratorFactory.eINSTANCE.createUserInteraction();
		ui.setIntent(intent);
		ret.getFlows().add(ui);
		return ret;
		
	}
}
