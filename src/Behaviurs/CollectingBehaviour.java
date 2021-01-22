package Behaviurs;

import GUI.GUI;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import models.enumEntity;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.abs;

public class CollectingBehaviour extends TickerBehaviour {
    int currentX;
    int currentY;

    int lastX;
    int lastY;

    GUI gui;
    /*    long duration;
        int timeToClearVisiteds = 20000;//20  seconds
        int sumOfTimes = 0;*/
    boolean isMovingToGetHerb = false;
    boolean isMovingToStock = false;
    int[] herbXY = new int[2];
    MessageTemplate mt;
    int myDistance = 1000;
    int stepCollector = 0;
    //todo a field to memorize herb locations
    boolean[][] visited = new boolean[20][20];
    public CollectingBehaviour(Agent a, long duration, GUI gui, int[] initialLocation) {
        super(a, duration);
        this.gui = gui;
        currentX = initialLocation[0];
        currentY = initialLocation[1];
        gui.updateUi(currentX, currentY, enumEntity.CollectorAgent);
    }

    /**
     * listen for herb location
     * move to herb on each tick
     */
    @Override
    protected void onTick() {
      //  System.out.println(String.format("Iam %s.  stepCollector:%d ", myAgent.getName(), stepCollector));

        if (stepCollector == 0) {
            mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
// Message received. Process it

                String[] message = msg.getContent().split(",");//x,y
                herbXY[0] = Integer.parseInt(message[0]);//x of herb
                herbXY[1] = Integer.parseInt(message[1]);//y of herb

                ACLMessage reply = msg.createReply();//reply with distance of this agent with herb
                if (!isMovingToGetHerb && !isMovingToStock)
                    myDistance = _calculateMyDistanceToHerb(herbXY);
                else {
                    myDistance = 1000;
                    System.out.println(String.format("Iam %s. Distance report:%d ", myAgent.getName(), myDistance));
                    if (isMovingToStock)
                        _moveToStock();
                    else if(isMovingToGetHerb)
                        _moveToGetHerb(herbXY);

                }

                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(String.valueOf(myDistance));
//                reply.setConversationId("herb-location");
                myAgent.send(reply);
                System.out.println(String.format("SEND PROPOSE from %s distance:%d ", myAgent.getName(), myDistance));

                if(myDistance!=1000)
                    stepCollector = 1;
            } else {
                block();
                if (isMovingToGetHerb )
                    _moveToGetHerb(herbXY);
                else if ( isMovingToStock)
                    _moveToStock();
                else
                    _moveRandom();
            }
         } else if (stepCollector == 1) {
            mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);

            if (msg != null) {
// Message received. Process it
                String message = msg.getContent();
                System.out.println(String.format("RECEIVED ACCEPT_PROPOSAL content:%s agents is %s ", message, myAgent.getName()));

                if (message.equals("ok")) {
                    isMovingToGetHerb = true;
                    _moveToGetHerb(herbXY);
                    System.out.println(String.format("******OK RECEIVED Iam %s.Distance was :%d ", myAgent.getName(),myDistance));

                } else// message is  "no"
                {
                    block();
                   /*  if (isMovingToGetHerb )
                    _moveToGetHerb(herbXY);
                    else if ( isMovingToStock)
                         _moveToStock();
                    else*/
                        _moveRandom();
                }

                  stepCollector = 0;
            } else {
                block();
                if (isMovingToGetHerb )
                    _moveToGetHerb(herbXY);
                else if ( isMovingToStock)
                    _moveToStock();
                else
                    _moveRandom();
            }
        }

    }

    private void _moveRandom() {

        lastX = currentX;
        lastY = currentY;

        int[] bestXY = _goBestXY(currentX, currentY, visited);
        if (bestXY != null) {
            currentX = bestXY[0];
            currentY = bestXY[1];
            visited[currentX][currentY] = true;
        } else {
            int[] randomXY = _getRandomXY(currentX, currentY);
            currentX = randomXY[0];
            currentY = randomXY[1];
        }

    /*    int[] randomXY = _getRandomXY(currentX, currentY);
        currentX = randomXY[0];
        currentY = randomXY[1];*/


        gui.updateUi(lastX, lastY, enumEntity.Content);
        gui.updateUi(currentX, currentY, enumEntity.CollectorAgent);

        if (gui.blocks[currentX][currentY].lstContent.contains(enumEntity.Herb)) {
            gui.blocks[currentX][currentY].lstContent.remove(enumEntity.Herb);
            gui.blocks[currentX][currentY].lstContent.add(enumEntity.DetectedHerb);
            gui.updateUi(currentX, currentY, enumEntity.DetectedHerb);
            isMovingToGetHerb = false;
            isMovingToStock = true;
            //so found a herb.move to stock on next tick
//            _moveToStock();

            int[] herbLoc = new int[2];
            herbLoc[0] = currentX;
            herbLoc[1] = currentY;
            gui.updateTotalHerbCollected(herbLoc);

        }

    }
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

    private void _moveToStock() {
        int[] stockXY = {10, 10};
        lastX = currentX;
        lastY = currentY;

        int dx = stockXY[0] - currentX;
        int dy = stockXY[1] - currentY;

        if (dx != 0) {
            if (dx > 0)
                currentX++;
            else
                currentX--;

            //Update Ui
            gui.updateUi(lastX, lastY, enumEntity.Content);
            gui.updateUi(currentX, currentY, enumEntity.GoingToStock);
        } else if (dy != 0) {
            if (dy > 0)
                currentY++;
            else
                currentY--;

            //Update Ui
            gui.updateUi(lastX, lastY, enumEntity.Content);
            gui.updateUi(currentX, currentY, enumEntity.GoingToStock);
        } else if ((dx + dy) == 0)//dy=dy=0 ==>here is Stock
        {
            gui.updateUi(currentX, currentY, enumEntity.Content);
            isMovingToStock = false;
            isMovingToGetHerb = false;
        }
    }

    private void _moveToGetHerb(int[] herbXY) {
        lastX = currentX;
        lastY = currentY;

        int dx = herbXY[0] - currentX;
        int dy = herbXY[1] - currentY;

        if (dx != 0) {
            if (dx > 0)
                currentX++;
            else
                currentX--;

            //Update Ui
            gui.updateUi(lastX, lastY, enumEntity.Content);
            gui.updateUi(currentX, currentY, enumEntity.GoingToHerb);
        } else if (dy != 0) {
            if (dy > 0)
                currentY++;
            else
                currentY--;

            //Update Ui
            gui.updateUi(lastX, lastY, enumEntity.Content);
            gui.updateUi(currentX, currentY, enumEntity.GoingToHerb);
        }
        if ((dx + dy) == 0)//dy=dy=0 ==>here is herb
        {
            if( !gui.blocks[currentX][currentY].lstContent.contains(enumEntity.Herb))
            {
                isMovingToStock = true;
                isMovingToGetHerb=false;
                gui.updateUi(currentX, currentY, enumEntity.CollectorAgent);//collect herb

            }
            gui.blocks[currentX][currentY].lstContent.remove(enumEntity.Herb);
            gui.blocks[currentX][currentY].lstContent.add(enumEntity.DetectedHerb);
            gui.updateUi(currentX, currentY, enumEntity.DetectedHerb);//collect herb
            gui.updateUi(currentX, currentY, enumEntity.Content);//collect herb
            isMovingToStock = true;
            isMovingToGetHerb=false;
            //store data
            int[] herbLoc = new int[2];
            herbLoc[0] = currentX;
            herbLoc[1] = currentY;
            gui.updateTotalHerbCollected(herbLoc);
        }

    }

    private int _calculateMyDistanceToHerb(int[] herbXY) {
        int dx = herbXY[0] - currentX;
        int dy = herbXY[1] - currentY;

        return abs(dx) + abs(dy);
    }


}
