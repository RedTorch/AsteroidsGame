//your variable declarations here
import java.util.*;
int width = 800;
int height = 800;
int flash = 255;
int jumpFuel = 10;
int reload = 0;
int energy = 2000;
boolean up = false;
boolean left = false;
boolean right = false;
boolean h = false;
boolean space = false;
boolean gameOver = false;
int timeSurvived = 0;
int spawnSize = 40;
int score = 0;
int gameOverTime = 0;
boolean alertBlink = true;
int alertPulse = 0;
int gunToggle = 1;
String gunMode = "AUTOCANNON"; //Options: "AUTOCANNON", "SPREADGUN", "GATLING", "MULTIGUN"
private SpaceShip ishikari = new SpaceShip();
star[] skyFullOfStars = new star[100];
ArrayList <Asteroid> drifters;
ArrayList <Particle> exhaust;
ArrayList <Shot> shots;
int level = 0;
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
  for(int i = 0; i < 40; i++)
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
          alertPulse = 0;
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
          //drifters.get(i).flipExist();
          drifters.get(i).takeDam();
          shots.get(o).flipExist();
        }
      }
      for(int i = 0; i < drifters.size(); i++)
      {
        if(drifters.get(i).getExist()==false)
        {
          //drifters.add(new Asteroid());
          //drifters.add(new Asteroid());
          for(int randy = 0; randy < 10; randy++)
          {
            exhaust.add(new Particle(color(200,100,50),drifters.get(i).getX(),drifters.get(i).getY(),(int)(Math.random()*360)));
          }
          drifters.remove(i);
          score++;
        }
        if(drifters.size()==0)
        {
          //for(int yo = 0; yo < spawnSize; yo++)
          for(int yo = 0; yo < 20; yo++)
          {
            drifters.add(new Asteroid());
          }
          level++;
          spawnSize = spawnSize + 40;
          energy = 2000;
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
    text("GAME OVER. LEVEL <" + (level+1) + ">. SCORE <" + score + ">",(int)(width/2),(int)(height/3));
    textSize(20);
    text("CONTROLS:",(int)(width/2),(int)(height/2)-50);
    text("ARROW KEYS TO TURN AND ACCELERATE",(int)(width/2),(int)(height/2)-25);
    text("<H> KEY TO INITIATE HYPERSPACE",(int)(width/2),(int)(height/2));
    text("SPACE BAR TO FIRE",(int)(width/2),(int)(height/2)+25);
    text("<F> KEY TO CYCLE THROUGH FIRING MODES",(int)(width/2),(int)(height/2)+50);
    if(gameOverTime<=0)
    {
      fill(200,50,50,alertPulse);
      textSize(35);
      text("-PRESS SPACE TO RE-LAUNCH-",(int)(width/2),(int)(height/3*2));
      keyResponse();
      if(alertPulse<=0)
      {
        alertBlink = true;
      }
      else if(alertPulse>=255)
      {
        alertBlink = false;
      }
      if(alertBlink == true)
      {
        alertPulse = alertPulse + 15;
      }
      else if(alertBlink == false)
      {
        alertPulse = alertPulse - 15;
      }
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
  private int randDir;
  private int myLife;
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
      randDir = (int)(Math.random()*100);
      if(randDir<50)
      {
        if((int)(Math.random()*100)<50)
        {
          setX((int)(Math.random()*width));
          setY(height + 100);
        }
        else
        {
          setY((int)(Math.random()*height));
          setX(height + 100);
        }
      }
      else if(randDir>=50 && randDir<75)
      {
        setX((int)(Math.random()*60)+ishikari.getX()-30);
        setY(height + 100);
        if((int)(Math.random()*100)<50){setPointDirection((int)(Math.random()*20)+80);}
        else{setPointDirection((int)(Math.random()*20)-100);}
      }
      else
      {
        setY((int)(Math.random()*60)+ishikari.getY()-30);
        setX(height + 100);
        if((int)(Math.random()*100)<50){setPointDirection((int)(Math.random()*20)+170);}
        else{setPointDirection((int)(Math.random()*20)-10);}
      }
      setDirectionX(0); 
      setDirectionY(0);   
      setPointDirection((int)(Math.random()*360));
      accelerate((int)(Math.random()*2)+2.5);
      wraps = true;
      exist = true;
      myLife = 8+(level*4);
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
  public void takeDam()
  {
    myLife--;
    for(int randy = 0; randy < 5; randy++)
    {
      exhaust.add(new Particle(color(200,100,50),(int)(myCenterX),(int)(myCenterY),(int)(Math.random()*360)));
    }
    if(myLife==0)
    {
      flipExist();
    }
  }
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
    accelerate(0.5);
  }
}
class Particle extends Floater  
{
  private int life;
  Particle(color leColor,int x,int y,int bearing)
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
      setX(x);
      setY(y);
      setDirectionX(0); 
      setDirectionY(0);   
      setPointDirection(bearing);
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
    fill(red(myColor),green(myColor),blue(myColor),life);
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
  void show()
  {
    stroke(255);
    ellipse(myX,myY,3,3);
    if(myX > width){myX = 0;}
    else if(myX < 0){myX = width;}
    if(myY > height){myY = 0;}
    else if(myY < 0){myY = height;}
  }
}
void keyPressed()
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
void keyReleased()
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
void keyResponse()
{
  if(up == true)
  {
    ishikari.accelerate(0.1);
    for(int i = 0; i < 1; i++)
    {
      exhaust.add(new Particle(color(50,100,200),ishikari.getX(),ishikari.getY(),(int)(ishikari.getPointDirection()+160+(int)(Math.random()*40))));
    }
  }
  if(left == true)
  {
    ishikari.setPointDirection((int)(ishikari.getPointDirection()-8));
  }
  if(right == true)
  {
    ishikari.setPointDirection((int)(ishikari.getPointDirection()+8));
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
    if(gunMode=="SPREADGUN" && energy > 25)
    {
      shots.add(new Shot(9));
      shots.add(new Shot(6));
      shots.add(new Shot(3));
      shots.add(new Shot(0));
      shots.add(new Shot(-3));
      shots.add(new Shot(-6));
      shots.add(new Shot(-9));
      reload = reload + 5;
      energy = energy - 12;
    }
    else if(gunMode=="AUTOCANNON" && energy > 10)
    {
      shots.add(new Shot(-2));
      shots.add(new Shot(-1));
      shots.add(new Shot(0));
      shots.add(new Shot(1));
      shots.add(new Shot(2));
      reload = reload + 4;
      energy = energy - 5;
    }
    else if(gunMode=="GATLING" && energy > 6)
    {
      shots.add(new Shot((int)(Math.random()*16-8)));
      shots.add(new Shot((int)(Math.random()*16-8)));
      reload = reload + 2;
      energy = energy - 3;
    }
    else if(gunMode=="MULTIGUN" && energy > 250)
    {
      for(int i = 0; i < 360; i = i + 2){shots.add(new Shot(i));}
      reload = reload + 120;
      energy = energy - 100;
    }
  }
}
void reset()
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
  //for(int i = 0; i < spawnSize; i++)
  for(int i = 0; i < 20; i++)
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
  energy = 2000;
  spawnSize = 40;
  level = 0;
}