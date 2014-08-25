import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Created by Patrick on 8/8/14.
 */
public class Form extends Application {

    public void start(final Stage primaryStage) {
        final Pane canvas = new Pane();
        canvas.setPrefSize(600,600);
        final GridPane gridPane = new GridPane();
        Button loadFile = new Button("Load Files");
        final FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Select .cnv file(s)","*.cnv");
        fileChooser.getExtensionFilters().add(txtFilter);
        loadFile.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        System.out.println("Loading...");
                        List<File> fileList = fileChooser.showOpenMultipleDialog(primaryStage);
                        if (fileList != null) {
                            for (int i = 0; i < fileList.size(); i++) {
                                System.out.println(fileList.get(i).getAbsoluteFile());

                            }
                        }
                    }
                }
        );

        loadFile.relocate(100,100);
        canvas.getChildren().addAll(loadFile);
        Scene scene = new Scene(canvas,Color.GREY);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


//    public static void main(String[] args) {
//        String[] fileNames = {"6370_2014_07_14_cast52_bin.cnv"};
//        ReadText readText = new ReadText(fileNames);
//        readText.loadFile();
//    }

}
