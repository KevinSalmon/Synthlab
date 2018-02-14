package sauvegarde;

public class SavedModuleOut extends SavedModule{

    private boolean isMute;
    private String recordFileName;
    private boolean isRecording;
    private double attenuateur;

    public SavedModuleOut() {
    }

    public SavedModuleOut(double xPos, double yPos, boolean isMute, String fileName, boolean isRecording, double attenuateur) {
        super(xPos, yPos);
        this.isMute = isMute;
        this.recordFileName = fileName;
        this.isRecording = isRecording;
        this.attenuateur = attenuateur;
    }

    public boolean isMute() {
        return isMute;
    }

    public String getRecordFileName() {
        return recordFileName;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public Double getAttenuateur() {
        return attenuateur;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    public void setRecordFileName(String recordFileName) {
        this.recordFileName = recordFileName;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public void setAttenuateur(double attenuateur) {
        this.attenuateur = attenuateur;
    }
}
