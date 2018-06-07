package com.paradroid.util.menuHandler;

import com.paradroid.Network.Lobby;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableLobby {
    /**
     * @autor jxv603
     */
    ObservableList<Lobby> data;

    public  TableLobby(ObservableList<Lobby> data){
        this.data=data;
    }

    /**
     * Create a table view for the lobby and set all the constrains of it
     * @return
     */
    public  TableView<Lobby> createObject(){
        TableView<Lobby> table = new TableView<>();
        table.setEditable(false);
        table.setMaxWidth(600);
        TableColumn nameColumn = new TableColumn("Lobby Name");
        nameColumn.setMinWidth(200);
        nameColumn.setMaxWidth(200);

        //Assign column to element of the lobby class below
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<Lobby, String>("name"));
        
        TableColumn ipColumn = new TableColumn("Lobby ID");
        ipColumn.setMinWidth(200);
        ipColumn.setMaxWidth(200);
        ipColumn.setCellValueFactory(
                new PropertyValueFactory<Lobby, String>("ID"));
        
        
        TableColumn numberPlayersColumn = new TableColumn("Number Of Players");
        numberPlayersColumn.setMinWidth(200);
        numberPlayersColumn.setMaxWidth(200);
        numberPlayersColumn.setCellValueFactory(
                new PropertyValueFactory<Lobby, String>("players"));

        //Add all the data inside the table
        table.setItems(data);
        table.getColumns().addAll(nameColumn, ipColumn, numberPlayersColumn);
        return table;
    }
}
