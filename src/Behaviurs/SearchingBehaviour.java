package Behaviurs;

import GUI.GUI;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import models.enumEntity;

import java.util.concurrent.ThreadLocalRandom;

public class SearchingBehaviour extends TickerBehaviour {
    int currentX;
    int currentY;

    int lastX;
    int lastY;
    GUI gui;
    long duration;
    int timeToClearVisiteds = 20000;//20  seconds
    int sumOfTimes = 0;

    boolean[][] visited = new boolean[20][20];

    public SearchingBehaviour(Agent a, long duration, GUI gui) {
        super(a, duration);
        this.duration = duration;
        this.gui = gui;
        currentX = ThreadLocalRandom.current().nextInt(0, 20);
        currentY = ThreadLocalRandom.current().nextInt(0, 20);
        lastX = currentX;
        lastY = currentY;
        gui.updateUi(currentX, currentY, enumEntity.SearcherAgent);
    }

    @Override
    protected void onTick() {
        int[] herbXY = new int[2];
        boolean found = false;
        sumOfTimes += duration;
        if (sumOfTimes == timeToClearVisiteds)
            visited = new boolean[20][20];

//        System.out.println("-----one searcher  Tick happened----");
        lastX = currentX;
        lastY = currentY;

        int nextX = 0;
        int nextY = 0;
        //explore environment
        int[] bestXY = _goBestXY(currentX, currentY, visited);
        if (bestXY != null) {
            nextX = bestXY[0];
            nextY = bestXY[1];
            visited[nextX][nextY] = true;
        } else {
            int[] randomXY = _getRandomXY(currentX, currentY);
            nextX = randomXY[0];
            nextY = randomXY[1];
        }

        currentX = nextX;
        currentY = nextY;
        if (gui.blocks[currentX][currentY].lstContent.contains(enumEntity.Herb)) {
            herbXY[0] = currentX;
            herbXY[1] = currentY;
            found = true;
        }

        gui.updateUi(lastX, lastY, enumEntity.Content);
        gui.updateUi(currentX, currentY, enumEntity.SearcherAgent);
        System.out.println(String.format("last x:%d  current x:%d", lastX, currentX));
        System.out.println(String.format("last y:%d  current y:%d", lastY, currentY));
        //if found a herb , send ACL message to collector agents
        if (found)
            myAgent.addBehaviour(new InformCollectorsBehaviour(herbXY ));


    }

//    public boolean done() {
//        return ((step == 2 && bestSeller == null) || step == 4);
//    }


    private int[] _goBestXY(int currentX, int currentY, boolean[][] visited) {
        return gui.exploreAround(currentX, currentY, visited);
    }

    private int[] _getRandomXY(int currentX, int currentY) {
        int[] xy = null;
        int nextX = 0;
        int nextY = 0;
        boolean isOk = false;
        while (!isOk) {
            nextX = ThreadLocalRandom.current().nextInt(currentX - 1, currentX + 2);
            nextY = ThreadLocalRandom.current().nextInt(currentY - 1, currentY + 2);
            if (nextX == 10 && nextY == 10)//can not go to stock
                isOk = false;
            if (nextX == lastX && nextY == lastY)//not be last position
                isOk = false;
            if (nextX < 20 && nextY < 20 && nextX >= 0 && nextY >= 0)
                isOk = true;
        }
        xy = new int[2];
        xy[0] = nextX;
        xy[1] = nextY;

        return xy;
    }

}