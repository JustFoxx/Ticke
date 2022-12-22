package io.github.justfoxx.ticke.cmds.ticket;

import discord4j.core.event.domain.message.MessageCreateEvent;
import io.github.justfoxx.ticke.cmds.Ticket;
import reactor.core.publisher.Mono;

public class TicketCreate extends Ticket.TicketCommand {
    public TicketCreate(Ticket ticket) {
        super(ticket);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public void canExecute(MessageCreateEvent event) throws Exception {

    }

    @Override
    public Mono<?> run(String[] args, MessageCreateEvent event) throws Exception {
        return null;
    }
}
