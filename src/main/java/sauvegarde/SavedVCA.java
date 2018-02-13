package sauvegarde;

public class SavedVCA extends SavedModule{

    private double attenuateur;

    public SavedVCA(double xPos, double yPos, double attenuateur) {
        super(xPos, yPos);
        this.attenuateur = attenuateur;
    }

    public double getAttenuateur() {
        return attenuateur;
    }
}
