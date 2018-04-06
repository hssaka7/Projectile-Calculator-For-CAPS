import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
//===============================================================================================
// This is the program we (CS-152) started to develop in class on Monday (Sept. 18).
// It extends javafx.application.Application and when run, it creates a window into
// which it draws an endless stream of random rectangles.
//
// NOTE: this program requires Java 1.8 or newer.
//===============================================================================================


//==========================================================================================
//A class that uses JavaFX must extend javafx.application.Application
//==========================================================================================
public class JavafxBall extends Application
{
  private static final int DRAW_WIDTH  = 900;
  private static final int DRAW_HEIGHT = 500;

  private Animation myAnimation;  //Reference to an inner class that gets called at 60Hz
  private Canvas canvas;          //Area on which to draw graphics items.
  private GraphicsContext gtx;    //Drawing methods for the Canvas.

  private double ballX, ballY,initAngle,gravity;
  private double speedX, speedY, totalSpeed;
  private double ballSize;

  @Override
  //=========================================================================
  //start(Stage stage)
  //This is a JavaFX callback method. It is called by JavaFX after JavaFX
  //   has created a window. The parameter, Stage stage, is a pointer to
  //   the part of the window where the programmer can add widgets, such as
  //   buttons, menus and canvases.
  //=========================================================================
  public void start(Stage stage) throws Exception
  {
    //Set the window's title in its title bar.
    stage.setTitle("A Random Box");


    //A Canvas is an area that supports graphics drawing
    //To get this to work, there is a hierarchy of objects that is needed:
    //   1) The canvas is placed in a new instance of VBox.
    //   2) The instance of VBox is placed in a new instance of Scene.
    //   3) The instance of Scene is placed in the given instance of Stage.
    canvas = new Canvas(DRAW_WIDTH, DRAW_HEIGHT);

    //A GraphicsContext, gtx, is a pointer to a set of drawing tools
    //   that can be performed on an instance of a Canvas, canvas.
    gtx = canvas.getGraphicsContext2D();



    VBox vBox = new VBox();
    vBox.getChildren().addAll(canvas);

    Scene scene = new Scene(vBox, DRAW_WIDTH, DRAW_HEIGHT);

    stage.setScene(scene);
    stage.show();

    //At this point, the an empty, white window is created.

    ballSize = 20;
    ballX  = 0; // start point x for ball
    ballY  = 0 ; // start point y for ball;
    speedX = 1 ; //start x speed
    speedY = 1; // start y speed
    gravity = 9.81;

    totalSpeed = Math.sqrt(1000);
    initAngle = Math.PI/4;

    //Now, we create an new AnimationTimer and start it running.
    //  this will tell JavaFX to call the AnimationTimer's handle method
    //  at a rate of 60 times per second.
    //Each time the handle method is called, a new image can be drawn.
    //Each new image is called a "frame". Thus, this will **attempt** to
    //  draw at 60 frames per second (fps).
    myAnimation = new Animation();
    myAnimation.start();
  }


  //===========================================================================================
  // Animation is an inner class of our JavafxRandomBox class.
  // Animation is an "inner class" because it is inside the JavafxRandomBox class.
  // Since Animation extends AnimationTimer, the Animation class MUST implement
  //   public void handle(long now), a callback method that is called by JavaFX at 60Hz.
  //===========================================================================================
  class Animation extends AnimationTimer
  {
    @Override
    //=========================================================================================
    //handel is a callback method called by JavaFX at 60Hz.
    //  Try printing the value of the parameter now. Can you guess what it is?
    //  Why is it a long?
    //  Note: printing to the console (System.out.println()) is a relatively slow task.
    //        Thus, while it can be instructive to call
    //          System.out.println("JavafxRandomBox.Animaton.handle() now="+now);
    //        in this callback, remember that JavaFX is calling it at 60Hz.
    //        Therefore, if you leave that print statement in handle, it will slow down the
    //        program.
    //=========================================================================================
    public void handle(long now)
    {
      //JavaFX defines colors using RGB color space.
      //  The red, green and blue values each must be in the range [0,1] where
      //  0 is totally off, 1 is full brightness, 0.5 is half brightness, ...

      gtx.setFill(Color.BLACK);
      //gtx.fillRect(left, top, width, height) will fill an axis-aligned rectangular
      //    area with the current fill color. The rectangle is defined by the 4
      //   parameters:
      //         left  (x-coordinate in pixels of the left corner of the rectangle).
      //         top   (y-coordinate in pixels of the top corner of the rectangle).
      //         width (width in pixels of the rectangle).
      //         height (height in pixels of the rectangle).

      System.out.println("the max range is  " + getRange(totalSpeed,initAngle));
      System.out.println("the max height is  " + height(totalSpeed,initAngle));
      gtx.fillRect(0, 0, DRAW_WIDTH, DRAW_HEIGHT);




      gtx.setFill(Color.RED);

      double Nx = normalizeX(ballX,totalSpeed,initAngle);
      double Ny = normalizeY(ballY,totalSpeed,initAngle);
      //gtx.fillRect(Nx, DRAW_HEIGHT-Ny-ballSize, ballSize, ballSize);
      gtx.fillOval(Nx,DRAW_HEIGHT-Ny-ballSize,20,20);

      System.out.println("x ==   " + Nx +"    y ====    "  + Ny);



      if (Nx < DRAW_WIDTH-ballSize) {

              ballX = ballX + 0.05;
              double firstPart = ballX * Math.tan(initAngle);
              double secondPart = ballX*ballX*gravity;
              double divider = 2 * totalSpeed*totalSpeed*Math.cos(initAngle)*Math.cos(initAngle);
              ballY = firstPart - (secondPart/divider);


      }

      else
      {
        ballX = getRange(totalSpeed,initAngle);
        ballY = 0;
      }

//      if (ballY < height(Math.sqrt(2), Math.PI/4))
//      {
//        ballY = ballY + Math.tan(Math.PI / 4) - (-9.81 * ballX * ballX) / (2 * totalSpeed * totalSpeed * (Math.cos(Math.PI / 4)));
//      }
//
//      else
//      {
//        ballY = ballY + Math.tan(Math.PI / 4) + (-9.81 * ballX * ballX) / (2 * totalSpeed * totalSpeed * (Math.cos(Math.PI / 4)));
//      }



    }
  } //This bracket ends Animation, the inner class.

  public double normalizeX(double val,double velocity,double angle)
  {
    double minX = 0;
    double maxX = getRange(velocity,angle);
    double MinValueWanted = 0;
    double MaxValueWanted = DRAW_WIDTH - ballSize;
    double ratio =  (MaxValueWanted - MinValueWanted) / (maxX-minX);
    double NormalizedX = ratio * (val - maxX) + MaxValueWanted;
    return NormalizedX;
  }

  public double normalizeY(double val,double velocity,double angle) {
    double minY = 0;
    double maxY = height(velocity, angle);
    double MinValueWanted = 0;
    double MaxValueWanted = DRAW_HEIGHT - ballSize;
    double ratio = (MaxValueWanted - MinValueWanted) / (maxY - minY);
    double NormalizedY = ratio * (val - maxY) + MaxValueWanted;
    return NormalizedY;
  }
  public double getRange(double velocity, double angle) {
    double sin = Math.sin(2 * angle);
    double h = ((velocity * velocity) * sin) / ((9.81));
    return h;
  }

  public double height(double velocity, double angle) {
    double sin = Math.sin(angle)*Math.sin(angle);
    double h = ((velocity*velocity) * sin) / (2 * (9.81));
    return h;
  }

  //===========================================================================================
  // Every Java program must have public static void main(String[] args).
  // In a JavaFX program, main starts JavaFX by calling:
  //     javafx.application.Application.launch(String[] args)
  //===========================================================================================
  public static void main(String[] args)
  {
    launch(args);
  }
}



