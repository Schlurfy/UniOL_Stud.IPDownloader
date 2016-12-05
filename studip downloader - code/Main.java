import java.io.PrintWriter;
import java.io.StringWriter;

import GUI.GUI;

public class Main
{ 
	public static void main(String[] args)
	{	
		try{
			GUI.initGUI();
		}	
		catch (Exception e)
		{	StringWriter sw = new StringWriter();		
			
			e.printStackTrace(new PrintWriter(sw));			
			
			GUI.callAlert("Error", "Es kam zu einen Fehler in der Anwendung!", sw.toString());}
	}
}
