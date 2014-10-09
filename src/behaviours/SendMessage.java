/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package behaviours;

import agentcommgui.AgentCommGUI;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Ethan_Hunt
 */
public class SendMessage extends OneShotBehaviour {
        
        private ACLMessage msg;
        
        private AgentCommGUI myAgent;
        
        public SendMessage(ACLMessage msg,Agent myAgent){
            super();
            this.msg = msg;
            this.myAgent = (AgentCommGUI)myAgent;
        }
        
        @Override
        public void action() {
            myAgent.send(msg);
            System.out.println("****I Sent Message to::> R1 *****"+"\n"+
                                "The Content of My Message is::>"+ msg.getContent());
            
            GuiEvent ge = new GuiEvent(this, AgentCommGUI.MESSAGE_SENT);
            ge.addParameter(msg);
            myAgent.postGuiEvent(ge);
        }
    }
