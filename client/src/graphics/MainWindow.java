package graphics;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class MainWindow {
    private Stage stage;
    private final String BG_COLOR = "#2a3950";
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;

    public MainWindow(Stage stage){
        this.stage = stage;
    }

    private Scene createMainScene(){
        Font bigFont = Font.loadFont(getClass().getResourceAsStream("fonts/aktifo.ttf"),20);
        Font simpleFont = Font.loadFont(getClass().getResourceAsStream("fonts/aktifo.ttf"),15);


        BorderPane root = new BorderPane();
        root.setPrefSize(WIDTH,HEIGHT);
        root.setStyle("-fx-background-color: " + BG_COLOR + ";");
        HBox topbar = new HBox();
        Label welcome = new Label("Добро пожаловать, Username!");
        Label textAreaLabel = new Label("Вывод:");
        HBox.setMargin(welcome, new Insets(15,0,0,15));
        HBox.setMargin(textAreaLabel, new Insets(15,0,0,625));
        topbar.getChildren().addAll(welcome,textAreaLabel);
        welcome.setAlignment(Pos.TOP_LEFT);
        textAreaLabel.setAlignment(Pos.TOP_RIGHT);
        welcome.setFont(bigFont);
        welcome.setTextFill(Color.WHITE);
        textAreaLabel.setFont(bigFont);
        textAreaLabel.setTextFill(Color.WHITE);



        VBox userButtonsArea = new VBox(15);
        Button[] userButtons = new Button[10];
        String[] buttonsNames = {"Добавить","Добавить если максимальное", "Добавить если минимальное", "Обновить элемент","Удалить элемент","Очистить", "Удалить первый элемент", "Средний возраст", "Уникальный вес", "Показать коллекцию"};
        for(int i=0;i<10;i++){
            userButtons[i] = new Button(buttonsNames[i]);
            userButtonsArea.getChildren().add(userButtons[i]);
            userButtons[i].setPrefWidth(250);
            userButtons[i].setFont(simpleFont);
        }
        userButtonsArea.setPadding(new Insets(25,0,0,20));

        //здесь админские команды

        var vizArea = createVizArea(600,600,0,100,0,100);
        vizArea.setAlignment(Pos.TOP_LEFT);
        vizArea.setPadding(new Insets(25,0,0,60));

        HBox center = new HBox();
        TextArea textArea = new TextArea();
        center.getChildren().addAll(vizArea,textArea);
        HBox.setMargin(textArea, new Insets(25,0,0,25));
        textArea.setMaxWidth(275);
        textArea.setFont(simpleFont);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setMaxHeight(600);

        root.setLeft(userButtonsArea);
        root.setTop(topbar);
        root.setCenter(center);


        return new Scene(root);
    }

    private StackPane createVizArea(double width, double height, double minx, double maxx, double miny, double maxy){
        Canvas canvas = new Canvas(width,height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,width,height);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.);

        for(int i =1; i<4; i++){
            double x = width/4 * i;
            gc.strokeLine(x,0,x,height);
        }
        for(int i =1; i<4; i++){
            double y = height/4 * i;
            gc.strokeLine(0,y,width,y);
        }
        Pane vizArea = new Pane();
        vizArea.setMaxSize(width, height);
        //vizArea.setStyle("-fx-background-color: white;");

        StackPane gridContainer = new StackPane();
        gridContainer.getChildren().addAll(vizArea, canvas);




        return gridContainer;
    }


    public void show(){
        stage.setScene(createMainScene());
        stage.setResizable(false);
        stage.show();
    }
}
