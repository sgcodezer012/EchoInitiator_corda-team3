package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(EchoInitiator.class)
public class EchoResponder extends FlowLogic<Void> {
    private FlowSession counterpartySession;

    public EchoResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        // Deserialize Incoming Message
        String inboundMessage = counterpartySession.receive(String.class).unwrap(s -> s);
        // Reverse Message
        String reverseMsg = reverse(inboundMessage);
        // Console Log
        System.out.println("Destination: " + counterpartySession.getDestination() +";InboundMessage: " + inboundMessage + ";ReverseMessage: " + reverseMsg);
        // Outbound Message
        counterpartySession.send(reverseMsg);
        return null;
    }

    public String reverse(String inputString) {
        return new StringBuilder(inputString).reverse().toString();
    }
}
