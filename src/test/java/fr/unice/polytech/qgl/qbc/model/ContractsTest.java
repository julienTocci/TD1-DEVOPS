package fr.unice.polytech.qgl.qbc.model;

import javafx.util.Pair;
import org.json.JSONArray;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Description
 *
 * @author Cyprien Levy
 */
public class ContractsTest {
    JSONArray jsonArrayContrat = new JSONArray("[\n" +
            "    { \"amount\": 600, \"resource\": \"WOOD\" },\n" +
            "    { \"amount\": 200, \"resource\": \"FLOWER\" }]");

    JSONArray jsonArrayContratSecond = new JSONArray("[\n" +
            "    { \"amount\": 600, \"resource\": \"WOOD\" },\n" +
            "    { \"amount\": 200, \"resource\": \"LEATHER\" }]");



    @Test
    public void testUpdateAmountResource() {
        Contracts contrat = new Contracts(jsonArrayContrat);
        assertTrue(contrat.getPrimaryResources().contains(Resource.WOOD));
        assertTrue(contrat.getPrimaryResources().contains(Resource.FLOWER));

        contrat.updateAmountResource(Resource.WOOD,625);
        assertFalse(contrat.getPrimaryResources().contains(Resource.WOOD));
        assertTrue(contrat.getPrimaryResources().contains(Resource.FLOWER));


    }

    @Test
    public void testGetPrimaryResources() {
        Contracts contrat = new Contracts(jsonArrayContratSecond);
        assertTrue(contrat.getPrimaryResources().contains(Resource.WOOD));

        //La ligne suivante vérifie si la ressource FUR nécessaire à la création de la ressource
        //LEATHER du contrat fait bien partie des ressources à ramasser
        assertTrue(contrat.getPrimaryResources().contains(Resource.FUR));
    }

    @Test
    public void testNeedTransform(){
        //Premier test avec un contrat de cuir
        Contracts contrat = new Contracts(jsonArrayContratSecond);
        //On n'a pas de quoi transformer de la fourrure en cuir
        assertNull(contrat.needTransform());
        //On ajoute les ressources nécessaires pour transformer
        contrat.updateAmountResource(Resource.FUR,4);

        List<Pair<Resource,Integer>> required=contrat.needTransform();
        assertNotNull(required);
        assertEquals(required.get(0).getKey(),Resource.FUR);
        assertEquals(required.get(0).getValue(),3,0.001);


        //Test avec un contrat qui a deux ressources différentes requises (sugar cane)
        JSONArray jsonArrayRum = new JSONArray("[\n" +
                "    { \"amount\": 100, \"resource\": \"RUM\" }]");
        Contracts contratRum = new Contracts(jsonArrayRum);
        contratRum.updateAmountResource(Resource.SUGAR_CANE,11);
        //On n'a pas toutes les ressources pour transformer (il manque les fruits)
        assertNull(contratRum.needTransform());
        //On ajoute les ressources manquantes
        contratRum.updateAmountResource(Resource.FRUITS,1);

        List<Pair<Resource,Integer>> requiredRum=contratRum.needTransform();
        assertNotNull(requiredRum);
        assertEquals(requiredRum.get(0).getKey(),Resource.SUGAR_CANE);
        assertEquals(requiredRum.get(0).getValue(),11,0.001);
        assertEquals(requiredRum.get(1).getKey(),Resource.FRUITS);
        assertEquals(requiredRum.get(1).getValue(),1,0.001);

        //Test avec différentes quantités de ressources
        /*Permet de vérifier qu'on n'affecte pas un contrat primaire deja rempli suite à une transformation
        */
        JSONArray jsonArrayIngot = new JSONArray("[\n" +
                "    { \"amount\": 100, \"resource\": \"WOOD\" },\n" +
                "    { \"amount\": 100, \"resource\": \"INGOT\" }]");
        Contracts contratIngot = new Contracts(jsonArrayIngot);
        contratIngot.updateAmountResource(Resource.WOOD,5);
        contratIngot.updateAmountResource(Resource.ORE,5);
        List<Pair<Resource,Integer>> requiredIngot=contratIngot.needTransform();
        assertNull(requiredIngot);

        //On doit ajouter 103 et non 100 par sécurité
        contratIngot.updateAmountResource(Resource.WOOD,103);
        requiredIngot=contratIngot.needTransform();
        assertEquals(requiredIngot.get(0).getValue(),5,0.001);

    }
}