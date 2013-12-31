import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
import java.awt.image.ColorModel;
import javax.swing.*;

public class breakout extends GraphicsProgram{
	/** Width and height of application window in pixel */
	public static final int APPLICATION_WIDTH=400;
	public static final int APPLICATION_HEIGHT=600;
	/** Dimension of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;
	/** Dimensions of paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;
	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;
	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;
	/** Number of rows of bricks*/
	private static final int NBRICK_ROWS = 10;
	/** Separation between bricks */
	private static final int BRICK_SEP=4;
	/** Width of a brick*/
	private static final int BRICK_WIDTH = (WIDTH-(NBRICKS_PER_ROW-1)*BRICK_SEP)/NBRICKS_PER_ROW;
	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;
	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;
	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;
	/** Number of turns */
	private static final int NTURNS = 3;
	/** paddle*/
	private static GRect paddle_cover,paddle_body;
	/** ball */
	private static SpecialBall ball;
	private static double x_ball,y_ball;//left up coordinate of the ball
	private static double x_move,y_move;
	private static int count=0,total=NBRICKS_PER_ROW*NBRICK_ROWS;
	private static double k,b; //path:y=kx+b;
	private static double MOVE = 0;
	private static double MOUTH_LOCATION = 0;
	private static boolean check=true;
	GLabel introduction1 = new GLabel("This is a silly arkanoid game, or breakout game, ",60,WIDTH*2/3);
	GLabel introduction2 = new GLabel("or whatever you want to call it.",100,WIDTH*2/3+10);
	private RandomGenerator rgen = RandomGenerator.getInstance();
	public void run(){
		this.setSize(WIDTH, HEIGHT);
		explanation();
		setBoard();
		addMouseListeners();
		//GLabel test1=new GLabel(x_ball+"   "+y_ball,0,10);
		//add(test1);
		for(int i=0;i<=NTURNS && count<total;i++){
			setBall();
			setPaddle();
			y_move=rgen.nextDouble(1,5);
			x_move=Math.sqrt(36-y_move*y_move);
			if(rgen.nextBoolean())
				x_move=-x_move;
			k=y_move/x_move;
			b=y_ball-k*x_ball;
			MOVE=0;
			ball.setRotation(0);
			while(judge()){
				moveBall();
				checkCollide();
				ball.rotation();
				//test1.setLabel(x_ball+"   "+y_ball);
				pause(30);
			}
			remove(ball);
			remove(paddle_body);
			remove(paddle_cover);
		}
	}
	private void explanation(){
		setBoard();
		setBall();
		setPaddle();
		y_move=rgen.nextDouble(2,4);
		x_move=Math.sqrt(36-y_move*y_move);
		if(rgen.nextBoolean())
			x_move=-x_move;
		k=y_move/x_move;
		b=y_ball-k*x_ball;
		MOVE=0;
		ball.setRotation(0);
		add(introduction1);
		add(introduction2);
		pause(3000);
		remove(introduction2);
		remove(introduction1);
		introduction1=new GLabel("The only interesting thing is the ball can rotate",70,WIDTH*2/3);
		introduction2 = new GLabel(" and change direction when it bounces.",90,WIDTH*2/3+10);
		add(introduction1);
		add(introduction2);
		pause(3000);
		while(y_ball+BALL_RADIUS+1<HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT && judge()){
			moveBall();
			checkCollide();
			movePaddle(x_ball-PADDLE_WIDTH/2);
			ball.rotation();
			pause(30);
		}
		remove(introduction2);
		remove(introduction1);
		introduction1=new GLabel("When the ball hits the paddle",110,WIDTH*2/3);
		introduction2 = new GLabel("you can move the paddle to make the ball rotate",60,WIDTH*2/3+10);
		add(introduction1);
		add(introduction2);
		pause(3000);
		int counter=0;
		while(counter<=750){
			if(y_ball+BALL_RADIUS+1>=HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT) {
				MOVE=rgen.nextDouble(1,5);
				ball.setRotation(MOVE);
				y_move=-y_move;
				k=y_move/x_move;
				b=y_ball-k*x_ball;
			}
			else
				checkCollide();
			moveBall();
			movePaddle(x_ball-PADDLE_WIDTH/2);
			ball.rotation();
			pause(30);
			counter++;
			if(counter==150){
				remove(introduction1);
				remove(introduction2);
				introduction1=new GLabel("The ball's angular velocity",110,WIDTH*2/3);
				introduction2 = new GLabel("will depend on the speed of paddle",100,WIDTH*2/3+10);
				add(introduction1);
				add(introduction2);}
			else if(counter==300){
				remove(introduction2);
				remove(introduction1);
				introduction1=new GLabel("When the rotating ball hits the wall or break",60,WIDTH*2/3);
				introduction2 = new GLabel("its angular velocity will decrease",100,WIDTH*2/3+10);
				add(introduction1);
				add(introduction2);}
			else if(counter==450){
				remove(introduction2);
				remove(introduction1);
				introduction1=new GLabel("Meanwhile the direction of the ball's movement",60,WIDTH*2/3);
				introduction2 = new GLabel("will be different from when it's not rotating",60,WIDTH*2/3+10);
				add(introduction1);
				add(introduction2);}
			else if(counter==600){
				remove(introduction2);
				remove(introduction1);
				introduction1=new GLabel("Also the specific value of angular velocity",60,WIDTH*2/3);
				introduction2 = new GLabel("will affect specific direction",100,WIDTH*2/3+10);
				add(introduction1);
				add(introduction2);}
		}
		remove(ball);
		remove(paddle_body);
		remove(paddle_cover);
		remove(introduction2);
		remove(introduction1);
		introduction1=new GLabel("Now, move the mouse, it's your turn",70,WIDTH*2/3);
		introduction2 = new GLabel("GOOD LUCK",130,WIDTH*2/3+10);
		add(introduction1);
		add(introduction2);
		pause(3000);
		remove(introduction2);
		remove(introduction1);
	}
	public void mouseMoved(MouseEvent e) {
		MOUTH_LOCATION=e.getX();
	//	int y=e.getY();
		movePaddle((int)MOUTH_LOCATION-PADDLE_WIDTH/2);
	}
	private void setBoard() {
		for(int i=0;i<NBRICK_ROWS;i++) {
			int color = NBRICK_ROWS/5; 
			for(int j=0;j<NBRICKS_PER_ROW;j++) {
				/*x variable of the first column*/
				double first=(WIDTH-BRICK_WIDTH*NBRICKS_PER_ROW-BRICK_SEP*(NBRICKS_PER_ROW-1))/2;
				GRect brick=new GRect(j*(BRICK_WIDTH+BRICK_SEP)+first,BRICK_Y_OFFSET+i*(BRICK_HEIGHT+BRICK_SEP),BRICK_WIDTH,BRICK_HEIGHT);
				brick.setFilled(true);
				if(i/color==0) {
					brick.setColor(Color.red);
					brick.setFillColor(Color.red);
				}
				else if(i/color==1) {
					brick.setColor(Color.orange);
					brick.setFillColor(Color.orange);
				}
				else if(i/color==2) {
					brick.setColor(Color.yellow);
					brick.setFillColor(Color.yellow);
				}
				else if(i/color==3) {
					brick.setColor(Color.green);
					brick.setFillColor(Color.green);
				}
				else {
					brick.setColor(Color.cyan);
					brick.setFillColor(Color.cyan);
				}
				GObject CHECK=getElementAt(j*(BRICK_WIDTH+BRICK_SEP)+first+1,BRICK_Y_OFFSET+i*(BRICK_HEIGHT+BRICK_SEP)+1);
				if(CHECK==null)
					add(brick);
			}
		}
	}
	private void setPaddle() {
		paddle_cover=new GRect((WIDTH-PADDLE_WIDTH)/2,HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT,PADDLE_WIDTH,PADDLE_HEIGHT/2);
		paddle_cover.setFilled(true);
		paddle_cover.setColor(Color.cyan);
		paddle_cover.setFillColor(Color.cyan);
		paddle_body=new GRect((WIDTH-PADDLE_WIDTH)/2,HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT,PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle_body.setFilled(true);
		paddle_body.setFillColor(Color.black);
		add(paddle_body);
		add(paddle_cover);
	}
	private void movePaddle(double x) {
		paddle_cover.setLocation(x,HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT);
		paddle_body.setLocation(x,HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT);
	}
	private void setBall() {
		ball=new SpecialBall(BALL_RADIUS);
		y_ball=HEIGHT/2;
		x_ball=WIDTH/2;
		ball.setLocation(x_ball,y_ball);
		add(ball);
	}
    private void moveBall() {
    	ball.move (x_move,y_move);
    	x_ball=x_ball+x_move;
    	y_ball=calculate(x_ball);
    }
    private boolean judge() {
    	if(count>=total)
    		return false;
    	else if(y_ball>=HEIGHT)
    		return false;
    	else
    		return true;
    }
	private double calculate (double x) {
		return k*x+b;
	}
	private void checkCollide() {
		double initial = MOUTH_LOCATION;
		GObject left=getElementAt(x_ball-BALL_RADIUS-1, y_ball);
		GObject right=getElementAt(x_ball+BALL_RADIUS+1, y_ball);
		GObject up=getElementAt(x_ball, y_ball-BALL_RADIUS-1);
		GObject down=getElementAt(x_ball, y_ball+BALL_RADIUS+1);
		if(left==introduction1 || left==introduction2 || right==introduction1 || right==introduction2 || up==introduction1 || up==introduction2 || down==introduction1 || down==introduction2) {
			left=null;
			right=null;
			up=null;
			down=null;
		}
		if(x_ball-BALL_RADIUS-1<=1) {
			MOVE=MOVE*0.7;
			y_move=y_move-MOVE;
			if(y_move>5.5)
				y_move=5.5;
			else if (y_move<-5.5)
				y_move=-5.5;
			x_move=Math.sqrt(36-y_move*y_move);
			if(x_move>5.5)
				x_move=5.5;
			k=y_move/x_move;
			b=y_ball-k*x_ball;
			ball.setRotation(MOVE);
		}
		if(x_ball+BALL_RADIUS+1>=WIDTH) {
			MOVE=MOVE*0.7;
			y_move=y_move+MOVE;
			if(y_move>5.5)
				y_move=5.5;
			else if (y_move<-5.5)
				y_move=-5.5;
			x_move=-Math.sqrt(36-y_move*y_move);
			if(x_move<-5.5)
				x_move=-5.5;
			k=y_move/x_move;
			b=y_ball-k*x_ball;
			ball.setRotation(MOVE);
		}
		if (y_ball-BALL_RADIUS-1<=1) {
			MOVE=MOVE*0.7;
			x_move=x_move+MOVE;
			if(x_move>5.5)
				x_move=5.5;
			if(x_move<-5.5)
				x_move=-5.5;
			y_move=Math.sqrt(36-x_move*x_move);
			k=y_move/x_move;
			b=y_ball-k*x_ball;
			ball.setRotation(MOVE);
		}
		if(y_ball+BALL_RADIUS+1>=HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT && y_ball<HEIGHT-PADDLE_Y_OFFSET-PADDLE_HEIGHT) {
			if(x_ball<=MOUTH_LOCATION+PADDLE_WIDTH/2 && x_ball>=MOUTH_LOCATION-PADDLE_WIDTH/2 && check){
				pause(5);
				double last = MOUTH_LOCATION;
				MOVE=MOVE+(initial-last);
				MOVE=MOVE*0.7;
				ball.move(last-initial, 0);
				x_ball=x_ball+last-initial;
				x_move=x_move-MOVE;
				if(x_move>5.5)
					x_move=5.5;
				if(x_move<-5.5)
					x_move=-5.5;
				y_move=-Math.sqrt(36-x_move*x_move);
				k=y_move/x_move;
				b=y_ball-k*x_ball;
				check=false;
				ball.setRotation(MOVE);
			}
		}
		else {
			check=true;
			if (left!=null && left!=paddle_body && left!= paddle_cover) {
				remove(left);
				MOVE=MOVE*0.7;
				y_move=y_move-MOVE;
				if(y_move>5.5)
					y_move=5.5;
				else if (y_move<-5.5)
					y_move=-5.5;
				if(x_move>0)
					x_move=Math.sqrt(36-y_move*y_move);
				else
					x_move=-Math.sqrt(36-y_move*y_move);
				if(x_move>5.5)
					x_move=5.5;
				if(x_move<-5.5)
					x_move=-5.5;
				k=y_move/x_move;
				ball.setRotation(MOVE);
				count++;
			}
			if (right!=null && right!=paddle_body && right!= paddle_cover) {
				remove(right);
				MOVE=MOVE*0.7;
				y_move=y_move+MOVE;
				if(y_move>5.5)
					y_move=5.5;
				else if (y_move<-5.5)
					y_move=-5.5;
				if(x_move>0)
					x_move=Math.sqrt(36-y_move*y_move);
				else
					x_move=Math.sqrt(36-y_move*y_move);
				if(x_move>5.5)
					x_move=5.5;
				if(x_move<-5.5)
					x_move=-5.5;
				k=y_move/x_move;
				count++;
				ball.setRotation(MOVE);
			}
			if (up!=null && up!=paddle_body && up!= paddle_cover) {
				remove(up);
				MOVE=MOVE*0.7;
				x_move=x_move+MOVE;
				if(x_move>5.5)
					x_move=5.5;
				if(x_move<-5.5)
					x_move=-5.5;
				y_move=Math.sqrt(36-x_move*x_move);
				k=y_move/x_move;
				count++;
				ball.setRotation(MOVE);
			}
			if (down!=null && down!=paddle_body && down!= paddle_cover) {
				remove(down);
				MOVE=MOVE*0.7;
				x_move=x_move-MOVE;
				if(x_move>5.5)
					x_move=5.5;
				if(x_move<-5.5)
					x_move=-5.5;
				y_move=-Math.sqrt(36-x_move*x_move);
				k=y_move/x_move;
				count++;
				ball.setRotation(MOVE);
			}
		}
		b=y_ball-k*x_ball;
	}

}