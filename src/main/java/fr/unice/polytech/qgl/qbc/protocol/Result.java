package fr.unice.polytech.qgl.qbc.protocol;

import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.model.map.Biome;
import fr.unice.polytech.qgl.qbc.model.map.ground.ResourceAmount;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description
 *
 * @author Robin Alonzo et Cyprien Levy
 */
public class Result {

    //TODO Utiliser les objets Resource, Biome ... au lieu des strings

    JSONObject result;

    static Logger logger = Logger.getLogger(Result.class);

    public Result(String results) {
        result = new JSONObject(results);
    }

    /**
     * Renvoit le coût de la dernière action
     * @return int représentant le coût
     */
    public int getCost() {
        return result.getInt("cost");
    }

    /**
     * renvoit la distance de l'obstacle
     * @return int représentant la distance
     */
    public int getEchoRange() {
        return result.getJSONObject("extras").getInt("range");
    }

    /**
     * renvoit le type d'obstacle rencontré
     * @return string représentant l'obstacle
     */
    public String getEchoFound() {
        return result.getJSONObject("extras").getString("found");
    }

    /**
     * renvoit le type de biome decouvert à l'aide du scan
     * @return String représentant le biome
     */
    public String[] getScanBiomes() {
        JSONArray biomeArray = result.getJSONObject("extras").getJSONArray("biomes");
        int len = biomeArray.length();
        String[] biomes = new String[len];
        for (int i = 0; i < len; i++) {
            biomes[i] = biomeArray.getString(i);
        }
        return biomes;
    }


   /* public List<Biome> getScanBiomes() {
        JSONArray biomeArray = result.getJSONObject("extras").getJSONArray("biomes");
        int len = biomeArray.length();
        List<Biome> biomes = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            biomes.add(Biome.valueOf(biomeArray.getString(i)));
        }
        return biomes;
    }
    */

    public String[] getCreeks() {
        JSONArray creeksArray = result.getJSONObject("extras").getJSONArray("creeks");
        int len = creeksArray.length();
        String[] creeks = new String[len];
        for (int i = 0; i<len; i++){
            creeks[i]=creeksArray.getString(i);
        }
        return creeks;

    }

    public HashMap<Resource,ResourceAmount> getExploreResources(){
        //TODO cette fonction ne permet pas d'obtenir les quantités et conditions des ressources
        JSONArray resourceArray = result.getJSONObject("extras").getJSONArray("resources");
        HashMap<Resource,ResourceAmount> resources = new HashMap<>();
        int len= resourceArray.length();
        for (int i = 0; i<len; i++){
            Resource resource = Resource.toResource(resourceArray.getJSONObject(i).getString("resource"));
            ResourceAmount amount = ResourceAmount.valueOf(resourceArray.getJSONObject(i).getString("amount"));
            resources.put(resource,amount);
        }

        return resources;
    }

    public String[] getScoutResources(){
        JSONArray scoutArray=result.getJSONObject("extras").getJSONArray("resources");
        String[] scoutResources=new String[scoutArray.length()];

        for(int i=0;i<scoutArray.length();i++){
            scoutResources[i]=scoutArray.getString(i);
        }
        return scoutResources;
    }

    public boolean getScoutReachable(){
        return result.getJSONObject("extras").isNull("unreachable");
    }

    //TODO faire une méthode qui récupère les résutlats des glimpse


    public List<HashMap<Biome,Double>> getGlimpseResult(){
        List<HashMap<Biome,Double>> glimpseList=new ArrayList<>();
        JSONArray glimpseArray=result.getJSONObject("extras").getJSONArray("report");
        for(int i=0;i<glimpseArray.length();i++){
            JSONArray glimpseArrayPart=glimpseArray.getJSONArray(i);
            HashMap<Biome,Double> map=new HashMap<>();
            if(i<2){
                for(int j=0;j<glimpseArrayPart.length();j++){
                    JSONArray glimpseArrayPartBiome=glimpseArrayPart.getJSONArray(j);
                    map.put(Biome.valueOf(glimpseArrayPartBiome.getString(0)),glimpseArrayPartBiome.getDouble(1));
                }
            }
            else{
                for(int j=0;j<glimpseArrayPart.length();j++){
                    map.put(Biome.valueOf(glimpseArrayPart.getString(j)),0.0);
                }
            }
            glimpseList.add(map);
        }
        return glimpseList;
    }

    /**
     * Permet de recuperer la quantité de ressource exploiter
     * @return int représentant la quantité de ressource exploitée
     */
    public int getExploitedAmount(){
        return result.getJSONObject("extras").getInt("amount");
    }

    /**
     * Permet de récupérer la quantité de ressource crée suite à la transformation
     * @return La quantité de ressource transformée obtenue
     */
    public int getTransformedAmount(){
        return result.getJSONObject("extras").getInt("production");
    }

    /**
     * Permet de récupérer le type de ressource crée suite à la transformation
     * @return Le type de ressource transformée obtenu
     */
    public Resource getTransformedKind(){
        return Resource.valueOf(result.getJSONObject("extras").getString("kind"));
    }
}
