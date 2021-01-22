package Behaviurs;

import GUI.GUI;
import jade.core.behaviours.OneShotBehaviour;
import models.enumEntity;

import java.util.concurrent.ThreadLocalRandom;

/**
 * plant three herbs in random places as initial state of boerd*/
public class InitialPlantingBehaviuor extends OneShotBehaviour {

    private GUI gui;

 public    InitialPlantingBehaviuor(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void action() {
        //Create 3 random x y to plant
        int x1 = ThreadLocalRandom.current().nextInt(0, 20);
        int x2 = ThreadLocalRandom.current().nextInt(0, 20);
        int x3 = ThreadLocalRandom.current().nextInt(0, 20);
        int y1 = ThreadLocalRandom.current().nextInt(0, 20);
        int y2 = ThreadLocalRandom.current().nextInt(0, 20);
        int y3 = ThreadLocalRandom.current().nextInt(0, 20);
        _plant(x1,y1,gui);
        _plant(x2,y2,gui);
        _plant(x3,y3,gui);
    }

    /**
     * plant given position a herb
     * and update in GUI
     */
    void _plant(int x, int y,GUI gui) {

        gui.updateUi(x,y, enumEntity.Herb);
    }
}
