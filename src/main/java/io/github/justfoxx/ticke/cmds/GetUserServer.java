package io.github.justfoxx.ticke.cmds;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import io.github.justfoxx.ticke.Handler;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

public class GetUserServer implements Handler.Command {
    @Override
    public String getName() {
        return "getuserserver";
    }

    @Override
    public String getDescription() {
        return "Gets the server of a user";
    }

    @Override
    public String getUsage() {
        return String.format("%s%s (user)", Handler.prefix, getName());
    }

    @Override
    public Exception canExecute(MessageCreateEvent event) {
        if (event.getMessage().getAuthor().get().isBot()) return new Exception("You cannot use this command as a bot");
        if (!event.getMessage().getAuthor().isPresent()) return new Exception("Required author is not present");
        return null;
    }

    @Override
    public Mono<?> run(String[] args, MessageCreateEvent event) {
        Message message = event.getMessage();
        User target = message.getAuthor().get();

        if (message.getUserMentions().size() > 0) {
            target = message.getUserMentions().get(0);
        }

        Snowflake targetId = target.getId();

        List<Guild> targetServers = event.getClient()
                .getGuilds()
                .publishOn(Schedulers.boundedElastic())
                .filter(guild -> Boolean.TRUE.equals(guild.getMembers()
                        .filter(member -> member.getId().equals(targetId))
                        .hasElements()
                        .block()))
                .collectList().block();

        if (targetServers == null || targetServers.size() == 0) {
            return message.getChannel().flatMap(channel -> channel.createMessage("User is not in any servers"));
        }

        User finalTarget = target;
        EmbedCreateSpec.Builder embedBuilder = EmbedCreateSpec.builder()
                .title(String.format("Servers of %s", finalTarget.getUsername()));

        for (Guild targetServer : targetServers) {
            embedBuilder.addField(targetServer.getName(), String.format("ID: %s", targetServer.getId().asString()), false);
        }

        return message.getChannel().flatMap(channel ->
                channel.createMessage(embedBuilder.build())
        );
    }
}
