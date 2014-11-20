import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

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
boolean space = false;
boolean gameOver = false;
int timeSurvived = 0;
private SpaceShip ishikari = new SpaceShip();
star[] skyFullOfStars = new star[100];
ArrayList <Asteroid> drifters;
ArrayList <Particle> exhaust;
public void setup() 
{
  size(width,height+50);
  drifters = new ArrayList <Asteroid>();
  exhaust = new ArrayList <Particle>();
  for(int i = 0; i < skyFullOfStars.length; i++)
  {
    skyFullOfStars[i] = new star();
  }
  for(int i = 0; i < 10; i++)
  {
    drifters.add(new Asteroid());
  }
}
public void draw()
{
  gameOver = false;//CHEAT CODE
  if(gameOver == false)
  {
    noFill();
    background(0);
    for(int i = 0; i < skyFullOfStars.length; i++)
    {
      skyFullOfStars[i].show();
    }
    if(exhaust.size()>0)
    {
      for(int i = 0; i < exhaust.size(); i++)
      {
        exhaust.get(i).move();
        println(i + " - " + exhaust.get(i).getLife());
        //fill(exhaust.get(i).myColor());
        //ellipse(exhaust.get(i).getX(),exhaust.get(i).getY(),3,3);
        exhaust.get(i).show();
        if(exhaust.get(i).getLife() <= 0)
        {
          exhaust.remove(i);
        }
      }
    }
    for(int i = 0; i < drifters.size(); i++)
    {
      drifters.get(i).setPointDirection((int)drifters.get(i).getPointDirection()+3);
      drifters.get(i).move();
      drifters.get(i).show();
      if(dist(drifters.get(i).getX(),drifters.get(i).getY(),ishikari.getX(),ishikari.getY())<100)
      {
        stroke(200,50,50);
        noFill();
        rect((int)(drifters.get(i).getX()-25),(int)(drifters.get(i).getY()-25),50,50);
        if(dist(drifters.get(i).getX(),drifters.get(i).getY(),ishikari.getX(),ishikari.getY())<27)
        {
          gameOver = true;
          space = false;
        }
      }
    }
    timeSurvived++;
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
    text("CO-ORDS <" + (int)(ishikari.getX()) + ", " + (int)(ishikari.getY()) + ">",(int)(width/8),height+25);
    text("SURVIVED FOR <" + (int)(timeSurvived/60) + ">",(int)(width/8*3),height+25);
    text("SPEED <" + (int)(dist(0,0,(int)(ishikari.getDirectionX()*10),(int)(ishikari.getDirectionY()*10))) + ">",(int)(width/8*5),height+25);
    text("JUMP FUEL <" + jumpFuel + ">",(int)(width/8*7),height+25);
    stroke(50,100,200);
    noFill();
    line(0,height,width,height);
    line((int)(width/4),height,(int)(width/4),height+50);
    line((int)(width/2),height,(int)(width/2),height+50);
    line((int)(width/4*3),height,(int)(width/4*3),height+50);
    rect(0,0,width-1,height+49);
  }
  else if(gameOver == true)
  {
    keyResponse();
    fill(200,50,50);
    background(0);
    textSize(35);
    text("R.I.P. ISHIKAWA AT <" + (int)(timeSurvived/60) + "> SECONDS",(int)(width/2),(int)(height/3));
    textSize(20);
    text("CONTROLS:",(int)(width/2),(int)(height/2)-38);
    text("ARROW KEYS TO TURN AND ACCELERATE",(int)(width/2),(int)(height/2)-13);
    text("<H> KEY TO INITIATE HYPERSPACE",(int)(width/2),(int)(height/2)+12);
    text("SPACE BAR TO FIRE",(int)(width/2),(int)(height/2)+37);
    textSize(35);
    text("PRESS SPACE TO RE-LAUNCH",(int)(width/2),(int)(height/3*2));
  }
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
        wraps = true;
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
class Asteroid extends Floater  
{
  Asteroid()
  {
      myColor = color(200,100,50);
      corners = 8;
      xCorners = new int[corners];
      yCorners = new int[corners];
      xCorners[0] = 20;
      yCorners[0] = 0;
      xCorners[1] = 15;
      yCorners[1] = 15;
      xCorners[2] = 0;
      yCorners[2] = 20;
      xCorners[3] = -15;
      yCorners[3] = 15;
      xCorners[4] = -20;
      yCorners[4] = 0;
      xCorners[5] = -15;
      yCorners[5] = -15;
      xCorners[6] = 0;
      yCorners[6] = -20;
      xCorners[7] = 15;
      yCorners[7] = -15;
      setX((int)(Math.random()*width));
      setY(height + 100);
      setDirectionX(0); 
      setDirectionY(0);   
      setPointDirection((int)(Math.random()*360));
      accelerate(3);
      wraps = true;
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
}
class Shot extends Floater
{
  Shot()
  {
      myColor = color(200,100,50);
      corners = 2;
      xCorners = new int[corners];
      yCorners = new int[corners];
      xCorners[0] = 10;
      yCorners[0] = 0;
      xCorners[1] = -10;
      yCorners[1] = 0;
      setX(ishikari.getX());
      setY(ishikari.getY());
      setDirectionX(0); 
      setDirectionY(0);   
      setPointDirection((int)(ishikari.getPointDirection()));
      accelerate(2.5f);
      wraps = true;
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
}
class Particle extends Floater  
{
  private int life;
  Particle()
  {
      myColor = color(150,150,150);
      corners = 4;
      xCorners = new int[corners];
      yCorners = new int[corners];
      xCorners[0] = 3;
      yCorners[0] = 0;
      xCorners[1] = 3;
      yCorners[1] = 0;
      xCorners[2] = 0;
      yCorners[2] = -3;
      xCorners[3] = -3;
      yCorners[3] = 0;
      setX(ishikari.getX());
      setY(ishikari.getY());
      setDirectionX(0); 
      setDirectionY(0);   
      setPointDirection((int)(ishikari.getPointDirection()+160+(int)(Math.random()*40)));
      accelerate(1.5f);
      wraps = true;
      life = 255;
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
  public void show()
  {
    noStroke();
    fill(150,150,150,life);
    ellipse((int)(myCenterX),(int)(myCenterY),(int)((255-life)/10)+3,(int)((255-life)/10)+3);
    life = life - 5;
  }
  public int getLife(){return life;}
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
  protected boolean wraps;
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
    if(wraps == true)
    { 
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
  }
  public void show ()  //Draws the floater at the current position
  {
    fill(0);
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
  else if(keyCode == 32)
  {
    space = true;
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
  else if(keyCode == 32)
  {
    space = false;
  }
}
public void keyResponse()
{
  if(up == true)
  {
    ishikari.accelerate(0.05f);
    for(int i = 0; i < 1; i++)
    {
      exhaust.add(new Particle());
    }
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
  if(space == true && gameOver==true)
  {
    gameOver=false;
    reset();
  }
}
public void reset()
{
  flash = 255;
  jumpFuel = 10;
  up = false;
  left = false;
  right = false;
  h = false;
  space = false;
  gameOver = false;
  ishikari = new SpaceShip();
  for(int i = 0; i < skyFullOfStars.length; i++)
  {
    skyFullOfStars[i] = new star();
  }
  for(int i = 0; i < drifters.size(); i++)
  {
    drifters.set(i,new Asteroid());
  }
  timeSurvived = 0;
  for(int i = 0; i < exhaust.size(); i++)
  {
    exhaust.remove(i);
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
