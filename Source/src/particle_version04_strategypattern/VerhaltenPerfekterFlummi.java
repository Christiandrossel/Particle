package particle_version04_strategypattern;

public class VerhaltenPerfekterFlummi implements Verhalten {
   private Flummi flummi;
   
   public VerhaltenPerfekterFlummi(Flummi flummi) {
      this.flummi = flummi;
   }
   
   @Override
   public void update() {
      flummi.yPos += flummi.ySpeed;

      if (flummi.yPos>480 || flummi.yPos<0)
         flummi.ySpeed *= -1;
   }
}
