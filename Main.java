import javafx.application.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.stage.*;
import javafx.animation.*;
import javafx.event.*;
import javafx.geometry.*;
import java.util.*;
import java.text.*;
import java.io.File;
import java.io.*;

public class Main extends Application {
    FlowPane fp;
    Player thePlayer = new Player(300,300);
    Canvas theCanvas = new Canvas(600,600);
    GraphicsContext gc;
    boolean gameLost = false;
    
    private ArrayList<Mine> mines = new ArrayList<>();
    private ArrayList<String> generatedZones = new ArrayList<>();
    
    boolean up = false;
    boolean down = false;
    boolean left = false;
    boolean right = false;

    public void start(Stage stage) {
        gc = theCanvas.getGraphicsContext2D();
        
        fp = new FlowPane();
        fp.getChildren().add(theCanvas);
        drawBackground(300,300,gc);
        
        thePlayer.draw(300,300,gc,true);
        
        fp.setOnKeyPressed(new KeyListenerMove());
        fp.setOnKeyReleased(new KeyListenerStop());
        
        gc.setFill(Color.WHITE);
        gc.fillText("Score: ",10,20);
        
        Scene scene = new Scene(fp,600,600);
        stage.setScene(scene);
        stage.setTitle("Project :)");
        stage.show();
        
        fp.requestFocus();
        
        new AnimationHandler().start();
    }
    
   Image background = new Image("stars.png");
   Image overlay = new Image("starsoverlay.png");
   Random backgroundRand = new Random();
   //this piece of code doesn't need to be modified
   public void drawBackground(float playerx, float playery, GraphicsContext gc)
   {
	  //re-scale player position to make the background move slower. 
      playerx*=.1;
      playery*=.1;
   
	//figuring out the tile's position.
      float x = (playerx) / 400;
      float y = (playery) / 400;
      
      int xi = (int) x;
      int yi = (int) y;
      
	  //draw a certain amount of the tiled images
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(background,-playerx+i*400,-playery+j*400);
         }
      }
      
	  //below repeats with an overlay image
      playerx*=2f;
      playery*=2f;
   
      x = (playerx) / 400;
      y = (playery) / 400;
      
      xi = (int) x;
      yi = (int) y;
      
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(overlay,-playerx+i*400,-playery+j*400);
         }
      }
   }
    
    public class AnimationHandler extends AnimationTimer {
        public void handle(long currentTimeInNanoSeconds) {
            
            //check that player hasn't lost the game
            if (!gameLost) {
            
               gc.clearRect(0,0,600,600);
               
               thePlayer.act(up,down,left,right);
               
               //USE THIS CALL ONCE YOU HAVE A PLAYER
               drawBackground(thePlayer.getX(),thePlayer.getY(),gc);
               
               //example calls of draw - this should be the player's call for draw
               thePlayer.draw(300, 300, gc, true); //all other objects will use false in the parameter.
               
               generateMines();
               
               //iterate backwards to mroe easily remove elements
               for (int i = mines.size() - 1; i >= 0; i--) {
                   Mine mine = mines.get(i);
                   mine.draw(thePlayer.getX(), thePlayer.getY(), gc, false);
                   
                   if (Math.abs(mine.getX() - thePlayer.getX()) > 800 || 
                       Math.abs(mine.getY() - thePlayer.getY()) > 800) {
                       //remove the zone from generatedZones
                       int gridX = (int)(mine.getX() / 100);
                       int gridY = (int)(mine.getY() / 100);
                       String zoneKey = gridX + "," + gridY;
                       generatedZones.remove(zoneKey);
                       
                       //remove the mine from list
                       mines.remove(i);
                   }
               }
               
               
               //make score smaller to be more easily read
               DecimalFormat round = new DecimalFormat("#");
               double reducedScore = thePlayer.score() / 100;
               String score = round.format(reducedScore);
               double reducedHighscore = getHighScore() / 100;
               String highscore = round.format(reducedHighscore);
               
               gc.setFill(Color.WHITE);
               gc.fillText("Score: "+score,0,20);
               gc.fillText("Highscore: "+highscore,0,40);
               
               //iterate to check if collision (end game if so)
               for (int i = 0; i < mines.size(); i++) {
                  if (thePlayer.distance(mines.get(i)) <= 20) {
                     gameLost = true;
                     mines.remove(i);
                  }
               }
            }
            //player lost game
            else {
               gc.clearRect(0,0,600,600);
               drawBackground(thePlayer.getX(), thePlayer.getY(), gc);
               
               //iterate backwards to mroe easily remove elements
               for (int i = mines.size() - 1; i >= 0; i--) {
                   Mine mine = mines.get(i);
                   mine.draw(thePlayer.getX(), thePlayer.getY(), gc, false);
               }
               
               //make score smaller to be more easily read
               DecimalFormat round = new DecimalFormat("#");
               double reducedScore = thePlayer.score() / 100;
               String score = round.format(reducedScore);
               double reducedHighscore = getHighScore() / 100;
               String highscore = round.format(reducedHighscore);
               
               gc.setFill(Color.WHITE);
               gc.fillText("Score: "+score,270,290);
               gc.fillText("High Score: "+highscore,270,310);
            }
        }
        
        private void generateMines() {
           int playerGridX = (int)(thePlayer.getX() / 100);
           int playerGridY = (int)(thePlayer.getY() / 100);
            
           // Origin point (300,300) in grid coordinates
           int originGridX = 3;
           int originGridY = 3;
            
           for (int i = -4; i <= 4; i++) {
               for (int j = -4; j <= 4; j++) {
                  if (Math.abs(i) == 4 || Math.abs(j) == 4) {
                     int gridX = playerGridX + i;
                     int gridY = playerGridY + j;
                     String zoneKey = gridX + "," + gridY;
                        
                     if (!generatedZones.contains(zoneKey)) {
                        generatedZones.add(zoneKey);
                         
                        //calculate distance from this grid cell to origin
                        float distanceFromOrigin = (float)Math.sqrt(Math.pow(gridX - originGridX, 2) * 10000 + Math.pow(gridY - originGridY, 2) * 10000);
                      
                        //max mines = distance/1000 (minimum 1 mine)
                        int maxMinesPerCell = Math.max(1, (int)(distanceFromOrigin / 1000));
                      
                        //generate mines with 30% chance each
                        for (int k = 0; k < maxMinesPerCell; k++) {
                           if (Math.random() < 0.3) {
                              float mineX = gridX * 100 + (float)(Math.random() * 80 - 40);
                              float mineY = gridY * 100 + (float)(Math.random() * 80 - 40);
                              mines.add(new Mine(mineX, mineY));
                           }
                        }
                     }
                  }
               }
            }
         }
        
         private float getHighScore() {
            //check if file has already been made containing the highschore
            try {
               Scanner read = new Scanner(new File("GameHighScore.txt"));
               float hs = 0;
               
               while(read.hasNext())
               hs = read.nextFloat();
               
               if (thePlayer.score() > hs) {
                  FileOutputStream fos = new FileOutputStream("GameHighScore.txt",false);
                  PrintWriter pw = new PrintWriter(fos);
                  pw.print(thePlayer.score());
                  pw.close();
                  return (float)thePlayer.score();
               }
               return hs;
            }
            //if not make the file using current score
            catch (FileNotFoundException fnfe) {
               try {
                  FileOutputStream fos = new FileOutputStream("GameHighScore.txt",false);
                  PrintWriter pw = new PrintWriter(fos);
                  pw.print(thePlayer.score());
                  pw.close();
                  return (float)thePlayer.score();
               }
               catch (FileNotFoundException exception) {
                  return -1;
               }
            }
         }
    }
    
    public class KeyListenerMove implements EventHandler<KeyEvent> {
        public void handle(KeyEvent event) {
            KeyCode key = event.getCode();
            
            if (key == KeyCode.W) {
                up = true;
                down = false;
            }
            else if (key == KeyCode.S) {
                down = true;
                up = false;
            }
            else if (key == KeyCode.A) {
                left = true;
                right = false;
            }
            else if (key == KeyCode.D) {
                right = true;
                left = false;
            }
        }
    }
    
    public class KeyListenerStop implements EventHandler<KeyEvent> {
        public void handle(KeyEvent event) {
            KeyCode key = event.getCode();
            
            if (key == KeyCode.W) {
                up = false;
            }
            else if (key == KeyCode.S) {
                down = false;
            }
            else if (key == KeyCode.A) {
                left = false;
            }
            else if (key == KeyCode.D) {
                right = false;
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}