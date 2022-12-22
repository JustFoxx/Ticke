package io.github.justfoxx.ticke.cmds;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import io.github.justfoxx.ticke.Handler;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.NonNull;

import java.util.List;

public class GetUserServer implements Handler.Command {
    @Override @NonNull
    public String getName() {
        return "getuserserver";
    }

    @Override @NonNull
    public String getDescription() {
        return "Gets the server of a user";
    }

    @Override @NonNull
    public String getUsage() {
        return String.format("%s%s (user)", Handler.prefix, getName());
    }

    @Override
    public void canExecute(MessageCreateEvent event) throws Exception {
        if (event.getMessage().getAuthor().isEmpty()) throw  new Exception("Required author is not present");
        if (event.getMessage().getAuthor().get().isBot()) throw  new Exception("You cannot use this command as a bot");
    }

    @Override @NonNull
    public Mono<?> run(String[] args, MessageCreateEvent event) throws Exception {
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
            throw new Exception("User is not in any servers");
        }

        EmbedCreateSpec.Builder embedBuilder = EmbedCreateSpec.builder()
                .title("Servers")
                .author(target.getUsername(), null, target.getAvatarUrl());

        for (Guild targetServer : targetServers) {
            embedBuilder.addField(targetServer.getName(), String.format("ID: %s", targetServer.getId().asString()), false);
        }

        return message.getChannel().flatMap(channel ->
                channel.createMessage(embedBuilder.build())
        );
    }
}
