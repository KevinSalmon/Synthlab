package module;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import ihm.observer.Obseurveur;
import ihm.observer.SubjectOscillo;
import utils.PortType;
import utils.Tuple;

import java.util.ArrayList;
import java.util.List;

public class Oscilloscope extends Module implements Obseurveur<SubjectOscillo> {

    public static final int TMAX = 100 * Synthesizer.FRAMES_PER_BLOCK; //combien de frames affichés en même temps sur l'oscillo

    private int t;

    private double[] screen;


    private UnitInputPort in;
    private UnitOutputPort out;

    public Oscilloscope() {

        t = 0;

        screen = new double[TMAX];

        for (int i = 0; i < TMAX; i++) {
            screen[i] = 0;
        }

        //Crée le port de sortie
        in = new UnitInputPort(PortType.INPUT.getType());
        addPort(in, PortType.INPUT.getType());

        //Crée le port de sortie
        out = new UnitOutputPort();
        addPort(out, PortType.OUTPUT.getType());
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);

        double[] inValues = this.in.getValues();
        double[] outValues = this.out.getValues();

        for (int j = start; j < limit; j++) {
            screen[t] = inValues[j]*12;

            outValues[j]=inValues[j];

            t++;
            if(t>=TMAX)t=0;

        }
        
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(PortType.OUTPUT.getType().equals(name)) return new Tuple<>(getPortByName(name),PortType.OUTPUT);
        if(PortType.INPUT.getType().equals(name)) return new Tuple<>(getPortByName(name),PortType.INPUT);
        return null;
    }

    public UnitOutputPort getOutput() {
        return out;
    }

    public UnitInputPort getInput() {
        return in;
    }

    public double[] getScreen() {
        return screen;
    }

    @Override
    public void update(SubjectOscillo o) {
        o.receiveSeries(screen);
    }

    @Override
    public Module getReference() {
        return this;
    }

    @Override
    public List<PortType> getAllPorts() {
        List<PortType> list = new ArrayList<>();
        list.add(PortType.INPUT);
        list.add(PortType.OUTPUT);
        return list;
    }
}
