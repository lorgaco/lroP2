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
	private List<String> langsList;
	private List<String> daysList;
	
	private String url;

	void TvmlReader(){}
	
	private void addLang(String langs){
		String[] langl = langs.split("\\ ");
		for(int ii=0; ii<langl.length; ii++){
			ListIterator<String> it = langsList.listIterator();
			boolean included = false;
			for(int jj=0; jj<langsList.size(); jj++){
				if(it.next().equals(langl[ii])) {
					included = true;
				}
			}
			if(!included) langsList.add(langl[ii]);
		}
	}
	
	void Read(){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.setErrorHandler(new TVML_ErrorHandler());
			DOMList = new ArrayList<Document>();
			langsList = new ArrayList<String>();
			daysList = new ArrayList<String>();
			
			Document doc = db.parse("http://localhost:8024/lro24/tvml-ok.xml");
			DOMList.add(doc);
			
			ListIterator<Document> it = DOMList.listIterator();
			int ii=0;
			do{
				doc = it.next();
				NodeList lChannels = doc.getElementsByTagName("Canal");
				daysList.add(doc.getDocumentElement().getElementsByTagName("Fecha").item(0).getTextContent());
				
				for(int jj=0; jj<lChannels.getLength(); jj++){
					Element eChannel = (Element)lChannels.item(jj);
					addLang(eChannel.getAttribute("lang").toString());
					
					// create languages list
					NodeList lPrograms = eChannel.getElementsByTagName("Programa");
					for(int ij=0; ij<lPrograms.getLength(); ij++){
						Element eProgram = (Element)lPrograms.item(ij);
						String lang = eProgram.getAttribute("langs");
						if(!lang.equals("")) addLang(lang);
					}
					
					// look for more tvmls
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
		String[] days = new String[daysList.size()];
		ListIterator<String> it = daysList.listIterator();
		for(int ii=0; ii<daysList.size();ii++){
			days[ii] = it.next();
		}
		return days;
	}
	
	String[] getLanguages(){
		String[] languages = new String[langsList.size()];
		ListIterator<String> it = langsList.listIterator();
		for(int ii=0; ii<langsList.size();ii++){
			languages[ii] = it.next();
		}
		return languages;
	}
	
	String[] getChannels(String day){
		ListIterator<String> it = daysList.listIterator();
		for(int ii=0; ii<daysList.size(); ii++){
			if(it.next().equals(day)) {
				ListIterator<Document> docIt = DOMList.listIterator(ii);
				NodeList lChannels = docIt.next().getElementsByTagName("Canal");
				String[] channels = new String[lChannels.getLength()];
				for(int jj=0; jj<lChannels.getLength(); jj++){
					Element eChannel = (Element)lChannels.item(jj);
					channels[jj] = eChannel.getElementsByTagName("NombreCanal").item(0).getTextContent();
				}
				return channels;
			}
		}
		
		return new String[0];
	}
	
	List<FilmPkg> getFilms(String day, String channel){
		List<FilmPkg> filmList = new ArrayList<FilmPkg>();
		ListIterator<String> it = daysList.listIterator();
		for(int ii=0; ii<daysList.size(); ii++){
			if(it.next().equals(day)) {
				ListIterator<Document> docIt = DOMList.listIterator(ii);
				NodeList lChannels = docIt.next().getElementsByTagName("Canal");
				for(int jj=0; jj<lChannels.getLength(); jj++){
					Element eChannel = (Element)lChannels.item(jj);
					String sChannel = eChannel.getElementsByTagName("NombreCanal").item(0).getTextContent(); 
					if(sChannel.equals(channel) || channel.equals("all")){
						NodeList lPrograms = eChannel.getElementsByTagName("Programa");
						for(int ij=0; ij<lPrograms.getLength(); ij++){
							Element eFilm = (Element)lPrograms.item(ij);
							String category = eFilm.getElementsByTagName("Categoria").item(0).getTextContent();
							if(category.equals("Cine")){
								FilmPkg film = new FilmPkg();
								film.title = eFilm.getElementsByTagName("NombrePrograma").item(0).getTextContent();
								film.synopsis = eFilm.getNodeValue();
								Element eIntervalo = (Element)eFilm.getElementsByTagName("Intervalo").item(0);  
								film.time = eIntervalo.getElementsByTagName("HoraInicio").item(0).getTextContent();
								filmList.add(film);
							}
						}
					}
				}
				return filmList;
			}
		}
		return filmList;
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