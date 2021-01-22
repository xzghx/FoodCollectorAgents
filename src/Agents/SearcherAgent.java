package Agents;

import Behaviurs.SearchingBehaviour;
import GUI.GUI;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.gui.GuiAgent;
import models.enumEntity;
import models.AgentEntity;
import jade.core.Agent;


public class SearcherAgent extends Agent implements AgentEntity {



    @Override
    protected void setup() {
        super.setup();
        System.out.println("Setup of SearcherAgent called");
//        System.out.println(getLocalName());
        GUI gui = (GUI) getArguments()[0];

        addBehaviour(new SearchingBehaviour(this, 1000, gui));
    }

    @Override
    public enumEntity Iam() {

        return enumEntity.SearcherAgent;
    }
}



/*    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
msg.addReceiver(new AID(“Peter”, AID.ISLOCALNAME));
msg.setLanguage(“English”);
msg.setOntology(“Weather-forecast-ontology”);
msg.setContent(“Today it’s raining”);
    send(msg);*/

/*
* public void action() {
switch (step) {
case 0:
// Send the cfp to all sellers
ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
for (int i = 0; i < sellerAgents.length; ++i) {
cfp.addReceiver(sellerAgents[i]);
}
cfp.setContent(targetBookTitle);
cfp.setConversationId(“book-trade”);
cfp.setReplyWith(“cfp”+System.currentTimeMillis()); // Unique value
myAgent.send(cfp);
// Prepare the template to get proposals
mt = MessageTemplate.and(MessageTemplate.MatchConversationId(“book-trade”),
MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
step = 1;
break;
case 1:
// Receive all proposals/refusals from seller agents
ACLMessage reply = myAgent.receive(mt);
if (reply != null) {
// Reply received
if (reply.getPerformative() == ACLMessage.PROPOSE) {
// This is an offer
int price = Integer.parseInt(reply.getContent());
if (bestSeller == null || price < bestPrice) {
// This is the best offer at present
bestPrice = price;
bestSeller = reply.getSender();
}
}
repliesCnt++;
if (repliesCnt >= sellerAgents.length) {
// We received all replies
step = 2;
}
}
else {
20
block();
}
break;
case 2:
// Send the purchase order to the seller that provided the best offer
ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
order.addReceiver(bestSeller);
order.setContent(targetBookTitle);
order.setConversationId(“book-trade”);
order.setReplyWith(“order”+System.currentTimeMillis());
myAgent.send(order);
// Prepare the template to get the purchase order reply
mt = MessageTemplate.and(MessageTemplate.MatchConversationId(“book-trade”),
MessageTemplate.MatchInReplyTo(order.getReplyWith()));
step = 3;
break;
case 3:
// Receive the purchase order reply
reply = myAgent.receive(mt);
if (reply != null) {
// Purchase order reply received
if (reply.getPerformative() == ACLMessage.INFORM) {
// Purchase successful. We can terminate
System.out.println(targetBookTitle+“ successfully purchased.”);
System.out.println(“Price = ”+bestPrice);
myAgent.doDelete();
}
step = 4;
}
else {
block();
}
break;
}
}
* */
