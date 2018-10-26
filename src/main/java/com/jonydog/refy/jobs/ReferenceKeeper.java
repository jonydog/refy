package com.jonydog.refy.jobs;

import com.jonydog.refy.business.interfaces.ReferenceService;
import com.jonydog.refy.daos.ReferenceDAO;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.statesources.SettingsState;
import com.jonydog.refy.util.RefyErrors;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ReferenceKeeper {


    @Autowired
    private ReferenceService referenceService;
    @Autowired
    private ReferenceDAO referenceDAO;
    @Autowired
    private SettingsState settingsState;

    private ExecutorService executorService;
    private AtomicLong lastWritten;

    @Getter
    private AtomicBoolean isAppClosed = new AtomicBoolean(false);


    public void initPersistenceJob(){

        this.lastWritten = new AtomicLong(0L);
        this.executorService = Executors.newSingleThreadExecutor();
        Thread worker = new Thread( ()->{ this.persistenceJob(); } );
        this.executorService.execute( worker );
        this.executorService.shutdown();

    }


    private void persistenceJob(){

        System.out.println("Persistence job");

        while( ! this.isAppClosed.get()) {

            System.out.println("Running persistence job");
            if( this.lastWritten.get() < this.referenceService.getLastModified().get() ) {

                System.out.println("Writing to file");
                RefyErrors errors = new RefyErrors();
                ArrayList<Reference> refsList = this.referenceService.getAllReferences(this.settingsState.getSettings().getHomeFolder(), errors);
                this.referenceDAO.save(refsList.toArray(new Reference[refsList.size()]), this.settingsState.getSettings().getHomeFolder(), errors);

                //update last written time
                this.lastWritten.set( System.currentTimeMillis() );
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
                e.printStackTrace();
            }

        }

        System.exit(0);
    }



}
