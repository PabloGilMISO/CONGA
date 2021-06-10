package org.xtext.botGenerator.generator;

import com.google.common.base.Objects;
import com.google.common.collect.Iterators;
import generator.Action;
import generator.Bot;
import generator.CompositeInput;
import generator.DefaultEntity;
import generator.Entity;
import generator.EntityInput;
import generator.EntityToken;
import generator.HTTPRequest;
import generator.HTTPResponse;
import generator.Image;
import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import generator.Language;
import generator.LanguageInput;
import generator.Literal;
import generator.Parameter;
import generator.ParameterReferenceToken;
import generator.ParameterToken;
import generator.RegexInput;
import generator.SimpleInput;
import generator.Text;
import generator.TextInput;
import generator.TextLanguageInput;
import generator.Token;
import generator.TrainingPhrase;
import generator.UserInteraction;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.eclipse.xtext.generator.IGeneratorContext;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.eclipse.xtext.xbase.lib.Pair;
import zipUtils.Zip;

@SuppressWarnings("all")
public class PandorabotsGenerator {
  private String path;
  
  protected static String uri;
  
  private Zip zip;
  
  public void doGenerate(final Resource resource, final IFileSystemAccess2 fsa, final IGeneratorContext context, final Zip zip) {
    try {
      String resourceName = resource.getURI().lastSegment().substring(0, resource.getURI().lastSegment().indexOf("."));
      Bot bot = IteratorExtensions.<Bot>toList(Iterators.<Bot>filter(resource.getAllContents(), Bot.class)).get(0);
      this.path = (resourceName + "/Pandorabots");
      this.zip = zip;
      String _replace = resourceName.toLowerCase().replace(" ", "_");
      String _plus = ((this.path + "/system/") + _replace);
      String systemPropertiesName = (_plus + ".properties");
      fsa.generateFile(systemPropertiesName, this.systemFileFill());
      InputStream systemPropertiesValue = fsa.readBinaryFile(systemPropertiesName);
      String _replace_1 = resourceName.toLowerCase().replace(" ", "_");
      String _plus_1 = (_replace_1 + ".properties");
      zip.addFileToFolder("system", _plus_1, systemPropertiesValue);
      String udcName = ((this.path + "/files/") + "udc.aiml");
      fsa.generateFile(udcName, this.udcFileFill());
      InputStream udcValue = fsa.readBinaryFile(udcName);
      zip.addFileToFolder("files", "udc.aiml", udcValue);
      String congaPath = "C:/CONGA/pandorabots/";
      Path utilsPath = Paths.get((congaPath + "utils.aiml"));
      byte[] _readAllBytes = Files.readAllBytes(utilsPath);
      String utils = new String(_readAllBytes);
      fsa.generateFile((this.path + "/files/utils.aiml"), utils);
      InputStream utilsValue = fsa.readBinaryFile((this.path + "/files/utils.aiml"));
      zip.addFileToFolder("files", "utils.aiml", utilsValue);
      Path aimlSLPath = Paths.get((congaPath + "aimlstandardlibrary.aiml"));
      byte[] _readAllBytes_1 = Files.readAllBytes(aimlSLPath);
      String aimlSL = new String(_readAllBytes_1);
      fsa.generateFile((this.path + "/files/aimlstandardlibrary.aiml"), aimlSL);
      InputStream aimlSLValue = fsa.readBinaryFile((this.path + "/files/aimlstandardlibrary.aiml"));
      zip.addFileToFolder("files", "aimlstandardlibrary.aiml", aimlSLValue);
      List<Entity> entities = IteratorExtensions.<Entity>toList(Iterators.<Entity>filter(resource.getAllContents(), Entity.class));
      for (final Entity entity : entities) {
        {
          String _name = entity.getName();
          String _plus_2 = ((this.path + "/maps/") + _name);
          String entityPath = (_plus_2 + ".map");
          fsa.generateFile(entityPath, this.entityMapFill(entity));
          InputStream entityValue = fsa.readBinaryFile(entityPath);
          String _name_1 = entity.getName();
          String _plus_3 = (_name_1 + ".map");
          zip.addFileToFolder("maps", _plus_3, entityValue);
          EList<LanguageInput> _inputs = entity.getInputs();
          for (final LanguageInput language_input : _inputs) {
            EList<EntityInput> _inputs_1 = language_input.getInputs();
            for (final EntityInput input : _inputs_1) {
              if ((input instanceof SimpleInput)) {
                String _name_2 = ((SimpleInput)input).getName();
                String _plus_4 = ((this.path + "/sets/") + _name_2);
                String inputSetPath = (_plus_4 + ".set");
                fsa.generateFile(inputSetPath, this.entitySetFill(((SimpleInput)input)));
                InputStream inputSetValue = fsa.readBinaryFile(inputSetPath);
                String _name_3 = ((SimpleInput)input).getName();
                String _plus_5 = (_name_3 + ".set");
                zip.addFileToFolder("sets", _plus_5, inputSetValue);
              }
            }
          }
        }
      }
      EList<UserInteraction> _flows = bot.getFlows();
      for (final UserInteraction transition : _flows) {
        this.createTransitionFiles(transition, "", fsa, bot);
      }
      zip.close();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public CharSequence systemFileFill() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[");
    _builder.newLine();
    _builder.append("[\"name\", \"set_when_loaded\"],");
    _builder.newLine();
    _builder.append("[\"default-get\", \"unknown\"],");
    _builder.newLine();
    _builder.append("[\"default-property\", \"unknown\"],");
    _builder.newLine();
    _builder.append("[\"default-map\", \"unknown\"],");
    _builder.newLine();
    _builder.append("[\"sentence-splitters\", \".!?\"],");
    _builder.newLine();
    _builder.append("[\"learn-filename\", \"pand_learn.aiml\"],");
    _builder.newLine();
    _builder.append("[\"max-learn-file-size\", \"1000000\"]");
    _builder.newLine();
    _builder.append("]");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence udcFileFill() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<aiml>");
    _builder.newLine();
    _builder.append("</aiml>");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence entitySetFill(final SimpleInput input) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[");
    _builder.newLine();
    {
      EList<String> _values = input.getValues();
      for(final String synonym : _values) {
        _builder.append("[\"");
        _builder.append(synonym);
        _builder.append("\"]");
        {
          boolean _isTheLast = PandorabotsGenerator.isTheLast(input.getValues(), synonym);
          boolean _not = (!_isTheLast);
          if (_not) {
            _builder.append(",");
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("]");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence entityMapFill(final Entity entity) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[");
    _builder.newLine();
    {
      EList<LanguageInput> _inputs = entity.getInputs();
      for(final LanguageInput input : _inputs) {
        {
          EList<EntityInput> _inputs_1 = input.getInputs();
          for(final EntityInput entry : _inputs_1) {
            CharSequence _entry = this.entry(entry);
            _builder.append(_entry);
            {
              boolean _isTheLast = PandorabotsGenerator.isTheLast(input.getInputs(), entry);
              boolean _not = (!_isTheLast);
              if (_not) {
                _builder.append(",");
              }
            }
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("]");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence entry(final EntityInput entry) {
    if ((entry instanceof SimpleInput)) {
      return this.entry(((SimpleInput)entry));
    } else {
      if ((entry instanceof CompositeInput)) {
        return this.entry(((CompositeInput)entry));
      } else {
        if ((entry instanceof RegexInput)) {
          return this.entry(((RegexInput)entry));
        }
      }
    }
    return null;
  }
  
  public CharSequence entry(final SimpleInput entry) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<String> _values = entry.getValues();
      for(final String synonym : _values) {
        _builder.append("[\"");
        _builder.append(synonym);
        _builder.append("\", \"");
        String _name = entry.getName();
        _builder.append(_name);
        _builder.append("\"]");
        {
          boolean _isTheLast = PandorabotsGenerator.isTheLast(entry.getValues(), synonym);
          boolean _not = (!_isTheLast);
          if (_not) {
            _builder.append(",");
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence entry(final CompositeInput entry) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  public CharSequence entry(final RegexInput entry) {
    StringConcatenation _builder = new StringConcatenation();
    return _builder;
  }
  
  public void createTransitionFiles(final UserInteraction transition, final String prefix, final IFileSystemAccess2 fsa, final Bot bot) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<aiml>");
    _builder.newLine();
    String intentFileContent = _builder.toString();
    String _intentFileContent = intentFileContent;
    String _createSaveParameter = this.createSaveParameter(transition.getIntent());
    intentFileContent = (_intentFileContent + _createSaveParameter);
    String _intentFileContent_1 = intentFileContent;
    CharSequence _intentFile = this.intentFile(transition, prefix, bot);
    intentFileContent = (_intentFileContent_1 + _intentFile);
    String _intentFileContent_2 = intentFileContent;
    intentFileContent = (_intentFileContent_2 + "</aiml>");
    String _name = transition.getIntent().getName();
    String intentFileName = (prefix + _name).toLowerCase().replace(" ", "");
    fsa.generateFile((((this.path + "/files/") + intentFileName) + ".aiml"), intentFileContent);
    InputStream intentValue = fsa.readBinaryFile((((this.path + "/files/") + intentFileName) + ".aiml"));
    this.zip.addFileToFolder("files", (intentFileName + ".aiml"), intentValue);
  }
  
  public ArrayList<String> getAllIntentResponses(final TextLanguageInput textAction) {
    ArrayList<String> responses = new ArrayList<String>();
    EList<TextInput> _inputs = textAction.getInputs();
    for (final TextInput input : _inputs) {
      {
        String response = "";
        EList<Token> _tokens = input.getTokens();
        for (final Token token : _tokens) {
          if ((token instanceof Literal)) {
            String _response = response;
            String _text = ((Literal)token).getText();
            response = (_response + _text);
            boolean _isTheLast = PandorabotsGenerator.isTheLast(input.getTokens(), token);
            boolean _not = (!_isTheLast);
            if (_not) {
              String _response_1 = response;
              response = (_response_1 + " ");
            }
          } else {
            if ((token instanceof ParameterToken)) {
              String _response_2 = response;
              String _name = ((ParameterToken)token).getParameter().getName();
              String _plus = ("<get name=\"" + _name);
              String _plus_1 = (_plus + "\"/> ");
              response = (_response_2 + _plus_1);
            }
          }
        }
        responses.add(response);
      }
    }
    return responses;
  }
  
  public ArrayList<String> getPhraseEntities(final TrainingPhrase phrase) {
    ArrayList<String> ret = new ArrayList<String>();
    EList<Token> _tokens = phrase.getTokens();
    for (final Token token : _tokens) {
      if ((token instanceof ParameterReferenceToken)) {
        ret.add(((ParameterReferenceToken)token).getParameter().getName());
      }
    }
    return ret;
  }
  
  public ArrayList<Pair<String, String>> getIntentParameterPrompts(final Intent intent) {
    ArrayList<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
    EList<Parameter> _parameters = intent.getParameters();
    for (final Parameter parameter : _parameters) {
      String _name = parameter.getName();
      String _get = parameter.getPrompts().get(0).getPrompts().get(0);
      Pair<String, String> _pair = new Pair<String, String>(_name, _get);
      ret.add(_pair);
    }
    return ret;
  }
  
  public Pair<String, String> getNextParamPetition(final Intent intent, final TrainingPhrase phrase) {
    ArrayList<String> entities = this.getPhraseEntities(phrase);
    ArrayList<Pair<String, String>> parameters = this.getIntentParameterPrompts(intent);
    ArrayList<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
    for (final Pair<String, String> parameter : parameters) {
      for (final String entity : entities) {
        String _key = parameter.getKey();
        boolean _notEquals = (!Objects.equal(_key, entity));
        if (_notEquals) {
          ret.add(parameter);
        } else {
          ret.remove(parameter);
        }
      }
    }
    Pair<String, String> _xifexpression = null;
    final ArrayList<Pair<String, String>> _converted_ret = (ArrayList<Pair<String, String>>)ret;
    int _length = ((Object[])Conversions.unwrapArray(_converted_ret, Object.class)).length;
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      _xifexpression = ret.get(0);
    } else {
      _xifexpression = new Pair<String, String>("", "");
    }
    return _xifexpression;
  }
  
  public CharSequence intentFile(final UserInteraction transition, final String prefix, final Bot bot) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("  ");
    _builder.append("<!-- Intent -->");
    _builder.newLineIfNotEmpty();
    CharSequence _intentGenerator = this.intentGenerator(transition, bot);
    _builder.append(_intentGenerator);
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<!-- Intent inputs -->");
    _builder.newLineIfNotEmpty();
    {
      EList<IntentLanguageInputs> _inputs = transition.getIntent().getInputs();
      for(final IntentLanguageInputs language : _inputs) {
        String lang = "";
        _builder.newLineIfNotEmpty();
        {
          Language _language = language.getLanguage();
          boolean _notEquals = (!Objects.equal(_language, Language.EMPTY));
          if (_notEquals) {
            String _xblockexpression = null;
            {
              lang = this.languageAbbreviation(language.getLanguage());
              _xblockexpression = "";
            }
            _builder.append(_xblockexpression);
            _builder.newLineIfNotEmpty();
          } else {
            String _xblockexpression_1 = null;
            {
              lang = this.languageAbbreviation(bot.getLanguages().get(0));
              _xblockexpression_1 = "";
            }
            _builder.append(_xblockexpression_1);
            _builder.newLineIfNotEmpty();
          }
        }
        {
          EList<IntentInput> _inputs_1 = language.getInputs();
          for(final IntentInput input : _inputs_1) {
            {
              if ((input instanceof TrainingPhrase)) {
                _builder.append("  ");
                _builder.append("<category>");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("<pattern>");
                {
                  EList<Token> _tokens = ((TrainingPhrase)input).getTokens();
                  for(final Token token : _tokens) {
                    {
                      if ((token instanceof Literal)) {
                        String _replace = ((Literal)token).getText().replace("?", " #");
                        _builder.append(_replace);
                      } else {
                        if ((token instanceof ParameterReferenceToken)) {
                          _builder.append("*");
                        }
                      }
                    }
                  }
                }
                _builder.append("</pattern>");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("<think>");
                _builder.newLineIfNotEmpty();
                List<String> entities = null;
                _builder.newLineIfNotEmpty();
                String _xblockexpression_2 = null;
                {
                  entities = this.getPhraseEntities(((TrainingPhrase)input));
                  _xblockexpression_2 = "";
                }
                _builder.append(_xblockexpression_2);
                _builder.newLineIfNotEmpty();
                {
                  for(final String entity : entities) {
                    _builder.append("      ");
                    _builder.append("<srai>");
                    _builder.newLineIfNotEmpty();
                    _builder.append("        ");
                    _builder.append("SAVE");
                    String _upperCase = entity.toUpperCase();
                    _builder.append(_upperCase);
                    _builder.append(" <star index=\"");
                    int _indexOf = entities.indexOf(entity);
                    int _plus = (_indexOf + 1);
                    _builder.append(_plus);
                    _builder.append("\"/>");
                    _builder.newLineIfNotEmpty();
                    _builder.append("      ");
                    _builder.append("</srai>");
                    _builder.newLineIfNotEmpty();
                  }
                }
                _builder.append("    ");
                _builder.append("</think>");
                _builder.newLineIfNotEmpty();
                String nextPrompt = null;
                _builder.newLineIfNotEmpty();
                String _xblockexpression_3 = null;
                {
                  nextPrompt = this.getNextParamPetition(transition.getIntent(), ((TrainingPhrase)input)).getValue();
                  _xblockexpression_3 = "";
                }
                _builder.append(_xblockexpression_3);
                _builder.newLineIfNotEmpty();
                {
                  if ((nextPrompt != "")) {
                    _builder.append("    ");
                    _builder.append("<template>");
                    _builder.append(nextPrompt);
                    _builder.append("</template>");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("    ");
                    _builder.append("<template></template>");
                    _builder.newLineIfNotEmpty();
                  }
                }
                _builder.append("  ");
                _builder.append("</category>");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    return _builder;
  }
  
  public HashMap<String, DefaultEntity> getIntentParameters(final Intent intent) {
    HashMap<String, DefaultEntity> ret = new HashMap<String, DefaultEntity>();
    EList<IntentLanguageInputs> _inputs = intent.getInputs();
    for (final IntentLanguageInputs language : _inputs) {
      EList<IntentInput> _inputs_1 = language.getInputs();
      for (final IntentInput input : _inputs_1) {
        if ((input instanceof TrainingPhrase)) {
          EList<Token> _tokens = ((TrainingPhrase) input).getTokens();
          for (final Token token : _tokens) {
            if ((token instanceof ParameterReferenceToken)) {
              boolean _contains = ret.keySet().contains(((ParameterReferenceToken)token).getParameter().getName());
              boolean _not = (!_contains);
              if (_not) {
                ret.put(((ParameterReferenceToken)token).getParameter().getName(), ((ParameterReferenceToken)token).getParameter().getDefaultEntity());
              }
            }
          }
        }
      }
    }
    return ret;
  }
  
  public String createSaveParameter(final Intent intent) {
    HashMap<String, DefaultEntity> parameters = this.getIntentParameters(intent);
    boolean _isEmpty = parameters.isEmpty();
    if (_isEmpty) {
      return "";
    } else {
      String ret = "  <!-- Entity saving -->\n";
      Set<String> _keySet = parameters.keySet();
      for (final String key : _keySet) {
        {
          DefaultEntity value = parameters.get(key);
          if (value != null) {
            switch (value) {
              case TEXT:
                String _ret = ret;
                StringConcatenation _builder = new StringConcatenation();
                _builder.append("  ");
                _builder.append("<category>");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("<pattern>SAVE");
                String _upperCase = key.toUpperCase();
                _builder.append(_upperCase);
                _builder.append(" *</pattern>");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("<template>");
                _builder.newLineIfNotEmpty();
                _builder.append("      ");
                _builder.append("<think><set name=\"");
                _builder.append(key);
                _builder.append("\"><star/></set></think>");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("</template>");
                _builder.newLineIfNotEmpty();
                _builder.append("  ");
                _builder.append("</category>");
                _builder.newLineIfNotEmpty();
                ret = (_ret + _builder);
                break;
              case TIME:
                String _ret_1 = ret;
                StringConcatenation _builder_1 = new StringConcatenation();
                _builder_1.append("  ");
                _builder_1.append("<category>");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("    ");
                _builder_1.append("<pattern>SAVE");
                String _upperCase_1 = key.toUpperCase();
                _builder_1.append(_upperCase_1);
                _builder_1.append(" * colon *</pattern>");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("    ");
                _builder_1.append("<template>");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("      ");
                _builder_1.append("<think>");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("        ");
                _builder_1.append("<set name=\"");
                _builder_1.append(key);
                _builder_1.append("_is_valid\"><srai>ISVALIDHOUR <star index=\"1\"/>:<star index=\"2\"/></srai></set>");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("      ");
                _builder_1.append("</think>");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("      ");
                _builder_1.append("<condition name=\"");
                _builder_1.append(key);
                _builder_1.append("_is_valid\">");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("        ");
                _builder_1.append("<li value=\"TRUE\">");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("          ");
                _builder_1.append("<think>");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("            ");
                _builder_1.append("<set name=\"");
                _builder_1.append(key);
                _builder_1.append("\"><star index=\"1\"/>:<star index=\"2\"/></set>");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("          ");
                _builder_1.append("</think>");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("        ");
                _builder_1.append("</li>");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("      ");
                _builder_1.append("</condition>");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("    ");
                _builder_1.append("</template>");
                _builder_1.newLineIfNotEmpty();
                _builder_1.append("  ");
                _builder_1.append("</category>");
                _builder_1.newLineIfNotEmpty();
                ret = (_ret_1 + _builder_1);
                break;
              case DATE:
                String _ret_2 = ret;
                StringConcatenation _builder_2 = new StringConcatenation();
                _builder_2.append("  ");
                _builder_2.append("<category>");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("    ");
                _builder_2.append("<pattern>SAVE");
                String _upperCase_2 = key.toUpperCase();
                _builder_2.append(_upperCase_2);
                _builder_2.append(" * slash * slash *</pattern>");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("    ");
                _builder_2.append("<template>");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("      ");
                _builder_2.append("<think>");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("        ");
                _builder_2.append("<set name=\"");
                _builder_2.append(key);
                _builder_2.append("_is_valid\">");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("          ");
                _builder_2.append("<srai>VALIDDATE <star index=\"1\"/>/<star index=\"2\"/>/<star index=\"3\"/></srai>");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("        ");
                _builder_2.append("</set>");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("      ");
                _builder_2.append("</think>");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("      ");
                _builder_2.append("<condition name=\"");
                _builder_2.append(key);
                _builder_2.append("_is_valid\">");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("        ");
                _builder_2.append("<li value=\"TRUE\">");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("          ");
                _builder_2.append("<think><set name=\"");
                _builder_2.append(key);
                _builder_2.append("\"><star index=\"1\"/>/<star index=\"2\"/>/<star index=\"3\"/></set></think>");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("        ");
                _builder_2.append("</li>");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("      ");
                _builder_2.append("</condition>");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("    ");
                _builder_2.append("</template>");
                _builder_2.newLineIfNotEmpty();
                _builder_2.append("  ");
                _builder_2.append("</category>");
                _builder_2.newLineIfNotEmpty();
                ret = (_ret_2 + _builder_2);
                break;
              case NUMBER:
                String _ret_3 = ret;
                StringConcatenation _builder_3 = new StringConcatenation();
                _builder_3.append("  ");
                _builder_3.append("<category>");
                _builder_3.newLineIfNotEmpty();
                _builder_3.append("    ");
                _builder_3.append("<pattern>SAVE");
                String _upperCase_3 = key.toUpperCase();
                _builder_3.append(_upperCase_3);
                _builder_3.append(" <set>number</set></pattern>");
                _builder_3.newLineIfNotEmpty();
                _builder_3.append("    ");
                _builder_3.append("<template>");
                _builder_3.newLineIfNotEmpty();
                _builder_3.append("      ");
                _builder_3.append("<think><set name=\"");
                _builder_3.append(key);
                _builder_3.append("\"><star/></set></think>");
                _builder_3.newLineIfNotEmpty();
                _builder_3.append("    ");
                _builder_3.append("</template>");
                _builder_3.newLineIfNotEmpty();
                _builder_3.append("  ");
                _builder_3.append("</category>");
                _builder_3.newLineIfNotEmpty();
                ret = (_ret_3 + _builder_3);
                break;
              default:
                String _ret_4 = ret;
                StringConcatenation _builder_4 = new StringConcatenation();
                _builder_4.append("  ");
                _builder_4.append("<category>");
                _builder_4.newLineIfNotEmpty();
                _builder_4.append("    ");
                _builder_4.append("<pattern>SAVE");
                String _upperCase_4 = key.toUpperCase();
                _builder_4.append(_upperCase_4);
                _builder_4.append(" *</pattern>");
                _builder_4.newLineIfNotEmpty();
                _builder_4.append("    ");
                _builder_4.append("<template>");
                _builder_4.newLineIfNotEmpty();
                _builder_4.append("      ");
                _builder_4.append("<think><set name=\"");
                _builder_4.append(key);
                _builder_4.append("\"><star/></set></think>");
                _builder_4.newLineIfNotEmpty();
                _builder_4.append("    ");
                _builder_4.append("</template>");
                _builder_4.newLineIfNotEmpty();
                _builder_4.append("  ");
                _builder_4.append("</category>");
                _builder_4.newLineIfNotEmpty();
                ret = (_ret_4 + _builder_4);
                break;
            }
          } else {
            String _ret_4 = ret;
            StringConcatenation _builder_4 = new StringConcatenation();
            _builder_4.append("  ");
            _builder_4.append("<category>");
            _builder_4.newLineIfNotEmpty();
            _builder_4.append("    ");
            _builder_4.append("<pattern>SAVE");
            String _upperCase_4 = key.toUpperCase();
            _builder_4.append(_upperCase_4);
            _builder_4.append(" *</pattern>");
            _builder_4.newLineIfNotEmpty();
            _builder_4.append("    ");
            _builder_4.append("<template>");
            _builder_4.newLineIfNotEmpty();
            _builder_4.append("      ");
            _builder_4.append("<think><set name=\"");
            _builder_4.append(key);
            _builder_4.append("\"><star/></set></think>");
            _builder_4.newLineIfNotEmpty();
            _builder_4.append("    ");
            _builder_4.append("</template>");
            _builder_4.newLineIfNotEmpty();
            _builder_4.append("  ");
            _builder_4.append("</category>");
            _builder_4.newLineIfNotEmpty();
            ret = (_ret_4 + _builder_4);
          }
        }
      }
      return ret;
    }
  }
  
  public ArrayList<String> getActionLanguages(final Action action) {
    ArrayList<String> languageList = new ArrayList<String>();
    if ((action instanceof Text)) {
      EList<TextLanguageInput> _inputs = ((Text)action).getInputs();
      for (final TextLanguageInput language : _inputs) {
        languageList.add(this.languageAbbreviation(language.getLanguage()));
      }
    }
    return languageList;
  }
  
  public ArrayList<TextLanguageInput> getLanguageActions(final List<Action> actions, final String lang) {
    ArrayList<TextLanguageInput> ret = new ArrayList<TextLanguageInput>();
    for (final Action action : actions) {
      if ((action instanceof Text)) {
        EList<TextLanguageInput> _inputs = ((Text)action).getInputs();
        for (final TextLanguageInput language : _inputs) {
          String _languageAbbreviation = this.languageAbbreviation(language.getLanguage());
          boolean _equals = Objects.equal(_languageAbbreviation, lang);
          if (_equals) {
            ret.add(language);
          }
        }
      }
    }
    return ret;
  }
  
  public CharSequence intentGenerator(final UserInteraction transition, final Bot bot) {
    StringConcatenation _builder = new StringConcatenation();
    String intentName = "";
    _builder.newLineIfNotEmpty();
    _builder.append("  ");
    _builder.append("<!-- Main intents -->");
    _builder.newLineIfNotEmpty();
    String _xblockexpression = null;
    {
      intentName = transition.getIntent().getName().toUpperCase().replace(" ", "");
      _xblockexpression = "";
    }
    _builder.append(_xblockexpression);
    _builder.newLineIfNotEmpty();
    {
      EList<IntentLanguageInputs> _inputs = transition.getIntent().getInputs();
      for(final IntentLanguageInputs language : _inputs) {
        String lang = "";
        _builder.newLineIfNotEmpty();
        {
          Language _language = language.getLanguage();
          boolean _notEquals = (!Objects.equal(_language, Language.EMPTY));
          if (_notEquals) {
            String _xblockexpression_1 = null;
            {
              lang = this.languageAbbreviation(language.getLanguage());
              _xblockexpression_1 = "";
            }
            _builder.append(_xblockexpression_1);
            _builder.newLineIfNotEmpty();
          } else {
            String _xblockexpression_2 = null;
            {
              lang = this.languageAbbreviation(bot.getLanguages().get(0));
              _xblockexpression_2 = "";
            }
            _builder.append(_xblockexpression_2);
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("  ");
        _builder.append("<category>");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("<pattern>");
        _builder.append(intentName);
        _builder.append("</pattern>");
        _builder.newLineIfNotEmpty();
        _builder.append("    ");
        _builder.append("<template>");
        _builder.newLineIfNotEmpty();
        {
          EList<Action> _actions = transition.getTarget().getActions();
          for(final Action action : _actions) {
            {
              if ((action instanceof Text)) {
                _builder.append("      ");
                _builder.append("<srai>");
                String _upperCase = lang.toUpperCase();
                String _plus = (intentName + _upperCase);
                _builder.append(_plus);
                _builder.append("</srai>");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("      ");
                _builder.append("<srai>");
                String _replace = action.getName().toUpperCase().replace(" ", "");
                String _plus_1 = (intentName + _replace);
                _builder.append(_plus_1);
                _builder.append("</srai>");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
        _builder.append("    ");
        _builder.append("</template>");
        _builder.newLineIfNotEmpty();
        _builder.append("  ");
        _builder.append("</category>");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("  ");
    _builder.append("<!-- Action intents -->");
    _builder.newLineIfNotEmpty();
    {
      EList<Action> _actions_1 = transition.getTarget().getActions();
      for(final Action action_1 : _actions_1) {
        {
          if ((action_1 instanceof Text)) {
            {
              EList<TextLanguageInput> _inputs_1 = ((Text)action_1).getInputs();
              for(final TextLanguageInput language_1 : _inputs_1) {
                String lang_1 = "";
                _builder.newLineIfNotEmpty();
                {
                  Language _language_1 = language_1.getLanguage();
                  boolean _notEquals_1 = (!Objects.equal(_language_1, Language.EMPTY));
                  if (_notEquals_1) {
                    String _xblockexpression_3 = null;
                    {
                      lang_1 = this.languageAbbreviation(language_1.getLanguage()).toUpperCase();
                      _xblockexpression_3 = "";
                    }
                    _builder.append(_xblockexpression_3);
                    _builder.newLineIfNotEmpty();
                  } else {
                    String _xblockexpression_4 = null;
                    {
                      lang_1 = this.languageAbbreviation(bot.getLanguages().get(0)).toUpperCase();
                      _xblockexpression_4 = "";
                    }
                    _builder.append(_xblockexpression_4);
                    _builder.newLineIfNotEmpty();
                  }
                }
                {
                  int _length = ((Object[])Conversions.unwrapArray(this.getAllIntentResponses(language_1), Object.class)).length;
                  boolean _greaterThan = (_length > 1);
                  if (_greaterThan) {
                    _builder.append("  ");
                    _builder.append("<category>");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("<pattern>");
                    String _upperCase_1 = (intentName + lang_1).toUpperCase();
                    _builder.append(_upperCase_1);
                    _builder.append("</pattern>");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("<template>");
                    _builder.newLineIfNotEmpty();
                    _builder.append("      ");
                    _builder.append("<random>");
                    _builder.newLineIfNotEmpty();
                    {
                      ArrayList<String> _allIntentResponses = this.getAllIntentResponses(language_1);
                      for(final String response : _allIntentResponses) {
                        _builder.append("        ");
                        _builder.append("<li>");
                        _builder.append(response);
                        _builder.append("</li>");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                    _builder.append("      ");
                    _builder.append("</random>");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("</template>");
                    _builder.newLineIfNotEmpty();
                    _builder.append("  ");
                    _builder.append("</category>");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("  ");
                    _builder.append("<category>");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("<pattern>");
                    String _upperCase_2 = (intentName + lang_1).toUpperCase();
                    _builder.append(_upperCase_2);
                    _builder.append("</pattern>");
                    _builder.newLineIfNotEmpty();
                    _builder.append("    ");
                    _builder.append("<template>");
                    String _get = this.getAllIntentResponses(language_1).get(0);
                    _builder.append(_get);
                    _builder.append("</template>");
                    _builder.newLineIfNotEmpty();
                    _builder.append("  ");
                    _builder.append("</category>");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
          } else {
            if ((action_1 instanceof Image)) {
              _builder.append("  ");
              _builder.append("<category>");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("<pattern>");
              String _replace_1 = ((Image)action_1).getName().toUpperCase().replace(" ", "");
              String _plus_2 = (intentName + _replace_1);
              _builder.append(_plus_2);
              _builder.append("</pattern>");
              _builder.newLineIfNotEmpty();
              _builder.append("    ");
              _builder.append("<template><image>");
              String _uRL = ((Image)action_1).getURL();
              _builder.append(_uRL);
              _builder.append("</image></template>");
              _builder.newLineIfNotEmpty();
              _builder.append("  ");
              _builder.append("</category>");
              _builder.newLineIfNotEmpty();
            } else {
              if ((action_1 instanceof HTTPRequest)) {
                _builder.append("  ");
                _builder.append("<category>");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("<pattern>");
                String _replace_2 = ((HTTPRequest)action_1).getName().toUpperCase().replace(" ", "");
                String _plus_3 = (intentName + _replace_2);
                _builder.append(_plus_3);
                _builder.append("</pattern>");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("<template>");
                _builder.newLineIfNotEmpty();
                _builder.append("      ");
                _builder.append("<callapi response_code_var=\"response");
                String _name = ((HTTPRequest)action_1).getName();
                String _plus_4 = ("_" + _name);
                _builder.append(_plus_4);
                _builder.append("\">");
                _builder.newLineIfNotEmpty();
                _builder.append("        ");
                _builder.append("<url>");
                String _uRL_1 = ((HTTPRequest) action_1).getURL();
                _builder.append(_uRL_1);
                _builder.append("</url>");
                _builder.newLineIfNotEmpty();
                _builder.append("      ");
                _builder.append("</callapi>");
                _builder.newLineIfNotEmpty();
                _builder.append("    ");
                _builder.append("</template>");
                _builder.newLineIfNotEmpty();
                _builder.append("  ");
                _builder.append("</category>");
                _builder.newLineIfNotEmpty();
              } else {
                if ((action_1 instanceof HTTPResponse)) {
                  _builder.append("  ");
                  _builder.append("<category>");
                  _builder.newLineIfNotEmpty();
                  _builder.append("    ");
                  _builder.append("<pattern>");
                  String _replace_3 = ((HTTPResponse)action_1).getName().toUpperCase().replace(" ", "");
                  String _plus_5 = (intentName + _replace_3);
                  _builder.append(_plus_5);
                  _builder.append("</pattern>");
                  _builder.newLineIfNotEmpty();
                  _builder.append("    ");
                  _builder.append("<template>");
                  _builder.newLineIfNotEmpty();
                  _builder.append("      ");
                  _builder.append("<get name=\"response_");
                  String _name_1 = ((HTTPResponse) action_1).getHTTPRequest().getName();
                  _builder.append(_name_1);
                  _builder.append("\"/>");
                  _builder.newLineIfNotEmpty();
                  _builder.append("    ");
                  _builder.append("</template>");
                  _builder.newLineIfNotEmpty();
                  _builder.append("  ");
                  _builder.append("</category>");
                  _builder.newLineIfNotEmpty();
                }
              }
            }
          }
        }
      }
    }
    return _builder;
  }
  
  public String returnText(final String value) {
    boolean _isEmpty = value.isEmpty();
    if (_isEmpty) {
      return "";
    }
    return value;
  }
  
  public String languageAbbreviation(final Language lan) {
    if (lan != null) {
      switch (lan) {
        case ENGLISH:
          return "en";
        case SPANISH:
          return "es";
        case DANISH:
          return "da";
        case GERMAN:
          return "de";
        case FRENCH:
          return "fr";
        case HINDI:
          return "hi";
        case INDONESIAN:
          return "id";
        case ITALIAN:
          return "it";
        case JAPANESE:
          return "ja";
        case KOREAN:
          return "ko";
        case DUTCH:
          return "nl";
        case NORWEGIAN:
          return "no";
        case POLISH:
          return "pl";
        case PORTUGUESE:
          return "pt";
        case RUSIAN:
          return "ru";
        case SWEDISH:
          return "sv";
        case THAI:
          return "th";
        case TURKISH:
          return "tr";
        case UKRANIAN:
          return "uk";
        case CHINESE:
          return "zh";
        default:
          return "en";
      }
    } else {
      return "en";
    }
  }
  
  public CharSequence entityFile(final Entity entity) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("\"id\": \"");
    String _string = UUID.randomUUID().toString();
    _builder.append(_string, "\t");
    _builder.append("\",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("\"name\": \"");
    String _name = entity.getName();
    _builder.append(_name, "\t");
    _builder.append("\",");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("\"isOverridable\": true,\t  ");
    _builder.newLine();
    {
      int _entityType = BotGenerator.entityType(entity);
      boolean _tripleEquals = (_entityType == BotGenerator.REGEX);
      if (_tripleEquals) {
        _builder.append("\t");
        _builder.append("\"isEnum\": false,");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\"isRegexp\":true,");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\"automatedExpansion\": true,");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\"allowFuzzyExtraction\": false");
        _builder.newLine();
      } else {
        int _entityType_1 = BotGenerator.entityType(entity);
        boolean _tripleEquals_1 = (_entityType_1 == BotGenerator.SIMPLE);
        if (_tripleEquals_1) {
          _builder.append("\t");
          _builder.append("\"isEnum\": false,");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\"isRegexp\": false,");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\"automatedExpansion\": true,");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\"allowFuzzyExtraction\": true");
          _builder.newLine();
        } else {
          _builder.append("\t");
          _builder.append("\"isEnum\": true,");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\"isRegexp\": false,");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\"automatedExpansion\": false,");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\"allowFuzzyExtraction\": false");
          _builder.newLine();
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public Object entityIsSimple(final Entity entity) {
    return null;
  }
  
  public CharSequence entriesFile(final LanguageInput entity) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("[");
    _builder.newLine();
    {
      EList<EntityInput> _inputs = entity.getInputs();
      for(final EntityInput entry : _inputs) {
        _builder.append("\t");
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("   ");
        CharSequence _entry = this.entry(entry);
        _builder.append(_entry, "\t   ");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("} ");
        {
          boolean _isTheLast = PandorabotsGenerator.isTheLast(entity.getInputs(), entry);
          boolean _not = (!_isTheLast);
          if (_not) {
            _builder.append(",");
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("]");
    _builder.newLine();
    return _builder;
  }
  
  public static boolean isTheLast(final List<?> list, final Object object) {
    int _indexOf = list.indexOf(object);
    int _size = list.size();
    int _minus = (_size - 1);
    boolean _equals = (_indexOf == _minus);
    if (_equals) {
      return true;
    }
    return false;
  }
  
  public String getCompositeEntry(final CompositeInput entry) {
    String ret = "";
    EList<Token> _expresion = entry.getExpresion();
    for (final Token token : _expresion) {
      if ((token instanceof Literal)) {
        String _ret = ret;
        String _text = ((Literal)token).getText();
        String _plus = (_text + " ");
        ret = (_ret + _plus);
      } else {
        if ((token instanceof EntityToken)) {
          String _ret_1 = ret;
          String _name = ((EntityToken)token).getEntity().getName();
          String _plus_1 = ("@" + _name);
          String _plus_2 = (_plus_1 + ":");
          String _name_1 = ((EntityToken)token).getEntity().getName();
          String _plus_3 = (_plus_2 + _name_1);
          String _plus_4 = (_plus_3 + " ");
          ret = (_ret_1 + _plus_4);
        }
      }
    }
    return ret;
  }
}
