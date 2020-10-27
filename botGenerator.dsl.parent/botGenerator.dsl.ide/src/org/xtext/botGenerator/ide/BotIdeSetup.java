/*
 * generated by Xtext 2.23.0
 */
package org.xtext.botGenerator.ide;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.xtext.util.Modules2;
import org.xtext.botGenerator.BotRuntimeModule;
import org.xtext.botGenerator.BotStandaloneSetup;

/**
 * Initialization support for running Xtext languages as language servers.
 */
public class BotIdeSetup extends BotStandaloneSetup {

	@Override
	public Injector createInjector() {
		return Guice.createInjector(Modules2.mixin(new BotRuntimeModule(), new BotIdeModule()));
	}
	
}