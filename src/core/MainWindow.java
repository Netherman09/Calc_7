package core;

import calculation.ControlFormula;
import controller.ControlButtons;
import controller.KeyController;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import nodes.FractionNode;

import java.util.ArrayList;
import java.util.List;

public class MainWindow extends Application {

    ContextMenu contextMenu;
    public VBox mainLayout;
    public StackPane rootStack;
    public Stage primaryStage;

    public Button zeroButton;
    public Button oneButton;
    public Button twoButton;
    public Button threeButton;
    public Button fourButton;
    public Button fiveButton;
    public Button sixButton;
    public Button sevenButton;
    public Button eightButton;
    public Button nineButton;
    public Button pointButton;
    public Button addButton;
    public Button subtractButton;
    public Button multiplicationButton;
    public Button divisionButton;
    public Button calculateButton;
    public Button leftButton;
    public Button rightButton;
    public Button clearButton;
    public Button deleteButton;
    public Button openingBracketButton;
    public Button closingBracketButton;
    public Button fractionButton;
    public Button rootButton;
    public Button exponentButton;
    public Button sineButton;
    public Button cosineButton;
    public Button tangentButton;
    public Button logButton;
    public Button lnButton;
    public Button varButton;
    public Button ansButton;
    public Button piButton;
    public Button eButton;
    public Button historyButton;
    public Button expButton;
    public Button shiftButton;
    StackPane equationHolder;
    ScrollPane equationBox;
    HBox resultBox;
    public Button topmostButton;
    double offset = 0;

    List<Button> shiftButtons = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        equationBox = new ScrollPane();
        equationBox.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        equationBox.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        equationBox.setPannable(true);
        StackPane.setAlignment(equationBox, Pos.TOP_LEFT);
        resultBox = new HBox();
        StackPane.setAlignment(resultBox, Pos.BOTTOM_RIGHT);
        equationHolder = new StackPane(resultBox, equationBox);
        equationHolder.setMinHeight(130);
        equationHolder.setMaxHeight(130);
        equationHolder.setMinWidth(377);
        equationHolder.setMaxWidth(377);
        equationHolder.setPadding(new Insets(10));
        equationHolder.getStyleClass().add("calculation_field");

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

        sineButton = createNewButton("sin", "asin", STYLE.Gray);
        cosineButton = createNewButton("cos", "acos", STYLE.Gray);
        tangentButton = createNewButton("tan", "atan", STYLE.Gray);
        logButton = createNewButton("log", "!", STYLE.Gray);
        lnButton = createNewButton("ln", STYLE.Gray);
        varButton = createNewButton("Var", STYLE.Gray); // Text: Var 1
        ansButton = createNewButton("Ans", STYLE.Gray); // Text: Var 2

        historyButton = createNewButton("", STYLE.Gray); // Text: CONST
        shiftButton = createNewButton("Shift", STYLE.Gray);

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
        buttonGrid.addColumn(0, shiftButton, sineButton, historyButton, sevenButton, fourButton, oneButton, zeroButton);
        buttonGrid.addColumn(1, leftButton, cosineButton, logButton, eightButton, fiveButton, twoButton, pointButton);
        buttonGrid.addColumn(2, rightButton, tangentButton, lnButton, nineButton, sixButton, threeButton, expButton);
        buttonGrid.addColumn(3, deleteButton, exponentButton, openingBracketButton, divisionButton, multiplicationButton, subtractButton, addButton);
        buttonGrid.addColumn(4, clearButton, rootButton, closingBracketButton, fractionButton, varButton, ansButton, calculateButton);
        buttonGrid.setPadding(new Insets(10, 0, 0, 0));

        topmostButton = new Button();
        topmostButton.getStyleClass().add("button_topmost");
        topmostButton.getStyleClass().add("button_gray");
        topmostButton.setMaxSize(6, 6);
        topmostButton.setMinSize(6, 6);
        StackPane.setAlignment(topmostButton, Pos.TOP_LEFT);
        StackPane.setMargin(topmostButton, new Insets(-5, 0, 0, -5));

        contextMenu = new ContextMenu();
        MenuItem closeItem = new MenuItem("Exit");
        closeItem.setOnAction(e -> closeWithStyle(primaryStage));
        contextMenu.getItems().add(closeItem);
        contextMenu.getStyleClass().add("context_menu");

        mainLayout = new VBox();
        mainLayout.getChildren().addAll(equationHolder, buttonGrid);

        VBox slidingBox = createSlidingPanel();

        rootStack = new StackPane(mainLayout, slidingBox, topmostButton);
        rootStack.getStyleClass().add("window_background");

        Scene root = new Scene(rootStack, Color.TRANSPARENT);
        root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setScene(root);
        primaryStage.setHeight(600);
        primaryStage.setWidth(397);
        primaryStage.setTitle("Timer");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();

        // In der start-Methode, nachdem die Stage-Größe feststeht:
        Platform.runLater(() -> {
            Rectangle clip = new Rectangle();
            clip.widthProperty().bind(rootStack.widthProperty());
            clip.heightProperty().bind(rootStack.heightProperty());

            // Diese Werte müssen exakt deinen abgerundeten Ecken im CSS entsprechen (z.B. 20px)
            clip.setArcWidth(24);
            clip.setArcHeight(24);

            rootStack.setClip(clip);
        });
        ControlFormula controlFormula = new ControlFormula(this);
        ControlButtons controlButtons = new ControlButtons(this, controlFormula);
        KeyController keyController = new KeyController(root, this, controlFormula);
    }

    public void closeWithStyle(Stage stage) {
        FadeTransition ft = new FadeTransition(Duration.millis(200), stage.getScene().getRoot());
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> Platform.exit());
        ft.play();
    }

    public void showContextMenu(double x, double y) {
        contextMenu.show(rootStack, x, y);
    }

    public void hideContextMenu() {
        contextMenu.hide();
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

    private Button createNewButton(String text, String shiftText, STYLE style) {
        int buttonWidth = 71;
        int buttonHeight = 55;

        Label buttonLabel = new Label(text);
        Label shiftLabel = new Label(shiftText);

        buttonLabel.setPadding(new Insets(16, 0, 0,0));
        buttonLabel.setStyle("-fx-text-fill: white; -fx-font-size: 19px; -fx-font-weight: bold;");
        shiftLabel.setPadding(new Insets(-6, 0, 0,0));
        shiftLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 13px; -fx-font-weight: bold;");

        buttonLabel.setId(text);
        shiftLabel.setId(shiftText);

        VBox content = new VBox(0);
        content.getChildren().addAll(buttonLabel, shiftLabel);
        content.setMinWidth(buttonWidth);
        content.setMaxWidth(buttonWidth);
        content.setMinHeight(buttonHeight);
        content.setMaxHeight(buttonHeight);

        content.setAlignment(Pos.CENTER);

        Button button = new Button();
        button.setGraphic(content);
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


        shiftButtons.add(button);
        return button;
    }

    public void toggleButtonText(boolean active) {
        shiftButton.getStyleClass().removeLast();
        if (active) shiftButton.getStyleClass().add("button_lightgray");
        if (!active) shiftButton.getStyleClass().add("button_gray");
        for (Button button : shiftButtons) {
            VBox content = ((VBox)button.getGraphic());
            String mainText = content.getChildren().get(0).getId();
            String shiftText = content.getChildren().get(1).getId();
            content.getChildren().get(0).setId(shiftText);
            ((Label)content.getChildren().get(0)).setText(shiftText);

            content.getChildren().get(1).setId(mainText);
            ((Label)content.getChildren().get(1)).setText(mainText);
        }
    }

    public void render(javafx.scene.Node node) {
        equationBox.getContent();
        equationBox.setFitToHeight(true);
        equationBox.setContent(node);
        equationBox.setHvalue(1);
    }

    public void clearAll() {
        equationBox.setContent(null);
        resultBox.getChildren().clear();
    }

    public void clearResult() {
        resultBox.getChildren().clear();
    }

    public void renderResult(String result) {
        Text text = new Text(result);
        text.getStyleClass().add("calculation_result");

        HBox resultWrapper = new HBox(text);
        resultWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        resultWrapper.setPadding(new  Insets(0, 0, -6, 0));

        HBox.setHgrow(resultWrapper, Priority.ALWAYS);

        clearResult();
        resultBox.getChildren().add(resultWrapper);
    }

    public void renderFractionResult(double[] result) {
        Text nominatorText =  new Text(String.valueOf((int)result[0]));
        Text denominatorText =  new Text(String.valueOf((int)result[1]));

        nominatorText.getStyleClass().add("calculation_result");
        denominatorText.getStyleClass().add("calculation_result");

        javafx.scene.shape.Line line = new javafx.scene.shape.Line(0, 0, 30, 0);
        line.setStroke(Color.WHITE);

        VBox resultWrapper = new VBox(nominatorText, line, denominatorText);

        resultWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        resultWrapper.setPadding(new  Insets(0, 0, -6, 0));

        HBox.setHgrow(resultWrapper, Priority.ALWAYS);

        clearResult();
        resultBox.getChildren().add(resultWrapper);
    }

    public void increaseOffset() {
        System.out.println("Increase offset");
        offset += 4;
        equationBox.setPadding(new Insets(0, 0, 0, offset));
    }

    public void decreaseOffset() {
        System.out.println("Decrease offset");
        offset -= 4;
        equationBox.setPadding(new Insets(0, 0, 0, offset));
    }

    public double[] getPosition() {
        return new double[] {primaryStage.getX(), primaryStage.getY()};
    }

    public void setPosition(double[] position) {
        primaryStage.setX(position[0]);
        primaryStage.setY(position[1]);
    }

    private double mouseAnchorY;
    private double lastTranslateY;

    private VBox createSlidingPanel() {
        VBox panel = new VBox();
        panel.getStyleClass().add("sliding_panel");
        panel.setMaxHeight(350); // Maximale Höhe (ca. die Hälfte des Fensters)
        panel.setPrefHeight(350);
        panel.setPrefWidth(397);
        panel.setMinWidth(397);

        // 1. Der "Griff" (Die Linie zum Ziehen)
        Region handle = new Region();
        handle.setPrefSize(40, 5);
        handle.setMaxSize(40, 5);
        handle.getStyleClass().add("pull_handle");

        HBox handleContainer = new HBox(handle);
        handleContainer.setAlignment(Pos.CENTER);
        handleContainer.setPadding(new Insets(10));
        handleContainer.setCursor(javafx.scene.Cursor.HAND);

        // Button Grid
        GridPane buttonGrid = new GridPane(5, 5);
        buttonGrid.setPadding(new Insets(10, 10, 0, 10));
        buttonGrid.addColumn(0, piButton);
        buttonGrid.addColumn(1, eButton);

        panel.getChildren().addAll(handleContainer, buttonGrid);

        double closedY = 450;
        double openedY = 125;
        panel.setTranslateY(closedY);

        handleContainer.setOnMousePressed(e -> {
            mouseAnchorY = e.getScreenY();
            lastTranslateY = panel.getTranslateY();
        });

        handleContainer.setOnMouseDragged(e -> {
            double deltaY = e.getScreenY() - mouseAnchorY;
            double newTranslateY = lastTranslateY + deltaY;

            // Begrenzung: Nicht höher als openedY und nicht tiefer als closedY
            if (newTranslateY >= openedY && newTranslateY <= closedY) {
                panel.setTranslateY(newTranslateY);
            }
        });

        handleContainer.setOnMouseReleased(e -> {
            double deltaY = e.getScreenY() - mouseAnchorY;
            System.out.println(deltaY);
            double currentY = panel.getTranslateY();
            double targetY;

            // Logik: Wenn mehr als 15% hochgezogen wurde, schnappt es ganz auf
            if (deltaY < 0) {
                targetY = openedY;
            } else {
                targetY = closedY;
            }

            // Die Animation erstellen
            TranslateTransition transition = new TranslateTransition(Duration.millis(150), panel);
            transition.setToY(targetY);

            // Interpolator.EASE_BOTH sorgt für sanftes Beschleunigen und Abbremsen
            transition.setInterpolator(Interpolator.EASE_BOTH);

            transition.play();
        });

        handleContainer.setOnMouseClicked(e -> {
            double deltaY = e.getScreenY() - mouseAnchorY;
            System.out.println("Klick " + deltaY + " " + e.getScreenY() + " " + mouseAnchorY);
            if (Math.abs(deltaY) > 10) return;
            System.out.println("Klick");
            double currentY = panel.getTranslateY();
            double targetY = 450;

            if (currentY == closedY) targetY = openedY;
            if (currentY == openedY) targetY = closedY;

            // Die Animation erstellen
            TranslateTransition transition = new TranslateTransition(Duration.millis(150), panel);
            transition.setToY(targetY);

            // Interpolator.EASE_BOTH sorgt für sanftes Beschleunigen und Abbremsen
            transition.setInterpolator(Interpolator.EASE_BOTH);

            transition.play();
        });

        return panel;
    }

    enum STYLE {
        Gray,
        Orange,
        OrangeText,
        LightGray,
        RedText
    }
}
