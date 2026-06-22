package graphics;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import models.*;
import sharedTools.DragonTableRow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowWindow {
    private Stage stage;
    private final String BG_COLOR = "#2a3950";
    private final int HEIGHT = 720;
    private final int WIDTH = 1280;
    public TableView<DragonTableRow> table;

    public ShowWindow(Stage stage){
        this.stage = stage;
        this.stage.setResizable(false);
        this.stage.setScene(createShowScene());
    }

    public Scene createShowScene(){
        Font bigFont = Font.loadFont(getClass().getResourceAsStream("fonts/aktifo.ttf"),20);
        Font simpleFont = Font.loadFont(getClass().getResourceAsStream("fonts/aktifo.ttf"),15);
        VBox root = new VBox();
        table = new TableView<>();
        root.setStyle("-fx-background-color: " + BG_COLOR+ ";");
        TableColumn<DragonTableRow,Long> idCol = new TableColumn<>("ID");
        TableColumn<DragonTableRow,Long> creatorIdCol = new TableColumn<>("creator ID");
        TableColumn<DragonTableRow,String> nameCol = new TableColumn<>("name");
        TableColumn<DragonTableRow,Float> xCol = new TableColumn<>("x");
        TableColumn<DragonTableRow,Double> yCol = new TableColumn<>("y");
        TableColumn<DragonTableRow,Double> creationDateCol = new TableColumn<>("creation date");
        TableColumn<DragonTableRow,Long> ageCol = new TableColumn<>("age");
        TableColumn<DragonTableRow,Integer> weightCol = new TableColumn<>("weight");
        TableColumn<DragonTableRow, Boolean> speakCol = new TableColumn<>("speaking");
        TableColumn<DragonTableRow,Color> colorCol = new TableColumn<>("color");
        TableColumn<DragonTableRow,String> persNameCol = new TableColumn<>("killer name");
        TableColumn<DragonTableRow, Date> birthCol = new TableColumn<>("birthday");
        TableColumn<DragonTableRow, String> passCol = new TableColumn<>("passportID");
        TableColumn<DragonTableRow, Country> natCol = new TableColumn<>("nationality");
        TableColumn<DragonTableRow,Integer> locXCol = new TableColumn<>("x");
        TableColumn<DragonTableRow,Integer> locYCol = new TableColumn<>("y");
        TableColumn<DragonTableRow,Integer> locZCol = new TableColumn<>("z");
        TableColumn<DragonTableRow,String> locNameCol = new TableColumn<>("name");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        creatorIdCol.setCellValueFactory(new PropertyValueFactory<>("creatorId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        xCol.setCellValueFactory(new PropertyValueFactory<>("x"));
        yCol.setCellValueFactory(new PropertyValueFactory<>("y"));
        creationDateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        speakCol.setCellValueFactory(new PropertyValueFactory<>("speaking"));
        colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));
        persNameCol.setCellValueFactory(new PropertyValueFactory<>("killerName"));
        birthCol.setCellValueFactory(new PropertyValueFactory<>("killerBirthday"));
        passCol.setCellValueFactory(new PropertyValueFactory<>("killerPassport"));
        natCol.setCellValueFactory(new PropertyValueFactory<>("killerNat"));
        locXCol.setCellValueFactory(new PropertyValueFactory<>("locX"));
        locYCol.setCellValueFactory(new PropertyValueFactory<>("locY"));
        locZCol.setCellValueFactory(new PropertyValueFactory<>("locZ"));
        locNameCol.setCellValueFactory(new PropertyValueFactory<>("locName"));

        table.getColumns().addAll(idCol,creatorIdCol,nameCol,xCol,yCol,creationDateCol,ageCol,weightCol,speakCol,colorCol,
                persNameCol,birthCol,passCol,natCol,locXCol,locYCol,locZCol,locNameCol);

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        root.getChildren().add(table);

        table.setMaxSize(1000,500);
        //table.setStyle("-fx-font: " + simpleFont.getFamily() + " " + simpleFont.getSize() + "px;");
        return new Scene(root,WIDTH, HEIGHT);
    }

    public void setTableFromCSV(String csv){
        List<Dragon> dragonList = Dragon.loadDragonsFromCSV(csv);
        List<DragonTableRow> dragonTableRowList = dragonList.stream()
                .map(Dragon::mapToTableRow)
                .toList();

        ObservableList<DragonTableRow> data = FXCollections.observableArrayList(dragonTableRowList);
        table.setItems(data);

    }


    public void show(){
        stage.show();
    }

    public void close(){
        stage.close();
    }
}
