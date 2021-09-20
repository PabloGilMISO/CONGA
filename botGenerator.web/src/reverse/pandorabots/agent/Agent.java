package reverse.pandorabots.agent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import generator.Action;
import generator.Bot;
import generator.Entity;
import generator.GeneratorFactory;
import generator.HTTPRequest;
import generator.Intent;
import generator.Language;
import generator.LanguageInput;
import generator.SimpleInput;
import generator.Text;
import generator.UserInteraction;

public class Agent {
	private String name;
	private List<Category> categories;
	private List<SetFile> setFiles;
	private List<MapFile> mapFiles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public void addCategories(List<Category> categories) {
		if (this.categories == null)
			this.categories = new ArrayList<Category>(categories);

		else
			this.categories.addAll(categories);
	}

	public List<SetFile> getSetFiles() {
		return setFiles;
	}

	public void setSetFiles(List<SetFile> setFiles) {
		this.setFiles = setFiles;
	}

	public void addSetFiles(List<SetFile> setFiles) {
		if (this.setFiles == null)
			this.setFiles = new ArrayList<SetFile>();

		this.setFiles.addAll(setFiles);
	}

	public void addSetFile(SetFile setFile) {
		if (this.setFiles == null)
			this.setFiles = new ArrayList<SetFile>();

		this.setFiles.add(setFile);
	}

	public List<MapFile> getMapFiles() {
		return mapFiles;
	}

	public void setMapFiles(List<MapFile> mapFiles) {
		this.mapFiles = mapFiles;
	}

	public void addMapFiles(List<MapFile> mapFiles) {
		if (this.mapFiles == null)
			this.mapFiles = new ArrayList<MapFile>();

		this.mapFiles.addAll(mapFiles);
	}

	public void addMapFile(MapFile mapFile) {
		if (this.mapFiles == null)
			this.mapFiles = new ArrayList<MapFile>();

		this.mapFiles.add(mapFile);
	}

	@Override
	public String toString() {
		String ret = "Agent [name=" + name + "\n" + "categories=\n";

		for (Category c : categories)
			ret += c + "\n";

		return ret + "setFiles=" + setFiles + ", \n" + "mapFiles=" + mapFiles + "\n" + "]";
	}

	// Realiza las transformaciones necesarias para transformar el agente en un bot de CONGA,
	// devolviendo el resultado final
	public Bot getBot() {
		Bot bot = GeneratorFactory.eINSTANCE.createBot();

		clearTexts();
		
		bot.setName(name);
		
		// GUARDADO DE ENTITIES
		bot.getEntities().addAll(getEntities());

		// GUARDADO DE INTENTS BÁSICOS
		bot.getIntents().addAll(getIntents());
		
		// GUARDADO DE FLUJOS EN INTENTS
		List<UserInteraction> flows = getFlows(bot.getIntents());
		getOutcomingsInFlows(flows);
		bot.getFlows().addAll(AgentIntentsGetter.copyFlows(flows));
		
		// GUARDADO DE ACTIONS
		bot.getActions().addAll(getActions(bot.getFlows()));
		
		return bot;
	}

	// Limpia las entradas y salidas de tipo texto que hay en los patterns y contestaciones
	// de cada category quitando espacios en blanco y saltos de línea
	public void clearTexts() {
		for (Category category: this.categories) {
			if (category.pattern != null && category.pattern.text != null)
				category.pattern.text = category.pattern.text.trim();
			
			if (category.template != null) {
				if (category.template.text != null)
					category.template.text = category.template.text.trim();
				
				if (category.template.srais != null) {
					for (Srai srai: category.template.srais) {
						if (srai.text != null) {
							srai.text = srai.text.trim();
							
							if (srai.stars != null)
								srai.text += " ";
						}
					}
				}
				
				if (category.template.think != null) {
					if (category.template.think.text != null)
						category.template.think.text = category.template.think.text.trim();
					
					if (category.template.think.srais != null) {
						for (Srai srai: category.template.think.srais) {
							if (srai.text != null) {
								srai.text = srai.text.trim();
								
								if (srai.stars != null)
									srai.text += " ";
							}
						}
					}
				}
				
				if (category.template.condition != null)
					clearOptionsRC(category.template.condition);
			}
		}
	}
	
	// Limpia las salidas 
	public void clearOptionsRC(Condition condition) {
		for (Option option: condition.options) {
			if (option.srais != null) {
				for (Srai srai: option.srais) {
					if (srai.text != null)
						srai.text = srai.text.trim();
				}
			}
			
			if (option.think != null && option.think.srais != null) {
				for (Srai srai: option.think.srais) {
					if (srai.text != null) {
						srai.text = srai.text.trim();
						
						if (srai.stars != null)
							srai.text += " ";
					}
				}
			}
			
			if (option.condition != null)
				clearOptionsRC(option.condition);
		}
	}
	
	// Recoge las entities siempre y cuando se utilicen los maps de Pandorabots como
	// entities
	public List<generator.Entity> getEntities() {
		List<generator.Entity> entities = new ArrayList<generator.Entity>();

		if (mapFiles != null) {
			for (MapFile map : mapFiles) {
				Entity entity = GeneratorFactory.eINSTANCE.createEntity();
				LanguageInput languageInput = GeneratorFactory.eINSTANCE.createLanguageInput();
	
				entity.setName(map.name);
				languageInput.setLanguage(Language.ENGLISH);
	
				for (String key : map.content.keySet()) {
					SimpleInput input = GeneratorFactory.eINSTANCE.createSimpleInput();
					input.setName(key);
					input.getValues().addAll(map.content.get(key));
					languageInput.getInputs().add(input);
				}
	
				entity.getInputs().add(languageInput);
				entities.add(entity);
			}
		}
		
		return entities;
	}
	
	// Recoge los intents de Pandorabots como intents de CONGA
	public List<Intent> getIntents() {
		List<Intent> intents = new ArrayList<Intent>();

		for (Category category : categories) {
			Intent intent = GeneratorFactory.eINSTANCE.createIntent();

			if (category.pattern != null) {
				// Caso en que la categoría sólo contenga sets de tipo <set>example</set>
				if (category.pattern.text == null)
					AgentIntentsGetter.addCategoryWithOnlyPatternSets(category, intent);
	
				// Caso en que contenga texto y pueda o no contener sets
				else {
					// Caso en que contenga fechas
					if (category.pattern.text.contains("* slash * slash *")
							|| category.pattern.text.contains("*slash*slash*"))
						AgentIntentsGetter.addCategoryWithDate(category, intent, mapFiles);
	
					// Caso en que contenga horas
					else if (category.pattern.text.contains("* colon *") || 
							 category.pattern.text.contains("*colon*"))
						AgentIntentsGetter.addCategoryWithHour(category, intent, mapFiles);
					
					// Caso base: intents que contengan texto y puedan o no contener parámetros
					else
						AgentIntentsGetter.addCategoryBasic(category, intent, mapFiles);
				}	
			}
			
			intents.add(intent);
		}
		
		return intents;
	}

	// Procesa los datos de Agent para extraer los flujos de conversación
	public List<UserInteraction> getFlows(List<Intent> intents) {
		List<UserInteraction> ret = new ArrayList<UserInteraction>();
		
		for (int i = 0; i < categories.size(); i++) {
			List<UserInteraction> categoryFlows = AgentIntentsGetter.getMainFlows(categories.get(i), intents.get(i), intents);
			if (categoryFlows != null)
				ret.addAll(categoryFlows);
		}
		
		return ret;
	}
	
	// Recorre los flujos de conversación añadiendo las conexiones a profundidades superiores a 1 (outcomings)
	public void getOutcomingsInFlows(List<UserInteraction> flows) {
		AgentIntentsGetter.getOutcomingsFlows(new ArrayList<UserInteraction>(flows), flows);
	}
	
	// Extrae las actions de los flujos de conversación creados anteriormente
	public List<Action> getActions(List<UserInteraction> flows) {
		List<Action> actions = new ArrayList<Action>();
		List<Action> ret = new ArrayList<Action>();
		
		for (UserInteraction flow: flows)
			actions.addAll(flow.getTarget().getActions());
		
	    java.util.Set<Action> s = new TreeSet<Action>(new Comparator<Action>() {
	        @Override
	        public int compare(Action a1, Action a2) {
	        	// Caso en que sean actions textuales
	        	if (a1 instanceof Text && a2 instanceof Text &&
	        		AgentIntentsGetter.equalTextInputs((Text)a1, (Text)a2))
	        		return 0;
	        	
	        	// Caso en que sean actions de tipo HTTP
	        	else if (a1 instanceof HTTPRequest && a2 instanceof HTTPRequest &&
		        		AgentIntentsGetter.equalHTTPRequest((HTTPRequest)a1, (HTTPRequest)a2))
	        		return 0;
	        	
	        	else
	        		return 1;
	        }
	    });
	    
        s.addAll(actions);
	    ret.addAll(s);
		
		return ret;
	}
}
