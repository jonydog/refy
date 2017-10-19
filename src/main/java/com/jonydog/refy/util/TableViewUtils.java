package com.jonydog.refy.util;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;

public class TableViewUtils {
	
	private static final Logger logger = Logger.getLogger(TableViewUtils.class);
	
	public static <T> boolean fillTableView( TableView<T> table ,List<T> dataList, String... fieldNames   ){
		
		if( table.getColumns().size() != fieldNames.length ){
			logger.error( "Table filler: columns size does not match with the number of designated fields." );
			return false;
		}
		
		for( int i=0; i<fieldNames.length; i++ ){
			
			TableColumn<T, ?> c = table.getColumns().get(i);
			c.setCellValueFactory( new PropertyValueFactory<>(fieldNames[i]));
		}
		
		table.setItems( FXCollections.observableArrayList( dataList ) );
		
		return true;
	}
	
		
	public static <T> boolean fillTableView( TableView<T> table ,ObservableList<T> dataList, String... fieldNames   ){
		
		if( table.getColumns().size() != fieldNames.length ){
			logger.error( "Table filler: columns size does not match with the number of designated fields." );
			return false;
		}
		
		for( int i=0; i<fieldNames.length; i++ ){
			
			TableColumn<T, ?> c = table.getColumns().get(i);
			c.setCellValueFactory( new PropertyValueFactory<>(fieldNames[i]));
		}
		
		table.setItems( dataList );
		
		return true;
	}


	/**
	 * This function receives a ResultSet and displays that data in the target TableView
	 *
	 *
	 * Obs: function only written for debug purposes
	 */
	public static TableView fillTableView( ObservableList<ObservableList>  data , ResultSet rs){


	    TableView tableView = new TableView<ObservableList<String>>();

		try{

			/**********************************
			 * TABLE COLUMN ADDED DYNAMICALLY *
			 **********************************/
			for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
				//We are using non property style for making dynamic table
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
				col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
					public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
						return new SimpleStringProperty(param.getValue().get(j)!=null ? param.getValue().get(j).toString() : "NULL" );
					}
				});

				tableView.getColumns().addAll(col);
				System.out.println("Column ["+i+"] ");
			}

			/********************************
			 * Data added to ObservableList *
			 ********************************/
			while(rs.next()){
				//Iterate Row
				ObservableList<String> row = FXCollections.observableArrayList();
				for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
					//Iterate Column
					row.add(rs.getString(i));
				}
				System.out.println("Row [1] added "+row );
				data.add(row);

			}

			//FINALLY ADDED TO TableView
			tableView.setItems(data);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Error on Building Data");
		}
		return  tableView;
	}
}