package particle_version05_vektoren;

public class VerhaltenPerfekterFlummi implements Verhalten {
   private Flummi flummi;
   
   public VerhaltenPerfekterFlummi(Flummi flummi) {
      this.flummi = flummi;
   }
   
   @Override
   public void update() {
      flummi.position.add(flummi.velocity);

      if (flummi.position.y>480 || flummi.position.y<0)
         flummi.velocity.y *= -1;
   }
}
