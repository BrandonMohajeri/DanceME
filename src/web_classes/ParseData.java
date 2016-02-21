package web_classes;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

/*
 * To get a quote from the specified website
 */
public class ParseData {
	private static final String TAG = null; 

	public String getQuote(){
		
		ArrayList<String> quotes = new ArrayList<String>();
		
		try {	 
			Random rand = new Random();  
			int randomPage = rand.nextInt((13-1) + 1) + 1;
			
			String sourceCode = QuoteScraper.scrapeQuote("http://www.goodreads.com/quotes/tag/dance?page=" + randomPage);
			 
			String tag = "&ldquo;";  	   
		    int lastIndex = 0;
		 
		     while(lastIndex != -1){
		    	 lastIndex = sourceCode.indexOf(tag,lastIndex);
		    	   
		    	 if(lastIndex != -1){
		    		 lastIndex += tag.length();
		    		     
			    	 String postString = "&rdquo;"; 			    	 
			    	 int startPosition = lastIndex;
				     int endPosition = sourceCode.indexOf(postString, startPosition);
				     String quote = sourceCode.substring(startPosition, endPosition);
				     
				     if(quote.length() <= 160 && !quote.contains("<br>")){		//Keep low character count to fit in activity
				    	 quotes.add("\"" + quote + "\"");
				     }          
		    	 }   	 		    	 
		     }			                    
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}		
		   
		Random rand = new Random();
		int randomPage = rand.nextInt(quotes.size());
		String randomQuote = quotes.get(randomPage);
		return randomQuote;		
	}
}
