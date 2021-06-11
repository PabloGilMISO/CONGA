package org.xtext.botGenerator.generator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

// Archivo de pruebas de Pablo para generaci�n de c�digo de Pandorabots
public class Main2 {

	public static void main(String[] args) {
		Path utilsPath = Paths.get("src/org/xtext/botGenerator/generator/pandorabots/utils.txt");
		
		try {
			String utils = new String(Files.readAllBytes(utilsPath));
			System.out.println(utils);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}