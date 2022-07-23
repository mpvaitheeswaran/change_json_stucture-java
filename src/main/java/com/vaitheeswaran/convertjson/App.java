package com.vaitheeswaran.convertjson;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws StreamReadException, DatabindException, IOException
    {
        
       ObjectMapper mapper = new ObjectMapper();
       //Map<?, ?> map = mapper.readValue(new File("json/FCC_MENU.json"), Map.class);
       JsonNode fccMenuNode = mapper.readTree(new File("json/FCC_MENU.json"));
       JsonNode navigationNode = mapper.readTree(new File("json/navigation.json"));
       JsonNode fccMenuNodeArray = fccMenuNode.get("menus");
       JsonNode navigationNodeArray = navigationNode.get("responseValue").get("app");
       if (fccMenuNodeArray.isArray()) {
    	   int index=0;
           for (JsonNode menuNode : fccMenuNodeArray) {
        	   if(navigationNodeArray.get(index)!=null) {
        		 //Set Menu name 
                  ((ObjectNode)navigationNodeArray.get(index)).set("name", menuNode.get("menuLabel"));
                //Set Title 
                  ((ObjectNode)navigationNodeArray.get(index)).set("title", menuNode.get("menuLabel"));
                  //Set sub menuGroup
                  JsonNode fccSubMenuNodeArray = fccMenuNodeArray.get(index).get("subMenus");
                  JsonNode navigationSubNodeArray=navigationNodeArray.get(index).get("menuGroup");
                  int subMenuIndex=0;
                  if(fccSubMenuNodeArray.isArray()) {
                	  for(JsonNode subMenu:fccSubMenuNodeArray) {
                		  if(navigationSubNodeArray.get(subMenuIndex)!=null) {
                			 //set submenu name
                    		 ((ObjectNode)navigationSubNodeArray.get(subMenuIndex))
                    		 	.set("name", subMenu.get("subMenuLabel"));
                    		 //Set submenu permission.
                    		 ((ObjectNode)navigationSubNodeArray.get(subMenuIndex))
                 		 		.set("permission", subMenu.get("subMenuPermission"));
                    		 //set url
                    		 ((ObjectNode)navigationSubNodeArray.get(subMenuIndex))
              		 			.set("url", subMenu.get("subMenuUrl"));
                		  }
                    	  
                		  subMenuIndex+=1;
                      }
                  }
                  
        	   }else {
        		   //If the menu is null add new
        		   JsonNode newNavMenu = mapper.readTree(new File("json/navigation_menu.json"));
        		   JsonNode navMenu = ((ArrayNode)navigationNodeArray).add(newNavMenu);
        		   //set menu name
        		   ((ObjectNode)navMenu.get(index)).set("name", menuNode.get("menuLabel"));
        		   //set title
        		   ((ObjectNode)navMenu.get(index)).set("title", menuNode.get("menuLabel"));
        		 //Set sub menuGroup for null menu
                   JsonNode fccSubMenuNodeArray = fccMenuNodeArray.get(index).get("subMenus");
                   JsonNode navigationSubNodeArray=navMenu.get(index).get("menuGroup");
                   int subMenuIndex=0;
                   if(fccSubMenuNodeArray.isArray()) {
                 	  for(JsonNode subMenu:fccSubMenuNodeArray) {
                 		 JsonNode newNavSubMenu = mapper.readTree(new File("json/navigation_submenu.json"));
                 		 JsonNode navSubMenu = ((ArrayNode)navigationSubNodeArray).add(newNavSubMenu);
                 		//set submenu name
                 		 ((ObjectNode)navSubMenu.get(subMenuIndex)).set("name",subMenu.get("subMenuLabel"));
                 		 //Set submenu permission.
                 		 ((ObjectNode)navSubMenu.get(subMenuIndex)).set("permission", subMenu.get("subMenuPermission"));
                 		//set url
                 		 ((ObjectNode)navSubMenu.get(subMenuIndex)).set("url", subMenu.get("subMenuUrl"));
                     	  
                 		  subMenuIndex+=1;
                       }
                   }
        		   //System.out.println(navMenu.get(index));
        	   }
        	   
              
               index+=1;
           }
           
       }
       //System.out.println(navigationNode.toPrettyString());
       ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
       writer.writeValue(new File("json/navigation_output.json"), navigationNode);
       
    }
}
