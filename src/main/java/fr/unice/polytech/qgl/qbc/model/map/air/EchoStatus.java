package fr.unice.polytech.qgl.qbc.model.map.air;

/**
 * Différents status d'une zone aérienne déterminée grace aux ECHO
 *
 * @author Robin Alonzo
 */
public enum EchoStatus {
    OCEAN,MAYBE_GROUND,GROUND,OUT_OF_RANGE,UNKNOWN;

    public static EchoStatus toStatus(String found) {
        if("GROUND".equals(found)){
            return GROUND;
        }
        if("OUT_OF_RANGE".equals(found)){
            return OUT_OF_RANGE;
        }
        throw new IllegalArgumentException();
    }
}
