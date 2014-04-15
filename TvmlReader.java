import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;


public class TvmlReader {
	List<Document> DOMList;
	private String url;

	void TvmlReader(){}
	
	void Read(){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new TVML_ErrorHandler());
			DOMList = new ArrayList<Document>();
			
			Document doc = db.parse("http://localhost:8024/lro24/tvml-ok.xml");
			DOMList.add(doc);
			
			ListIterator<Document> it = DOMList.listIterator();
			int ii=0;
			do{
				doc = it.next();
				NodeList lChannels = doc.getElementsByTagName("Canal");
				
				for(int jj=0; jj<lChannels.getLength(); jj++){
					Element eChannel = (Element)lChannels.item(jj);
					NodeList nlUrl = eChannel.getElementsByTagName("UrlTVML");
					if(nlUrl.getLength()>0){
						url=nlUrl.item(0).getTextContent();
						try{
							doc = db.parse(url);
							DOMList.add(doc);
						}catch(Exception ex){
							ex.printStackTrace();
							url="no doc found";
						}
					}
				}
				
				ii++;
			}while(ii<DOMList.size());
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	String[] getDays(){
		String[] days = {"Url: " + url,
						 "02/12/2013",
						 "03/12/2013",
						 "04/12/2013",
						 "05/12/2013"
						};

		return days;
	}
	
	String[] getLanguages(){
		String[] languages =   {"en",
						 		"fr",
						 		"it",
						 		"de",
						 		"es"
							   };
		return languages;
	}
	
	String[] getChannels(){
		String[] channels = {"TVE",
						 	 "Antena3",
						 	 "Telecinco"
							};
		return channels;
	}
	
	FilmPkg[] getFilms(){
		FilmPkg[] films = new FilmPkg[3];
		for(int ii=0; ii<films.length; ii++){
			films[ii] = new FilmPkg();
			films[ii].title = "Pelicula " + ii;
			films[ii].time = Integer.toString(10+ii) + ":00";
			films[ii].synopsis = "sinopsis pelicula " + ii;
		}
		return films;
	}
	
	ShowPkg[] getShows(){
		ShowPkg[] shows = new ShowPkg[3];
		for(int ii=0; ii<shows.length; ii++){
			shows[ii] = new ShowPkg();
			shows[ii].name = "Programa " + ii;
			shows[ii].time = Integer.toString(16+ii) + ":00";
			shows[ii].age = Integer.toString(16+ii);
		}
		return shows;
	}
}

class TVML_ErrorHandler extends DefaultHandler {
	public TVML_ErrorHandler () {}
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


class FilmPkg {
	public String title;
	public String time;
	public String synopsis;
}

class ShowPkg {
	public String name;
	public String time;
	public String age;
}