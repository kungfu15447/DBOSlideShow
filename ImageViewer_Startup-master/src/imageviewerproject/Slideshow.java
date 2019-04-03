/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imageviewerproject;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Frederik Jensen
 */
public class Slideshow implements Runnable
{
    private final List<Image> images;
    private final List<String> imagenames;
    private int currentIndex = 0;
    private final ImageView imageview;
    private final Label label;
    private ExecutorService executor;
    
    public Slideshow(List<Image> images, List<String> imagenames, ImageView imageview,
        Label label) {
        this.images = images;
        this.imagenames = imagenames;
        this.imageview = imageview;
        this.label = label;
    }

    @Override
    public void run()
    {
            try
            {
                for (int i = currentIndex; i < images.size(); i++)
                {
                    currentIndex = i;
                    if (i == images.size() - 1)
                    {
                        i = 0;
                    }
                    Platform.runLater(() ->
                    {
                        imageview.setImage(images.get(currentIndex));
                        label.setText(imagenames.get(currentIndex));
                    });
                    TimeUnit.MILLISECONDS.sleep(1500);

                }
            } catch (InterruptedException ex)
            {
                System.out.println("Slideshow has been stopped");
            }
    }
    
    public synchronized void start() {
        executor = Executors.newSingleThreadExecutor();
        executor.submit(this);
    }
    
    public synchronized void stop() {
        executor.shutdownNow();
    } 
}
