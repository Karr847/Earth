package packt13;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class Earth extends Application {
    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;
    private double anchorX,anchorY;
    private double anchorAngleX=0;
    private double anchorAngleY=0;
    private final DoubleProperty angleX=new SimpleDoubleProperty ( 0 );
    private final DoubleProperty angleY=new SimpleDoubleProperty ( 0 );
    private final Sphere sphere=new Sphere ( 150 );

    @Override
    public void start(Stage primaryStage) throws Exception {
        Camera camera = new PerspectiveCamera (true);
        camera.setNearClip (1);
        camera.setFarClip (10000);
        camera.translateZProperty ( ).set (-1000);
        SmartGroup world = new SmartGroup ( );
        world.getChildren ( ).add (prepareEarth ( ));
        Group root=new Group ();
        root.getChildren ().add (world);
        root.getChildren ().add (prepareImageView ());


        Scene scene = new Scene (root, WIDTH, HEIGHT, true);
        scene.setFill (Color.SILVER);
        scene.setCamera (camera);
        initMouseControl (world, scene, primaryStage);
        primaryStage.setTitle ("Globe");
        primaryStage.setScene (scene);
        primaryStage.show ( );

        prepareAnimation ( );
    }
    private void prepareAnimation() {
        AnimationTimer timer = new AnimationTimer ( ) {
            @Override
            public void handle(long now) {
                sphere.rotateProperty ( ).set (sphere.getRotate ( ) + 0.2);

            }
        };
        timer.start ( );
    }
    private ImageView prepareImageView(){
        Image image=new Image (Earth.class.getResourceAsStream ("/resources/galaxy-v.jpg"));
        ImageView imageView=new ImageView ( image );
        imageView.setPreserveRatio (true);
        imageView.getTransforms ().add (new Translate (-image.getWidth ()/2,-image.getHeight ()/2,-100));
        return imageView;





    }

    private Node prepareEarth() {

        PhongMaterial earthMaterial = new PhongMaterial ( );
        earthMaterial.setDiffuseMap (new Image (getClass ( ).getResourceAsStream ("/resources/earth-a.jpg")));
        earthMaterial.setSelfIlluminationMap (new Image (getClass ( ).getResourceAsStream ("/resources/earth-d.jpg")));
        earthMaterial.setSpecularMap (new Image (getClass ( ).getResourceAsStream ("/resources/earth-e.jpg")));
        earthMaterial.setBumpMap (new Image (getClass ( ).getResourceAsStream ("/resources/earth-h.jpg")));






        sphere.setRotationAxis (Rotate.Y_AXIS);
        sphere.setMaterial (earthMaterial);

        return sphere;


    }
    private void initMouseControl(SmartGroup group, Scene scene,Stage stage){
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms ().addAll (
                xRotate=new Rotate ( 0,Rotate.X_AXIS ),
                yRotate=new Rotate ( 0,Rotate.Y_AXIS )
        );
        xRotate.angleProperty ().bind (angleX);
        yRotate.angleProperty ().bind (angleY);
        scene.setOnMousePressed (event->{
            anchorX=event.getSceneX ();
            anchorY=event.getSceneY ();
            anchorAngleX=angleX.get ();
            anchorAngleY=angleY.get ();

        });
        scene.setOnMouseDragged (event->{
            angleX.set (anchorAngleX-(anchorY-event.getSceneY ()));
            angleY.set (anchorAngleY+anchorX-event.getSceneX ());

        });
        stage.addEventHandler (ScrollEvent.SCROLL, event->{
            double delta=event.getDeltaY ();
            group.translateZProperty ().set (group.getTranslateZ()+delta);
        });




    }
    class SmartGroup extends Group {
        Rotate r;
        Transform t = new Rotate ( );


        void rotateByX(int ang) {
            r = new Rotate (ang, Rotate.X_AXIS);
            t = t.createConcatenation (r);
            this.getTransforms ( ).clear ( );
            this.getTransforms ( ).addAll (t);
        }

        void rotateByY(int ang) {
            r = new Rotate (ang, Rotate.Y_AXIS);
            t = t.createConcatenation (r);
            this.getTransforms ( ).clear ( );
            this.getTransforms ( ).addAll (t);
        }
    }
}



