package io.github.justfoxx.ticke.cmds;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Permission;
import io.github.justfoxx.ticke.Handler;
import io.github.justfoxx.ticke.Main;
import io.github.justfoxx.ticke.cmds.ticket.*;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
@SuppressWarnings("ALL")
public class Ticket implements Handler.Command {
    private final HashSet<TicketCommand> cmds = new HashSet<>();
    public void register() {
        cmds.add(new TicketHelp(this));
        cmds.add(new TicketCreate(this));
        cmds.add(new TicketClose(this));
        cmds.add(new TicketAdd(this));
        cmds.add(new TicketRemove(this));
        cmds.add(new TicketDelete(this));
    }
    @Override @NonNull
    public String getName() {
        return "ticket";
    }

    @Override @NonNull
    public String getDescription() {
        return "Ticket management";
    }

    @Override @NonNull
    public String getUsage() {
        return String.format("%s (cmd)", getName());
    }

    @Override
    public void canExecute(MessageCreateEvent event) throws Exception {
        if (event.getMessage().getAuthor().isEmpty()) throw  new Exception("Required author is not present");
        if (event.getMessage().getAuthor().get().isBot()) throw  new Exception("You cannot use this command as a bot");
    }

    @Override @NonNull
    public Mono<?> run(String[] args, MessageCreateEvent event) throws Exception {
        if(!(args.length > 0)) return sendHelp(event);
        String cmdName = args[0].toLowerCase();
        String[] arr = Arrays.copyOfRange(args, 1, args.length);
        for (Handler.Command cmd : cmds) {
            if (!cmd.getName().equals(cmdName)) continue;
            cmd.canExecute(event);
            return cmd.run(arr, event);
        }

        return sendHelp(event);
    }

     public Mono<?> sendHelp(MessageCreateEvent event) {
        Message message = event.getMessage();
        User user = event.getClient().getSelf().block();
        EmbedCreateSpec.Builder embedBuilder = EmbedCreateSpec.builder()
                .title("Ticket TicketHelp")
                .description("Ticket management")
                .author(user.getUsername(), null, user.getAvatarUrl());

        for (Handler.Command cmd : cmds) {
            embedBuilder.addField(cmd.getName(), "Usage: `"+cmd.getUsage()+"`\n"+cmd.getDescription(), true);
        }

        return message.getChannel().flatMap(channel -> channel.createMessage(embedBuilder.build()));
    }

    public boolean hasPermission(Member member) {
        return member.getBasePermissions().block().contains(Permission.MANAGE_CHANNELS);
    }

    public boolean isTicketChannel(MessageCreateEvent event) {
        var channel = event.getMessage().getChannel().ofType(TextChannel.class).block();
        return isTicketChannel(channel);
    }

    public boolean isTicketChannel(TextChannel channel) {
        var topic = channel.getTopic().orElse("");
        try {
            Map<String, Object> code = Main.yaml.load(topic);
            return code.containsKey("ticket") && code.get("ticket").equals(channel.getId().asLong());
        } catch (Exception e) {
            return false;
        }
    }

    public Optional<Map<String, Object>> getTicketCode(TextChannel channel) {
        var topic = channel.getTopic().orElse("");
        try {
            return Optional.of(Main.yaml.load(topic));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static abstract class TicketCommand implements Handler.Command {
        protected final Ticket ticket;

        public TicketCommand(Ticket ticket) {
            this.ticket = ticket;
        }
    }
}
