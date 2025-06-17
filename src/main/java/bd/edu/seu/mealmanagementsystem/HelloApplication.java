package bd.edu.seu.mealmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1058, 715);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    public static void changeScene(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml+".fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1058, 715);
            stage.setScene(scene);

        }catch (IOException ex){
            ex.printStackTrace();
            System.out.println("Failed to load fxml file");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
