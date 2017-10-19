package com.jonydog.refy.statesources;

import com.jonydog.refy.business.interfaces.ReferenceService;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.util.RefyErrors;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReferencesState extends StateSource {

    // business services
    @Autowired
    private ReferenceService referenceService;

    @Getter
    private ObservableList<Reference> currentReferences;

    @Getter
    private SimpleObjectProperty<Reference> selectedReference;


    @Override
    public void refreshState(RefyErrors errors) {

    }

    @Override
    public void init() {

        //instantiate
        this.currentReferences = FXCollections.observableArrayList();
        this.selectedReference = new SimpleObjectProperty<>();

        //load
        this.currentReferences.addAll( this.referenceService.getAllReferences( new RefyErrors() ) );
    }
}
