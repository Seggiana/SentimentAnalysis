package sample.WelcomePage;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WelcomeController {
    @FXML
    Label lbl_promote;
    @FXML
    Label lbl_institute;
    @FXML
    Label lbl_year;
    @FXML
    Label lbl_sect;
    @FXML
    Label lbl_welcome;
    @FXML
    Label lbl_instructions;
    @FXML
    Label lbl_creator;
    @FXML
    Label lbl_university;
    @FXML
    Image image_logo;
    @FXML
    ImageView image_logoView;

    public void init() throws IOException {
        lbl_welcome.setText("Witam w aplikacji!");
        lbl_welcome.setFont(new Font("Times New Roman", 22));
        lbl_instructions.setText("Aplikacja powstała w ramach pracy \"Narzędzie programowe do analizy sentymentu\"." +
                " \n\n•Metoda słownikowa znajduje się w zakładce \"Metoda słownikowa\"." +
                " \n\n•Metoda Sentiment Treebank znajduje się w zakładce \"Metoda Sentiment Treebank\"." +
                " \n\n•Naiwny klasyfikator Bayesa znajduje się w zakładce \"Klasyfikator Bayesa \"." +
                " \n\n•Metoda sieci neuronowej znajduje się w zakładce \"Sieć neuronowa \"." +
                " \n\n•Testowanie metod dla własnego przykładu znajduje się w zakładce \"Testowanie\" ");
        lbl_instructions.setWrapText(true);
        lbl_instructions.setFont(new Font("Times New Roman", 14));
        Font uni = new Font("Times New Roman", 12);
        lbl_creator.setFont(uni);
        lbl_promote.setFont(uni);
        lbl_university.setFont(uni);
        lbl_institute.setFont(uni);
        lbl_year.setFont(uni);
        lbl_sect.setFont(uni);
        BufferedImage bufferedImage;
        bufferedImage = ImageIO.read(new File("C:\\Users\\Toshiba\\Desktop\\SEMESTR 10\\MGR\\SentimentAnalysis\\Logo.png"));
        Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        image_logoView.setImage(image);
    }
}