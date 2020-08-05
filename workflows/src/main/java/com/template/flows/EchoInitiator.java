package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.node.ServiceHub;
import net.corda.core.node.services.IdentityService;
import net.corda.core.utilities.ProgressTracker;
import org.w3c.dom.css.Counter;

import java.util.Set;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class EchoInitiator extends FlowLogic<String> {
    private final ProgressTracker progressTracker = new ProgressTracker();
    private String message;
    private String counterParty;
    public EchoInitiator(String Message, String Counterparty) {
        this.message= Message;
        this.counterParty = Counterparty;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public String call() throws FlowException {

        ServiceHub serviceHub = getServiceHub();
        IdentityService identityService = serviceHub.getIdentityService();
        // Get Peer Parties
        Set<Party> parties = identityService.partiesFromName(this.counterParty, true);
        // Get Next Parties Iteration
        Party receiver = parties.iterator().next();

        FlowSession flowSession = initiateFlow(receiver);
        // Initiator Send message
        flowSession.send(message);
        // Initiator Receiver/ Outbound Receiver
        String outbound = flowSession.receive(String.class).unwrap( s-> s);
        System.out.println("Outbound Msg: " + outbound);
        return outbound;
    }



}
