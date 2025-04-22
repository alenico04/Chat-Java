package client.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.geometry.Rectangle2D;

public class ImageUtils {
    public static StackPane createCircularImage(String imageUrl, double size) {
        StackPane container = new StackPane();
        ImageView imageView = new ImageView();
        
        // Load image
        Image image = new Image(imageUrl, true);
        
        // Configure container
        container.setPrefSize(size, size);
        container.setMaxSize(size, size);
        container.setMinSize(size, size);
        container.setStyle("-fx-background-radius: 50%; -fx-background-color: white;");
        
        // Wait for image to load
        image.progressProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == 1.0) {
                double width = image.getWidth();
                double height = image.getHeight();
                
                // Calculate square crop dimensions
                double cropSize = Math.min(width, height);
                double x = (width - cropSize) / 2;
                double y = (height - cropSize) / 2;
                
                // Set viewport for square crop
                Rectangle2D viewport = new Rectangle2D(x, y, cropSize, cropSize);
                imageView.setImage(image);
                imageView.setViewport(viewport);
                
                // Set size
                imageView.setFitWidth(size);
                imageView.setFitHeight(size);
                imageView.setPreserveRatio(true);
                
                // Create circular clip
                Circle clip = new Circle(size / 2);
                clip.setCenterX(size / 2);
                clip.setCenterY(size / 2);
                imageView.setClip(clip);
                
                container.getChildren().add(imageView);
            }
        });
        
        return container;
    }
}