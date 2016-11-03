package com.azure.digitrecognition;

import java.io.File;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

	private TextField field;
	private TextField num;
	private TextArea summary;
	private TextArea matrix;
	private Button newIn;
	private Button datatrain;
	private Button start;
	private Group root;
	private FileChooser chooser;
	private ComboBox algorithm;
	private Label sumLabel;
	private Label matLabel;

	private BuildModel build;
	private PixelGrabber grab;
	
	private String dir;
	private int clss;
	
	private void initialize() throws Exception {
		build = new BuildModel();
		build.createModel();
		
		root = new Group();
		chooser = new FileChooser();

		ObservableList<String> options = FXCollections.observableArrayList("J48", "IBk", "Naive Bayes");

		field = new TextField();
		field.setPrefWidth(210);
		field.setPromptText("Browse image...");
		field.setLayoutX(10);
		field.setLayoutY(10);

		num = new TextField();
		num.setPrefWidth(43);
		num.setPromptText("Class");
		num.setLayoutX(225);
		num.setLayoutY(10);
		
		algorithm = new ComboBox(options);
		algorithm.setPrefSize(96, 10);
		algorithm.setLayoutX(10);
		algorithm.setLayoutY(40);
		algorithm.setPromptText("Algorithm");

		newIn = new Button("New Instance");
		newIn.setLayoutX(273);
		newIn.setLayoutY(10);

		summary = new TextArea();
		summary.setLayoutX(10);
		summary.setLayoutY(70);
		summary.setPrefSize(310, 250);
		summary.setEditable(false);

		sumLabel = new Label("Summary");
		sumLabel.setLayoutX(10);
		sumLabel.setLayoutY(320);

		matrix = new TextArea();
		matrix.setLayoutX(330);
		matrix.setLayoutY(70);
		matrix.setPrefSize(210, 250);
		matrix.setEditable(false);

		matLabel = new Label("Confusion Matrix");
		matLabel.setLayoutX(330);
		matLabel.setLayoutY(320);

		start = new Button("Start");
		start.setPrefSize(65, newIn.getHeight());
		start.setLayoutX(155);
		start.setLayoutY(40);

		root.getChildren().addAll(field, num, newIn, summary, sumLabel, matrix, matLabel, algorithm, start);
	}

	private void setOnAction() {
		field.setOnAction(e -> {
			System.out.println(field.getText());
		});

		start.setOnAction(e -> {
			try {
				if (algorithm.getSelectionModel().getSelectedItem().toString().equals("J48")) {
					clss = Integer.parseInt(num.getText());
					grab = new PixelGrabber(dir, clss);
					grab.createArff();
					build.usingJ48();
					summary.setText(build.getJ48Summary());
					matrix.setText(build.getMatJ48());
				}

				if (algorithm.getSelectionModel().getSelectedItem().toString().equals("IBk")) {
					clss = Integer.parseInt(num.getText());
					grab = new PixelGrabber(dir, clss);
					grab.createArff();
					build.usingIBk();
					summary.setText(build.getIBkSummary());
					matrix.setText(build.getMatIBk());

				}
				
				if (algorithm.getSelectionModel().getSelectedItem().toString().equals("Naive Bayes")) {
					clss = Integer.parseInt(num.getText());
					grab = new PixelGrabber(dir, clss);
					grab.createArff();
					build.usingNaiveBayes();
					summary.setText(build.getNBSummary());
					matrix.setText(build.getMatNB());

				}
			} catch (Exception e1) {
				e1.printStackTrace();
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warning");
				alert.setHeaderText(null);
				alert.setContentText("You still do not choose the algorithm or the file is not .arff!");
				alert.showAndWait();
			}
		});
	}

	@Override
	public void start(Stage stage) throws Exception {
		initialize();

		Scene scene = new Scene(root, 540, 340);
		scene.setFill(Color.ALICEBLUE);
		stage.setTitle("Digit Recognition");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();

		newIn.setOnAction(e -> {
			File file = chooser.showOpenDialog(stage);
			dir = file.getAbsolutePath();
			field.setText(dir);
		});

		setOnAction();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
