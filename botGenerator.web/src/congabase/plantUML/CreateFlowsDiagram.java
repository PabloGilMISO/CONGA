package congabase.plantUML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import generator.Action;
import generator.BotInteraction;
import generator.UserInteraction;

public class CreateFlowsDiagram {

	private static final String ENTR = "\n";
	public static final String START = "@startuml" + ENTR + ENTR;

	public static final String START_CLASS_DIAGRAM = START + "skinparam class {" + ENTR + "}" + ENTR
			+ "skinparam nodeSep 30 " + ENTR /////// configura espacio horizontal
			+ "skinparam rankSep 30 " + ENTR /////// configura espacio vertical
			+ "hide empty members   " + ENTR; /////// oculta la cajita de atributos si est� vac�a
	public static final String END = ENTR + "@enduml";

	private Map<BotInteraction, String> stateName = new HashMap<>();
	private List<BotInteraction> printed = new ArrayList<BotInteraction>();
	//private List<String> names = new ArrayList<>();
	
	public String createUML(List<UserInteraction> flows) {
		String cad = START_CLASS_DIAGRAM;
		for (UserInteraction flow : flows) {
			cad += printFlow(flow);
		}
		cad += END;
		return cad;
	}

	private String printFlow(UserInteraction user) {
		String flowString = printState(user);
		if (user.getTarget() != null) {
			if (!printed.contains(user.getTarget())) {
				flowString += printStateActions(user.getTarget());
				if (user.getTarget().getOutcoming().isEmpty()) {
					flowString += printEndState(user.getTarget());
				}
				printed.add(user.getTarget());
				for (UserInteraction interaction : user.getTarget().getOutcoming()) {
					flowString += printFlow(interaction);
				}
				
			}
		}
		return flowString;

	}

	private String printEndState(BotInteraction target) {
		return stateName(target) + "-->[*]" + ENTR;
	}

	private String printState(UserInteraction userInteraction) {
		String previousState;
		String nextState;
		if (userInteraction.getSrc() == null) {
			previousState = "[*]";
		} else {
			previousState = stateName(userInteraction.getSrc());
		}

		if (userInteraction.getTarget() == null) {
			nextState = "[*]";
		} else {
			nextState = stateName(userInteraction.getTarget());
		}
		return previousState + "-->" + nextState + " : " + userInteraction.getIntent().getName() + ENTR;

	}

	private String printStateActions(BotInteraction interaction) {
		String actions = "";
		for (Action a : interaction.getActions()) {
			actions += stateName(interaction) + " : " + a.getName() + ENTR;
		}
		return actions;

	}

	private String stateName(BotInteraction interaction) {
		if (stateName.containsKey(interaction)) {
			return stateName.get(interaction);
		}
		
		int i = 0;
		String name = interaction.getActions().get(0).getName();
		boolean flagContinue = false;
		do {
			if (!stateName.containsValue(name)) {
				stateName.put(interaction, name);
				flagContinue = true;
			}else {
				i++;
				name = interaction.getActions().get(0).getName()+i;
			}
		}while (flagContinue != true);
		return name;
		
		
	}
	
//
//	private int getBotInteractionNumber(BotInteraction interaction) {
//		Integer i = stateNumber.get(interaction);
//		if (i == null) {
//			i = getLastNumber() + 1;
//			stateNumber.put(interaction, i);
//		}
//		return i;
//	}
//
//	private int getLastNumber() {
//		return stateNumber.size();
//	}

}
