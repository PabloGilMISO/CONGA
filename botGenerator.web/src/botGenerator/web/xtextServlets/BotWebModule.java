/*
 * generated by Xtext 2.23.0
 */
package botGenerator.web.xtextServlets;

import org.eclipse.xtext.web.server.persistence.IResourceBaseProvider;
import org.eclipse.xtext.web.server.persistence.IServerResourceHandler;

import com.google.inject.Binder;

import congabase.main.CongaData;

/**
 * Use this class to register additional components to be used within the web application.
 */
public class BotWebModule extends AbstractBotWebModule {
	
	
	public void configure(Binder binder) {
		binder.bind(IServerResourceHandler.class).to(MyFileResourceHandler.class);
		binder.bind(IResourceBaseProvider.class).toInstance(
				new MyResourceBaseProvider(CongaData.getPath()));
		super.configure(binder);
	}
	
	
}