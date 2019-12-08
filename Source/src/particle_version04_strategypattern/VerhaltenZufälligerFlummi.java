package particle_version04_strategypattern;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class VerhaltenZufälligerFlummi implements Verhalten {
   private Flummi flummi;
   private Random rand = ThreadLocalRandom.current();
   
   public VerhaltenZufälligerFlummi(Flummi flummi) {
      this.flummi = flummi;
   }
   
   @Override
   public void update() {
      flummi.yPos += rand.nextFloat()*2 - 1;

      if (flummi.yPos>480 || flummi.yPos<0)
         flummi.ySpeed *= -1;
   }
}
