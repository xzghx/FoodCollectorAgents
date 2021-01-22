package Agents;

import GUI.GUI;
import models.enumEntity;
import models.AgentEntity;
import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.HashMap;
import java.util.Map;

public class StarterAgent extends Agent implements AgentEntity {

    private static final int AGENTS_COUNT = 5;

    @Override
    protected void setup() {
        System.out.println("Setup of starter agent");
        //Create GUI
        GUI gui = new GUI();

        //Create Agents Container and Controller
        AgentContainer agentContainer = getContainerController();
        AgentController[] controller = new AgentController[AGENTS_COUNT];

        //create name for agents
        Map<String, String> agentNames = new HashMap<String, String>(5);
        _createNamesForAgents(agentNames);

        //create agents
        _createCollectorAgent(agentContainer, controller, agentNames, gui);
        _createSearcherAgent(agentContainer, controller, agentNames, gui);
        _createPlantingAgent(agentContainer, controller, agentNames, gui);
    }

    private void _createNamesForAgents(Map<String, String> agentNames) {
        agentNames.put("CollectorAgent0", "CollectorAgent0");
        agentNames.put("CollectorAgent1", "CollectorAgent1");
        agentNames.put("CollectorAgent2", "CollectorAgent2");
        agentNames.put("SearcherAgent", "SearcherAgent");
        agentNames.put("PlanterAgent", "PlanterAgent");
    }

    /**
     * create Three Collector agents
     */
    private void _createCollectorAgent(AgentContainer agentContainer, AgentController[] controller, Map<String, String> agentNames, GUI gui) {
        try {
            Object[] arg = new Object[1];
            arg[0] = gui;

            for (int i = 0; i <= 2; i++) {
                controller[i] = agentContainer.createNewAgent(agentNames.get(String.format("CollectorAgent%d", i)), CollectorAgent.class.getName(), arg);
                controller[i].start();
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    private void _createSearcherAgent(AgentContainer agentContainer, AgentController[] controller, Map<String, String> agentNames, GUI gui) {
        try {
            Object[] args = new Object[1];
            args[0] = gui;
            controller[3] = agentContainer.createNewAgent(agentNames.get("SearcherAgent"), SearcherAgent.class.getName(), args);
            controller[3].start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }

    }


    private void _createPlantingAgent(AgentContainer agentContainer, AgentController[] controller, Map<String, String> agentNames, GUI gui) {
        try {
            Object[] args = new Object[1];
            args[0] = gui;

            controller[4] = agentContainer.createNewAgent(agentNames.get("PlanterAgent"), PlanterAgent.class.getName(), args);
            controller[4].start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public enumEntity Iam() {
        return enumEntity.StarterAgent;
    }


}
