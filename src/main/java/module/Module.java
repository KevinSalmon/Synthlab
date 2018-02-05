package module;

import Exceptions.PortTypeException;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.ports.UnitPort;
import com.jsyn.unitgen.Circuit;
import utils.Tuple;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Module extends Circuit {

    /**
     * Récupère Le port identifié par name
     * @param name name du port à récupérer
     * @return Un tuple contenant le port demandé et son type. Renvoi null si le port n'existe pas.
     */
     abstract Tuple<UnitPort,PortType> getPort(String name);

    private static final Logger Log = Logger.getLogger( Module.class.getName() );

    public void connect(Module dest, String namePortSource, String namePortDest) throws PortTypeException {
        Tuple<UnitPort, PortType> portsSource = getPort(namePortSource);
        Tuple<UnitPort, PortType> portsDest = dest.getPort(namePortDest);

        if(portsSource.getRight().equals(PortType.OUTPUT)
                && portsDest.getRight().equals(PortType.INPUT)){
            ((UnitOutputPort) portsSource.getLeft()).connect((UnitInputPort) portsDest.getLeft());
            Log.log(Level.INFO, "IsConnected :"+(((UnitOutputPort) portsSource.getLeft()).isConnected()));
        } else throw new PortTypeException("Incompatible ports type : "+namePortSource+" must be an output and "+namePortDest+" must be an input");
    }
}
