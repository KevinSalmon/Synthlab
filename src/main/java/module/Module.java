package module;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.Circuit;
import exceptions.PortTypeException;
import sauvegarde.Memento;
import sauvegarde.SavedModule;
import utils.PortType;
import utils.Tuple;

public abstract class Module extends Circuit {

    /**
     * Récupère Le port identifié par name
     * @param name name du port à récupérer
     * @return Un tuple contenant le port demandé et son type. Renvoie null si le port n'existe pas.
     */
     abstract Tuple<UnitPort, PortType> getPort(String name);


    public void connect(Module dest, String namePortSource, String namePortDest) throws PortTypeException {
        Tuple<UnitPort, PortType> portsSource = getPort(namePortSource);
        Tuple<UnitPort, PortType> portsDest = dest.getPort(namePortDest);
        if(portsSource.getRight().getType().contains(PortType.OUTPUT.getType())
                && portsDest.getRight().getType().contains(PortType.INPUT.getType())){

           ((UnitOutputPort) portsSource.getLeft()).connect((UnitInputPort) portsDest.getLeft());

        } else throw new PortTypeException("Incompatible ports type : "+namePortSource+" must be an output and "+namePortDest+" must be an input");
    }

    public void disconnect(Module dest, String namePortSource, String namePortDest) {
        if(dest != null){
            Tuple<UnitPort, PortType> portsSource = getPort(namePortSource);
            Tuple<UnitPort, PortType> portsDest = dest.getPort(namePortDest);

            if(namePortSource != null && namePortDest != null)
                ((UnitOutputPort) portsSource.getLeft()).disconnect((UnitInputPort) portsDest.getLeft());

        }
    }
}
