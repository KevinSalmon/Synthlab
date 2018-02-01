package module;

import com.jsyn.ports.UnitPort;
import utils.Tuple;

import java.util.Map;

public interface Module {

    /**
     * Récupère Le port identifié par name
     * @param name name du port à récupérer
     * @return Un tuple contenant le port demandé et son type. Renvoi null si le port n'existe pas.
     */
    Tuple<UnitPort,PortType> getOutputs(String name);
}
