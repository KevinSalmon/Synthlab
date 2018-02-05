package filter;

import com.jsyn.unitgen.UnitFilter;
import com.softsynth.math.AudioMath;

public class AttenuationFilter extends UnitFilter {

    private double voltAttenuation = 1.0;
    private double decibels = 0.0;

    @Override
    public void generate(int start, int limit) {
        double[] inputs = input.getValues();
        double[] outputs = output.getValues();

        for (int i = start; i < limit; i++) {
            outputs[i] = this.voltAttenuation * inputs[i];
        }
    }

    public void setDecibelsAttenuation(double decibels) {
        this.decibels = decibels;
        this.voltAttenuation = AudioMath.decibelsToAmplitude(this.decibels);
    }

    public double getDecibelsAttenuation() {
        return this.decibels;
        //return AudioMath.amplitudeToDecibels(this.voltAttenuation); // Pour -42 db, retourne 41.999999...
    }
}
