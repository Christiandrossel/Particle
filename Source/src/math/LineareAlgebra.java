package math;

public class LineareAlgebra {
	
	private LineareAlgebra() {}
	
	public static Vektor2D add(Vektor2D vec1, Vektor2D vec2)
	{
		if ((vec1.x + vec2.x >= Double.MAX_VALUE) || (vec1.x + vec2.x <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		if ((vec1.y + vec2.y >= Double.MAX_VALUE) || (vec1.y + vec2.y <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");	
		}
		
		return new Vektor2D(vec1.x + vec2.x, 
							vec1.y + vec2.y);
	}
	
	public static Vektor3D add(Vektor3D vec1, Vektor3D vec2)
	{
		if ((vec1.x + vec2.x >= Double.MAX_VALUE) || (vec1.x + vec2.x <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		if ((vec1.y + vec2.y >= Double.MAX_VALUE) || (vec1.y + vec2.y <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		if ((vec1.z + vec2.z >= Double.MAX_VALUE) || (vec1.z + vec2.z <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");	
		}
		
		return new Vektor3D(vec1.x + vec2.x, 
							vec1.y + vec2.y, 
							vec1.z + vec2.z);
	}
	
	public static Vektor2D sub(Vektor2D vec1, Vektor2D vec2)
	{
		vec2.x = -vec2.x;
		vec2.y = -vec2.y;
			
	return (add(vec1, vec2));
	}
	
	public static Vektor3D sub(Vektor3D vec1, Vektor3D vec2)
	{
		vec2.x = -vec2.x;
		vec2.y = -vec2.y;
		vec2.z = -vec2.z;
			
	return (add(vec1, vec2));
	}
	
	public static Vektor2D mult(Vektor2D vec, double s)
	{
		if ((vec.x * s >= Double.MAX_VALUE) || (vec.x * s <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		if ((vec.y * s >= Double.MAX_VALUE) || (vec.y * s <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		
		return new Vektor2D(vec.x * s, 
							vec.y * s);
	}
	
	public static Vektor3D mult(Vektor3D vec, double s)
	{
		if ((vec.x * s >= Double.MAX_VALUE) || (vec.x * s <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		if ((vec.y * s >= Double.MAX_VALUE) || (vec.y * s <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		if ((vec.z * s >= Double.MAX_VALUE) || (vec.z * s <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		
		return new Vektor3D(vec.x * s, 
							vec.y * s, 
							vec.z * s);
	}
	
	public static Vektor2D div(Vektor2D vec, double s)
	{
		if(s == 0.0)
			throw new java.lang.ArithmeticException("Division durch Null");
	
		return (mult(vec, 1/s));
	}
	
	public static Vektor3D div(Vektor3D vec, double s)
	{
		if(s == 0.0) {
			throw new java.lang.ArithmeticException("Division durch Null");
		}
		
		return (mult(vec, 1/s));
	}

	public static boolean isEqual(Vektor2D vec1, Vektor2D vec2)
	{
		if (vec1.x != vec2.x || vec1.y != vec2.y) {
			return false;
		}	
		return true;
	}
	
	public static boolean isEqual(Vektor3D vec1, Vektor3D vec2)
	{
		if (vec1.x != vec2.x || vec1.y != vec2.y || vec1.z != vec2.z) {
			return false;
		}
		
		return true;
	}
	
	public static boolean isNotEqual(Vektor2D vec1, Vektor2D vec2)
	{
		return !isEqual(vec1, vec2);
	}
	
	public static boolean isNotEqual(Vektor3D vec1, Vektor3D vec2)
	{	
		return !isEqual(vec1, vec2);
	}
	
	public static double length(Vektor2D vec1)
	{	
		if ((vec1.x * vec1.x >= Double.MAX_VALUE) || (vec1.y * vec1.y >= Double.MAX_VALUE)) {
			throw new RuntimeException("Speicherüberlauf");	
		}
	
		Vektor2D vec2 = new Vektor2D();
		vec2.x = Math.pow(vec1.x, 2);
		vec2.y = Math.pow(vec1.y, 2);
		
		if (vec2.x + vec2.y >= Double.MAX_VALUE) {
			throw new RuntimeException("Speicherüberlauf");	
		}
		
		double erg = vec2.x + vec2.y;
		return Math.sqrt(erg);
	}
	
	public static double length(Vektor3D vec1)
	{	
		if ((vec1.x * vec1.x >= Double.MAX_VALUE) ||
			(vec1.y * vec1.y >= Double.MAX_VALUE) ||
			(vec1.z * vec1.z >= Double.MAX_VALUE)) {
				throw new RuntimeException("Speicherüberlauf");	
		}
	
		Vektor3D vec2 = new Vektor3D();
		vec2.x = Math.pow(vec1.x, 2);
		vec2.y = Math.pow(vec1.y, 2);
		vec2.z = Math.pow(vec1.z, 2);
		
		if (vec2.x + vec2.y + vec2.z >= Double.MAX_VALUE) {
			throw new RuntimeException("Speicherüberlauf");	
		}
		
		double erg = vec2.x + vec2.y + vec2.z;
			
		return Math.sqrt(erg);
	}
	
	public static Vektor3D normalize(Vektor3D vec)
	{
		return new Vektor3D(div(vec, length(vec)));	
	}
	
	public static Vektor2D normalize(Vektor2D vec)
	{
		return new Vektor2D(div(vec, length(vec)));	
	}
	
	public static double euklDistance(Vektor2D vec1, Vektor2D vec2)
	{
		if ((vec1.x - vec2.x >= Double.MAX_VALUE) || (vec1.x - vec2.x <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("Speicherüberlauf");
		}
		if ((vec1.y - vec2.y >= Double.MAX_VALUE) || (vec1.y - vec2.y <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("Speicherüberlauf");
		}
		
		Vektor2D vec = new Vektor2D(vec2.x - vec1.x, vec2.y - vec1.y);

		return length(vec);
	}
	
	public static double euklDistance(Vektor3D vec1, Vektor3D vec2)
	{
		if ((vec1.x - vec2.x >= Double.MAX_VALUE) || (vec1.x - vec2.x <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("Speicherüberlauf");
		}
		if ((vec1.y - vec2.y >= Double.MAX_VALUE) || (vec1.y - vec2.y <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("Speicherüberlauf");
		}
		if ((vec1.z - vec2.z >= Double.MAX_VALUE) || (vec1.z - vec2.z <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("Speicherüberlauf");
		}
		
		Vektor3D vec = new Vektor3D(vec2.x - vec1.x, vec2.y - vec1.y, vec2.z - vec1.z);
		
		return length(vec);
	}
	
	public static double manhattanDistance(Vektor2D vec1, Vektor2D vec2)
	{
		Vektor2D vec = new Vektor2D();
		
		if ((vec1.x - vec2.x >= Double.MAX_VALUE) || (vec1.x - vec2.x <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("Speicherüberlauf");
		}
		if ((vec1.y - vec2.y >= Double.MAX_VALUE) || (vec1.y - vec2.y <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("Speicherüberlauf");
		}

		vec.x = Math.abs(vec1.x - vec2.x);
		vec.y = Math.abs(vec1.y - vec2.y);
		
		return (vec.x + vec.y);
	}
	
	public static double manhattanDistance(Vektor3D vec1, Vektor3D vec2)
	{
		Vektor3D vec = new Vektor3D();
		
		if ((vec1.x - vec2.x >= Double.MAX_VALUE) || (vec1.x - vec2.x <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("Speicherüberlauf");
		}
		if ((vec1.y - vec2.y >= Double.MAX_VALUE) || (vec1.y - vec2.y <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("Speicherüberlauf");
		}
		if ((vec1.z - vec2.z >= Double.MAX_VALUE) || (vec1.z - vec2.z <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("Speicherüberlauf");
		}
		vec.x = Math.abs(vec1.x - vec2.x);
		vec.y = Math.abs(vec1.y - vec2.y);
		vec.z = Math.abs(vec1.z - vec2.z);
		
		return (vec.x + vec.y + vec.z);
	}
	
	public static double crossProduct(Vektor2D vec1, Vektor2D vec2) {
		return determinante(vec1, vec2);
	}

	public static Vektor3D crossProduct(Vektor3D vec1, Vektor3D vec2)
	{
		Vektor3D vec = new Vektor3D();
		
		if ((vec1.x * vec2.x >= Double.MAX_VALUE) || (vec1.x * vec2.x <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");	
		}
		if ((vec1.y * vec2.y >= Double.MAX_VALUE) || (vec1.y * vec2.y <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");	
		}
		if ((vec1.z * vec2.z >= Double.MAX_VALUE) || (vec1.z * vec2.z <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		
		vec.x = vec1.y * vec2.z - vec1.z * vec2.y;
		vec.y = vec1.z * vec2.x - vec1.x * vec2.z;
		vec.z = vec1.x * vec2.y - vec1.y * vec2.x;
		
		return vec;
	}
	
	public static double dotProduct(Vektor2D vec1, Vektor2D vec2) {
		if ((vec1.x * vec2.x >= Double.MAX_VALUE) || (vec1.x * vec2.x <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		if ((vec1.y * vec2.y >= Double.MAX_VALUE) || (vec1.y * vec2.y <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		if ((vec1.x * vec2.x + vec1.y * vec2.y >= Double.MAX_VALUE) || (vec1.x * vec2.x + vec1.y * vec2.y <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		
		return (vec1.x * vec2.x + vec1.y * vec2.y);
	}
	
	public static double dotProduct(Vektor3D vec1, Vektor3D vec2) {
		if ((vec1.x * vec2.x >= Double.MAX_VALUE) || (vec1.x * vec2.x <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		if ((vec1.y * vec2.y >= Double.MAX_VALUE) || (vec1.y * vec2.y <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		if ((vec1.z * vec2.z >= Double.MAX_VALUE) || (vec1.z * vec2.z <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		if ((vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z >= Double.MAX_VALUE) || 
			(vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z <= (-1) * Double.MAX_VALUE)) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		
		return (vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z);
	}
	
	public static double cosEquation(Vektor2D vec1, Vektor2D vec2) {
		
		return dotProduct(normalize(vec1), normalize(vec2));
	}
	
	public static double cosEquation(Vektor3D vec1, Vektor3D vec2) {
		
		return dotProduct(normalize(vec1), normalize(vec2));
	}
	
	public static double sinEquation (Vektor2D vec1, Vektor2D vec2) {
		if (length(vec1) == 0 || length(vec2) == 0) {
			throw new java.lang.ArithmeticException("Division durch Null");
		}
		
		return Math.abs(crossProduct(vec1, vec2)) / (length(vec1) * length(vec2));
	}
	
	public static double sinEquation (Vektor3D vec1, Vektor3D vec2) {
		if (length(vec1) == 0 || length(vec2) == 0) {
			throw new java.lang.ArithmeticException("Division durch Null");
		}
		
		return length(crossProduct(vec1, vec2)) / (length(vec1) * length(vec2));
	}
	
	public static double angleRad (Vektor2D vec1, Vektor2D vec2) {
		return Math.acos(cosEquation (vec1, vec2));
	}
	
	public static double angleRad (Vektor3D vec1, Vektor3D vec2) {
		return Math.acos(cosEquation (vec1, vec2));
	}
	
	public static double angleDegree(Vektor2D vec1, Vektor2D vec2) {
		return radToDegree(Math.acos(cosEquation(vec1, vec2)));
	}
	
	public static double angleDegree(Vektor3D vec1, Vektor3D vec2) {
		return radToDegree(Math.acos(cosEquation(vec1, vec2)));
	}
	
	public static double radToDegree (double rad) {
		if ((rad < 0.0) || (rad >= Math.PI * 2.0d)) {
			throw new RuntimeException("falscher Wertebereich [0, 2pi)");
		}
		
		return (rad * 180.0d / Math.PI);
	}
	
	public static double degreeToRad (double degree) {
		if ((degree < 0.0) || (degree >= 360.0)) {
			throw new RuntimeException("falscher Wertebereich [0, 2pi)");
		}
		
		return (degree * Math.PI / 180.0d);
	}
	
	public static double determinante (Vektor2D vec1, Vektor2D vec2) {
		if (vec1.x * vec2.y >= Double.MAX_VALUE ||
			vec1.y * vec2.x >= Double.MAX_VALUE ||
			vec1.x * vec2.y - vec1.y * vec2.x >= Double.MAX_VALUE ||
			vec1.x * vec2.y <= (-1) * Double.MAX_VALUE ||
			vec1.y * vec2.x <= (-1) * Double.MAX_VALUE ||
			vec1.x * vec2.y - vec1.y * vec2.x <= (-1) * Double.MAX_VALUE) {
			throw new RuntimeException("SpeicherÃ¼berlauf");
		}
		
		return (vec1.x * vec2.y - vec1.y * vec2.x);
	}
	
	public static double determinante (Vektor3D vec1, Vektor3D vec2, Vektor3D vec3) {
		if (vec1.x * vec2.y * vec3.z >= Double.MAX_VALUE ||
			vec2.x * vec3.y * vec1.z >= Double.MAX_VALUE ||
			vec3.x * vec1.y * vec2.z >= Double.MAX_VALUE ||
			vec1.x * vec2.y * vec3.z <= (-1) * Double.MAX_VALUE ||
			vec2.x * vec3.y * vec1.z <= (-1) * Double.MAX_VALUE ||
			vec3.x * vec1.y * vec2.z <= (-1) * Double.MAX_VALUE ||
			vec1.z * vec2.y * vec3.x >= Double.MAX_VALUE ||
			vec2.z * vec3.y * vec1.x >= Double.MAX_VALUE ||
			vec3.z * vec1.y * vec2.x >= Double.MAX_VALUE ||
			vec1.z * vec2.y * vec3.x <= (-1) * Double.MAX_VALUE ||
			vec2.z * vec3.y * vec1.x <= (-1) * Double.MAX_VALUE ||
			vec3.z * vec1.y * vec2.x <= (-1) * Double.MAX_VALUE) {
				throw new RuntimeException("SpeicherÃ¼berlauf");
		}
			
		
		return (  vec1.x * vec2.y * vec3.z
				+ vec2.x * vec3.y * vec1.z
				+ vec3.x * vec1.y * vec2.z
				- vec1.z * vec2.y * vec3.x
				- vec2.z * vec3.y * vec1.x
				- vec3.z * vec1.y * vec2.x);
	}
	
	public static Vektor2D abs (Vektor2D vec) {
		return new Vektor2D(Math.abs(vec.x), Math.abs(vec.y));
	}
	
	public static Vektor3D abs (Vektor3D vec) {		
		return new Vektor3D(Math.abs(vec.x), Math.abs(vec.y), Math.abs(vec.z));
	}
	
	public static void show (Vektor2D vec) {
		System.out.println("Der Vektor lautet:\nx = " + vec.x + "\ny = " + vec.y);
	}
	
	public static void show (Vektor3D vec) {
		System.out.println("Der Vektor lautet:\nx = " + vec.x + "\ny = " + vec.y + "\nz = " + vec.z);
	}
	
	public static void main(String args[])
	{
		Vektor2D vektor1 = new Vektor2D(5, 2);
		Vektor2D vektor2 = new Vektor2D(2, 1);
		
		Vektor2D vektor3 = sub(vektor1, vektor2);
		//boolean x =  isNotEqual(vektor1, vektor2);
		//double x = manhattanDistance(vektor1, vektor2);
		System.out.println(vektor3.x);
		System.out.println(vektor3.y);

		//if (vektor3 == null)
		//	System.out.print(" ! Angegebene Vektore haben nicht die gleiche lÃ¤nge ! ");
		//else		
		
	}
	
}