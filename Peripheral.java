import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

//import com.fasterxml.jackson.databind.node.ObjectNode;

public class Peripheral {

    LinkedList<String> storeJson = new LinkedList<String>();
   
    public void genJson() {
    	System.out.println(storeJson.toString());
        /*jsonNodeString jNode = objectMapper.readTree(jsonNodeString);
        System.out.println(jNode.toString());*/	       	
    }

    public void parsing() {
    	try {
            File f = new File("peripheral.txt");
            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine = "";
            String jsonNodeString = "";
            String titleNodeString = "";
            String psuJsonNodeString = "";

            while ((readLine = b.readLine()) != null) {
                //System.out.println(readLine);
                if (readLine.contains(":")) {
            		String[] s = {"", ""};
            		s = readLine.split(":");                	
                	if(jsonNodeString.equals("{")) {
                        jsonNodeString = jsonNodeString + "\"" + s[0].trim() + "\" : \"" + s[1].trim() + "\"";
                    } else {
                    	jsonNodeString = jsonNodeString + ", " + "\"" + s[0].trim() + "\" : \"" + s[1].trim() + "\"";
                    }
                } else { //handle title
                	if (titleNodeString.length() == 0) {
                        titleNodeString = readLine.trim().replace(" ", "-");
                        jsonNodeString = "{";
                	} else {
                	    if (titleNodeString.contains("PSU") || psuJsonNodeString.length()!=0) {
                	    	if ( psuJsonNodeString.length() == 0 && !jsonNodeString.contains("Unplugged")) {
	                	    	psuJsonNodeString = "\"" + titleNodeString + "\" : " + jsonNodeString + ",";
                	    	} else if (jsonNodeString.contains("Unplugged")) {
                	    		jsonNodeString =  "\"" + titleNodeString + "\" : " + jsonNodeString + "}";
             	    		    storeJson.add(jsonNodeString);
                	    	} else if (titleNodeString.contains("PSU") && psuJsonNodeString.length()!=0) {
                                //this means next psu
                            	psuJsonNodeString = psuJsonNodeString + jsonNodeString;
                            	storeJson.add(psuJsonNodeString);	
                            	psuJsonNodeString = "";		                                                                        	
                            } else {
	                	    	jsonNodeString =  "\"" + titleNodeString + "\" : " + jsonNodeString + "}";
                                //in same psu
                            	psuJsonNodeString = psuJsonNodeString + jsonNodeString;
                            }

	                        titleNodeString = readLine.trim().replace(" ", "_");;
	                        jsonNodeString = "{";                            
                	    } else {
	                        jsonNodeString = jsonNodeString + "}";
	                        //convert to json
	                        jsonNodeString = "\"" + titleNodeString + "\" : " + jsonNodeString + "";
	                        storeJson.add(jsonNodeString);

	                        //start next title
	                        titleNodeString = readLine.trim().replace(" ", "_");;
	                        jsonNodeString = "{";
	                    }
                	}
                }
            }            
            if (psuJsonNodeString.length() != 0) {
            	psuJsonNodeString = psuJsonNodeString + "," + "\"" + titleNodeString + "\" : " + jsonNodeString + "}}";
            	storeJson.add(psuJsonNodeString);

            	psuJsonNodeString = "";                
            } else if (titleNodeString.length() != 0) {
            	jsonNodeString = jsonNodeString + "}";
                //convert to json
                jsonNodeString = "\"" + titleNodeString + "\" : " + jsonNodeString + "";
                storeJson.add(jsonNodeString);
            }
              
        } catch (IOException ioe) {

        }
    }
    

	public static void main(String args[]) {
        Peripheral p = new Peripheral();
        p.parsing();
        p.genJson();
	}
}
