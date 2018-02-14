package sauvegarde;

public class SavedCable {

    private int idModuleIn;
    private int idModuleOut;
    private String inputName;
    private String outputName;

    public SavedCable() {
    }

    public SavedCable(int idModuleIn, int idModuleOut, String inputName, String outputName) {
        this.idModuleIn = idModuleIn;
        this.idModuleOut = idModuleOut;
        this.inputName = inputName;
        this.outputName = outputName;
    }

    public int getIdModuleIn() {
        return idModuleIn;
    }

    public int getIdModuleOut() {
        return idModuleOut;
    }

    public String getInputName() {
        return inputName;
    }

    public String getOutputName() {
        return outputName;
    }

    public void setIdModuleIn(int idModuleIn) {
        this.idModuleIn = idModuleIn;
    }

    public void setIdModuleOut(int idModuleOut) {
        this.idModuleOut = idModuleOut;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }

    public void setOutputName(String outputName) {
        this.outputName = outputName;
    }
}
