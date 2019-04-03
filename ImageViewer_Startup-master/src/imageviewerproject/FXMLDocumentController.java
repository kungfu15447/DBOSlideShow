package imageviewerproject;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable
{

    Slideshow show;
    private final List<Image> images = new ArrayList<>();
    private final List<String> imageNames = new ArrayList<>();
    private int currentImageIndex = 0;
    private ExecutorService executor;
    @FXML
    Parent root;

    @FXML
    private Button btnLoad;

    @FXML
    private Button btnPrevious;

    @FXML
    private Button btnNext;

    @FXML
    private ImageView imageView;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private Label lblMessage;

    private void handleBtnLoadAction(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image files");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Images",
                "*.png", "*.jpg", "*.gif", "*.tif", "*.bmp"));
        List<File> files = fileChooser.showOpenMultipleDialog(new Stage());

        if (!files.isEmpty())
        {
            files.forEach((File f) ->
            {
                images.add(new Image(f.toURI().toString()));
                imageNames.add(f.getName());
            });
            displayImage();
        }
        show = new Slideshow(images, imageNames, imageView, lblMessage);
    }

    private void handleBtnPreviousAction(ActionEvent event)
    {
        if (!images.isEmpty())
        {
            currentImageIndex
                    = (currentImageIndex - 1 + images.size()) % images.size();
            displayImage();
        }
    }

    private void handleBtnNextAction(ActionEvent event)
    {
        if (!images.isEmpty())
        {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            displayImage();

        }
    }

    private void displayImage()
    {
        if (!images.isEmpty())
        {
            imageView.setImage(images.get(currentImageIndex));
            lblMessage.setText(imageNames.get(currentImageIndex));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        btnLoad.setOnAction((ActionEvent event) ->
        {
            handleBtnLoadAction(event);
        });

        btnPrevious.setOnAction((ActionEvent event) ->
        {
            handleBtnPreviousAction(event);
        });

        btnNext.setOnAction((ActionEvent event) ->
        {
            handleBtnNextAction(event);
        });
    }

    @FXML
    private synchronized void handlerStartSlideshow(ActionEvent event)
    {
        show.start();
    }

    @FXML
    private synchronized void handlerStopSlideshow(ActionEvent event)
    {
        show.stop();
    }
}
