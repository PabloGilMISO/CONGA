/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/9.0.43
 * Generated at: 2021-05-24 09:48:45 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import congabase.main.CongaData;
import congabase.Project;

public final class editor_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("congabase.main.CongaData");
    _jspx_imports_classes.add("congabase.Project");
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    if (!javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      final java.lang.String _jspx_method = request.getMethod();
      if ("OPTIONS".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        return;
      }
      if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method)) {
        response.setHeader("Allow","GET, HEAD, POST, OPTIONS");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET, POST or HEAD. Jasper also permits OPTIONS");
        return;
      }
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n");
      out.write("<meta http-equiv=\"Content-Language\" content=\"en-us\">\r\n");
      out.write("<title>CONGA Web Editor</title>\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\"\r\n");
      out.write("\thref=\"bootstrap\\css\\bootstrap.min.css\" />\r\n");
      out.write("<link href='https://fonts.googleapis.com/css?family=Atma'\r\n");
      out.write("\trel='stylesheet'>\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/index.css\" />\r\n");
      out.write("<link rel=\"stylesheet\"\r\n");
      out.write("\thref=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css\">\r\n");
      out.write("\r\n");
      out.write("<script\r\n");
      out.write("\tsrc=\"https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js\"></script>\r\n");
      out.write("<script\r\n");
      out.write("\tsrc=\"https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js\"></script>\r\n");
      out.write("<script src=\"bootstrap\\js\\bootstrap.min.js\"></script>\r\n");
      out.write("\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"xtext/2.23.0/xtext-ace.css\" />\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\" />\r\n");
      out.write("<script src=\"webjars/requirejs/2.3.6/require.min.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("\tvar baseUrl = window.location.pathname;\r\n");
      out.write("\tconsole.log(baseUrl)\r\n");
      out.write("\tvar fileIndex = baseUrl.indexOf(\"editor.html\");\r\n");
      out.write("\tvar fileIndex2 = baseUrl.indexOf(\"createProject\");\r\n");
      out.write("\tvar fileIndex3 = baseUrl.indexOf(\"openProject\");\r\n");
      out.write("\tif (fileIndex > 0)\r\n");
      out.write("\t\tbaseUrl = baseUrl.slice(0, fileIndex);\r\n");
      out.write("\tif (fileIndex2 > 0)\r\n");
      out.write("\t\tbaseUrl = baseUrl.slice(0, fileIndex2);\r\n");
      out.write("\tif (fileIndex3 > 0)\r\n");
      out.write("\t\tbaseUrl = baseUrl.slice(0, fileIndex3);\r\n");
      out.write("\trequire\r\n");
      out.write("\t\t\t.config({\r\n");
      out.write("\t\t\t\tbaseUrl : baseUrl,\r\n");
      out.write("\t\t\t\tpaths : {\r\n");
      out.write("\t\t\t\t\t\"jquery\" : \"webjars/jquery/3.4.1/jquery.min\",\r\n");
      out.write("\t\t\t\t\t\"ace/ext/language_tools\" : \"webjars/ace/1.3.3/src/ext-language_tools\",\r\n");
      out.write("\t\t\t\t\t\"xtext/xtext-ace\" : \"xtext/2.23.0/xtext-ace\"\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\trequire(\r\n");
      out.write("\t\t\t[ \"webjars/ace/1.3.3/src/ace\" ],\r\n");
      out.write("\t\t\tfunction() {\r\n");
      out.write("\t\t\t\trequire(\r\n");
      out.write("\t\t\t\t\t\t[ \"xtext/xtext-ace\" ],\r\n");
      out.write("\t\t\t\t\t\tfunction(xtext) {\r\n");
      out.write("\t\t\t\t\t\t\tvar editor = xtext\r\n");
      out.write("\t\t\t\t\t\t\t\t\t.createEditor({\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\tbaseUrl : baseUrl,\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\tsyntaxDefinition : \"xtext-resources/generated/mode-bot\"\r\n");
      out.write("\t\t\t\t\t\t\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#save-button\").click(\r\n");
      out.write("\t\t\t\t\t\t\t\t\tfunction() {\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\tdocuments = editor.xtextServices\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t.saveResource();\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\tlocation.reload()\r\n");
      out.write("\t\t\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#format-button\").click(function() {\r\n");
      out.write("\t\t\t\t\t\t\t\tdocuments = editor.xtextServices.format();\r\n");
      out.write("\t\t\t\t\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#change-resource\")\r\n");
      out.write("\t\t\t\t\t\t\t\t\t.change(\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\tfunction() {\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\tvar resourceId = $(\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\"#change-resource option:selected\")\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t.attr(\"value\");\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\teditor.xtextServices.serviceBuilder\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t.changeResource(resourceId);\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#get-button\")\r\n");
      out.write("\t\t\t\t\t\t\t\t\t.click(\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\tfunction() {\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\tvar e = document\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t.getElementById(\"tool\");\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\tvar tool = e.options[e.selectedIndex].value;\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\tjQuery\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t.get(\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t'http://'\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ location.host\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ '/botGenerator.web/xtext-service/generate?resource='\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ editor.xtextServices.options.resourceId\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ '&allArtifacts=true',\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tfunction(result) {\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\twindow\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t.open('http://'\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ location.host\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ '/botGenerator.web/getfiles?resource='\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ editor.xtextServices.options.resourceId\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ '&tool='\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ tool)\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t\t$(\"#recomender\")\r\n");
      out.write("\t\t\t\t\t\t\t\t\t.click(\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\tfunction() {\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\tjQuery\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t.get(\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t'http://'\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ location.host\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ '/botGenerator.web/goRecommender?resource='\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+ editor.xtextServices.options.resourceId,\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tfunction(result) {\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t\t\t\t\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t});\r\n");
      out.write("</script>\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body>\r\n");
      out.write("\t");

		Project project = (Project) getServletContext().getAttribute("project");
	CongaData conga = CongaData.getCongaData(getServletContext());
	
      out.write("\r\n");
      out.write("\t<div class=\"container\">\r\n");
      out.write("\t\t");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "header2.jsp", out, false);
      out.write("\r\n");
      out.write("\t\t<nav class=\"navbar navbar-light\" style=\"background-color: #FFFFFF;\">\r\n");
      out.write("\t\t\t<div class=\"form-inline\" style=\"width: 100%\">\r\n");
      out.write("\t\t\t\t<button id=\"save-button\"\r\n");
      out.write("\t\t\t\t\tclass=\"btn btn-outline-secondary button-nav-size button-nav-margin button-nav-height\">\r\n");
      out.write("\t\t\t\t\t<i class=\"fa fa-save\" style=\"font-size: 20px\"></i>\r\n");
      out.write("\t\t\t\t</button>\r\n");
      out.write("\t\t\t\t<a\r\n");
      out.write("\t\t\t\t\tclass=\"btn btn-outline-secondary button-nav-size button-nav-margin button-nav-height dropdown-toggle\"\r\n");
      out.write("\t\t\t\t\tdata-toggle=\"dropdown\" href=\"#\" role=\"button\" aria-haspopup=\"true\"\r\n");
      out.write("\t\t\t\t\taria-expanded=\"false\">New</a>\r\n");
      out.write("\t\t\t\t<div class=\"dropdown-menu\">\r\n");
      out.write("\t\t\t\t\t<a class=\"dropdown-item\" href=\"newproject.jsp\">Empty project</a> <a\r\n");
      out.write("\t\t\t\t\t\tclass=\"dropdown-item\" href=\"loadproject.jsp\">Load from\r\n");
      out.write("\t\t\t\t\t\tDialogflow files</a>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<button id=\"format-button\"\r\n");
      out.write("\t\t\t\t\tclass=\"btn btn-outline-secondary  button-nav-size button-nav-margin button-nav-height\">\r\n");
      out.write("\t\t\t\t\tFormat</button>\r\n");
      out.write("\t\t\t\t<div class=\"form-group button-nav-margin\">\r\n");
      out.write("\t\t\t\t\t<select name=\"tool\" id=\"tool\"\r\n");
      out.write("\t\t\t\t\t\tclass=\"form-control button-nav-size button-nav-height\"\r\n");
      out.write("\t\t\t\t\t\tstyle=\"width: 125px;\">\r\n");
      out.write("\t\t\t\t\t\t");

							for (String tool : CongaData.supportedTools()) {
						
      out.write("\r\n");
      out.write("\t\t\t\t\t\t<option value=\"");
      out.print(tool);
      out.write('"');
      out.write('>');
      out.print(tool);
      out.write("</option>\r\n");
      out.write("\t\t\t\t\t\t");

							}
						
      out.write("\r\n");
      out.write("\t\t\t\t\t</select>\r\n");
      out.write("\t\t\t\t\t<button id=\"get-button\"\r\n");
      out.write("\t\t\t\t\t\tclass=\"btn btn-outline-secondary button-nav-size button-nav-height\">\r\n");
      out.write("\t\t\t\t\t\t<svg width=\"1em\" height=\"1em\" viewBox=\"0 0 16 16\"\r\n");
      out.write("\t\t\t\t\t\t\tclass=\"bi bi-download\" fill=\"currentColor\"\r\n");
      out.write("\t\t\t\t\t\t\txmlns=\"http://www.w3.org/2000/svg\">\r\n");
      out.write("\t\t  \t\t\t\t<path fill-rule=\"evenodd\"\r\n");
      out.write("\t\t\t\t\t\t\t\td=\"M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5z\" />\r\n");
      out.write("\t\t  \t\t\t\t<path fill-rule=\"evenodd\"\r\n");
      out.write("\t\t\t\t\t\t\t\td=\"M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708l3 3z\" />\r\n");
      out.write("\t\t\t\t\t</svg>\r\n");
      out.write("\t\t\t\t\t</button>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<a href=\"questionnaire1.jsp?projectName=");
      out.print(project.getName());
      out.write("\"\r\n");
      out.write("\t\t\t\t\tid=\"\"\r\n");
      out.write("\t\t\t\t\tclass=\"btn btn-outline-secondary button-nav-size button-nav-margin button-nav-height\">\r\n");
      out.write("\t\t\t\t\tQuestionnaire <svg width=\"1em\" height=\"1em\" viewBox=\"0 0 16 16\"\r\n");
      out.write("\t\t\t\t\t\tclass=\"bi bi-question-octagon-fill\" fill=\"currentColor\"\r\n");
      out.write("\t\t\t\t\t\txmlns=\"http://www.w3.org/2000/svg\">\r\n");
      out.write("  \t\t\t\t\t<path fill-rule=\"evenodd\"\r\n");
      out.write("\t\t\t\t\t\t\td=\"M11.46.146A.5.5 0 0 0 11.107 0H4.893a.5.5 0 0 0-.353.146L.146 4.54A.5.5 0 0 0 0 4.893v6.214a.5.5 0 0 0 .146.353l4.394 4.394a.5.5 0 0 0 .353.146h6.214a.5.5 0 0 0 .353-.146l4.394-4.394a.5.5 0 0 0 .146-.353V4.893a.5.5 0 0 0-.146-.353L11.46.146zM5.496 6.033a.237.237 0 0 1-.24-.247C5.35 4.091 6.737 3.5 8.005 3.5c1.396 0 2.672.73 2.672 2.24 0 1.08-.635 1.594-1.244 2.057-.737.559-1.01.768-1.01 1.486v.105a.25.25 0 0 1-.25.25h-.81a.25.25 0 0 1-.25-.246l-.004-.217c-.038-.927.495-1.498 1.168-1.987.59-.444.965-.736.965-1.371 0-.825-.628-1.168-1.314-1.168-.803 0-1.253.478-1.342 1.134-.018.137-.128.25-.266.25h-.825zm2.325 6.443c-.584 0-1.009-.394-1.009-.927 0-.552.425-.94 1.01-.94.609 0 1.028.388 1.028.94 0 .533-.42.927-1.029.927z\" />\r\n");
      out.write("\t\t\t\t</svg>\r\n");
      out.write("\t\t\t\t</a>\r\n");
      out.write("\t\t\t\t");

					if (project.getQuestionnaire() != null && project.getQuestionnaire().getDate() != null) {
				
      out.write("\r\n");
      out.write("\t\t\t\t<a href=\"Ranking.jsp?projectName=");
      out.print(project.getName());
      out.write("\"\r\n");
      out.write("\t\t\t\t\tclass=\"btn btn-outline-secondary button-nav-size button-nav-margin button-nav-height\">\r\n");
      out.write("\t\t\t\t\tRecommender <svg width=\"1em\" height=\"1em\" viewBox=\"0 0 16 16\"\r\n");
      out.write("\t\t\t\t\t\tclass=\"bi bi-eye\" fill=\"currentColor\"\r\n");
      out.write("\t\t\t\t\t\txmlns=\"http://www.w3.org/2000/svg\">\r\n");
      out.write("\t\t\t\t\t\t<path fill-rule=\"evenodd\"\r\n");
      out.write("\t\t\t\t\t\t\td=\"M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.134 13.134 0 0 0 1.66 2.043C4.12 11.332 5.88 12.5 8 12.5c2.12 0 3.879-1.168 5.168-2.457A13.134 13.134 0 0 0 14.828 8a13.133 13.133 0 0 0-1.66-2.043C11.879 4.668 10.119 3.5 8 3.5c-2.12 0-3.879 1.168-5.168 2.457A13.133 13.133 0 0 0 1.172 8z\" />\r\n");
      out.write("\t\t\t\t\t\t<path fill-rule=\"evenodd\"\r\n");
      out.write("\t\t\t\t\t\t\td=\"M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5zM4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0z\" />\r\n");
      out.write("\t\t\t\t\t</svg>\r\n");
      out.write("\t\t\t\t</a>\r\n");
      out.write("\t\t\t\t");

					} else {
				
      out.write("\r\n");
      out.write("\t\t\t\t<button\r\n");
      out.write("\t\t\t\t\tclass=\"btn btn-outline-secondary button-nav-size button-nav-margin button-nav-height\"\r\n");
      out.write("\t\t\t\t\tdisabled=\"disabled\">\r\n");
      out.write("\t\t\t\t\tRecommender\r\n");
      out.write("\t\t\t\t\t<svg width=\"1em\" height=\"1em\" viewBox=\"0 0 16 16\"\r\n");
      out.write("\t\t\t\t\t\tclass=\"bi bi-eye-slash\" fill=\"currentColor\"\r\n");
      out.write("\t\t\t\t\t\txmlns=\"http://www.w3.org/2000/svg\">\r\n");
      out.write("\t\t\t\t\t  <path\r\n");
      out.write("\t\t\t\t\t\t\td=\"M13.359 11.238C15.06 9.72 16 8 16 8s-3-5.5-8-5.5a7.028 7.028 0 0 0-2.79.588l.77.771A5.944 5.944 0 0 1 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.134 13.134 0 0 1 14.828 8c-.058.087-.122.183-.195.288-.335.48-.83 1.12-1.465 1.755-.165.165-.337.328-.517.486l.708.709z\" />\r\n");
      out.write("\t\t\t\t\t  <path\r\n");
      out.write("\t\t\t\t\t\t\td=\"M11.297 9.176a3.5 3.5 0 0 0-4.474-4.474l.823.823a2.5 2.5 0 0 1 2.829 2.829l.822.822zm-2.943 1.299l.822.822a3.5 3.5 0 0 1-4.474-4.474l.823.823a2.5 2.5 0 0 0 2.829 2.829z\" />\r\n");
      out.write("\t\t\t\t\t  <path\r\n");
      out.write("\t\t\t\t\t\t\td=\"M3.35 5.47c-.18.16-.353.322-.518.487A13.134 13.134 0 0 0 1.172 8l.195.288c.335.48.83 1.12 1.465 1.755C4.121 11.332 5.881 12.5 8 12.5c.716 0 1.39-.133 2.02-.36l.77.772A7.029 7.029 0 0 1 8 13.5C3 13.5 0 8 0 8s.939-1.721 2.641-3.238l.708.709z\" />\r\n");
      out.write("\t\t\t\t\t  <path fill-rule=\"evenodd\"\r\n");
      out.write("\t\t\t\t\t\t\td=\"M13.646 14.354l-12-12 .708-.708 12 12-.708.708z\" />\r\n");
      out.write("\t\t\t\t</svg>\r\n");
      out.write("\t\t\t\t</button>\r\n");
      out.write("\t\t\t\t");

					}
				
      out.write("\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t</nav>\r\n");
      out.write("\r\n");
      out.write("\t\t<div class=\"row justify-content-md-center\">\r\n");
      out.write("\t\t\t<div class=\"col-7\">\r\n");
      out.write("\t\t\t\t<div class=\"row\">\r\n");
      out.write("\t\t\t\t\t<div class=\"col\">\r\n");
      out.write("\t\t\t\t\t\t<div id=\"xtext-editor\" data-editor-xtext-lang=\"bot\"\r\n");
      out.write("\t\t\t\t\t\t\tdata-editor-resource-id=\"");
      out.print(project.getOwner().getNick() + "/" + project.getName());
      out.write(".bot\"\r\n");
      out.write("\t\t\t\t\t\t\tdata-editor-dirty-element=\"dirty-indicator\"\r\n");
      out.write("\t\t\t\t\t\t\tdata-editor-enable-save-action=\"true\"\r\n");
      out.write("\t\t\t\t\t\t\tdata-editor-enable-formatting-action=\"true\"\r\n");
      out.write("\t\t\t\t\t\t\tdata-editor-show-error-dialogs=\"true\"></div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<div class=\"row\">\r\n");
      out.write("\t\t\t\t\t<div class=\"col\">\r\n");
      out.write("\t\t\t\t\t\t<div id=\"dirty-indicator\">modified</div>\r\n");
      out.write("\t\t\t\t\t\t<div id=\"status\">\r\n");
      out.write("\t\t\t\t\t\t\tWelcome to <a href=\"User.jsp\">CONGA</a> editor\r\n");
      out.write("\t\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\r\n");
      out.write("\t\t\t<div class=\"col-5\">\r\n");
      out.write("\t\t\t\t<!-- <div class=\"col-5 align-self-center\">  -->\r\n");
      out.write("\t\t\t\t<div class=\"row justify-content-end\">\r\n");
      out.write("\t\t\t\t\t<img alt=\"Flow legend\" class=\"align-self-end legend\"\r\n");
      out.write("\t\t\t\t\t\tsrc=\"img/legend.png\">\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<div class=\"row justify-content-md-center\">\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t\t<h5>\r\n");
      out.write("\t\t\t\t\t\tFlow Diagram\r\n");
      out.write("\t\t\t\t\t\t<svg width=\"1em\" height=\"1em\" viewBox=\"0 0 16 16\"\r\n");
      out.write("\t\t\t\t\t\t\tclass=\"bi bi-diagram-3\" fill=\"currentColor\"\r\n");
      out.write("\t\t\t\t\t\t\txmlns=\"http://www.w3.org/2000/svg\">\r\n");
      out.write("\t  \t\t\t\t<path fill-rule=\"evenodd\"\r\n");
      out.write("\t\t\t\t\t\t\t\td=\"M6 3.5A1.5 1.5 0 0 1 7.5 2h1A1.5 1.5 0 0 1 10 3.5v1A1.5 1.5 0 0 1 8.5 6v1H14a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-1 0V8h-5v.5a.5.5 0 0 1-1 0V8h-5v.5a.5.5 0 0 1-1 0v-1A.5.5 0 0 1 2 7h5.5V6A1.5 1.5 0 0 1 6 4.5v-1zM8.5 5a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1zM0 11.5A1.5 1.5 0 0 1 1.5 10h1A1.5 1.5 0 0 1 4 11.5v1A1.5 1.5 0 0 1 2.5 14h-1A1.5 1.5 0 0 1 0 12.5v-1zm1.5-.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-1zm4.5.5A1.5 1.5 0 0 1 7.5 10h1a1.5 1.5 0 0 1 1.5 1.5v1A1.5 1.5 0 0 1 8.5 14h-1A1.5 1.5 0 0 1 6 12.5v-1zm1.5-.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-1zm4.5.5a1.5 1.5 0 0 1 1.5-1.5h1a1.5 1.5 0 0 1 1.5 1.5v1a1.5 1.5 0 0 1-1.5 1.5h-1a1.5 1.5 0 0 1-1.5-1.5v-1zm1.5-.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-1z\" />\r\n");
      out.write("\t\t\t\t</svg>\r\n");
      out.write("\t\t\t\t\t</h5>\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t\t<div class=\"row justify-content-md-center\">\r\n");
      out.write("\t\t\t\t\t<img alt=\"Flow image\" class=\"align-self-center image\"\r\n");
      out.write("\t\t\t\t\t\tsrc=\"FileServlet?projectName=");
      out.print(project.getName());
      out.write("\">\r\n");
      out.write("\t\t\t\t</div>\r\n");
      out.write("\t\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</div>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}