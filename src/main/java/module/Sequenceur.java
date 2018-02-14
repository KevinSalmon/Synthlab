package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import controller.Obseurveur;
import controller.SubjectSeq;
import utils.PortType;
import utils.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * Sequencer class
 */
public class Sequenceur extends Module implements Obseurveur<SubjectSeq>{
    private UnitInputPort inputPort;
    private UnitOutputPort outputPort;
    private double [] values = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    private int actualValue;
    private boolean isCheck = false;

    public Sequenceur(){
        inputPort = new UnitInputPort(PortType.INPUT.getType());
        outputPort = new UnitOutputPort(PortType.OUTPUT.getType());
        addPort(inputPort);
        addPort(outputPort);
        actualValue = 0;
    }
    @Override
    Tuple<UnitPort, PortType> getPort(String name) {
        if(name.contains(PortType.INPUT.getType())) return new Tuple<>(inputPort, PortType.INPUT);
        if(name.contains(PortType.OUTPUT.getType())) return new Tuple<>(outputPort, PortType.OUTPUT);
        return null;
    }

    /**
     * Met à jour le tableau des valeurs actuelles des sliders coté fonctionnel
     * @param o
     */
    @Override
    public void update(SubjectSeq o) {
        values[o.getCurrentSlider()-1] = o.getSliderValue(o.getCurrentSlider());

    }

    /**
     * Generation de la séquence
     * Si la valeur d'entrée est supérieur à 5V, le séquenceur passe au pas suivant
     * @param start
     * @param limit
     */
    @Override
    public void generate(int start, int limit){

        super.generate(start, limit);
        double[] inValues = inputPort.getValues();
        double[] outValues = outputPort.getValues();
        for (int i = start; i < limit; i++){
            if((inValues[i] *12)>= 5 && !isCheck){
                actualValue = (actualValue+1) %values.length;
                isCheck = true;
            }
            else if(inValues[i] <= 0){
                isCheck = false;
            }
            outValues[i] = values[actualValue] / 12;

        }
    }

    public void resetToOne(SubjectSeq o){
        update(o);
        actualValue=0;

    }
    @Override
    public Module getReference() {
        return this;
    }

    public UnitOutputPort getOutputPort(){
        return outputPort;
    }

    public UnitInputPort getInputPort() {
        return inputPort;
    }

    public double[] getValues() {
        return values;
    }
    public int getActualValue(){
        return  actualValue;
    }

    public boolean isCheck(){
        return isCheck;
    }

    @Override
    public List<PortType> getAllPorts() {
        List<PortType> list = new ArrayList<>();
        list.add(PortType.INPUT);
        list.add(PortType.OUTPUT);
        return list;
    }
}
