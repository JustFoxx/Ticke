package io.github.justfoxx.ticke.cmds.ticket;

import discord4j.core.event.domain.message.MessageCreateEvent;
import io.github.justfoxx.ticke.cmds.Ticket;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

public class TicketCreate extends Ticket.TicketCommand {
    public TicketCreate(Ticket ticket) {
        super(ticket);
    }

    @Override @NonNull
    public String getName() {
        return "create";
    }

    @Override @NonNull
    public String getDescription() {
        return "Create a ticket";
    }

    @Override @NonNull
    public String getUsage() {
        return "create";
    }

    @Override
    public void canExecute(MessageCreateEvent event) throws Exception {

    }

    @Override @NonNull
    public Mono<?> run(String[] args, MessageCreateEvent event) throws Exception {
        return Mono.empty();
    }
}
