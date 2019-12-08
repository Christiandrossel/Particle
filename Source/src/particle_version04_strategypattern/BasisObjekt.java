package particle_version04_strategypattern;

public abstract class BasisObjekt {
   public int id;
   public float xPos, yPos;
   
   public BasisObjekt() {
      this(0, 0);
   }
   
   public BasisObjekt(float xPos, float yPos) {
      this.xPos = xPos;
      this.yPos = yPos;
   }
  
   public abstract void render();
}
