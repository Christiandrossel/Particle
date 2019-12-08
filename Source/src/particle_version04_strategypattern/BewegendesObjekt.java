package particle_version04_strategypattern;

public abstract class BewegendesObjekt extends BasisObjekt {
   public float ySpeed;
   public Verhalten verhalten = null;
   
   public BewegendesObjekt(float xPos, float yPos, float ySpeed) {
      super(xPos, yPos);
      this.ySpeed = ySpeed;
   }
   
   public void setVerhalten(Verhalten verhalten) {
      this.verhalten = verhalten;
   }

   public void update() {
      if (verhalten!=null)
         verhalten.update();
   }
}
