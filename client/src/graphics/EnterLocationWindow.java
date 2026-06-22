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
import models.Coordinates;
import models.Location;

public class EnterLocationWindow {
    private Stage stage;
    private final String BG_COLOR = "#2a3950";
    private final int WIDTH = 350;
    private final int HEIGHT = 450;
    private Location value;

    public EnterLocationWindow(Stage stage){
        this.stage = stage;
        stage.setResizable(false);
        stage.setTitle("input Location");
        stage.setScene(createEnterLocationScene());
    }

    public Scene createEnterLocationScene(){
        Font bigFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"),20);
        Font simpleFont = Font.loadFont(getClass().getResourceAsStream("resources/fonts/aktifo.ttf"),15);
        VBox root = new VBox();
        root.setStyle("-fx-background-color: " + BG_COLOR +";");
        Label title = new Label("input Location");
        Label xLabel = new Label("input X");
        Label yLabel = new Label("input Y");
        Label zLabel = new Label("input Z");
        Label nameLabel = new Label("input name");
        title.setFont(bigFont);
        xLabel.setFont(simpleFont);
        yLabel.setFont(simpleFont);
        zLabel.setFont(simpleFont);
        nameLabel.setFont(simpleFont);
        title.setTextFill(Color.WHITE);
        xLabel.setTextFill(Color.WHITE);
        yLabel.setTextFill(Color.WHITE);
        zLabel.setTextFill(Color.WHITE);
        nameLabel.setTextFill(Color.WHITE);
        TextField xInput = new TextField();
        xInput.setFont(simpleFont);
        TextField yInput = new TextField();
        yInput.setFont(simpleFont);
        TextField zInput = new TextField();
        zInput.setFont(simpleFont);
        TextField nameInput = new TextField();
        nameInput.setFont(simpleFont);
        Button executeBtn = new Button("enter");
        executeBtn.setFont(simpleFont);
        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(simpleFont);
        HBox errorBox = new HBox(errorLabel);
        errorBox.setAlignment(Pos.CENTER);
        root.getChildren().addAll(title,xLabel,xInput,yLabel,yInput,zLabel,zInput,nameLabel,nameInput,errorBox,executeBtn);

        xInput.setMaxWidth(125);
        yInput.setMaxWidth(125);
        zInput.setMaxWidth(125);
        nameInput.setMaxWidth(125);

        errorBox.setPadding(new Insets(20,0,0,0));
        VBox.setMargin(title,new Insets(20,0,0,20));
        VBox.setMargin(xLabel,new Insets(20,0,0,20));
        VBox.setMargin(yLabel,new Insets(20,0,0,20));
        VBox.setMargin(zLabel,new Insets(20,0,0,20));
        VBox.setMargin(nameLabel,new Insets(20,0,0,20));

        VBox.setMargin(xInput,new Insets(5,0,0,20));
        VBox.setMargin(yInput,new Insets(5,0,0,20));
        VBox.setMargin(zInput,new Insets(5,0,0,20));
        VBox.setMargin(nameInput,new Insets(5,0,0,20));

        VBox.setMargin(executeBtn,new Insets(20,0,0,250));


        executeBtn.setOnAction(e ->{
            try{
                Integer x = Integer.parseInt(xInput.getText());
                Integer y = Integer.parseInt(yInput.getText());
                Integer z = Integer.parseInt(zInput.getText());
                String name = nameInput.getText().trim();
                Location loc = new Location(x,y,z,name);
                loc.validate();
                value = loc;
                stage.close();
            } catch (Exception ex) {
                errorLabel.setText("error.incorrect_format");
            }
        });
        return new Scene(root,WIDTH,HEIGHT);
    }

    public Location showAndGetLocation(){
        stage.showAndWait();
        return value;
    }
}
