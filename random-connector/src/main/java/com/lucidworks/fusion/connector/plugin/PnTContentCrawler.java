package com.lucidworks.fusion.connector.plugin;

import org.apache.chemistry.opencmis.client.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PnTContentCrawler {

    String startFolder;
    private String[] DO_NOT_CRAWL = new String[]{"_trash","Archive"};


    public PnTContentCrawler(String startFolder){
        this.startFolder = startFolder;
    }

    public List<String> getAllDocuments(Session session){
        return crawl(session.getObjectByPath(startFolder));
    }

    List<String> crawl(CmisObject object){

        List<String> objectIds = new ArrayList<>();

        if(object.getBaseType().getDisplayName().equalsIgnoreCase("Folder")){

            Folder f = (Folder)object;


            if(!Arrays.asList(DO_NOT_CRAWL).contains(f.getName())) {
                Iterator<CmisObject> iterator = f.getChildren().iterator();
                while (iterator.hasNext()) {
                    objectIds.addAll(crawl(iterator.next()));
                }
            }

        }
        else if(object.getBaseType().getDisplayName().equalsIgnoreCase("Document")){
            objectIds.add(object.getId());
        }

        return objectIds;
    }



}
