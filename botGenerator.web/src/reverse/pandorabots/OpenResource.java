package reverse.pandorabots;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

//		String pandorabotsPath = "C:/CONGA/pandorabots/veterinaryCenterPruebas";
		String pandorabotsPath = "C:/CONGA/pandorabots/bots-externos/aiml-en-us-foundation-alice/client";

		removeZip(pandorabotsPath);
		buildZip(pandorabotsPath);

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
		Literal lit1 = GeneratorFactory.eINSTANCE.createLiteral();
		lit1.setText("Hello");
		phrase1.getTokens().add(lit1);

		TrainingPhrase phrase2 = GeneratorFactory.eINSTANCE.createTrainingPhrase();
		Literal lit2 = GeneratorFactory.eINSTANCE.createLiteral();
		lit2.setText("Hi");
		phrase2.getTokens().add(lit2);

		TrainingPhrase phrase3 = GeneratorFactory.eINSTANCE.createTrainingPhrase();
		Literal lit3 = GeneratorFactory.eINSTANCE.createLiteral();
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

	// Elimina un zip existente en el path sin extensión indicado
	public static void removeZip(String path) {
		File oldZip = new File(path + ".zip");
		if (oldZip.delete()) {
			System.out.println("Zip eliminado correctamente.");
		} else {
			System.out.println("Error eliminando el zip.");
		}
	}

	// Crea un zip a partir del archivo en path sin extensión indicada
	// Credit: https://www.baeldung.com/java-compress-and-uncompress
	public static void buildZip(String path) {
		try {
			String sourceFile = path + ".aiml";
			FileOutputStream fos = new FileOutputStream(path + ".zip");
			ZipOutputStream zipOut = new ZipOutputStream(fos);
			File fileToZip = new File(sourceFile);
			FileInputStream fis = new FileInputStream(fileToZip);
			ZipEntry zipEntry = new ZipEntry(fileToZip.getName());

			zipOut.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zipOut.write(bytes, 0, length);
			}
			zipOut.close();
			fis.close();
			fos.close();
			System.out.println("Zip generado correctamente.");
		} catch (Exception e) {
			System.out.println("Error generando el zip.");
		}
	}
}
