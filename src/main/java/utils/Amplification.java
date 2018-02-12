package utils;

import com.softsynth.math.AudioMath;

public class Amplification {

    private double amplification;
    private double decibels;

    public Amplification() {
        setDecibelsAmplification(0);
    }


    public void setDecibelsAmplification(double db) {
        decibels = db;
        this.amplification = AudioMath.decibelsToAmplitude(db);
    }

    public void setAmplification(double amp) {
        decibels = AudioMath.amplitudeToDecibels(amp);
        this.amplification = amp;
    }

    public double getDecibelsAmplification() {
        return this.decibels;
    }

    public double getAmplification() {
        return this.amplification;
    }

    public double applyAmplification(double sig){
        return sig * amplification;
    }
}
