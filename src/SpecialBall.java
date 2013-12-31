import acm.graphics.*;

public class SpecialBall extends GCompound{
	private static GImage ball;
	private static double rotation=0;
	private static int x=9; 
	public SpecialBall(int radius) {
		ball=new GImage("F:\\Java\\HellowWorld\\images\\0.gif",-radius,-radius);
		ball.setSize(2*radius, 2*radius);
		add(ball);
	}
	public void setRotation (double temp) {
		rotation=temp;
	}
	public void rotation(){
		boolean temp=false;
		x=x+(int)(rotation+0.5);
		if(x<0){
			temp=true;
			Math.abs(x);
		}
		if(x>35)
			x=x%36;
		if(temp) x=36-x;
		if(x==0)
			ball.setImage("0.gif");
		else if(x==1)
			ball.setImage("10.gif");
		else if(x==2)
			ball.setImage("20.gif");
		else if(x==3)
			ball.setImage("30.gif");
		else if(x==4)
			ball.setImage("40.gif");
		else if(x==5)
			ball.setImage("50.gif");
		else if(x==6)
			ball.setImage("60.gif");
		else if(x==7)
			ball.setImage("70.gif");
		else if(x==8)
			ball.setImage("80.gif");
		else if(x==9)
			ball.setImage("90.gif");
		else if(x==10)
			ball.setImage("100.gif");
		else if(x==11)
			ball.setImage("110.gif");
		else if(x==12)
			ball.setImage("120.gif");
		else if(x==13)
			ball.setImage("130.gif");
		else if(x==14)
			ball.setImage("140.gif");
		else if(x==15)
			ball.setImage("150.gif");
		else if(x==16)
			ball.setImage("160.gif");
		else if(x==17)
			ball.setImage("170.gif");
		else if(x==18)
			ball.setImage("180.gif");
		else if(x==19)
			ball.setImage("190.gif");
		else if(x==20)
			ball.setImage("200.gif");
		else if(x==21)
			ball.setImage("210.gif");
		else if(x==22)
			ball.setImage("220.gif");
		else if(x==23)
			ball.setImage("230.gif");
		else if(x==24)
			ball.setImage("240.gif");
		else if(x==25)
			ball.setImage("250.gif");
		else if(x==26)
			ball.setImage("260.gif");
		else if(x==27)
			ball.setImage("270.gif");
		else if(x==28)
			ball.setImage("280.gif");
		else if(x==29)
			ball.setImage("290.gif");
		else if(x==30)
			ball.setImage("300.gif");
		else if(x==31)
			ball.setImage("310.gif");
		else if(x==32)
			ball.setImage("320.gif");
		else if(x==33)
			ball.setImage("330.gif");
		else if(x==34)
			ball.setImage("340.gif");
		else if(x==35)
			ball.setImage("350.gif");
	}
}
