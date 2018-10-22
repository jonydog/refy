package com.jonydog.refy.jobs;

import com.jonydog.refy.business.interfaces.ReferenceService;
import com.jonydog.refy.daos.ReferenceDAO;
import com.jonydog.refy.model.Reference;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ReferenceKeeper {


    @Autowired
    private ReferenceService referenceService;
    @Autowired
    private ReferenceDAO referenceDAO;

    private ExecutorService executorService;

    @Getter
    private AtomicBoolean isAppClosed = new AtomicBoolean(false);


    public void initPersistenceJob(){

        this.executorService = Executors.newSingleThreadExecutor();
        this.executorService.execute( new Thread( ()->{ this.persistenceJob(); } ) );

        this.executorService.shutdown();
    }


    private void persistenceJob(){

        while( ! this.isAppClosed.get()) {

            System.out.println("Running persistence job");
            ArrayList<Reference> refsList = this.referenceService.getAllReferences(null);
            this.referenceDAO.save(refsList.toArray(new Reference[refsList.size()]), null);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
