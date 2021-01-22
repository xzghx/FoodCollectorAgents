package Agents;

import Behaviurs.CollectingBehaviour;
import GUI.GUI;
import models.enumEntity;
import models.AgentEntity;
import jade.core.Agent;

import java.util.Random;

public class CollectorAgent extends Agent implements AgentEntity {

    private GUI gui;


    @Override
    protected void setup() {
        super.setup();
        System.out.println("Setup of CollectorAgent called ");

        this.gui = (GUI) getArguments()[0];

        Random random = new Random();
        int[] initialLocation = {random.nextInt(20),
                random.nextInt(20)};

        CollectingBehaviour _collectingBehaviour = new CollectingBehaviour(this, 1000, gui, initialLocation);
        addBehaviour(_collectingBehaviour);


    }


    @Override
    public enumEntity Iam() {
        return enumEntity.CollectorAgent;
    }


}
