package client.addProfilePicture;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import javafx.scene.layout.StackPane;
import client.utils.ImageUtils;

public class ProfilePictureSelect {
    private final Stage mainStage;
    private Stage pictureStage;
    private final GaussianBlur blur;
    private VBox dialogContent;
    private String selectedImageUrl;
    private List<StackPane> imageViews;  // Change type from ImageView to StackPane
    private Button continueButton;
    private static final int IMAGES_PER_ROW = 5;
    private boolean showingMemes = false;
    private Button switchButton;
    private ScrollPane imageScrollPane;
    
    private static final List<String> DEFAULT_IMAGES = List.of(
        "https://static.vecteezy.com/system/resources/thumbnails/036/324/708/small_2x/ai-generated-picture-of-a-tiger-walking-in-the-forest-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/022/433/835/small_2x/lamp-and-a-picture-frame-are-on-ramadan-ai-generated-free-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/051/547/293/small_2x/cute-korean-girl-with-long-hair-smiling-and-holding-a-canon-eos-m-camera-in-her-hand-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/039/227/820/small_2x/ai-generated-a-young-woman-poses-an-american-street-market-american-street-markets-photo.jpeg",
        "https://static.vecteezy.com/system/resources/thumbnails/050/078/507/small_2x/majestic-lion-in-a-suit-and-tie-posing-for-a-business-picture-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/040/710/818/small_2x/birdgraphy-bird-picture-most-beautiful-birdgraphy-naturegraphy-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/036/223/135/small_2x/ai-generated-white-cat-with-a-retro-camera-on-a-city-street-in-sunny-day-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/052/755/129/small_2x/realistic-red-apple-picture-with-white-background-free-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/056/369/002/small_2x/serene-mountain-lake-reflection-a-picturesque-nature-escape-photo.jpeg",
        "https://static.vecteezy.com/system/resources/thumbnails/059/994/062/small_2x/vintage-bicycle-with-a-basket-full-of-colorful-flowers-against-a-sunset-sky-photo.jpeg",
        "https://static.vecteezy.com/system/resources/thumbnails/053/193/636/small_2x/a-person-taking-a-picture-with-snow-free-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/051/103/448/small_2x/a-beautiful-arrangement-of-pink-cherry-blossoms-sits-gracefully-in-a-glass-vase-free-photo.jpeg",
        "https://static.vecteezy.com/system/resources/thumbnails/023/182/198/small_2x/cute-hamster-with-a-camera-on-the-background-of-the-nature-ai-generative-image-free-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/050/507/255/small_2x/single-pink-cosmos-flower-with-soft-background-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/046/609/861/small_2x/calm-waters-mirror-the-tranquility-of-the-mind-photo.jpeg",
        "https://static.vecteezy.com/system/resources/thumbnails/054/419/707/small_2x/seagull-perched-on-a-rock-by-a-tranquil-lake-surrounded-by-autumn-colored-trees-free-photo.jpeg",
        "https://static.vecteezy.com/system/resources/thumbnails/039/324/019/small_2x/ai-generated-misty-morning-background-image-generative-ai-free-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/053/707/915/small_2x/a-clear-pyramid-with-a-flower-inside-on-the-ground-photo.jpeg",
        "https://static.vecteezy.com/system/resources/thumbnails/056/868/277/small_2x/elegant-male-waiter-in-sophisticated-restaurant-portrait-image-man-in-classic-black-vest-and-bow-tie-picturerealisticgraphy-fine-dining-occupation-concept-realistic-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/060/036/421/small_2x/standing-and-smiling-woman-in-white-coat-is-holding-ultrasound-picture-of-baby-photo.JPG",
        "https://static.vecteezy.com/system/resources/thumbnails/059/998/922/small_2x/mountain-landscape-picture-waterfall-flowing-down-cliffs-yellow-grass-hills-under-a-blue-and-cloudy-sky-nature-scene-from-a-valley-with-rocky-mountains-photo.jpeg",
        "https://static.vecteezy.com/system/resources/thumbnails/059/811/532/small_2x/a-modern-living-room-with-a-yellow-couch-and-a-large-orange-framed-picture-free-photo.jpeg",
        "https://static.vecteezy.com/system/resources/thumbnails/059/460/314/small_2x/easter-eggs-and-tulips-framing-blank-picture-frame-on-wooden-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/059/179/714/small_2x/joyful-sheiks-taking-a-selfie-picture-together-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/059/470/215/small_2x/evening-bicycle-ride-picture-free-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/059/157/852/small_2x/a-man-in-a-white-lab-coat-is-smiling-and-holding-a-stethoscope-concept-of-professionalism-and-confidence-as-the-man-is-posing-for-a-picture-while-wearing-his-medical-attire-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/057/123/516/small_2x/a-dog-wagging-its-tail-and-smiling-while-playing-outside-in-the-grass-a-true-picture-of-pure-happiness-free-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/054/873/231/small_2x/the-picture-features-a-selection-of-ingredients-often-used-in-italian-cooking-spaghetti-tied-with-a-red-ribbon-ripe-tomatoes-free-photo.jpg"
    );

    private static final List<String> MEME_IMAGES = List.of(
        "https://static.wikia.nocookie.net/brainrotnew/images/e/e3/Bombini_Gusini.jpg/revision/latest/scale-to-width-down/536?cb=20250416185048",
        "https://static.wikia.nocookie.net/brainrotnew/images/a/ac/Tralalero_tralala.jpg/revision/latest/scale-to-width/360?cb=20250321131418",
        "https://static.wikia.nocookie.net/brainrotnew/images/d/df/Anomali_tung_tung_tung.png/revision/latest/thumbnail/width/720/height/900?cb=20250417061140",
        "https://static.wikia.nocookie.net/brainrotnew/images/f/f1/Lirili_rili_ralila.png/revision/latest/thumbnail/width/720/height/900?cb=20250324152453",
        "https://static.wikia.nocookie.net/brainrotnew/images/6/62/Trippi_Troppi2.webp/revision/latest/scale-to-width/360?cb=20250408115944",
        "https://static.wikia.nocookie.net/brainrotnew/images/1/14/640.jpg/revision/latest/scale-to-width/360?cb=20250416053733",
        "https://static.wikia.nocookie.net/brainrotnew/images/f/f7/Brr_Brr_Patapim.png/revision/latest/scale-to-width-down/732?cb=20250326180223",
        "https://static.wikia.nocookie.net/brainrotnew/images/3/38/Hq720.jpg/revision/latest/scale-to-width-down/284?cb=20250405141658",
        "https://static.wikia.nocookie.net/brainrotnew/images/f/ff/Ballerina_Cappucina.jpg/revision/latest/scale-to-width-down/340?cb=20250413201033",
        "https://static.wikia.nocookie.net/brainrotnew/images/1/10/Bombardiro_Crocodilo.jpg/revision/latest/thumbnail/width/720/height/900?cb=20250417102447",
        "https://static.wikia.nocookie.net/brainrotnew/images/5/5c/ChimpanziniBananini.jpg/revision/latest/scale-to-width-down/224?cb=20250407155205",
        "https://static.wikia.nocookie.net/brainrotnew/images/f/fc/Piccione_Macchina.png/revision/latest/thumbnail/width/720/height/900?cb=20250411000814",
        "https://static.wikia.nocookie.net/brainrotnew/images/1/18/Chefcrabracadabra.png/revision/latest/scale-to-width-down/536?cb=20250412001053",
        "https://static.wikia.nocookie.net/brainrotnew/images/f/f3/Rugginato_LupoGT.png/revision/latest/scale-to-width-down/341?cb=20250412070843",
        "https://static.wikia.nocookie.net/brainrotnew/images/2/26/Trulimero_Trulichina.png/revision/latest/scale-to-width/360?cb=20250403115127",
        "https://static.vecteezy.com/system/resources/thumbnails/054/357/727/small_2x/angry-man-with-wild-hair-and-glasses-shouting-free-photo.jpg",
        "https://static.vecteezy.com/system/resources/thumbnails/030/688/022/small_2x/grumpy-pets-expressing-their-discontent-with-adorable-grum-free-photo.jpg"
    );

    public ProfilePictureSelect(Stage stage) {
        this.mainStage = stage;
        this.blur = new GaussianBlur(5);
        this.imageViews = new ArrayList<>();
        initializeDialog();
    }

    private void initializeDialog() {
        pictureStage = new Stage(StageStyle.TRANSPARENT);
        pictureStage.initModality(Modality.APPLICATION_MODAL);
        pictureStage.initOwner(mainStage);

        setupContent();

        Scene scene = new Scene(dialogContent);
        scene.setFill(Color.TRANSPARENT);
        pictureStage.setScene(scene);
    }

    private void setupContent() {
        dialogContent = new VBox(20);
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setPadding(new Insets(30));
        dialogContent.setPrefWidth(800);
        dialogContent.setPrefHeight(600);
        dialogContent.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #cccccc;" +
            "-fx-border-width: 2px;" +
            "-fx-background-radius: 15px;" +
            "-fx-border-radius: 15px;"
        );

        Label headerLabel = new Label("Select your profile picture");
        headerLabel.setFont(Font.font("Verdana", 20));

        Label infoLabel = new Label("You must select a profile picture to continue");
        infoLabel.setFont(Font.font("Verdana", 14));
        infoLabel.setStyle("-fx-text-fill: #666666;");

        // Create bottom container for buttons
        HBox bottomContainer = new HBox(10);
        bottomContainer.setAlignment(Pos.CENTER_RIGHT);
        
        setupSwitchButton();
        setupContinueButton();

        // Add spacer between continue and switch buttons
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        bottomContainer.getChildren().addAll(spacer, switchButton, continueButton);
        
        imageScrollPane = createImageGrid(DEFAULT_IMAGES);
        
        dialogContent.getChildren().addAll(
            headerLabel, 
            infoLabel, 
            imageScrollPane, 
            bottomContainer
        );
    }

    private void setupSwitchButton() {
        switchButton = new Button("Show Meme Images");
        switchButton.setStyle(
            "-fx-font-family: 'Verdana';" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 20;" +
            "-fx-min-width: 150px;" +
            "-fx-min-height: 35px;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: #9775fa;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 20;" +
            "-fx-text-fill: #9775fa;" +
            "-fx-cursor: hand;"
        );

        switchButton.setOnAction(e -> toggleImageSet());
        
        // Hover effect with transparent background
        switchButton.setOnMouseEntered(e -> {
            switchButton.setStyle(switchButton.getStyle() + 
                "-fx-background-color: rgba(151, 117, 250, 0.1);" +  // Very light purple with opacity
                "-fx-effect: dropshadow(gaussian, #9775fa, 4, 0.2, 0, 0);"
            );
        });
        
        switchButton.setOnMouseExited(e -> {
            switchButton.setStyle(switchButton.getStyle() + 
                "-fx-background-color: transparent;" +
                "-fx-effect: none;"
            );
        });
    }

    private void toggleImageSet() {
        showingMemes = !showingMemes;
        selectedImageUrl = null;
        continueButton.setDisable(true);
        imageViews.clear();
        
        // Update button text
        switchButton.setText(showingMemes ? "Show Default Images" : "Show Meme Images");
        
        // Create new grid with appropriate images
        ScrollPane newGrid = createImageGrid(showingMemes ? MEME_IMAGES : DEFAULT_IMAGES);
        
        // Replace old grid with new one
        int gridIndex = dialogContent.getChildren().indexOf(imageScrollPane);
        dialogContent.getChildren().set(gridIndex, newGrid);
        imageScrollPane = newGrid;
    }

    private ScrollPane createImageGrid(List<String> images) {
        VBox gridContainer = new VBox(10);
        gridContainer.setAlignment(Pos.CENTER);

        int totalImages = images.size();
        int rows = (totalImages + IMAGES_PER_ROW - 1) / IMAGES_PER_ROW;

        for (int row = 0; row < rows; row++) {
            HBox imageRow = new HBox(10);
            imageRow.setAlignment(Pos.CENTER);

            for (int col = 0; col < IMAGES_PER_ROW; col++) {
                int index = row * IMAGES_PER_ROW + col;
                if (index < totalImages) {
                    StackPane imageContainer = (StackPane) createCircularImageView(images.get(index));
                    imageViews.add(imageContainer);
                    imageRow.getChildren().add(imageContainer);
                }
            }
            gridContainer.getChildren().add(imageRow);
        }

        ScrollPane scrollPane = new ScrollPane(gridContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        return scrollPane;
    }

    private Node createCircularImageView(String imageUrl) {
        StackPane imageContainer = ImageUtils.createCircularImage(imageUrl, 100);
        
        // Base style for container
        String baseStyle = "-fx-background-color: transparent;" +
                          "-fx-border-color: transparent;" +
                          "-fx-border-width: 3;" +
                          "-fx-shape: \"M50,0 C77.6,0 100,22.4 100,50 C100,77.6 77.6,100 50,100 C22.4,100 0,77.6 0,50 C0,22.4 22.4,0 50,0 Z\";" +
                          "-fx-cursor: hand;";
        
        imageContainer.setStyle(baseStyle);
        
        // Add selection handling
        imageContainer.setOnMouseClicked(e -> {
            selectedImageUrl = imageUrl;
            // Reset all containers to default style
            imageViews.forEach(iv -> iv.setStyle(baseStyle));
            
            // Apply selected style
            imageContainer.setStyle(baseStyle + 
                "-fx-border-color: #51cf66;" +
                "-fx-effect: dropshadow(gaussian, #51cf66, 10, 0.5, 0, 0);");
            continueButton.setDisable(false);
        });

        // Hover effects
        imageContainer.setOnMouseEntered(e -> {
            if (!imageUrl.equals(selectedImageUrl)) {
                imageContainer.setStyle(baseStyle + 
                    "-fx-border-color: #cccccc;" +
                    "-fx-effect: dropshadow(gaussian, #cccccc, 5, 0.3, 0, 0);");
            }
        });

        imageContainer.setOnMouseExited(e -> {
            if (!imageUrl.equals(selectedImageUrl)) {
                imageContainer.setStyle(baseStyle);
            }
        });

        return imageContainer;
    }

    private void setupContinueButton() {
        continueButton = new Button("Continue");
        continueButton.setDisable(true);
        continueButton.setStyle(
            "-fx-font-family: 'Verdana';" +
            "-fx-font-size: 14px;" +
            "-fx-background-radius: 20;" +
            "-fx-min-width: 150px;" +
            "-fx-min-height: 35px;" +
            "-fx-background-color: #51cf66;" +
            "-fx-text-fill: white;"
        );

        continueButton.setOnMouseEntered(e -> 
            continueButton.setStyle(continueButton.getStyle() + "-fx-background-color: #40c057;")
        );
        continueButton.setOnMouseExited(e -> 
            continueButton.setStyle(continueButton.getStyle() + "-fx-background-color: #51cf66;")
        );
    }

    public CompletableFuture<String> showDialog() {
        CompletableFuture<String> result = new CompletableFuture<>();

        if (mainStage.getScene() != null) {
            mainStage.getScene().getRoot().setEffect(blur);
        }

        continueButton.setOnAction(e -> {
            if (selectedImageUrl != null) {
                cleanup();
                result.complete(selectedImageUrl);
            }
        });

        // Center the dialog
        pictureStage.setOnShown(e -> {
            pictureStage.setX(mainStage.getX() + (mainStage.getWidth() - pictureStage.getWidth()) / 2);
            pictureStage.setY(mainStage.getY() + (mainStage.getHeight() - pictureStage.getHeight()) / 2);
        });

        pictureStage.show();
        return result;
    }

    private void cleanup() {
        if (mainStage.getScene() != null) {
            mainStage.getScene().getRoot().setEffect(null);
        }
        pictureStage.hide();
    }
}
