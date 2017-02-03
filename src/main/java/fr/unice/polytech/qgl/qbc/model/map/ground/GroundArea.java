package fr.unice.polytech.qgl.qbc.model.map.ground;

import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.model.map.Biome;

import java.util.*;
import java.util.Map;

/**
 * Classe représentant une case de la carte
 *
 * @author Robin Alonzo
 */
public class GroundArea {

    private boolean reachable; // Faux si la case est hors limite
    private int altitude; // Altitude de la case

    private HashMap<Biome,Double> biomes; // Les biomes présents dans la zone assiciée à leur proportion
    private HashMap<Resource,ResourceAmount> resourceAmount; // Les ressources dans la case avec leur quantité
    private HashMap<Resource,ResourceCondition> resourceCondition; // Les ressources de la case avec leur difficultés d'exploitation

    private boolean explored; // EXPLORE a été utilisé sur cette case
    private boolean scouted; // SCOUT a été utilisé sur cette case
    private boolean glimpsed; // GLIPSE a été utilisé sur cette case
    private boolean preciseGlimpse; // GLIMPSE a été utilisé à courte portée sur cette case


    public GroundArea(){
        reachable = true;
        explored = false;
        scouted = false;
        preciseGlimpse = false;
        biomes = new HashMap<>();
        resourceAmount = new HashMap<>();
        resourceCondition = new HashMap<>();
    }

    /**
     * @return Vrai si la case a été EXPLORée, faux sinon
     */
    public boolean isExplored(){
        return explored;
    }

    /**
     * @return Vrai si la case a été SCOUTée, faux sinon
     */
    public boolean isScouted(){
        return scouted;
    }

    /**
     * @return Vrai si la case a été GLIMPSée, faux sinon
     */
    public boolean isGlimpsed(){
        return glimpsed;
    }

    public boolean isReachable(){
        return reachable;
    }

    /**
     * Permet de renseigner que la case a été GLIMPSée
     */
    private void setGlimpsed() {
        this.glimpsed = true;
    }

    /**
     * Permet de renseigner que la case a été SCOUTée
     * Comme un SCOUT contient plus d'information qu'un GLIMPSE, on peut aussi considérée qu'elle a été GLIMPSée aussi
     */
    public void setScouted(){
        setGlimpsed();
        this.scouted = true;
    }

    /**
     * A executer quand un EXPLORE a été exécuté sur cette case
     * Comme un EXPLORE contient plus d'information qu'un SCOUT, on peut aussi considérée qu'elle a été SCOUTée aussi
     */
    public void setExplored() {
        setScouted();
        this.explored = true;
    }

    /**
     * Permet de renseigner si la case est hors limit ou pas
     * @param reachable Faux si elle est hors limit, vrai sinon
     */
    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    /**
     * Remet à zero les information suite à un EXPLORE
     * Ces information peuvent changer suite à un EXPLOIT, cela force le bot à refaire un EXPLORE la prochaine fois
     */
    public void resetExplore() {
        explored = false;
        scouted = false;
        resourceAmount.clear();
        resourceCondition.clear();
    }

    /**
     * Remet à zero les information suite à un SCOUT
     * Ces information peuvent changer suite à un EXPLOIT, cela force le bot à refaire un SCOUT la prochaine fois
     */
    public void resetScout(){
        scouted = false;
        resourceAmount.clear();
        resourceCondition.clear();
    }

    /**
     * Permet de stocker dans la case les informations obtenus suite à un SCOUT
     * @param resource Ressource obtenue grace au SCOUT
     */
    public void addScoutedResource(Resource resource){
        setScouted();
        if(!explored){
            resourceAmount.put(resource, ResourceAmount.UNKNOWN);
            resourceCondition.put(resource, ResourceCondition.UNKNOWN);
        }
    }

    /**
     * Permet de stocker dans la case les informations obtenus suite à un EXPLORE
     */
    public void addExploredResource(Resource resource,ResourceAmount amount,ResourceCondition condition){
        setExplored();
        resourceAmount.put(resource,amount);
        resourceCondition.put(resource,condition);
    }

    /**
     * Permet de stocker dans la case les informations obtenus suite à un GLIMPSE
     */
    public void setGlimpse(Biome biome, double proportion){
        setGlimpsed();
        if(proportion > 0.01){
            preciseGlimpse = true;
        }
        biomes.put(biome,proportion);
    }



    /**
     * Determine si une resource donnée peut être trouvée dans cette case
     * @param resource Ressource dont on vérifie la présence
     * @return Faux si la ressource est introuvable, vrai sinon
     */
    public boolean isPossibleResource(Resource resource) {
        if (!reachable) return false;
        if (explored) {
            return isExploitableResource(resource);
        }
        if (scouted){
            return resourceAmount.containsKey(resource);
        }
        if (glimpsed){
            for (Map.Entry<Biome,Double> biome : biomes.entrySet()){
                if(biome.getKey().containsResource(resource)){
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * Determine si des ressources parmis un ensemble de ressources peuvent etre collectés
     * @param resources Ensemble de ressource dont on veut vérifier la présence potentielle
     * @return Faux si aucune ressource ne s'y trouve, vrai sinon
     */
    public boolean arePossibleResources(Set<Resource> resources) {
        if(!reachable) return false;
        for (Resource resource : resources){
            if(isPossibleResource(resource)) return true;
        }
        return false;
    }

    /**
     * Determine si il est possible d'exploiter la ressource
     * @param resource Ressource que l'on veut exploiter
     * @return Vrai si la quantité est suffisante, faux sinon
     */
    public boolean isExploitableResource(Resource resource){
        if(!reachable) return false;

        if(explored){
            // Il se peut que la quantité de ressource soit à LOW alors qu'il ne reste rien
            if(resourceAmount.get(resource) == ResourceAmount.HIGH || resourceAmount.get(resource) == ResourceAmount.MEDIUM){
                return true;
            }
        }
        return false;
    }

    public boolean isPureOcean(){
        return biomes.containsKey(Biome.OCEAN) && biomes.size() == 1;
    }

    public HashMap<Resource,ResourceAmount> getResourceAmount(){
        return resourceAmount;
    }

    /**
     * Change la quantité de la ressource exploitée à LOW
     *
     * C'est ce qui arrive dans la majorité des cas après avoir exploité une ressource.
     * Cela permet d'éviter de faire un EXPLORE inutile après un EXPLOIT
     *
     * @param exploitedResource La ressource exploitée
     */
    public void setExploited(Resource exploitedResource) {
        resourceAmount.replace(exploitedResource,ResourceAmount.LOW);
    }
}
