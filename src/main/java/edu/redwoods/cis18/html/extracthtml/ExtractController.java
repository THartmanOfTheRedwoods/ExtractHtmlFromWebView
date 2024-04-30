package edu.redwoods.cis18.html.extracthtml;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import java.net.URL;
import java.util.ResourceBundle;

public class ExtractController implements Initializable {
    @FXML
    private TextField textFieldUrl;

    @FXML
    private TextArea taQuery;

    @FXML
    private TextArea taResult;

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

    private void scriptExecutor(WebEngine webEngine, String script) {
        Object result = webEngine.executeScript(script);
        System.out.println(result.getClass().getTypeName());
        StringBuilder elementHtml;
        if (result instanceof JSObject jsObject) {
            if (jsObject.getMember("length") != null) {
                // It's a list of elements
                elementHtml = new StringBuilder();
                try {
                    int length = Integer.parseInt(jsObject.getMember("length").toString());
                    for (int i = 0; i < length; i++) {
                        JSObject element = (JSObject) jsObject.getSlot(i);
                        elementHtml.append((String) element.getMember("outerHTML"));
                    }
                } catch (NumberFormatException ignoredNfe) {
                    elementHtml = new StringBuilder((String) jsObject.getMember("outerHTML"));
                }
            } else {
                elementHtml = new StringBuilder((String) jsObject.getMember("outerHTML"));
            }
        } else {
            elementHtml = new StringBuilder(result.toString());
        }
        taResult.setText(elementHtml.toString());
    }

    @FXML
    public void onGoButtonClick(ActionEvent ignoredEvent) {
        String uri = textFieldUrl.getText();
        if(isValidURL(uri)) {
            WebEngine webEngine = webView.getEngine();
            webEngine.load(uri);

            // Wait for the page to finish loading
            webEngine.getLoadWorker().stateProperty().addListener((ignoredObs, ignoredOldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    // Execute JavaScript to retrieve element IDs
                    String script = "var ids = [];"
                            + "var elements = document.querySelectorAll('[id]');"
                            + "for (var i = 0; i < elements.length; i++) {"
                            + "    ids.push(elements[i].id);"
                            + "}"
                            + "ids.join(';')";
                    this.scriptExecutor(webEngine, script);
                }
            });
        } else {
            ExtractController.handleError(ExtractApplication.getMainStage());
        }
    }

    @FXML
    public void onQueryButtonClick(ActionEvent ignoredEvent) {
        WebEngine webEngine = webView.getEngine();
        this.scriptExecutor(webEngine, taQuery.getText());
    }
}