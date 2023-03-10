package io.github.justfoxx.ticke.cmds.ticket;

import discord4j.core.event.domain.message.MessageCreateEvent;
import io.github.justfoxx.ticke.cmds.Ticket;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;
@SuppressWarnings("ALL")
public class TicketClose extends Ticket.TicketCommand {
    public TicketClose(Ticket ticket) {
        super(ticket);
    }

    @Override @NonNull
    public String getName() {
        return "close";
    }

    @Override @NonNull
    public String getDescription() {
        return "Close a ticket";
    }

    @Override @NonNull
    public String getUsage() {
        return "close";
    }

    @Override
    public void canExecute(MessageCreateEvent event) throws Exception {
        ticket.canExecute(event); 
        if (ticket.hasPermission(event.getMember().get())) throw new Exception("You do not have permission to add users to tickets");
        if (event.getMessage().getMemberMentions().size() < 1) throw new Exception("You must mention a user to add to the ticket");
        if (ticket.isTicketChannel(event)) throw new Exception("This channel is not ticket channel");
    }

    @Override @NonNull
    public Mono<?> run(String[] args, MessageCreateEvent event) throws Exception {
        return Mono.empty();
    }
}
