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
int reload = 0;
int energy = 1000;
boolean up = false;
boolean left = false;
boolean right = false;
boolean h = false;
boolean space = false;
boolean gameOver = false;
int timeSurvived = 0;
int score = 0;
int gameOverTime = 0;
boolean alertBlink = true;
int gunToggle = 0;
String gunMode = "AUTOCANNON"; //Options: "AUTOCANNON", "SPREADGUN", "GATLING", "MULTIGUN"
private SpaceShip ishikari = new SpaceShip();
star[] skyFullOfStars = new star[100];
ArrayList <Asteroid> drifters;
ArrayList <Particle> exhaust;
ArrayList <Shot> shots;
public void setup() 
{
  size(width,height+50);
  drifters = new ArrayList <Asteroid>();
  exhaust = new ArrayList <Particle>();
  shots = new ArrayList <Shot>();
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
  if(gameOver == false)
  {
    if(reload>0){reload--;}
    if(energy<1000){energy = energy + 1;}
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
          gameOverTime = 60;
          alertBlink = false;
        }
      }
    }
    for(int o = 0; o < shots.size(); o++)
    {
      shots.get(o).move();
      shots.get(o).show();
    }
    for(int o = 0; o < shots.size(); o++)
    {
      for(int i = 0; i < drifters.size(); i++)
      {
        if(dist(drifters.get(i).getX(),drifters.get(i).getY(),shots.get(o).getX(),shots.get(o).getY())<15)
        {
          drifters.get(i).flipExist();
          shots.get(o).flipExist();
        }
      }
      for(int i = 0; i < drifters.size(); i++)
      {
        if(drifters.get(i).getExist()==false)
        {
          drifters.add(new Asteroid());
          drifters.add(new Asteroid());
          drifters.remove(i);
          score++;
        }
      }
    }
    for(int o = 0; o < shots.size(); o++)
    {
      if((shots.get(o).getX()<0 || shots.get(o).getX()>width || shots.get(o).getY()<0 || shots.get(o).getY()>height)||shots.get(o).getExist()==false)
      {
        shots.remove(o);
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
    text(gunMode + " <" + reload + ">",(int)(width/8),height+25);
    text("ENERGY <" + energy + ">",(int)(width/8*3),height+25);
    text("SCORE <" + score + ">",(int)(width/8*5),height+25);
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
    gameOverTime--;
    fill(200,50,50);
    background(0);
    textSize(32);
    text("R.I.P. DE-ISHIKARI. SCORE <" + score + ">",(int)(width/2),(int)(height/3));
    textSize(20);
    text("CONTROLS:",(int)(width/2),(int)(height/2)-50);
    text("ARROW KEYS TO TURN AND ACCELERATE",(int)(width/2),(int)(height/2)-25);
    text("<H> KEY TO INITIATE HYPERSPACE",(int)(width/2),(int)(height/2));
    text("SPACE BAR TO FIRE",(int)(width/2),(int)(height/2)+25);
    text("<F> KEY TO CYCLE THROUGH FIRING MODES",(int)(width/2),(int)(height/2)+50);
    if(gameOverTime<=0)
    {
      if(gameOverTime%15==0)
      {
        alertBlink = !alertBlink;
      }
      //fill(50,12,12);
      //textSize(35);
      //text("-PRESS SPACE TO RE-LAUNCH-",(int)(width/2),(int)(height/3*2));
      if(alertBlink == true)
      {
        fill(200,50,50);
        textSize(35);
        text("-PRESS SPACE TO RE-LAUNCH-",(int)(width/2),(int)(height/3*2));
      }
      keyResponse();
    }
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
        exist = true;
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
      exist = true;
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
  public void move()
  {
    //sets asteroid to point at player (!!!)
    // if(dist(ishikari.getX(),ishikari.getY(),getX(),getY())<300)
    // {
    //   double moveX = ishikari.getX()-myCenterX;
    //   double moveY = ishikari.getY()-myCenterY;
    //   if(moveX>0&&moveY>0){setPointDirection((int)((Math.asin(moveY))/Math.PI*360));}
    //   if(moveX>0&&moveY>0){setPointDirection((int)((Math.acos(moveX))/Math.PI*360));}
    //   if(moveX>0&&moveY>0){setPointDirection((int)((360-Math.acos(moveX))/Math.PI*360));}
    //   if(moveX>0&&moveY>0){setPointDirection((int)((Math.sin(moveY))/Math.PI*360));}
    //   accelerate(0.1);
    // }
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
    else if(wraps = false)
    {
      if(myCenterX >width || myCenterY >height || myCenterY < 0 || myCenterX<0)
      {
        exist = false;
      }
    }}
}
class Shot extends Floater
{
  Shot(int bearingAlt)
  {
      myColor = color(200,100,50);
      corners = 4;
      xCorners = new int[corners];
      yCorners = new int[corners];
      xCorners[0] = 3;
      yCorners[0] = -2;
      xCorners[1] = 3;
      yCorners[1] = 2;
      xCorners[2] = -3;
      yCorners[2] = 2;
      xCorners[3] = -3;
      yCorners[3] = -2;
      setX(ishikari.getX());
      setY(ishikari.getY());
      setDirectionX(0); 
      setDirectionY(0);   
      setPointDirection((int)(ishikari.getPointDirection())+bearingAlt);
      accelerate(10);
      wraps = true;
      exist = true;
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
  public void move ()   //move the floater in the current direction of travel
  {      
    //change the x and y coordinates by myDirectionX and myDirectionY       
    myCenterX += myDirectionX;    
    myCenterY += myDirectionY;
    accelerate(0.5f);
  }
}
class Particle extends Floater  
{
  private int life;
  Particle(int leColor)
  {
      myColor = color(leColor);
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
      accelerate(3);
      wraps = true;
      life = 255;
      exist = true;
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
    fill(25,50,100,life);
    //ellipse((int)(myCenterX),(int)(myCenterY),(int)((255-life)/10)+3,(int)((255-life)/10)+3);
    ellipse((int)(myCenterX),(int)(myCenterY),10,10);
    life = life - 15;
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
  protected boolean exist;
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
  public void flipExist(){exist = false;}
  public boolean getExist(){return exist;}
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
    else if(wraps = false)
    {
      if(myCenterX >width || myCenterY >height || myCenterY < 0 || myCenterX<0)
      {
        exist = false;
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
  else if(key == 'f')
  {
    gunToggle++;
    if(gunToggle>4)
    {
      gunToggle = 1;
    }
    if(gunToggle==1)
    {
      gunMode="SPREADGUN";
    }
    else if(gunToggle==2)
    {
      gunMode="AUTOCANNON";
    }
    else if(gunToggle==3)
    {
      gunMode="GATLING";
    }
    else if(gunToggle==4)
    {
      gunMode="MULTIGUN";
    }
  }
}
public void keyResponse()
{
  if(up == true)
  {
    ishikari.accelerate(0.05f);
    for(int i = 0; i < 1; i++)
    {
      exhaust.add(new Particle(color(50,100,200)));
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
  else if(space == true && gameOver == false && reload == 0)
  {
    if(gunMode=="SPREADGUN" && energy > 100)
    {
      shots.add(new Shot(9));
      shots.add(new Shot(6));
      shots.add(new Shot(3));
      shots.add(new Shot(0));
      shots.add(new Shot(-3));
      shots.add(new Shot(-6));
      shots.add(new Shot(-9));
      reload = reload + 15;
      energy = energy - 100;
    }
    else if(gunMode=="AUTOCANNON" && energy > 20)
    {
      shots.add(new Shot(0));
      reload = reload + 5;
      energy = energy - 10;
    }
    else if(gunMode=="GATLING" && energy > 30)
    {
      shots.add(new Shot((int)(Math.random()*16-8)));
      reload = reload + 3;
      energy = energy - 15;
    }
    else if(gunMode=="MULTIGUN" && energy > 400)
    {
      shots.add(new Shot(30));
      shots.add(new Shot(20));
      shots.add(new Shot(10));
      shots.add(new Shot(0));
      shots.add(new Shot(350));
      shots.add(new Shot(340));
      shots.add(new Shot(330));
      shots.add(new Shot(320));
      shots.add(new Shot(310));//E
      shots.add(new Shot(120));
      shots.add(new Shot(110));
      shots.add(new Shot(100));
      shots.add(new Shot(90));
      shots.add(new Shot(80));
      shots.add(new Shot(70));
      shots.add(new Shot(60));
      shots.add(new Shot(50));
      shots.add(new Shot(40));
      shots.add(new Shot(30));//E
      shots.add(new Shot(210));
      shots.add(new Shot(200));
      shots.add(new Shot(190));
      shots.add(new Shot(180));
      shots.add(new Shot(170));
      shots.add(new Shot(160));
      shots.add(new Shot(150));
      shots.add(new Shot(140));
      shots.add(new Shot(130));
      shots.add(new Shot(120));//E
      shots.add(new Shot(300));
      shots.add(new Shot(290));
      shots.add(new Shot(280));
      shots.add(new Shot(270));
      shots.add(new Shot(260));
      shots.add(new Shot(250));
      shots.add(new Shot(240));
      shots.add(new Shot(230));
      shots.add(new Shot(220));//E
      reload = reload + 160;
      energy = energy - 400;
    }
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
  while(drifters.size()>0)
  {
    drifters.remove(0);
  }
  for(int i = 0; i < 10; i++)
  {
    drifters.add(new Asteroid());
  }
  timeSurvived = 0;
  for(int i = 0; i < exhaust.size(); i++)
  {
    exhaust.remove(i);
  }
  for(int i = 0; i < shots.size(); i++)
  {
    shots.remove(i);
  }
  reload = 0;
  score = 0;
  energy = 1000;
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
