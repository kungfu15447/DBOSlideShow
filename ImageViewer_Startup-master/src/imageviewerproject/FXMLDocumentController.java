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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable
{

    private final List<Image> images = new ArrayList<>();
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
            });
            displayImage();
        }
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
    private void handlerStartSlideshow(ActionEvent event)
    {
        Runnable task = () ->
        {
            try
            {
                for (int i = currentImageIndex; i < images.size(); i++)
                {

                    if (i == images.size() - 1)
                    {
                        i = 0;
                    }
                    imageView.setImage(images.get(i));
                    TimeUnit.MILLISECONDS.sleep(2000);

                }
            } catch (InterruptedException ex)
            {
                
            }
        };
        executor = Executors.newSingleThreadExecutor();
        executor.submit(task);
    }

    @FXML
    private void handlerStopSlideshow(ActionEvent event)
    {
        executor.shutdownNow();
    }

}
