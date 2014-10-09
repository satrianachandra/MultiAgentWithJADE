/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agentcommgui.AgentCommGUI;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Ethan_Hunt
 */
public class ReceiveMessage extends CyclicBehaviour {

        // Variable to Hold the content of the received Message
        private String Message_Performative;
        private String Message_Content;
        private String SenderName;
        private String MyPlan;
        private AgentCommGUI myAgent;

        public ReceiveMessage(Agent myAgent){
            super();
            this.myAgent = (AgentCommGUI) myAgent;
        }
        
        public void action() {
            ACLMessage msg = myAgent.receive();
            if(msg != null) {

                Message_Performative = msg.getPerformative(msg.getPerformative());
                Message_Content = msg.getContent();
                SenderName = msg.getSender().getLocalName();
                System.out.println(" ****I Received a Message***" +"\n"+
                        "The Sender Name is::>"+ SenderName+"\n"+
                        "The Content of the Message is::> " + Message_Content + "\n"+
                        "::: And Performative is::> " + Message_Performative + "\n");
                System.out.println("ooooooooooooooooooooooooooooooooooooooo");
                
                GuiEvent ge = new GuiEvent(this, AgentCommGUI.MESSAGE_RECEIVED);
                ge.addParameter(msg);
                myAgent.postGuiEvent(ge);
                
            }

        }
    }
