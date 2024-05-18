module ui.a2enhanced {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens ui.a2enhanced to javafx.fxml;
    exports ui.a2enhanced;
}