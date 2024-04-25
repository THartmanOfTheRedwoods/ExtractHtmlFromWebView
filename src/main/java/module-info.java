module edu.redwoods.cis18.html.extracthtml {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens edu.redwoods.cis18.html.extracthtml to javafx.fxml;
    exports edu.redwoods.cis18.html.extracthtml;
}