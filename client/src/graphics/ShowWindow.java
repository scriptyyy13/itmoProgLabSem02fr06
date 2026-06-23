package graphics;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import sharedTools.DragonTableRow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ShowWindow {
    private Stage stage;
    private final String BG_COLOR = "#2a3950";
    private final int HEIGHT = 720;
    private final int WIDTH = 1280;
    public TableView<DragonTableRow> table;
    public ObservableList<DragonTableRow> showItems;
    private ComboBox<String> dragonsFieldsfilter;
    private ComboBox<String> operations;
    private TextField filterValue;
    private Button filterButton;

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

        TableColumn<DragonTableRow,Void> buttonsCol = new TableColumn<>("");


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
        VBox.setMargin(table,new Insets(20,0,0,20));
        table.setMaxSize(1240,500);

        buttonsCol.setCellFactory(param -> new TableCell<>() {
            private final Button detailsBtn = new Button("More");

            @Override
            protected void updateItem(Void item, boolean empty) {
                if (empty) {

                    setGraphic(null);
                    setText(null);
                } else {
                    DragonTableRow currentRow = getTableView().getItems().get(getIndex());


                    detailsBtn.setOnAction(e -> {
                        Stage infoStage = new Stage();
                        EditDragonWindow edw = new EditDragonWindow(
                                infoStage,
                                currentRow,
                                MainWindow.generateColorFromId(currentRow.getCreatorId())
                        );
                        infoStage.initModality(Modality.APPLICATION_MODAL);
                        edw.showAndWait();
                    });


                    setGraphic(detailsBtn);
                    setText(null);
                }

            }

        });
        table.getColumns().add(buttonsCol);

        dragonsFieldsfilter = new ComboBox<>();
        dragonsFieldsfilter.getItems().addAll("id","creator id","x","y","creationDate","age","weight", "speaking","color","killerName","birthday","killerPassport","killerNat","locX","locY","locZ","locName");
        operations = new ComboBox<>();
        operations.getItems().addAll(">","<","=","<=",">=");
        filterValue = new TextField();
        filterButton = new Button("filter");
        root.getChildren().addAll(dragonsFieldsfilter,filterValue,operations,filterButton);
        filterButton.setOnAction(this::filter);

        VBox.setMargin(dragonsFieldsfilter,new Insets(20,0,0,20));
        VBox.setMargin(filterValue,new Insets(20,0,0,20));
        VBox.setMargin(operations,new Insets(20,0,0,20));
        VBox.setMargin(filterButton,new Insets(20,0,0,20));

        filterValue.setMaxWidth(100);
        return new Scene(root,WIDTH, HEIGHT);
    }

    private void filter(ActionEvent actionEvent) {
        String field = dragonsFieldsfilter.getValue();
        String op = operations.getValue();
        String rawValue = filterValue.getText().trim();

        // Если поле не выбрано или значение пустое — сбрасываем фильтр (показываем всё)
        if (field == null || rawValue.isEmpty() || op == null) {
            table.setItems(showItems);
            return;
        }


        List<DragonTableRow> filtered = table.getItems().stream()
                .filter(row -> {
                    try {
                        return checkCondition(row, field, op, rawValue);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        table.getItems().setAll(filtered);

    }


    private boolean checkCondition(DragonTableRow row, String field, String op, String rawValue) throws Exception {
        switch (field) {
            case "id":
            case "creator id":
                long longVal = Long.parseLong(rawValue);
                long rowLong = field.equals("id") ? row.getId() : row.getCreatorId();
                return compareNumbers(rowLong, longVal, op);

            case "age":
                long ageVal = Long.parseLong(rawValue);
                return compareNumbers(row.getAge(), ageVal, op);

            case "weight":
                if (row.getWeight() == null) return false;
                int weightVal = Integer.parseInt(rawValue);
                return compareNumbers(row.getWeight(), weightVal, op);

            case "y":
                if (row.getY() == null) return false;
                java.lang.Double doubleVal = Double.parseDouble(rawValue);
                Double rowDouble =  row.getY();
                return compareNumbers(rowDouble, doubleVal, op);
            case "x":
                if (row.getX() == null) return false;
                float floatVal = Float.parseFloat(rawValue);
                float rowFloat = row.getX();
                return compareNumbers(rowFloat, floatVal, op);

            case "locX":
            case "locY":
            case "locZ":
                Integer locVal = Integer.parseInt(rawValue);
                Integer rowLoc = switch (field) {
                    case "locX" -> row.getLocX();
                    case "locY" -> row.getLocY();
                    default -> row.getLocZ();
                };
                if (rowLoc == null) return false;
                return compareNumbers(rowLoc, locVal, op);

            case "creationDate":
            case "birthday":
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
                Date dateVal = sdf.parse(rawValue);
                Date rowDate = field.equals("creationDate") ? row.getCreationDate() : row.getKillerBirthday();
                if (rowDate == null) return false;
                return compareDates(rowDate, dateVal, op);

            case "speaking":
                boolean boolVal = Boolean.parseBoolean(rawValue);
                return (row.getSpeaking() == boolVal) && (op.equals("=") || op.equals("<=") || op.equals(">="));

            case "color":
            case "killerName":
            case "killerPassport":
            case "killerNat":
            case "locName":
                String strVal = rawValue.toLowerCase();
                String rowStr = switch (field) {
                    case "color" -> row.getColor();
                    case "killerName" -> row.getKillerName();
                    case "killerPassport" -> row.getKillerPassport();
                    case "killerNat" -> row.getKillerNat();
                    default -> row.getLocName();
                };
                if (rowStr == null) return false;
                if (op.equals("=")) {
                    return rowStr.equalsIgnoreCase(strVal);
                } else if (op.equals("<=") || op.equals(">=")) {
                    return rowStr.toLowerCase().contains(strVal);
                }
                return false;

            default:
                return false;
        }
    }

    private boolean compareNumbers(Number rowVal, Number filterVal, String op) {
        double r = rowVal.doubleValue();
        double f = filterVal.doubleValue();
        return switch (op) {
            case ">" -> r > f;
            case "<" -> r < f;
            case "=" -> r == f;
            case ">=" -> r >= f;
            case "<=" -> r <= f;
            default -> false;
        };
    }

    private boolean compareDates(Date rowDate, java.util.Date filterDate, String op) {
        long r = rowDate.getTime();
        long f = filterDate.getTime();
        return switch (op) {
            case ">" -> r > f;
            case "<" -> r < f;
            case "=" -> r == f;
            case ">=" -> r >= f;
            case "<=" -> r <= f;
            default -> false;
        };
    }

    public void setTableFromCSV(String csv){
        List<Dragon> dragonList = Dragon.loadDragonsFromCSV(csv);
        List<DragonTableRow> dragonTableRowList = dragonList.stream()
                .map(Dragon::mapToTableRow)
                .toList();

        ObservableList<DragonTableRow> data = FXCollections.observableArrayList(dragonTableRowList);
        showItems = data;
        table.setItems(data);

    }



    public void show(){
        stage.show();
    }

    public void close(){
        stage.close();
    }
}
