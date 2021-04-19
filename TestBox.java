import com.sun.javafx.perf.PerformanceTracker;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TestBox extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static PerformanceTracker tracker;

    public float getFPS () {
        float fps = tracker.getAverageFPS();
        tracker.resetAverageFPS();
        return fps;
    }
    @Override
    public void start(Stage primaryStage)  {
        Group root = new Group();
        Scene scene = new Scene(root, 1200, 800, Color.BLACK);
        primaryStage.setScene(scene);



        Group circles = new Group();


        Rectangle colors = new Rectangle(scene.getWidth(), scene.getHeight(),
                new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new
                        Stop[]{
                        new Stop(0, Color.web("#f8bd55")),
                        new Stop(0.14, Color.web("#c0fe56")),
                        new Stop(0.28, Color.web("#5dfbc1")),
                        new Stop(0.43, Color.web("#64c2f8")),
                        new Stop(0.57, Color.web("#be4af7")),
                        new Stop(0.71, Color.web("#ed5fc2")),
                        new Stop(0.85, Color.web("#ef504c")),
                        new Stop(1, Color.web("#f2660f")),}));
        colors.widthProperty().bind(scene.widthProperty());
        colors.heightProperty().bind(scene.heightProperty());


        for (int i = 0; i < 2000; i++) {        //!!! aici pui cate cercuri vrei sa faca, momentan sunt 2000. cu cat pui mai mult cu atat streseaza mai mult gpu dar in anumita masura si cpu
            Circle circle = new Circle(1000, Color.web("white", 0.01));     //Circle(radius,color/color properties) cu cat creste raza cercului creste si stresul pe gpu pana ajunge pe la raza 1000, orice valuare mai mare nu va provoca stres suplimentar
            circle.setStrokeType(StrokeType.OUTSIDE);
            circle.setStroke(Color.web("white", 0.16));
            circle.setStrokeWidth(4);
            circles.getChildren().add(circle);
        }

        circles.setEffect(new BoxBlur(10, 10, 3));

        Group blendModeGroup =
                new Group(new Group(new Rectangle(scene.getWidth(), scene.getHeight(),
                        Color.BLACK), circles), colors);
        colors.setBlendMode(BlendMode.OVERLAY);
        root.getChildren().add(blendModeGroup);

        primaryStage.show();



            Timeline timeline = new Timeline();
            for (Node circle : circles.getChildren()) {
                timeline.getKeyFrames().addAll(
                        new KeyFrame(Duration.ZERO, // set start position
                                new KeyValue(circle.translateXProperty(), Math.random() * 1200),
                                new KeyValue(circle.translateYProperty(), Math.random() * 800)
                        ),
                        new KeyFrame(new Duration(20000), // set end position
                                new KeyValue(circle.translateXProperty(), Math.random() * 1200),
                                new KeyValue(circle.translateYProperty(), Math.random() * 800)
                        )
                );
            }



        tracker = PerformanceTracker.getSceneTracker(scene);
        AnimationTimer frameRateMeter = new AnimationTimer() {

            @Override
            public void handle(long now) {      // aici primeste numarul mediu de fps si il afiseaza in terminal
                System.out.println(String.format("Current frame rate: %.3f fps", getFPS()));
            }
        };
        frameRateMeter.start();
            timeline.play();




       // timeline.play();


    }
}
