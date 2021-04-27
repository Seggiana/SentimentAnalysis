package sample;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class WelcomeController {
    @FXML
    Label lbl_welcome ;
    @FXML
    Image fxml_logo;

    public void init(){
        lbl_welcome.setText("Witam w aplikacji \n W celu nauczenia modelu sieci neuronowej " +
                "i drzewa klasyfikacyjnego należy wybrać zakładkę 'Learning'," +
                " w celu analizy wybranego tekstu nalezy wybrac zakladke 'Training' ");
    }
}