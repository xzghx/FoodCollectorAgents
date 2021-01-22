package Agents;

import Behaviurs.InitialPlantingBehaviuor;
import Behaviurs.PlantingBehaviour;
import GUI.GUI;
import models.enumEntity;
import models.AgentEntity;
import jade.core.Agent;

/**
 * Plant a herb each 5 seconds
 */
public class PlanterAgent extends Agent implements AgentEntity {

    @Override
    protected void setup() {
        System.out.println("Agent " + getAID().getLocalName() + "setup called");

        //get gui from args
        GUI gui = (GUI) getGUI(getArguments());

        //add one shot behaviour to plant 3 herb at start
        InitialPlantingBehaviuor _initialBehaviuor = new InitialPlantingBehaviuor(gui);
        addBehaviour(_initialBehaviuor);

        //Add planting Behaviour to plant  herb each 5 seconds
        PlantingBehaviour _plantingBehaviour = new PlantingBehaviour(this, 5000, gui);
        addBehaviour(_plantingBehaviour);

    }


    private Object getGUI(Object[] arguments) {
        if (arguments.length > 0)
            return arguments[0];
        return null;
    }

    @Override
    public enumEntity Iam() {
        return enumEntity.PlanterAgent;
    }
}
