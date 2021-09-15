package reverse.pandorabots;

import java.io.File;
import java.io.IOException;

import generator.Bot;
import reverse.pandorabots.agent.Agent;

public class TempMain {

	// Clase temporal para probar la conversión de Pandorabots a CONGA
	public static void main(String[] args) throws IOException {
//		String pandorabotsPath = "C:/CONGA/pandorabots/veterinaryCenterPruebas.zip";
//		String pandorabotsPath = "C:/Users/pablo/CONGA/pandorabots/veterinaryCenterPruebas.zip";
		String pandorabotsPath = "C:/CONGA/pandorabots/bots-externos/aiml-en-us-foundation-alice/alice.zip";
		File zip = new File(pandorabotsPath);
		ReadPandorabotsAgent reader = new ReadPandorabotsAgent();
		Agent fullAgent = reader.getAgent(zip);
		
		Bot bot = fullAgent.getBot();
		
		System.out.println(fullAgent);

		// Pruebas
//		System.out.println(getPruebaAgent(zip));
	}
}