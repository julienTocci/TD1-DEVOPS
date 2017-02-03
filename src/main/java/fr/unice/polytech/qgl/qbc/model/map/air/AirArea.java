package fr.unice.polytech.qgl.qbc.model.map.air;

import fr.unice.polytech.qgl.qbc.model.map.Biome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akhmadov Baisangour on 14/02/2016.
 */
public class AirArea {
    private boolean scanned;
    private List<Biome> biomes;
    private EchoStatus echoStatus;

    public AirArea(){
        scanned=false;
        biomes=new ArrayList<>();
        echoStatus = EchoStatus.UNKNOWN;
    }

    public void setScan(){
        scanned=true;
    }

    public boolean needScan(){
        return !scanned;
    }

    public void addBiomes(List<Biome> biomesList){
        biomes=biomesList;
    }

    public boolean isOcean(){
        return echoStatus == EchoStatus.OCEAN;
    }

    public void setOutOfRange(){
        echoStatus = EchoStatus.OUT_OF_RANGE;
    }

    public void setEcho(EchoStatus echo) {
        echoStatus = echo;
    }

    public EchoStatus getEcho() {
        return echoStatus;
    }
}
