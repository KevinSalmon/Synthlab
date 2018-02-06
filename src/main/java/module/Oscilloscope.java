package module;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.UnitSink;
import controller.Obseurveur;
import controller.Subject;
import controller.SubjectVCO;
import javafx.scene.chart.XYChart;
import utils.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Oscilloscope extends Module {

    private static int TMAX = 10 * Synthesizer.FRAMES_PER_BLOCK; //combien de frames affichés en même temps sur l'oscillo

    private List<Obseurveur<Subject>> obseuveurList;

    private int tBlock;
    private int numBlock;
    private int t;

    private XYChart.Series<Integer,Double> screen;

    UnitInputPort in;
    UnitOutputPort out;

    public Oscilloscope() {

        t = 0;
        tBlock = 0;
        numBlock = 0;

        screen = new XYChart.Series<>();

        for (int i = 0; i < TMAX; i++) {
            screen.getData().add(new XYChart.Data(i, 0));
        }

        //Crée le port de sortie
        addPort(in = new UnitInputPort("in"), "in");

        //Crée le port de sortie
        addPort(out = new UnitOutputPort(), "out");
    }

    @Override
    public void generate(int start, int limit) {
        super.generate(start, limit);

        double[] in = this.in.getValues();
        double[] out = this.out.getValues();

        for (int j = start; j < limit; j++) {
            screen.getData().get(j).setYValue(in[j]*12);

            out[j]=in[j];

            t++;
            if(t>=TMAX)t=0;
        }
    }

    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(name.equals("out")) return new Tuple<>(getPortByName(name),PortType.OUTPUT);
        if(name.equals("in")) return new Tuple<>(getPortByName(name),PortType.INPUT);
        return null;
    }

    public UnitOutputPort getOutput() {
        return out;
    }

    public UnitInputPort getInput() {
        return in;
    }

    public XYChart.Series<Integer, Double> getScreen() {
        return screen;
    }
}
