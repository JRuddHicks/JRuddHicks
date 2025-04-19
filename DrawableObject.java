import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public abstract class DrawableObject
{
   public DrawableObject(float x, float y)
   {
      this.x = x;
      this.y = y;
   }

   //positions
   private float x;
   private float y;
   
   //takes the position of the player and calls draw me with appropriate positions
   public void draw(float playerx, float playery, GraphicsContext gc, boolean isPlayer)
   {
      //the 300,300 places the player at 300,300, if you want to change it you will have to modify it here
      
      if(isPlayer)
         drawMe(playerx,playery,gc);
      else
         drawMe(-playerx+300+x,-playery+300+y,gc);
   }
   
   //this method you implement for each object you want to draw. Act as if the thing you want to draw is at x,y.
   //NOTE: DO NOT CALL DRAWME YOURSELF. Let the the "draw" method do it for you. I take care of the math in that method for a reason.
   public abstract void drawMe(float x, float y, GraphicsContext gc);
   
   private float accelerationX = 0;
   private float accelerationY = 0;
   private float speedX = 0;
   private float speedY = 0;
   
   public void act(boolean up,boolean down,boolean left,boolean right) {
      if (up) {
         if (speedY > 0)
            accelerationY = (float) -0.125;
         else
            accelerationY = (float) -0.1;
         
         speedY += accelerationY;
         y += speedY;
      }
      else if (down) {
         if (speedY < 0)
            accelerationY = (float) 0.125;
         else
            accelerationY = (float) 0.1;
            
         speedY += accelerationY;
         y += speedY;
      }
      else {
         if (speedY > -0.025 && speedY < 0.025) {
            accelerationY = 0;
            speedY = 0;
         }
         else if (speedY > 0) {
            accelerationY = (float) -0.025;
            speedY += accelerationY;
            y += speedY;
         }
         else if (speedY < 0) {
            accelerationY = (float) 0.025;
            speedY += accelerationY;
            y += speedY;
         }
      }
      
      if (left) {
         if (speedX > 0)
            accelerationX = (float) -0.125;
         else
            accelerationX = (float) -0.1;
            
         speedX += accelerationX;
         x += speedX;
      }
      else if (right) {
         if (speedX < 0)
            accelerationX = (float) 0.125;
         else
            accelerationX = (float) 0.1;
            
         speedX += accelerationX;
         x += speedX;
      }
      else {
         if (speedX > -0.025 && speedX < 0.025) {
            accelerationX = 0;
            speedX = 0;
         }
         else if (speedX > 0) {
            accelerationX = (float) -0.025;
            speedX += accelerationX;
            x += speedX;
         }
         else if (speedX < 0) {
            accelerationX = (float) 0.025;
            speedX += accelerationX;
            x += speedX;
         }
      }
      
      if (speedY > 10) {
         speedY = 10;
      }
      else if (speedY < -10) {
         speedY = -10;
      }
      
      if (speedX > 10) {
         speedX = 10;
      }
      else if (speedX < -10) {
         speedX = -10;
      }
   }
   
   public float getX(){return x;}
   public float getY(){return y;}
   public void setX(float x_){x = x_;}
   public void setY(float y_){y = y_;}
   
   public double distance(DrawableObject other)
   {
      return (Math.sqrt((other.x-x)*(other.x-x) +  (other.y-y)*(other.y-y)   ));
   }
}