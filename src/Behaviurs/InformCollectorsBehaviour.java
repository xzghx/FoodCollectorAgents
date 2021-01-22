package Behaviurs;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;

public class InformCollectorsBehaviour extends Behaviour {

    int[] herbXY;
    int minimumDistance = 1000;
    // The list of known collector agents

    final AID[] collectorAgents =
            {
                    new AID("CollectorAgent0", AID.ISLOCALNAME),
                    new AID("CollectorAgent1", AID.ISLOCALNAME),
                    new AID("CollectorAgent2", AID.ISLOCALNAME)
            };
    private AID nearestAgent = null;
    private int repliesCnt = 0; // The counter of replies from collector agents
    private MessageTemplate mt; // The template to receive replies
    private int step = 0;


    InformCollectorsBehaviour(int[] herbLoc) {
        herbXY = herbLoc;
/*        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        System.out.println("AID names");
        System.out.println(collectorAgents[0]);
        System.out.println(collectorAgents[1]);
        System.out.println(collectorAgents[2]);*/
    }

    @Override
    public void action() {
       // System.out.println(String.format("Iam %s.  step:%d ", myAgent.getName(), step));

        //offer Message carrying a request for offer herbXY
        switch (step) {
            case 0:
// Send the cfp to all Collector Agents
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                for (int i = 0; i < collectorAgents.length; ++i) {
                    cfp.addReceiver(collectorAgents[i]);
                }
                cfp.setContent(String.format("%d,%d", herbXY[0], herbXY[1]));
//                cfp.setConversationId("herb-location");
//                cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
                myAgent.send(cfp);
// Prepare the template to get proposals
//                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("herb-location"), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
//                mt = MessageTemplate.MatchConversationId("herb-location");

                System.out.println(String.format("!!!!!Send CFP Message from Searcher to collectors "));
                step = 1;
                break;
            case 1:
// Receive all proposals  from collector agents
                ACLMessage reply = myAgent.receive(mt);
                if (reply != null) {
// Reply received
                    if (reply.getPerformative() == ACLMessage.PROPOSE) {
// This is an offer to collect herb
                        int offeredDistance;
                        String response = (reply.getContent());
                        offeredDistance = Integer.parseInt(response);

                        //todo here is log
                        System.out.println(String.format("REceived PROPOSE Message with offered distance of %d ", offeredDistance));

                        if (offeredDistance < minimumDistance) {
// This is the best offer at present
                            minimumDistance = offeredDistance;
                            nearestAgent = reply.getSender();
                        }
                    }
                    repliesCnt++;
                    if (repliesCnt >= collectorAgents.length) {
// We received all replies
                        step = 2;
                    }

                } else {
                    block();
                }
                break;
            case 2:
// Send the accept to the collector that has smallest distance to herb
                ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                order.addReceiver(nearestAgent);
                order.setContent("ok");
//                order.setConversationId("herb-location");
//                order.setReplyWith("order" + System.currentTimeMillis());
                myAgent.send(order);

                //Send no message to other collector agents
                ACLMessage noOrder = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
//                List<AID> otherCollectors = _getOtherCollectors(collectorAgents, nearestAgent);

                if(nearestAgent !=null)
                {
                    if (!collectorAgents[0].equals( nearestAgent) )
                        noOrder.addReceiver(collectorAgents[0]);
                    if (!collectorAgents[1] .equals(nearestAgent))
                        noOrder.addReceiver(collectorAgents[1]);
                    if (!collectorAgents[2].equals(nearestAgent))
                        noOrder.addReceiver(collectorAgents[2]);
                }
                else //is null beacause all collectors are busy So send no to all f them
                    for (int i = 0; i < collectorAgents.length; ++i) {
                        noOrder.addReceiver(collectorAgents[i]);
                    }

           /*     //No near agent found because all of collectors have a herb to collect
                if (nearestAgent == null)
                    noOrder.addReceiver(otherCollectors.get(2));*/

                noOrder.setContent("no");
//                noOrder.setConversationId("herb-location");
//                order.setReplyWith("order" + System.currentTimeMillis());
                myAgent.send(noOrder);

                //todo here is log
                if (nearestAgent != null)
                    System.out.println(String.format("ACCEPT_PROPOSAL for to %s ", nearestAgent.getName()));
                else
                    System.out.println("ACCEPT_PROPOSAL for NO Body .All agent are BUSY ");

                nearestAgent = null;
                step = 3;
                break;

        }
    }

    /**
     * exclude nearest agent from other collectore agents
     * because we need to send NO message to them
     *
     * @return
     */
 /*   private List<AID> _getOtherCollectors(AID[] collectorAgents, AID nearestAgent) {
        List<AID> otherCollectors = new ArrayList<>(3);


        for (int i = 0; i < collectorAgents.length; i++) {
            if (collectorAgents[i] != nearestAgent)
                otherCollectors.add(collectorAgents[i]);
        }
        return otherCollectors;
    }*/
    @Override
    public boolean done() {
        System.out.println("_______________________________________________");
        return (step == 3);
    }

}
