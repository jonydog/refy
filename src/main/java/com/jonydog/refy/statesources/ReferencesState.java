package com.jonydog.refy.statesources;

import com.jonydog.refy.business.interfaces.ReferenceService;
import com.jonydog.refy.model.Reference;
import com.jonydog.refy.util.RefyErrors;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReferencesState extends StateSource {

    // business services
    @Autowired
    private ReferenceService referenceService;
    @Autowired
    private SettingsState settingsState;

    @Getter
    private ObservableList<Reference> currentReferences;

    @Getter
    private SimpleObjectProperty<Reference> selectedReference;

    @Getter
    @Setter
    private SimpleBooleanProperty editMode;


    @Override
    public void refreshState(RefyErrors errors) {

        // get all refs
        this.currentReferences.clear();
        this.currentReferences.addAll( this.referenceService.getAllReferences(this.settingsState.getSettings().getHomeFolder(), errors ) );
    }

    public void refreshFromFile(RefyErrors errors){
        // get all refs
        this.currentReferences.clear();
        this.currentReferences.addAll( this.referenceService.getAllReferences(this.settingsState.getSettings().getHomeFolder(), errors ,true) );
    }

    @Override
    public void init() {

        //instantiate
        this.currentReferences = FXCollections.observableArrayList();
        RefyErrors errors = new RefyErrors();
        this.currentReferences.addAll( this.referenceService.getAllReferences(this.settingsState.getSettings().getHomeFolder(),errors) );
        this.selectedReference = new SimpleObjectProperty<>();
        this.editMode = new SimpleBooleanProperty(false);

        //load
        //this.currentReferences.addAll( this.referenceService.getAllReferences( new RefyErrors() ) );
        //this.currentReferences.addAll( this.referenceService.getAllReferences( new RefyErrors() ) );
    }
}
