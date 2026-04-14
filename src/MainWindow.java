import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainWindow extends Application {

    ContextMenu contextMenu;
    VBox mainLayout;
    Stage primaryStage;

    Button zeroButton;
    Button oneButton;
    Button twoButton;
    Button threeButton;
    Button fourButton;
    Button fiveButton;
    Button sixButton;
    Button sevenButton;
    Button eightButton;
    Button nineButton;
    Button pointButton;
    Button addButton;
    Button subtractButton;
    Button multiplicationButton;
    Button divisionButton;
    Button calculateButton;
    Button leftButton;
    Button rightButton;
    Button clearButton;
    Button deleteButton;
    Button openingBracketButton;
    Button closingBracketButton;
    Button fractionButton;
    Button rootButton;
    Button exponentButton;
    Button sineButton;
    Button cosineButton;
    Button tangentButton;
    Button logButton;
    Button lnButton;
    Button varButton;
    Button ansButton;
    Button piButton;
    Button eButton;
    Button constButton;
    Button expButton;
    Button tempButton; // Platzhalter
    HBox equationBox;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        equationBox = new HBox();
        equationBox.setMinHeight(130);
        equationBox.setMaxHeight(130);
        equationBox.setMinWidth(377);
        equationBox.setMaxWidth(377);
        equationBox.setPadding(new Insets(10));
        equationBox.getStyleClass().add("calculation_field");

        zeroButton = createNewButton("0", STYLE.LightGray);
        oneButton = createNewButton("1", STYLE.LightGray);
        twoButton = createNewButton("2", STYLE.LightGray);
        threeButton = createNewButton("3", STYLE.LightGray);
        fourButton = createNewButton("4", STYLE.LightGray);
        fiveButton = createNewButton("5", STYLE.LightGray);
        sixButton = createNewButton("6", STYLE.LightGray);
        sevenButton = createNewButton("7", STYLE.LightGray);
        eightButton = createNewButton("8", STYLE.LightGray);
        nineButton = createNewButton("9", STYLE.LightGray);
        pointButton = createNewButton(".", STYLE.LightGray);

        sineButton = createNewButton("sin", STYLE.Gray);
        cosineButton = createNewButton("cos", STYLE.Gray);
        tangentButton = createNewButton("tan", STYLE.Gray);
        logButton = createNewButton("", STYLE.Gray); // Text: log
        lnButton = createNewButton("", STYLE.Gray); // Text: ln
        varButton = createNewButton("", STYLE.Gray); // Text: Var 1
        ansButton = createNewButton("Ans", STYLE.Gray); // Text: Var 2

        constButton = createNewButton("", STYLE.Gray); // Text: CONST
        tempButton = createNewButton("", STYLE.Gray); // Platzhalter

        expButton = createNewButton("", STYLE.LightGray); // Text: EXP

        piButton = createNewButton("\uD835\uDF45", STYLE.Gray);
        eButton = createNewButton("e", STYLE.Gray);

        addButton = createNewButton("+", STYLE.OrangeText);
        subtractButton = createNewButton("-", STYLE.OrangeText);
        multiplicationButton = createNewButton("×", STYLE.OrangeText);
        divisionButton = createNewButton("/", STYLE.OrangeText);

        calculateButton = createNewButton("=", STYLE.Orange);

        leftButton = createNewButton("<", STYLE.Gray);
        rightButton = createNewButton(">", STYLE.Gray);

        clearButton = createNewButton("AC", STYLE.RedText);
        deleteButton = createNewButton("DEL", STYLE.RedText);

        openingBracketButton = createNewButton("(", STYLE.Gray);
        closingBracketButton = createNewButton(")", STYLE.Gray);
        fractionButton = createNewButton("÷", STYLE.Gray);
        rootButton = createNewButton("√", STYLE.Gray);
        exponentButton = createNewButton("x^y", STYLE.Gray);

        GridPane buttonGrid = new GridPane(5, 5);
        buttonGrid.addColumn(0, leftButton, sineButton, varButton, sevenButton, fourButton, oneButton, zeroButton);
        buttonGrid.addColumn(1, rightButton, cosineButton, ansButton, eightButton, fiveButton, twoButton, pointButton);
        buttonGrid.addColumn(2, constButton, tangentButton, tempButton, nineButton, sixButton, threeButton, expButton);
        buttonGrid.addColumn(3, deleteButton, exponentButton, openingBracketButton, divisionButton, multiplicationButton, subtractButton, addButton);
        buttonGrid.addColumn(4, clearButton, rootButton, closingBracketButton, fractionButton, logButton, lnButton, calculateButton);
        buttonGrid.setPadding(new Insets(10, 0, 0, 0));

        contextMenu = new ContextMenu();
        MenuItem closeItem = new MenuItem("✕");
        closeItem.setOnAction(e -> Platform.exit());
        contextMenu.getItems().add(closeItem);
        contextMenu.getStyleClass().add("context_menu");

        mainLayout = new VBox();
        mainLayout.getStyleClass().add("window_background");
        mainLayout.getStyleClass().add("window_all_border");
        mainLayout.getChildren().addAll(equationBox, buttonGrid);

        Scene root = new Scene(mainLayout, Color.TRANSPARENT);
        root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(root);
        primaryStage.setHeight(577);
        primaryStage.setWidth(397);
        primaryStage.setTitle("Timer");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        ControlButtons controlButtons = new ControlButtons(this, new ControlFormula(this));
    }

    public void showContextMenu() {
        contextMenu.show(mainLayout, Side.TOP, 358, 0);
        mainLayout.getStyleClass().remove("window_all_border");
        mainLayout.getStyleClass().add("window_partly_border");
    }

    public void hideContextMenu() {
        contextMenu.hide();
        mainLayout.getStyleClass().add("window_all_border");
        mainLayout.getStyleClass().remove("window_partly_border");
    }

    private Button createNewButton(String text, STYLE style) {
        int buttonWidth = 71;
        int buttonHeight = 55;

        Button button = new Button(text);
        button.getStyleClass().add("standard_button");
        if (style == STYLE.Gray) button.getStyleClass().add("button_gray");
        if (style == STYLE.Orange) button.getStyleClass().add("button_orange");
        if (style == STYLE.OrangeText) button.getStyleClass().add("button_orange_text");
        if (style == STYLE.LightGray) button.getStyleClass().add("button_lightgray");
        if (style == STYLE.RedText) button.getStyleClass().add("button_red_text");
        button.setMinWidth(buttonWidth);
        button.setMaxWidth(buttonWidth);
        button.setMinHeight(buttonHeight);
        button.setMaxHeight(buttonHeight);
        return button;
    }

    public void render(javafx.scene.Node node) {
        equationBox.getChildren().clear();
        equationBox.getChildren().add(node);
    }

    public void clearAll() {
        equationBox.getChildren().clear();
    }

    public void renderResult(String result) {
        Text text = new Text(result);
        text.getStyleClass().add("calculation_result");

        HBox resultWrapper = new HBox(text);
        resultWrapper.setAlignment(Pos.BOTTOM_RIGHT);

        HBox.setHgrow(resultWrapper, Priority.ALWAYS);

        equationBox.getChildren().add(resultWrapper);
    }

    public double[] getPosition() {
        return new double[] {primaryStage.getX(), primaryStage.getY()};
    }

    public void setPosition(double[] position) {
        primaryStage.setX(position[0]);
        primaryStage.setY(position[1]);
    }

    enum STYLE {
        Gray,
        Orange,
        OrangeText,
        LightGray,
        RedText
    }
}
