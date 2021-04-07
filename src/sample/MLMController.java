package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class MLMController {
    @FXML
    private Label lbl_result;

    @FXML
    private TextArea txtA_ToAnalyse;

    public void handleSubmit() {
        lbl_result.setText("Resulted");
        System.out.println(txtA_ToAnalyse.textProperty().getValue());
    }


}
