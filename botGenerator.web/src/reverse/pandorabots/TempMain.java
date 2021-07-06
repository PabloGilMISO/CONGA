package reverse.pandorabots;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import reverse.pandorabots.agent.Agent;
import zipUtils.Unzipper;

public class TempMain {
	
	// Clase temporal para probar la conversión de Pandorabots a CONGA
	public static void main(String[] args) throws IOException {
		String pandorabotsPath = "C:/Users/pablo/CONGA/pandorabots/veterinaryCenter.zip";
		File zip = new File(pandorabotsPath);
		ReadPandorabotsAgent reader = new ReadPandorabotsAgent();
		Agent fullAgent = reader.getAgent(zip);
		System.out.println(fullAgent);
	}
}
