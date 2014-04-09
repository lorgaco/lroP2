import java.io.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class TvmlReader {

	void TvmlReader(/*tvml1*/){
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		db.setErrorHandler(new DTDErrorHandler());
		Document doc = db.parse("http://clave.det.uvigo.es:8080/~lroprof/13-14/tvml14-20-06.xml"); //todo, change
		String tipo = doc.getDoctype().getName();
		System.out.println(”El tipo de documento es: "+tipo);
		Element raiz = doc.getDocumentElement();
		System.out.println(”El elemento raíz es: ”+raiz.getTagName());
	}
	
	String[] getDays(){
		String[] days = {"01/12/2013",
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