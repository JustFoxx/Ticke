package io.github.justfoxx.ticke.cmds.ticket;

import discord4j.core.event.domain.message.MessageCreateEvent;
import io.github.justfoxx.ticke.cmds.Ticket;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

public class TicketHelp extends Ticket.TicketCommand {
    public TicketHelp(Ticket ticket) {
        super(ticket);
    }

    @Override @NonNull
    public String getName() {
        return "help";
    }

    @Override @NonNull
    public String getDescription() {
        return "Shows this help message";
    }

    @Override @NonNull
    public String getUsage() {
        return String.format("%s", getName());
    }

    @Override
    public void canExecute(MessageCreateEvent event) throws Exception{}

    @Override @NonNull
    public Mono<?> run(String[] args, MessageCreateEvent event) throws Exception {
        return ticket.sendHelp(event);
    }
}
