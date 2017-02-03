package fr.unice.polytech.qgl.qbc.model;

import javafx.util.Pair;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Description
 *
 * @author Robin Alonzo et Akhmadov Baisangour et Cyprien Levy
 */
public class Contracts {

    static Logger logger = Logger.getLogger(Contracts.class);

    private HashMap<Resource,Integer> contractList;
    private HashMap<Resource,Integer> exploitedResourceList;
    private HashMap<Resource,Integer> manufacturedList;

    public Contracts(JSONArray array){
        contractList= new HashMap<>();
        exploitedResourceList = new HashMap<>();
        manufacturedList = new HashMap<>();


        for(int i=0; i<array.length();i++){
            JSONObject obj=array.getJSONObject(i);
            contractList.put(Resource.toResource(obj.getString("resource")), obj.getInt("amount"));
            exploitedResourceList.put(Resource.toResource((obj.getString("resource"))),0);
            Resource res=Resource.toResource((obj.getString("resource")));
            if(!res.isPrimary()){
                manufacturedList.put(Resource.toResource(obj.getString("resource")), obj.getInt("amount"));
                List<Pair<Resource,Integer>> requirements=res.requiredPrimary(res,1);
                for (Pair<Resource, Integer> requirement : requirements) {
                    exploitedResourceList.put(requirement.getKey(), 0);
                }
            }
        }
    }

    /**
     * Fonction permettant de stocker le nombre de ressource récoltées
     * @param exploitedResource ressource exploitée
     * @param exploitedAmount quantité de cette ressource
     *
     *
     */
    public void updateAmountResource(Resource exploitedResource, int exploitedAmount){
        exploitedResourceList.replace(exploitedResource,exploitedAmount+exploitedResourceList.get(exploitedResource));
    }



    /**
     * Donne l'ensemble des resources primaires concernés par les contracts
     * @return L'ensemble des resources dont on a besoin
     */
    public Set<Resource> getPrimaryResources(){


        Set<Resource> primaryResources = new TreeSet<>();
        Set<Resource> resources = contractList.keySet();
        for(Resource resource : resources){
            if(resource.isPrimary() && (exploitedResourceList.get(resource)<contractList.get(resource))){
                primaryResources.add(resource);
            }
            /*Mettre les lignes suivantes en commentaire pour désactiver la récupération de ressources
            nécessaires pour des contrats secondaires
            */
            else if(!resource.isPrimary() && (exploitedResourceList.get(resource)<contractList.get(resource))){

                /*Cette variable définit le facteur qu'on utilise pour calculer le nombre max d'une ressource primaire à ramasser
                    lorsqu'il s'agit d'un ingrédient d'une ressource transformée
                 */
                final double UPPER_LIMIT_FACTOR=1.2;
                List<Pair<Resource,Integer>> requirements=resource.requiredPrimary(resource,1);
                for (Pair<Resource, Integer> requirement : requirements) {
                    //On vérifie que l'on a encore besoin de collecter la ressource
                    if (contractList.containsKey(requirement.getKey())){
                        if (exploitedResourceList.get(requirement.getKey()) < ((int)(requirement.getValue() * manufacturedList.get(resource) * UPPER_LIMIT_FACTOR) + contractList.get(requirement.getKey()))) {
                            primaryResources.add(requirement.getKey());
                        }
                    }
                    else if (exploitedResourceList.get(requirement.getKey()) < requirement.getValue() * manufacturedList.get(resource) * UPPER_LIMIT_FACTOR) {
                        primaryResources.add(requirement.getKey());

                    }
                }
            }

        }
        return primaryResources;
    }


    /**
     * Fonction qui vérifie si on peut transformer des ressources primaires
     * en ressources manufacturées d'un contrat
     * @return une liste de ressources et de quantité à transformer
     */
    public List<Pair<Resource,Integer>> needTransform(){

        boolean canTransform;
        double factor=1;
        Set keys=manufacturedList.keySet();
        for (Object key : keys) {
            canTransform = true; //Variable qui indiquer si on peut effectuer une transformation
            Resource res = (Resource) key;
            List<Pair<Resource, Integer>> required = res.requiredPrimary(res, 1);
            List<Pair<Resource, Integer>> transformParameter = new ArrayList<>();
            for (Pair<Resource, Integer> aRequired : required) {

                if (!exploitedResourceList.containsKey(aRequired.getKey())
                        || exploitedResourceList.get(aRequired.getKey()) < aRequired.getValue()) {
                    //On ne possède pas la ressource primaire requise ou alors on n'en possède pas assez pour faire la transformation
                    canTransform = false;
                    break;
                } else {
                    Resource currentPrimary = aRequired.getKey();

                    //On détermine le nombre de ressources transformées que l'on veut créer
                    factor = transformFactor(aRequired, res);


                    //Vérification qu'on n'empêche pas la réalisation d'un contrat primaire
                    if (!contractList.containsKey(currentPrimary)) {
                        transformParameter.add(new Pair<>(currentPrimary, ((int) (aRequired.getValue() * factor))));
                    } else if ((exploitedResourceList.get(currentPrimary) - contractList.get(currentPrimary)) > aRequired.getValue() + 2) {
                        transformParameter.add(new Pair<>(currentPrimary, (exploitedResourceList.get(currentPrimary) - contractList.get(currentPrimary)) - 1));
                    }
                    // Cas ou on ne doit pas faire de transform a cause d'un risque d'affecter la réalisation d'un contrat prmaire
                    else {
                        canTransform = false;
                    }

                }

            }
            if (canTransform && (exploitedResourceList.get(res) < contractList.get(res))) {
                for (int j = 0; j < required.size(); j++) {
                    //Mise a jour du nombre de ressources primaires ramassées
                    exploitedResourceList.put(required.get(j).getKey(), exploitedResourceList.get(required.get(j).getKey()) - transformParameter.get(j).getValue());
                }
                //logger.info("Transformation incoming: " + transformParameter);

                return transformParameter;
            }

        }
        return null;
    }

    /**
     *
     * @param primary La ressource primaire et sa quantité nécessaire pour créer la ressource transformée
     * @param transformed la ressource transformée à créer
     * @return un facteur qui correspond au nombre de ressources transformées qu'on va chercher à créer
     */
    private double transformFactor(Pair<Resource,Integer> primary, Resource transformed){
        double factor;
        //Cas ou on a plus de ressources primaires qu'il n'en faut pour créer assez de ressources pour remplir le contrat
        if(exploitedResourceList.get(primary.getKey())>manufacturedList.get(transformed)*primary.getValue()){
            factor=manufacturedList.get(transformed)-exploitedResourceList.get(transformed);
        }
        //Cas ou on doit achever un contrat presque rempli
        else if(exploitedResourceList.get(primary.getKey())>((manufacturedList.get(transformed)-exploitedResourceList.get(transformed))*primary.getValue()*2)){
            factor=(manufacturedList.get(transformed)-exploitedResourceList.get(transformed))*1.5;
        }
        //Cas ou on crée juste autant de ressources que possible
        else{
            //TODO Ne pas créer de ressources si ca pourrait affecter un contrat primaire
            factor=exploitedResourceList.get(primary.getKey())/primary.getValue();
        }

        return factor;
    }






}
