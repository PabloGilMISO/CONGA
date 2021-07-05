package reverse.pandorabots;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import zipUtils.Unzipper;

public class TempMain {
	
	// Clase temporal para probar la conversión de Pandorabots a CONGA
	public static void main(String[] args) throws IOException {
		XmlMapper mapper = new XmlMapper();

		String pandorabotsPath = "C:/Users/pablo/CONGA/pandorabots/veterinaryCenter.zip";
		File zip = new File(pandorabotsPath);
		File zipFiles = new Unzipper(zip.getCanonicalPath()).unzip();
		if (zipFiles == null) {
			System.out.println("Error descomprimiendo el zip del bot.");
			return;
		}
		
		boolean flag = false;
		List<File> files = new ArrayList<File>();
		files.add(zipFiles);
		while (flag == false && !files.isEmpty()) {
			File currentFile = files.get(0);
			if (currentFile.isDirectory()) {
				for (File f : currentFile.listFiles()) {
					// Lectura de ficheros de código aiml
					if (f.getName().contains(".aiml")) {
						// TODO: lectura de ficheros de código AIML
						// Se ignoran los ficheros de funciones auxiliares necesarios en Pandorabots únicamente
						if (!f.getName().equals("aimlstandardlibrary.aiml") && !f.getName().equals("utils.aiml"))
							
					} 
					// Lectura de sets
					else if (f.getName().contains(".set")) {
						// TODO: lectura de ficheros set
					}
					// Lectura de maps
					else if (f.getName().contains(".map")) {
						// TODO: lectura de ficheros map
					}
					// Si el zip está separado en subcarpetas
					else if (f.isDirectory()) {
						files.add(f);
					}
				}
			}
			files.remove(currentFile);
		}
//		if (agentCreated) {
//			agent.setEntities(entities);
//			agent.setIntents(intents);
//		}
//		return agent;
	}
}
