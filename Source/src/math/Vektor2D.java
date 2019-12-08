package math;

public class Vektor2D {
	public double x, y;
	
	public Vektor2D(){
		this(0.0, 0.0);
	}
	
	public Vektor2D(double x, double y){
		this.x = x;
		this.y = y;	
	}
	
	public Vektor2D(Vektor2D vec) {
		this(vec.x, vec.y);
	}
	
	void setPosition(double x, double y){
		this.x = x;
		this.y = y;		
	}
	
	public void show() {
		System.out.printf("x = %f, y = %f\n", this.x, this. y);
	}
	
	public boolean isNullVector() {
		if(this.x == 0 && this.y == 0) {
			return true;
		}
		
		return false;
	}
	
	public Vektor2D add(double x, double y) {		
		if ((this.x + x >= Double.MAX_VALUE) || (this.x + x <= Double.MAX_VALUE * (-1))) {		
			throw new RuntimeException("Speicherüberlauf");
		}
		if ((this.y + y >= Double.MAX_VALUE) || (this.y + y <= Double.MAX_VALUE * (-1))) {
			throw new RuntimeException("Speicherüberlauf");
		}
		
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vektor2D add(Vektor2D vec) {
		return this.add(vec.x, vec.y);
	}
	
	public Vektor2D sub(double x, double y) {
		return this.add(-x, -y);
	}
	
	public Vektor2D sub(Vektor2D vec) {
		return this.add(-vec.x, -vec.y);
	}
	
	public Vektor2D mult(double f) {
		if (this.x * f >= Double.MAX_VALUE || this.y * f >= Double.MAX_VALUE) {
			throw new RuntimeException("Speicherüberlauf");
		}
		
		this.x *= f;
		this.y *= f;
		return this;
	}
	
	public Vektor2D div(double d) {
		if (d == 0.0) {
			throw new java.lang.ArithmeticException("Division durch Null");
		}
		return this.mult(1.0 / d);		
	}
	
	public boolean isEqual(Vektor2D vec) {
		if(this.x == vec.x && this.y == vec.y) {
			return true;
		}
		return false;
	}
	
	public boolean isNotEqual(Vektor2D vec) {
		return !(this.isEqual(vec));
	}
	
	public double length() {
		if (this.x * this.x >= Double.MAX_VALUE || this.y * this.y >= Double.MAX_VALUE) {
			throw new RuntimeException("Speicherüberlauf");
		}		
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public Vektor2D normalize() {
		return this.div(this.length());
	}
}
