package sample;

import javafx.fxml.FXML;

import javafx.scene.control.Label;

public class WelcomeController {
    @FXML
    Label lbl_welcome = new Label("Witam w aplikacji \n W celu nauczenia modelu sieci neuronowej " +
            "i drzewa klasyfikacyjnego należy wybrać zakładkę 'Learning'," +
            " w celu analizy wybranego tekstu nalezy wybrac zakladke 'Training' ");
}