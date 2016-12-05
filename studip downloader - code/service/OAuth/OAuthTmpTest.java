package service.OAuth;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.Scanner;
import java.awt.Desktop;
import java.net.*;
import org.scribe.model.*;

import service.ManagementService;


public class OAuthTmpTest {	

	private static final String PROTECTED_RESOURCE_URL = "https://elearning.uni-oldenburg.de/plugins.php/restipplugin/api/courses/semester";
	
	public static void login()
	{
		OAuthService service = new ServiceBuilder()
	            .provider(model.StudIPApi.class)
	            .apiKey()
	            .apiSecret()
	            .callback("https://elearning.uni-oldenburg.de/index.php")
	            .build();

	    Scanner in = new Scanner(System.in);
		
		System.out.println("=== StudIP's OAuth Workflow ===");
	    System.out.println();

	    // Anfordern des Request Token
	    System.out.println("Anfordern des Request Token...");
	    Token requestToken = service.getRequestToken();
	    System.out.println("Request Token erhalten!");
	    System.out.println();

	    System.out.println("Autorisierung ueber die folgende Webadresse:\n");
	    System.out.println(service.getAuthorizationUrl(requestToken));
	    System.out.println("\nNach dem Klick auf Erlauben aus der Url den oauth_verifier kopieren und hier einfuegen!");
	    System.out.print(">>");
	    
	    /*
			Das beschriebene Problem kommt hier zum Tragen.

			Es wird der Browser geoeffnet mit der Authorization URL, welche auf die Seite fuehrt, auf der man der Anwendung die benoetigen Berechtigen Erlaubt. Bei einem Klick auf Erlauben sollte sich eine URL mit oauth_token & oauth_verifier oeffnen, hier wird stattdessen direkt auf die Hauptseite weiterverwiesen und man kann es nur ueber den Networkreiter des Dev-Tools abfangen.
	    
			Hier eine gute Beschreibung zu 3-legged OAuth mit konkreten Code Snippets und Darstellung des Verlaufes.
			https://dev.twitter.com/web/sign-in/implementing

	    */	    
	    
	    //�ffnet die Website um die App Berechtigung zu setzten
	    openWebpage(service.getAuthorizationUrl(requestToken));
	    Verifier verifier = new Verifier(in.nextLine());
	    System.out.println();
	    in.close();
	    
	    // Tausche Request Token und Verfier gegen Access Token
	    System.out.println("Mit dem Request Token und dem oauth_verifier bekommt man den dem Access Token.");
	    Token accessToken = service.getAccessToken(requestToken, verifier);
	    System.out.println("Access Token erhalten!");
	    System.out.println("(Dieser ist: " + accessToken + " )");
	    System.out.println();

	    
	    ManagementService.newAccessToken(accessToken);	    
	    ManagementService.saveUser();
	    
	    // Nutzung der RestApi
	    System.out.println("Jetzt hat man freien Zugang zur RestApi :-)");
	    OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
	    service.signRequest(accessToken, request);
	    Response response = request.send();
	    System.out.println("Hier z.B. mal die besuchten Semester(im .JSON-Format)");
	    System.out.println();
	    System.out.println(response.getBody());

	    System.out.println();
	    System.out.println("Fertig :)");
	}

	 public static void openWebpage(String url) {
		    try {
		        Desktop.getDesktop().browse(new URL(url).toURI());
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}

}
