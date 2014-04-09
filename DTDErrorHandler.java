
class XML_DTD_ErrorHandler extends DefaultHandler {
public XML_DTD_ErrorHandler () {}
public void warning(SAXParseException spe) {
System.out.println("Warning: "+spe.toString());
}
public void error(SAXParseException spe) {
System.out.println("Error: "+spe.toString());
}
public void fatalerror(SAXParseException spe) {
System.out.println("Fatal Error: "+spe.toString());
}
}
