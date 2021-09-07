package reverse.pandorabots;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import generator.Bot;
import generator.TextLanguageInput;
import reverse.pandorabots.agent.Agent;
import reverse.pandorabots.agent.AgentIntentsGetter;
import reverse.pandorabots.agent.Category;
import zipUtils.Unzipper;

public class TempMain {

	// Clase temporal para probar la conversión de Pandorabots a CONGA
	public static void main(String[] args) throws IOException {
		String pandorabotsPath = "C:/CONGA/pandorabots/veterinaryCenterPruebas.zip";
//		String pandorabotsPath = "C:/Users/pablo/CONGA/pandorabots/veterinaryCenterPruebas.zip";
//		String pandorabotsPath = "C:/CONGA/pandorabots/prueba.zip";
		File zip = new File(pandorabotsPath);
		ReadPandorabotsAgent reader = new ReadPandorabotsAgent();
		Agent fullAgent = reader.getAgent(zip);
		
		Bot bot = fullAgent.getBot();
		
		System.out.println(fullAgent);

		// Pruebas
//		System.out.println(getPruebaAgent(zip));
	}
}