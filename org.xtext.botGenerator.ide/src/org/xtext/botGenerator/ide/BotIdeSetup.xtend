/*
 * generated by Xtext 2.19.0
 */
package org.xtext.botGenerator.ide

import com.google.inject.Guice
import org.eclipse.xtext.util.Modules2
import org.xtext.botGenerator.BotRuntimeModule
import org.xtext.botGenerator.BotStandaloneSetup

/**
 * Initialization support for running Xtext languages as language servers.
 */
class BotIdeSetup extends BotStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new BotRuntimeModule, new BotIdeModule))
	}
	
}
