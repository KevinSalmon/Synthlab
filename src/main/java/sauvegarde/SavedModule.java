package sauvegarde;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SavedVCO.class, name = "vco"),
        @JsonSubTypes.Type(value = SavedModuleOut.class, name  = "out")
})
public class SavedModule implements Memento{

    private double xPos;
    private double yPos;
    private String moduleFXMLFile;
    private int idModule;


    public SavedModule() {
    }

    public SavedModule(double xPos, double yPos){
        this.xPos = xPos;
        this.yPos = yPos;

        this.moduleFXMLFile = null;
        this.idModule = -1;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public String getModuleFXMLFile() {
        return moduleFXMLFile;
    }

    public void setModuleFXMLFile(String moduleFXMLFile) {
        this.moduleFXMLFile = moduleFXMLFile;
    }

    public int getIdModule() {
        return idModule;
    }

    public void setIdModule(int idModule) {
        this.idModule = idModule;
    }
}
