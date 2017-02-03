package fr.unice.polytech.qgl.qbc.protocol;

import org.json.JSONObject;

import java.util.List;

/**
 * Classe s'occupant de générer les différentes actions en JSON.
 *
 * @author Robin Alonzo et Cyprien Levy
 */
public class ActionGenerator {

    private ActionGenerator(){}

    /**
     * Génère une String JSON correspondant à l'action STOP
     * @return Une String JSON
     */
    public static String stop(){
        return new JSONObject()
                .put("action", "stop")
                .toString();
    }

    /**
     * Génère une String JSON correspondant à l'action FLY
     * @return Une String JSON
     */
    public static String fly() {
        return new JSONObject()
                .put("action", "fly")
                .toString();
    }

    /**
     * Génère une String JSON correspondant à l'action HEADING
     * @param direction N, S, E ou W
     * @return Une String JSON
     */
    public static String heading(String direction) {
        JSONObject parameters = new JSONObject().put("direction", direction);
        return new JSONObject()
                .put("action", "heading")
                .put("parameters", parameters)
                .toString();
    }

    /**
     * Génère une String JSON correspondant à l'action ECHO
     * @param direction N, S, E ou W
     * @return Une String JSON
     */
    public static String echo(String direction) {
        JSONObject parameters = new JSONObject().put("direction", direction);
        return new JSONObject().put("action","echo")
                .put("parameters", parameters)
                .toString();
    }

    /**
     * Génère une String JSON correspondant à l'action SCAN
     * @return Une String JSON
     */
    public static String scan() {
        return new JSONObject()
                .put("action","scan").toString();
    }

    /**
     * Génère une String JSON correspondant à l'action LAND
     * @param idCreek L'id de la creek sur laquelle débarquer
     * @param nbrMen Nombre de personne à envoyer
     * @return Une String JSON
     */
    public static String land(String idCreek,int nbrMen) {
        JSONObject parameters = new JSONObject().put("creek", idCreek).put("people", nbrMen);
        return new JSONObject()
                .put("action", "land")
                .put("parameters", parameters)
                .toString();
    }

    public static String move_to(String direction){
        JSONObject parameters = new JSONObject().put("direction", direction);
        return new JSONObject().put("action","move_to")
                .put("parameters", parameters)
                .toString();
    }

    public static String scout(String direction){
        JSONObject parameters = new JSONObject().put("direction", direction);
        return new JSONObject().put("action","scout")
                .put("parameters", parameters)
                .toString();
    }

    public static String glimpse(String direction, int distance){
        JSONObject parameters = new JSONObject().put("direction", direction).put("range",distance);
        return new JSONObject().put("action","glimpse")
                .put("parameters", parameters)
                .toString();
    }

    public static String explore() {
        return new JSONObject()
                .put("action","explore").toString();
    }

    public static String exploit(String resource){
        JSONObject parameters = new JSONObject().put("resource", resource);
        return new JSONObject().put("action","exploit")
                .put("parameters", parameters)
                .toString();
    }

    public static String transform(String[] resources, int[] amount){
        JSONObject parameters = new JSONObject();
        for(int i=0;i<resources.length;i++){
            parameters.put(resources[i],amount[i]);
        }
        return new JSONObject().put("action","transform")
                .put("parameters", parameters)
                .toString();
    }

    public static String transform(List<String> resources, List<Integer> quantity) {
        JSONObject parameters = new JSONObject();
        for(int i=0;i<resources.size();i++){
            parameters.put(resources.get(i),quantity.get(i));
        }
        return new JSONObject().put("action","transform")
                .put("parameters", parameters)
                .toString();
    }
}
