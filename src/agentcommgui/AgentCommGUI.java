    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */
    package agentcommgui;

    import behaviours.ReceiveMessage;
    import behaviours.SendMessage;
    import jade.core.AID;

    import jade.domain.DFService;
    import jade.domain.FIPAAgentManagement.DFAgentDescription;
    import jade.domain.FIPAAgentManagement.SearchConstraints;
    import jade.domain.FIPAAgentManagement.ServiceDescription;
    import jade.domain.FIPAException;
    import jade.lang.acl.ACLMessage;
    import jade.gui.*;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.logging.Level;
    import java.util.logging.Logger;



    /**
     *
     * @author Ethan_Hunt
     */
    public class AgentCommGUI extends GuiAgent {

        private AgentForm2 RA1;
        public static final int MESSAGE_RECEIVED = 1;
        public static final int MESSAGE_SENT = 2;
        public List<AID> agentsList= null;

        //an example of adding 1 remote platforms
        public AID remoteDF;
        //private List<AID>dfList;

        protected void setup() {
            /** Registration with the DF */
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("SenderAgent");
            sd.setName(getName());
            sd.setOwnership("ExampleReceiversOfJADE");
            sd.addOntologies("SenderAgent");
            dfd.setName(getAID());

            dfd.addServices(sd);
            try {
            DFService.register(this,dfd);
            } catch (FIPAException e) {
            System.err.println(getLocalName()+" registration with DF unsucceeded. Reason: "+e.getMessage());
            //doDelete();
            }
            /*
            AID aDF = new AID("df@Platform2",AID.ISGUID);
            aDF.addAddresses("http://sakuragi:54960/acc");
            */
            RA1 = new AgentForm2();
            RA1.setAgent(this);
            RA1.setTitle("Agent " + this.getName());
            try {
                RefetchAgentsList();
                RA1.populateAgentsListOnGUI();
            } catch (FIPAException ex) {
                Logger.getLogger(AgentCommGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            ReceiveMessage rm = new ReceiveMessage(this);
            addBehaviour(rm);
        }

        @Override
        protected void onGuiEvent(GuiEvent ge) {
            int type = ge.getType();
            if (type==MESSAGE_RECEIVED){
                ACLMessage theMessage = (ACLMessage)ge.getParameter(0);
                RA1.appendReceivedMessagesText(theMessage.getSender().getLocalName()+" says "+theMessage.getContent()+"\n");
                AID msgSender = theMessage.getSender();
                //add the sender if it's not on the list yet
                if (!agentsList.contains(msgSender)){
                    agentsList.add(msgSender);
                    RA1.populateAgentsListOnGUI();
                }
            }else if (type ==MESSAGE_SENT){
                ACLMessage theMessage = (ACLMessage)ge.getParameter(0);
                RA1.appendSentMessagesText(theMessage.getSender().getLocalName()+" says "+theMessage.getContent()+"\n");
            }
            //RA1.setTitle("My Agent Name is: " + this.getName());
        }

        public void RefetchAgentsList() throws FIPAException{
            agentsList= new ArrayList<AID>();
            agentsList.addAll(findAgents(getDefaultDF()));
            if (remoteDF!=null){
                agentsList.addAll(findAgents(remoteDF));
            }
        }

        public List<AID> findAgents(AID dfAID) throws FIPAException{
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription templateSd = new ServiceDescription();
            templateSd.setType("SenderAgent");
            template.addServices(templateSd);
            SearchConstraints sc = new SearchConstraints();
            // We want to receive 10 results at most
            sc.setMaxResults(new Long(20));

            DFAgentDescription[] results = DFService.search(this,dfAID, template, sc);
            List<AID> myAgentsList = new ArrayList<AID>();
            if (results.length>0){
                for(int i=0;i<results.length;i++){
                    DFAgentDescription agentDesc = results[i];
                    AID provider = agentDesc.getName();
                    myAgentsList.add(provider);
               }   
            }
            return myAgentsList;
        }

    }
