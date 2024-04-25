package edu.redwoods.cis18.html.extracthtml;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private TextField textFieldUrl;

    @FXML
    private TextField textFieldQuery;

    @FXML
    private WebView webView;

    @FXML
    protected void HelloController() {
        // Called before controls are mapped, this is where you can get things like config/communication
        // singletons.
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // This is where you can initialize controls as they have been mapped via fx:id at this point.
    }

    private static void handleError(Stage primaryStage) {
        // Create an alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid URL");
        alert.setHeaderText(null);
        alert.setContentText("You entered an invalid URL. Kindly stop that.");

        // Make the alert modal
        alert.initOwner(primaryStage);
        alert.initModality(Modality.APPLICATION_MODAL);

        // Show the alert and wait for user interaction
        alert.showAndWait();
    }

    private boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void scriptExecutor(WebEngine webEngine, String script) {
        String result = (String)webEngine.executeScript(script);
        System.out.printf("RESULT: %s%n", result);
    }

    @FXML
    public void onGoButtonClick(ActionEvent ignoredEvent) {
        String uri = textFieldUrl.getText();
        if(isValidURL(uri)) {
            WebEngine webEngine = webView.getEngine();
            webEngine.load(uri);

            // Wait for the page to finish loading
            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    // Execute JavaScript to retrieve element IDs
                    String script = "var ids = [];"
                            + "var elements = document.querySelectorAll('[id]');"
                            + "for (var i = 0; i < elements.length; i++) {"
                            + "    ids.push(elements[i].id);"
                            + "}"
                            + "ids.join(';')";
                    HelloController.scriptExecutor(webEngine, script);
                }
            });
        } else {
            HelloController.handleError(HelloApplication.getMainStage());
        }
    }

    @FXML
    public void onQueryButtonClick(ActionEvent ignoredEvent) {
        WebEngine webEngine = webView.getEngine();
        HelloController.scriptExecutor(webEngine, textFieldQuery.getText());
    }
}