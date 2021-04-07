package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new StackPane(), 640, 400);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/fxml_root.fxml"));
        scene.setRoot(loader.load());
        RootController controller = loader.getController();
        controller.init();

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("Sentiment Analysis");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
