package fr.unice.polytech.qgl.qbc.protocol;

import fr.unice.polytech.qgl.qbc.model.Resource;
import fr.unice.polytech.qgl.qbc.model.map.Direction;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




/**
 * Classe représentant une action
 *
 * @author Robin Alonzo
 */
public class Action {


    public enum ActionName {
        STOP, FLY, HEADING, ECHO, SCAN, LAND, MOVE_TO, GLIMPSE, SCOUT, EXPLORE, EXPLOIT, TRANSFORM
    }

    public enum ParameterName{
        DIRECTION, RESOURCE, CREEK, PEOPLE, RANGE, QUARTZ, WOOD, ORE, FUR, SUGAR_CANE, FRUITS
    }



    final ActionName action;
    HashMap<ParameterName,String> stringParameters;
    HashMap<ParameterName,Integer> intParameters;

    private Action(ActionName action){
        this.action = action;
        stringParameters = new HashMap<>();
        intParameters = new HashMap<>();
    }

    private void setParameter(ParameterName parameter, String value){
        stringParameters.put(parameter,value);
    }

    private void setParameter(ParameterName parameter, Integer value){
        intParameters.put(parameter,value);
    }



    public ActionName getAction(){
        return action;
    }

    public String getStringParameter(ParameterName parameter){
        return stringParameters.get(parameter);
    }

    public Integer getIntParameter(ParameterName parameter){
        return intParameters.get(parameter);
    }



    /**
     * Donne l'action STOP
     * @return Un objet Action
     */
    public static Action stop(){
        return new Action(ActionName.STOP);
    }

    /**
     * Donne l'action FLY
     * @return Un objet Action
     */
    public static Action fly(){
        return new Action(ActionName.FLY);
    }

    /**
     * Donne l'action HEADING
     * @return Un objet Action
     */
    public static Action heading(Direction direction){
        Action action = new Action(ActionName.HEADING);
        action.setParameter(ParameterName.DIRECTION, direction.toString());
        return action;
    }

    /**
     * Donne l'action ECHO
     * @return Un objet Action
     */
    public static Action echo(Direction direction){
        Action action = new Action(ActionName.ECHO);
        action.setParameter(ParameterName.DIRECTION, direction.toString());
        return action;
    }

    /**
     * Donne l'action SCAN
     * @return Un objet Action
     */
    public static Action scan(){
        return new Action(ActionName.SCAN);
    }

    /**
     * Donne l'action LAND
     * @return Un objet Action
     */
    public static Action land(String creek,int people){
        Action action = new Action(ActionName.LAND);
        action.setParameter(ParameterName.CREEK, creek);
        action.setParameter(ParameterName.PEOPLE, people);
        return action;
    }

    /**
     * Donne l'action MOVE_TO
     * @return Un objet Action
     */
    public static Action move_to(Direction direction){
        Action action = new Action(ActionName.MOVE_TO);
        action.setParameter(ParameterName.DIRECTION, direction.toString());
        return action;
    }

    /**
     * Donne l'action GLIMPSE
     * @return Un objet Action
     */
    public static Action glimpse(Direction direction, int range){
        if(range < 1 || range > 4){
            throw new IllegalArgumentException(range + " n'est pas une distance valide pour glimpse.");
        }
        Action action = new Action(ActionName.GLIMPSE);
        action.setParameter(ParameterName.DIRECTION, direction.toString());
        action.setParameter(ParameterName.RANGE, range);
        return action;
    }

    /**
     * Donne l'action EXPLORE
     * @return Un objet Action
     */
    public static Action explore(){
        return new Action(ActionName.EXPLORE);
    }

    /**
     * Donne l'action EXPLOIT
     * @return Un objet Action
     */
    public static Action exploit(Resource resource){
        if(!resource.isPrimary()) throw new IllegalArgumentException(resource.toString() + " n'est pas une ressource primaire.");
        Action action = new Action(ActionName.EXPLOIT);
        action.setParameter(ParameterName.RESOURCE,resource.toString());
        return action;
    }

    /**
     * Donne l'action TRANSFORM
     * @return Un objet Action
     */
    public static Action transform(List<Pair<Resource,Integer>> resources){
        Action action = new Action(ActionName.TRANSFORM);

        for (int i = 0; i < resources.size(); i++) {
            String resourceName = resources.get(i).getKey().toString();
            ParameterName parameter = ParameterName.valueOf(resourceName);
            switch (parameter) {
                case QUARTZ:
                case WOOD:
                case ORE:
                case FUR:
                case SUGAR_CANE:
                case FRUITS:
                    action.setParameter(parameter, resources.get(i).getValue());
                    break;
                default:
                    throw new IllegalArgumentException(resourceName + " n'est pas une ressource transformable.");
            }
        }
        return action;
    }

    /**
     * Donne l'action SCOUT
     * @return Un objet Action
     */
    public static  Action scout(Direction direction){
        Action action = new Action(ActionName.SCOUT);
        action.setParameter(ParameterName.DIRECTION, direction.toString());
        return action;
    }


    /**
     * Convertit l'action en String JSON
     * @return String JSON de l'action
     */
    public String toJSON(){
        switch (action){
            case STOP:
                return ActionGenerator.stop();
            case FLY:
                return ActionGenerator.fly();
            case HEADING:
                return ActionGenerator.heading(getStringParameter(ParameterName.DIRECTION));
            case ECHO:
                return ActionGenerator.echo(getStringParameter(ParameterName.DIRECTION));
            case SCAN:
                return ActionGenerator.scan();
            case LAND:
                return ActionGenerator.land(getStringParameter(ParameterName.CREEK),getIntParameter(ParameterName.PEOPLE));
            case MOVE_TO:
                return ActionGenerator.move_to(getStringParameter(ParameterName.DIRECTION));
            case GLIMPSE:
                return ActionGenerator.glimpse(getStringParameter(ParameterName.DIRECTION),getIntParameter(ParameterName.RANGE));
            case SCOUT:
                return ActionGenerator.scout(getStringParameter(ParameterName.DIRECTION));
            case EXPLORE:
                return ActionGenerator.explore();
            case EXPLOIT:
                return ActionGenerator.exploit(getStringParameter(ParameterName.RESOURCE));
            case TRANSFORM:
                ParameterName[] params = ParameterName.values();
                List<String> resources = new ArrayList<>();
                List<Integer> quantity = new ArrayList<>();
                for (ParameterName param : params) {
                    if(getStringParameter(param) == null && getIntParameter(param) == null){
                        continue;
                    }
                    switch (param){
                        case QUARTZ:
                        case WOOD:
                        case ORE:
                        case FUR:
                        case SUGAR_CANE:
                        case FRUITS:
                            resources.add(param.toString());
                            quantity.add(getIntParameter(param));
                            break;
                        default:
                            throw new RuntimeException();
                    }
                }
                return ActionGenerator.transform(resources,quantity);
        }
        return ActionGenerator.stop();
    }

}
