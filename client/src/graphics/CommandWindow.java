package graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CommandWindow {
    private Stage stage;
    private final String BG_COLOR = "#2a3950";
    private final int HEIGHT = 275;
    private final int WIDTH = 200;
    public Button enterModel;
    public Button executeButton;
    public TextField[] argsWindows;
    public Label errorLabel;
    public String commandName;

    public CommandWindow(Stage stage, String commandName){
        this.stage = stage;
        this.commandName = commandName;
        this.stage.setResizable(false);
        this.stage.setScene(createCommandScene());
    }

    public Scene createCommandScene(){
        Font bigFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"),20);
        Font simpleFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"),15);
        String[] argsNames;
        Boolean isHavingModel = false;
        switch (commandName){
            case "Add":
                stage.setTitle("Add");
                argsNames= new String[]{};
                isHavingModel = true;
                break;
            case "AddIfMax":
                stage.setTitle("AddIfMax");
                argsNames= new String[]{};
                isHavingModel = true;
                break;
            case "AddIfMin":
                stage.setTitle("AddIfMin");
                argsNames= new String[]{};
                isHavingModel = true;
                break;
            case "Update":
                stage.setTitle("Update");
                argsNames= new String[]{"id_dragon" + ":"};
                isHavingModel = true;
                break;
            case "RemoveById":
                stage.setTitle("RemoveById");
                argsNames= new String[]{"id_dragon" + ":"};
                break;
            default:
                argsNames = new String[]{};
        }
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + BG_COLOR +";");
        Label title = new Label(commandName);
        title.setFont(bigFont);
        title.setTextFill(Color.WHITE);
        title.setAlignment(Pos.TOP_LEFT);
        root.getChildren().add(title);
        Label[] argsTitles = new Label[argsNames.length];
        argsWindows = new TextField[argsNames.length];

        for(int i=0;i<argsWindows.length;i++){
            argsTitles[i] = new Label(argsNames[i]);
            argsTitles[i].setFont(simpleFont);
            argsTitles[i].setAlignment(Pos.TOP_LEFT);
            argsTitles[i].setTextFill(Color.WHITE);
            argsWindows[i] = new TextField();
            argsWindows[i].setFont(simpleFont);
            argsWindows[i].setMaxWidth(100);
            VBox.setMargin(argsTitles[i],new Insets(20,0,0,20));
            VBox.setMargin(argsWindows[i],new Insets(5,0,0,20));
            root.getChildren().addAll(argsTitles[i],argsWindows[i]);
        }
        enterModel = new Button("enter dragon");
        enterModel.setFont(simpleFont);
        if(isHavingModel) root.getChildren().add(enterModel);
        executeButton = new Button("execute");
        executeButton.setFont(simpleFont);
        VBox.setMargin(enterModel, new Insets(20,0,0,20));
        VBox.setMargin(title, new Insets(20,0,0,20));
        errorLabel = new Label("");
        errorLabel.setFont(simpleFont);
        errorLabel.setTextFill(Color.RED);
        HBox errorBox = new HBox(errorLabel);
        HBox executeBox = new HBox(executeButton);
        executeBox.setAlignment(Pos.BOTTOM_RIGHT);
        errorBox.setAlignment(Pos.CENTER);
        errorBox.setPadding(new Insets(20,0,0,20));
        executeBox.setPadding(new Insets(5,20,0,0));
        root.getChildren().addAll(errorBox,executeBox);

        return new Scene(root,WIDTH, HEIGHT);
    }


    public void show(){
        stage.show();
    }

    public void close(){
        stage.close();
    }



}
