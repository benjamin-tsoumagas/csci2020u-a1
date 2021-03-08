package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("a1.fxml"));
        primaryStage.setTitle("Spam Master 3000");

        //Sets application icon as custom no spam image
        Image icon = new Image("file:no_spam.png");
        primaryStage.getIcons().add(icon);

        sample.Controller.getFileWordCount();
        System.out.println(sample.Controller.hamFreq);
        System.out.println(sample.Controller.spamFreq);
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
