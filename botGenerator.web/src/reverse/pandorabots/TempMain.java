package reverse.pandorabots;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import generator.Bot;
import reverse.pandorabots.agent.Agent;

public class TempMain {

	// Clase temporal para probar la conversión de Pandorabots a CONGA
	public static void main(String[] args) throws IOException {
		String pandorabotsPath = "C:/CONGA/pandorabots/veterinaryCenterPruebas";
//		String pandorabotsPath = "C:/Users/pablo/CONGA/pandorabots/veterinaryCenterPruebas.zip";
//		String pandorabotsPath = "C:/CONGA/pandorabots/bots-externos/aiml-en-us-foundation-alice/atomic";
		
//		// Lectura de un archivo AIML
		removeZip(pandorabotsPath);
		buildZip(pandorabotsPath);
		
		// Lectura de un zip ya existente
		File zip = new File(pandorabotsPath + ".zip");		
		ReadPandorabotsAgent reader = new ReadPandorabotsAgent();
		Agent fullAgent = reader.getAgent(zip);
		
		Bot bot = fullAgent.getBot();
		
		System.out.println(fullAgent);

		// Pruebas
//		System.out.println(getPruebaAgent(zip));
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
	        while((length = fis.read(bytes)) >= 0) {
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