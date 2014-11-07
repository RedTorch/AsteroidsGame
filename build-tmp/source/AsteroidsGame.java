import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class AsteroidsGame extends PApplet {

//your variable declarations here
int width = 800;
int height = 800;
int flash = 255;
int jumpFuel = 10;
boolean up = false;
boolean left = false;
boolean right = false;
boolean h = false;
private SpaceShip ishikari = new SpaceShip();
star[] skyFullOfStars = new star[100];
public void setup() 
{
  size(width,height+50);
  for(int i = 0; i < skyFullOfStars.length; i++)
  {
    skyFullOfStars[i] = new star();
  }
}
public void draw() 
{
  noFill();
  background(0);
  for(int i = 0; i < skyFullOfStars.length; i++)
  {
    skyFullOfStars[i].show();
  }
  keyResponse();
  ishikari.move();
  ishikari.show();
  if(flash>0)
  {
    flash = flash - 2;
    if(flash<0)
    {
      flash = 0;
    }
  }
  noFill();
  stroke(50,100,200,flash);
  rect(ishikari.getX()-(int)(flash),ishikari.getY()-(int)(flash),flash*2,flash*2);
  noStroke();
  fill(255,255,255,flash);
  rect(0,0,width,height);
  fill(0);
  rect(0,height,width,50);
  stroke(50,100,200);
  fill(50,100,200);
  textSize(15);
  textAlign(CENTER);
  text("X-COORD <" + (int)(ishikari.getX()) + '>',(int)(width/8),height+25);
  text("Y-COORD <" + (int)(ishikari.getY()) + '>',(int)(width/8*3),height+25);
  text("SPEED <" + (int)(dist(0,0,(int)(ishikari.getDirectionX()*10),(int)(ishikari.getDirectionY()*10))) + '>',(int)(width/8*5),height+25);
  text("JUMP FUEL <" + jumpFuel + '>',(int)(width/8*7),height+25);
  stroke(50,100,200);
  noFill();
  rect(0,0,width-1,height+49);
}
class SpaceShip extends Floater  
{
    SpaceShip()
    {
        myColor = color(50,100,200);
        corners = 4;
        xCorners = new int[corners];
        yCorners = new int[corners];
        xCorners[0] = -10;
        yCorners[0] = -10;
        xCorners[1] = -7;
        yCorners[1] = 0;
        xCorners[2] = -10;
        yCorners[2] = 10;
        xCorners[3] = 10;
        yCorners[3] = 0;
        setX((int)(width/2));
        setY((int)(height/2));
        setDirectionX(0); 
        setDirectionY(0);   
        setPointDirection(270);
    }
    public void setX(int x) {myCenterX = x;}
    public void setY(int y) {myCenterY = y;}
    public int getX() {return (int)(myCenterX);}
    public int getY() {return (int)(myCenterY);}
    public void setDirectionX(double directionX){myDirectionX = directionX;}
    public void setDirectionY(double directionY){myDirectionY = directionY;}
    public double getDirectionX(){return myDirectionX;}
    public double getDirectionY(){return myDirectionY;}
    public void setPointDirection(int leDegrees){myPointDirection = leDegrees;}
    public double getPointDirection(){return myPointDirection;}
    public void hyperspace()
    {
      setX((int)(Math.random()*width));
      setY((int)(Math.random()*height));
      setDirectionX(0);
      setDirectionY(0);
      setPointDirection((int)(Math.random()*360));
      flash = 255;
    }
}
abstract class Floater //Do NOT modify the Floater class! Make changes in the SpaceShip class 
{   
  protected int corners;  //the number of corners, a triangular floater has 3   
  protected int[] xCorners;   
  protected int[] yCorners;   
  protected int myColor;   
  protected double myCenterX, myCenterY; //holds center coordinates   
  protected double myDirectionX, myDirectionY; //holds x and y coordinates of the vector for direction of travel   
  protected double myPointDirection; //holds current direction the ship is pointing in degrees    
  abstract public void setX(int x);  
  abstract public int getX();   
  abstract public void setY(int y);   
  abstract public int getY();   
  abstract public void setDirectionX(double x);   
  abstract public double getDirectionX();   
  abstract public void setDirectionY(double y);   
  abstract public double getDirectionY();   
  abstract public void setPointDirection(int degrees);   
  abstract public double getPointDirection(); 
  //Accelerates the floater in the direction it is pointing (myPointDirection)   
  public void accelerate (double dAmount)   
  {          
    //convert the current direction the floater is pointing to radians    
    double dRadians =myPointDirection*(Math.PI/180);     
    //change coordinates of direction of travel    
    myDirectionX += ((dAmount) * Math.cos(dRadians));    
    myDirectionY += ((dAmount) * Math.sin(dRadians)); 
  }   
  public void rotate (int nDegreesOfRotation)   
  {     
    //rotates the floater by a given number of degrees    
    myPointDirection+=nDegreesOfRotation;   
  }   
  public void move ()   //move the floater in the current direction of travel
  {      
    //change the x and y coordinates by myDirectionX and myDirectionY       
    myCenterX += myDirectionX;    
    myCenterY += myDirectionY;     

    //wrap around screen    
    if(myCenterX >width)
    {     
      myCenterX = 0;    
    }    
    else if (myCenterX<0)
    {     
      myCenterX = width;    
    }    
    if(myCenterY >height)
    {    
      myCenterY = 0;    
    }   
    else if (myCenterY < 0)
    {
      myCenterY = height; 
    }
  }
  public void show ()  //Draws the floater at the current position
  {
    //fill(myColor);
    noFill();
    stroke(myColor);    
    //convert degrees to radians for sin and cos         
    double dRadians = myPointDirection*(Math.PI/180);                 
    int xRotatedTranslated, yRotatedTranslated;    
    beginShape();         
    for(int nI = 0; nI < corners; nI++)    
    {     
      //rotate and translate the coordinates of the floater using current direction
      xRotatedTranslated = (int)((xCorners[nI]* Math.cos(dRadians)) - (yCorners[nI] * Math.sin(dRadians))+myCenterX);     
      yRotatedTranslated = (int)((xCorners[nI]* Math.sin(dRadians)) + (yCorners[nI] * Math.cos(dRadians))+myCenterY);      
      vertex(xRotatedTranslated,yRotatedTranslated);    
    }   
    endShape(CLOSE);  
  }   
} 
class star
{
  private int myX,myY,size;
  star()
  {
    myX = (int)(Math.random()*width);
    myY = (int)(Math.random()*height);
  }
  public void show()
  {
    stroke(255);
    //fill(255);
    ellipse(myX,myY,3,3);
  }
}
public void keyPressed()
{
  if(key == CODED && keyCode == UP)
  { 
    up = true;
  }
  else if(key == CODED && keyCode == LEFT)
  { 
    left = true;
  }
  else if(key == CODED && keyCode == RIGHT)
  { 
    right = true;
  }
  else if(key == 'h')
  {
    h = true;
  }
}
public void keyReleased()
{
  if(key == CODED && keyCode == UP)
  { 
    up = false;
  }
  else if(key == CODED && keyCode == LEFT)
  { 
    left = false;
  }
  else if(key == CODED && keyCode == RIGHT)
  { 
    right = false;
  }
  else if(key == 'h')
  {
    h = false;
  }
}
public void keyResponse()
{
  if(up == true)
  {
    ishikari.accelerate(0.05f);
  }
  if(left == true)
  {
    ishikari.setPointDirection((int)(ishikari.getPointDirection()-5));
  }
  if(right == true)
  {
    ishikari.setPointDirection((int)(ishikari.getPointDirection()+5));
  }
  if(h == true && flash == 0 && jumpFuel >= 1)
  {
    ishikari.hyperspace();
    jumpFuel--;
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "AsteroidsGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
