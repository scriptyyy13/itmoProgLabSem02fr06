package graphics;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CommandWindow {
    private String commandName;
    private Stage stage;
    private final String BG_COLOR = "#2a3950";
    private final int HEIGHT = 400;
    private final int WIDTH = 430;
    public Button enterModel;
    public Button executeButton;
    public TextField[] argsWindows;
    public Label errorLabel;

    public CommandWindow(Stage stage, String commandName){
        this.stage = stage;
        this.commandName = commandName;
        this.stage.setResizable(false);
        this.stage.setScene(createCommandScene());
    }

    public Scene createCommandScene(){
        Font bigFont = Font.loadFont(getClass().getResourceAsStream("fonts/aktifo.ttf"),20);
        Font simpleFont = Font.loadFont(getClass().getResourceAsStream("fonts/aktifo.ttf"),15);
        String[] argsNames;
        Boolean isHavingModel = false;
        switch (commandName){
            case "Add":
                argsNames= new String[]{};
                isHavingModel = true;
                break;
            case "AddIfMax":
                argsNames= new String[]{};
                isHavingModel = true;
                break;
            case "AddIfMin":
                argsNames= new String[]{};
                isHavingModel = true;
                break;
            case "Update":
                argsNames= new String[]{"ID дракона:"};
                isHavingModel = true;
                break;
            case "RemoveById":
                argsNames= new String[]{"ID дракона:"};
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
            root.getChildren().addAll(argsTitles[i],argsWindows[i]);
        }
        enterModel = new Button("enter_dragon");
        enterModel.setFont(simpleFont);
        enterModel.setAlignment(Pos.TOP_LEFT);
        if(isHavingModel) root.getChildren().add(enterModel);
        executeButton = new Button("execute");
        executeButton.setFont(simpleFont);
        executeButton.setAlignment(Pos.BOTTOM_RIGHT);
        errorLabel = new Label("");
        errorLabel.setFont(simpleFont);
        errorLabel.setTextFill(Color.RED);
        root.getChildren().addAll(errorLabel,executeButton);

        return new Scene(root,WIDTH, HEIGHT);
    }


    public void show(){
        stage.show();
    }

    public void close(){
        stage.close();
    }



}
