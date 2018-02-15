package module;

import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.WhiteNoise;
import ihm.observer.Obseurveur;
import ihm.observer.SubjectBruitBlanc;
import utils.PortType;
import utils.Tuple;

import java.util.ArrayList;
import java.util.List;

public class BruitBlancModule extends Module implements Obseurveur<SubjectBruitBlanc> {

    private UnitOutputPort out;
    private WhiteNoise whiteNoise;

    public BruitBlancModule(){
        this.out = new UnitOutputPort(PortType.OUTPUT.getType());
        addPort(out, PortType.OUTPUT.getType());

        whiteNoise = new WhiteNoise();
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(name.equals(PortType.OUTPUT.getType()))
            return new Tuple<>(getPortByName(name),PortType.OUTPUT);
        return null;
    }

    @Override
    public List<PortType> getAllPorts() {
        List<PortType> list = new ArrayList<>();
        list.add(PortType.OUTPUT);
        return list;
    }

    @Override
    public void generate(int start, int limit) {

        whiteNoise.generate(start, limit);

        double[] outputs = whiteNoise.getOutput().getValues();
        double[] outputsModule = this.out.getValues();

        // Relie l'oscillateur courant à la sortie du VCO en copiant les données
        for (int i = start; i < limit; i++) {
            outputsModule[i] = outputs[i] * 5.0/12.0;
        }
    }

    @Override
    public void update(SubjectBruitBlanc o) { throw new UnsupportedOperationException("Methode inutilisee"); }

    @Override
    public Module getReference() {
        return this;
    }

    public UnitOutputPort getOut() {
        return out;
    }

    public void setOut(UnitOutputPort out) {
        this.out = out;
    }

    public WhiteNoise getWhiteNoise() {
        return whiteNoise;
    }

    public void setWhiteNoise(WhiteNoise whiteNoise) {
        this.whiteNoise = whiteNoise;
    }
}
