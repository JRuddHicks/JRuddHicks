import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import java.util.*;

public class Mine extends DrawableObject {
   private Random rand = new Random();
   private boolean isRed;
   private int iterationCount = 0;

   public Mine(float x,float y) {
      super(x,y);
      
      int startColor = rand.nextInt(2);
      
      if (startColor == 0) {
         isRed = true;
      }
      else {
         isRed = false;
      }
   }
   
   public void drawMe(float xPos,float yPos,GraphicsContext gc) {
      if (isRed) {
         gc.setFill(Color.RED);
      }
      else {
         gc.setFill(Color.WHITE);
      }
      
      gc.fillOval(xPos-6,yPos-6,12,12);
      gc.setStroke(Color.BLACK);
      gc.strokeOval(xPos-6,yPos-6,12,12);
      
      iterationCount++;
      
      if (iterationCount == 20) {
         iterationCount = 0;
         if (isRed)
            isRed = false;
         else
            isRed = true;
      }
   }
}