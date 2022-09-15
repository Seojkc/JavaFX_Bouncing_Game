package BouncingGame_JAVAFX;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * @author Seo JAMES
 */

public class Main extends Application {

    /**Store the Score **/
    private Score scoreClass;
    private Label scoreLabel;

    /**Store the restart button **/
    private Button restart;

    /**Store the canvas **/
    private Canvas canvas;

    /**Store the canvas **/
    private int chance=0;

    /**Store the UserAction **/
    private enum UserAction{
        NONE, LEFT, RIGHT
    }

    /**Store the width of canvas **/
    private static final int width = 800;

    /**Store the height of canvas**/
    private static final int height = 600;

    /**Store the ball Radius **/
    private static final int ballRadius = 10;

    /**Store the bat width **/
    private static final int batWidth = 100;

    /**Store the bat height **/
    private static final int batHeight = 20;


    /**Store the ball**/
    private Circle ball = new Circle(ballRadius);

    /**Store the bat **/
    private Rectangle bat = new Rectangle(batWidth,batHeight);


    /**Store the condition of ball **/
    private boolean ballUp = true , ballLeft = false;
    private UserAction action = UserAction.NONE;

    private Timeline timeline = new Timeline();

    /**Store the movement**/
    private boolean running = true;

    /**
     * create the content ie. the game
     * @return the root
     */
    private Parent createContent(){
        Pane root = new Pane();
        root.setId("pane");
        scoreClass=new Score();
        root.setPrefSize(width,height+100);

        canvas=new Canvas(width, 100);
        canvas.setId("canvas");//canvas

        scoreLabel=new Label("Score : "+scoreClass.getScore());//score card
        scoreLabel.setId("score");

        restart=new Button("RESTART");
        restart.setOnAction(this::setRestart);//restart button

        Label instructions=new Label("Press A and D to move the board.");
        instructions.setId("score");//instruction


        scoreLabel.relocate(20, height+10);
        canvas.relocate(0, height);
        restart.relocate(width-100,height+20);
        instructions.relocate(100, height+43);

        root.getChildren().addAll(instructions,canvas,scoreLabel,restart);//setting scene

        bat.setTranslateX(width/2);
        bat.setTranslateY(height - batHeight);
        bat.setFill(Color.BLUE);
        ball.setFill(Color.RED);

        //playing the movement of ball bat
        KeyFrame frame = new KeyFrame(Duration.seconds(0.016), event ->{

            if(!running){
                return;
            }
            switch (action){
                case LEFT :
                    if(bat.getTranslateX() - 5 >=0)
                        bat.setTranslateX(bat.getTranslateX() - 5 );
                    break;
                case RIGHT:
                    if(bat.getTranslateX() + batWidth + 5 <= width)
                        bat.setTranslateX(bat.getTranslateX() + 5 );
                    break;
                case NONE:
                    break;
            }

            ball.setTranslateX(ball.getTranslateX() + (ballLeft ? -5 : 5));
            ball.setTranslateY(ball.getTranslateY() + (ballUp ? -5 : 5));

            if(ball.getTranslateX() - ballRadius == 0) {
                ballLeft = false;
            }
            else if (ball.getTranslateX() + ballRadius == width) {
                ballLeft = true;
            }

            if(ball.getTranslateY() - ballRadius == 0) {
                ballUp = false;
            }
            else if (ball.getTranslateY() + ballRadius == height - batHeight
                    && ball.getTranslateX() + ballRadius >= bat.getTranslateX()
                    && ball.getTranslateX() - ballRadius <= bat.getTranslateX() + batWidth) {
                ballUp = true;

                scoreClass.setScore(5);
                scoreLabel.setText("Score : "+scoreClass.getScore());

            }

            if(ball.getTranslateY() + ballRadius == height){
                restartGame();
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);

        root.getChildren().addAll(ball,bat);

        return root;
    }


    /**
     * Setting the restart button
     * @param actionEvent is action event
     */
    private void setRestart(ActionEvent actionEvent) {
        scoreClass=new Score();
        scoreLabel.setText("Score : "+scoreClass.getScore());
        chance=0;
        startGame();
    }

    /**
     * method for Asking to restart the game
     */
    private void restartGame(){
        stopGame();
        chance += 1;
        if (chance<8){
            startGame();
        }
        else{
            scoreLabel.setText("Game over please restart .Score : "+scoreClass.getScore());
        }
    }

    /**
     * Method to stop the game
     */
    private void stopGame(){
        running = false;
        timeline.stop();



    }

    /**
     * Method to start game
     */
    private void startGame(){
        ballUp = true;
        ball.setTranslateX(width/2);
        ball.setTranslateY(height/2);
        timeline.play();
        running = true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Scene scene = new Scene(createContent());
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());//calling css external file

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()){
                case A :
                    action = UserAction.LEFT;
                    break;
                case D :
                    action = UserAction.RIGHT;
                    break;
            }
        });

        scene.setOnKeyReleased(event ->
        {
            switch (event.getCode()){
                case A :
                    action = UserAction.NONE;
                    break;
                case D :
                    action = UserAction.NONE;
                    break;
            }
        });



        primaryStage.setTitle("SJ'S_Pong_Game_");
        primaryStage.setScene(scene);
        primaryStage.show();
        startGame();

    }

    public static void main(String[] args) {
        launch(args);

    }

}