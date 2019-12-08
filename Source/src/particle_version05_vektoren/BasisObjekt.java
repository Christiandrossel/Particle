package particle_version05_vektoren;

import math.Vektor2D;

public abstract class BasisObjekt {
   public int id;
   public Vektor2D position;
   
   public BasisObjekt() {
      this(new Vektor2D(0,0));
   }
   
   public BasisObjekt(Vektor2D position) {
      this.position = new Vektor2D(position);
   }
  
   public abstract void render();
}
