package math;

public class Vektor3D {
public double x, y, z;
	
	public Vektor3D(){
		this(0.0, 0.0, 0.0);
	}
	
	public Vektor3D(double x, double y, double z){
		this.x = x;
		this.y = y;	
		this.z = z;	
	}
	
	public Vektor3D(Vektor3D vec) {
		this(vec.x, vec.y, vec.z);
	}
	
	void setPosition(double x, double y, double z){
		this.x = x;
		this.y = y;	
		this.z = z;
	}
	
	public void show() {
		System.out.printf("x = %f, y = %f, z = %f\n", this.x, this.y, this.z);
	}
	
	public boolean isNullVector() {
		if(this.x == 0 && this.y == 0 && this.z == 0) {
			return true;
		}
		
		return false;
	}
	
	public Vektor3D add(double x, double y, double z) {
		
		if ((this.x + x >= Double.MAX_VALUE) || (this.x + x <= Double.MAX_VALUE * (-1))) {		
			throw new RuntimeException("Speicherüberlauf");
		}
		if ((this.y + y >= Double.MAX_VALUE) || (this.y + y <= Double.MAX_VALUE * (-1))) {
			throw new RuntimeException("Speicherüberlauf");
		}
		if ((this.z + z >= Double.MAX_VALUE) || (this.z + z <= Double.MAX_VALUE * (-1))) {
			throw new RuntimeException("Speicherüberlauf");
		}
		
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	public Vektor3D add(Vektor3D vec) {
		return this.add(vec.x, vec.y, vec.z);
	}
	
	public Vektor3D sub(double x, double y, double z) {
		return this.add(-x, -y, -z);
	}
	
	public Vektor3D sub(Vektor3D vec) {
		return this.add(-vec.x, -vec.y, -vec.z);
	}
	
	public Vektor3D mult(double f) {
		if (this.x * f >= Double.MAX_VALUE || this.y * f >= Double.MAX_VALUE  || this.z * f >= Double.MAX_VALUE) {
			throw new RuntimeException("Speicherüberlauf");
		}
		
		this.x *= f;
		this.y *= f;
		this.z *= f;
		return this;
	}
	
	public Vektor3D div(double d) {
		if (d == 0) {
			throw new java.lang.ArithmeticException("Division durch Null");
		}
		return this.mult(1.0 / d);
	}
	
	public boolean isEqual(Vektor3D vec) {
		if(this.x == vec.x && this.y == vec.y  && this.z == vec.z) {
			return true;
		}
		
		return false;
	}
	
	public boolean isNotEqual(Vektor3D vec) {
		return !(this.isEqual(vec));
	}
	
	public double length() {
		if (this.x * this.x >= Double.MAX_VALUE || this.y * this.y >= Double.MAX_VALUE || this.z * this.z >= Double.MAX_VALUE) {
			throw new RuntimeException("Speicherüberlauf");
		}
		
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}
	
	public Vektor3D normalize() {
		return this.div(this.length());
	}
}
