package com.jonydog.refy.daos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.util.RefyErrors;
import com.sun.javafx.PlatformUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

@Component
public class ReferenceDAO {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String REFY_FILENAME = "refy.json";

    public Reference[] getAllReferences(String homeFolder){

        File file = new File(homeFolder+"/"+REFY_FILENAME);

        try{

            if( file.exists() ){
                Reference[] refs = this.objectMapper.readValue( file,Reference[].class);
                return refs;
            }else{
                Reference[] refs = new Reference[0];
                return refs;
            }
        }
        catch (Exception e) {
            return new Reference[0];
        }
    }

    public void save(Reference[] refs, String homeFolder, RefyErrors errors) {


        File file = new File(homeFolder+"/"+REFY_FILENAME);

        //this.makeFileInvisible(file);

        try {
            this.objectMapper.writeValue(file, refs);
            System.out.println("File written:"+file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            errors.addError(
                    "Could not save to file"
            );
        }

    }

    /** not used for now, have to find workaround to be able to read hidden files **/
    private void makeFileInvisible(File f){

        if(PlatformUtil.isWindows()) {

            Path path = f.toPath();
            Boolean hidden = null;
            try {
                hidden = (Boolean) Files.getAttribute(path, "dos:hidden", LinkOption.NOFOLLOW_LINKS);
                if (hidden != null && !hidden) {
                    Files.setAttribute(path,"dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
