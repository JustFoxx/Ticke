package io.github.justfoxx.ticke.cmds.ticket;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import io.github.justfoxx.ticke.cmds.Ticket;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;
@SuppressWarnings("ALL")
public class TicketRemove extends Ticket.TicketCommand {
    public TicketRemove(Ticket ticket) {
        super(ticket);
    }

    @Override @NonNull
    public String getName() {
        return "remove";
    }

    @Override @NonNull
    public String getDescription() {
        return "Remove a user from a ticket";
    }

    @Override @NonNull
    public String getUsage() {
        return "remove <user>";
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
        TextChannel channel = (TextChannel) event.getMessage().getChannel().block();
        Member member = event.getMessage().getMemberMentions().get(0).asFullMember().block();

        var permissions = channel.getEffectivePermissions(member).block();

        if(!permissions.contains(Permission.VIEW_CHANNEL)) throw new Exception("This user is not in the ticket");

        permissions.add(Permission.VIEW_CHANNEL);

        return channel.addMemberOverwrite(member.getId(), PermissionOverwrite.forMember(member.getId(), PermissionSet.none(),permissions));
    }
}
