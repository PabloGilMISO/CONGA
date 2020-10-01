/*
 * generated by Xtext 2.23.0
 */
package botGenerator.web;

import com.google.inject.Injector;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.eclipse.xtext.util.DisposableRegistry;
import org.eclipse.xtext.web.servlet.XtextServlet;
import org.xtext.botGenerator.generator.BotGenerator;

/**
 * Deploy this class into a servlet container to enable DSL-specific services.
 */
@WebServlet(name = "XtextServices", urlPatterns = "/xtext-service/*")
public class BotServlet extends XtextServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DisposableRegistry disposableRegistry;
	
	public void init() throws ServletException {
		super.init();
		Injector injector = new BotWebSetup().createInjectorAndDoEMFRegistration();
		BotGenerator.newPath(BasePath.BASE_PATH);
		this.disposableRegistry = injector.getInstance(DisposableRegistry.class);
		
	}
	
	public void destroy() {
		if (disposableRegistry != null) {
			disposableRegistry.dispose();
			disposableRegistry = null;
		}
		super.destroy();
	}
	
}
