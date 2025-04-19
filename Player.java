import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public class Player extends DrawableObject {
   
   public Player(float x,float y) {
      super(x,y);
   }
   
   public void drawMe(float x,float y,GraphicsContext gc) {
      gc.setFill(Color.BLUE);
      gc.fillOval(x-17.5,y-17.5,35,35);
      
      gc.setStroke(Color.BLACK);
      gc.strokeOval(x-17.5,y-17.5,35,35);
      
      gc.setFill(Color.MAGENTA);
      gc.fillOval(x-5,y-5,10,10);
      
      gc.strokeOval(x-5,y-5,10,10);
   }
   
   public double score() {
      return Math.sqrt(Math.pow(300-getX(),2) + Math.pow(300-getY(),2));
   }
}