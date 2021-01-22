package Behaviurs;

import GUI.GUI;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import models.enumEntity;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PlantingBehaviour extends TickerBehaviour {
    private long tick;
    private Agent myAgent;
    private GUI gui;

    public PlantingBehaviour(Agent a, long tick, GUI gui) {
        super(a, tick);
        this.tick = tick;
        this.myAgent = a;
        this.gui = gui;
    }

    @Override
    protected void onTick() {

        int[] xy = _createRandomXY();
        //index 0 is x
        //index 1 is y
        _plant(xy[0], xy[1],gui);
    }

    private int[] _createRandomXY() {
        //Get random x y position in [20,20 ] range
        // Obtain a number between [0 - 19].
        boolean isOk = false;
        int x = 0, y = 0;
        while (!isOk) {
            isOk=true;
            x = ThreadLocalRandom.current().nextInt(0,20) ;//gives random from 0 to 19
            y = ThreadLocalRandom.current().nextInt(0,20) ;

            //position 10,10 can not be used because it is StockRoom
            if (x == 10 && y == 10)
                isOk = false;

            if (gui.blocks[x][y].lstContent.contains(enumEntity.Herb))
                isOk=false;
        }

        int[] xy = {x, y};

        return xy;
    }

    /**
     * plant given position a herb
     * and update in GUI
     */
    void _plant(int x, int y,GUI gui) {

        gui.updateUi(x,y, enumEntity.Herb);
        System.out.println("************************************************");
        System.out.println(String.format("Planted a Herb in location:(%d,%d)",x,y));
        System.out.println("************************************************");


    }

}
