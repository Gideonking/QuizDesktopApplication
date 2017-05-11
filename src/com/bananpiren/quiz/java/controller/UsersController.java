package com.bananpiren.quiz.java.controller;

import com.bananpiren.quiz.Entity.User;
import com.bananpiren.quiz.Services.FindUserService;
import com.bananpiren.quiz.java.view.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.IOException;

public class UsersController {

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label mailLabel;

    @FXML
    private Label userLevelLabel;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    @FXML
    private TableColumn<User, String> levelColumn;

    @FXML
    private Button editButton;

    @FXML
    private TableView<User> personTable;

    @FXML
    private Button deleteButton;

    @FXML
    private Button addButton;

    final ObservableList<User> data = FXCollections.observableArrayList();

    public UsersController() {
        // TODO: Load data to table
        FindUserService findUserService = new FindUserService();
        data.addAll(findUserService.findAllUsers());

//        personTable.setItems(data);

        for (User u : data){
            System.out.println(u.getFirstName() + u.getLastName() + u.getAccountLevel());
        }
    }

    @FXML
    private void initialize() {
        // Setting data to right column "cellvalue"
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<User, String>("accountLevel"));
        personTable.setItems(data);

        // Get tableselection and setting labels
        personTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(ObservableValue<? extends User> observable, User oldValue, User newValue) {
                if(personTable.getSelectionModel().getSelectedItem() != null) {
                    editButton.setDisable(false);
                    firstNameLabel.setText("" + firstNameColumn.getCellData(newValue));
                    lastNameLabel.setText("" + lastNameColumn.getCellData(newValue));
                    mailLabel.setText("Finns ej");
                    userLevelLabel.setText("" + levelColumn.getCellData(newValue));
                }
            }
        });

        addButton.setOnAction(e -> showPersonAddDialog());

        editButton.setOnAction(e -> showPersonEditDialog());

        deleteButton.setOnAction(e -> handleDeletePerson());
    }

    public void showPersonAddDialog() {
        try {
            // Load FXML file to dialog stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("PersonAddDialog.fxml"));
            BorderPane page = loader.load();

            // Create the dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Lägg till ny användare");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //TODO: Skicka med vald anvädnare till lägga till scenen
            // Set the person into the controller.
//            PersonEditDialogController controller = loader.getController();
//            controller.setDialogStage(dialogStage);
//            controller.setPerson(user);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPersonEditDialog() {
        try {
            // Load FXML file to dialog stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("PersonEditDialog.fxml"));
            BorderPane page = loader.load();

            // Create the dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Redigera användare");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //TODO: Skicka med vald anvädnare till redigera scenen
            // Set the person into the controller.
//            PersonEditDialogController controller = loader.getController();
//            controller.setDialogStage(dialogStage);
//            controller.setPerson(user);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeletePerson() {

        if(personTable.getSelectionModel().getSelectedItem() != null) {
            int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
            personTable.getItems().remove(selectedIndex);
            //TODO: Delete in database
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Inget valt");
            alert.setHeaderText("Ingen person är vald");
            alert.setContentText("För att ta bort, välj en person");

            alert.showAndWait();
        }
    }

    private void showPersonDetails(User user) {
        if (user != null) {
            // TODO: Get data from JPA person

            // Fill the labels with info from the person object.
            firstNameLabel.setText(user.getFirstName());
            lastNameLabel.setText(user.getLastName());
            mailLabel.setText(user.getEmail());
            userLevelLabel.setText(user.getAccountLevel());
        } else {
            // Person is null, remove all the text.
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            mailLabel.setText("");
            userLevelLabel.setText("");
        }
    }
}
