package io.github.justfoxx.ticke.cmds;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import io.github.justfoxx.ticke.Handler;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

import java.time.Instant;

@SuppressWarnings("ALL")
public class GetMemberJoinDate implements Handler.Command {
    @Override @NonNull
    public String getName() {
        return "getmemberjoindate";
    }

    @Override @NonNull
    public String getDescription() {
        return "Gets the join date of a member";
    }

    @Override @NonNull
    public String getUsage() {
        return String.format("%s (user)", getName());
    }

    @Override
    public void canExecute(MessageCreateEvent event) throws Exception {
        if (event.getMessage().getAuthor().isEmpty()) throw new Exception("Required author is not present");
        if (event.getMessage().getAuthor().get().isBot()) throw new Exception("You cannot use this command as a bot");
    }

    @Override @NonNull
    public Mono<?> run(String[] args, MessageCreateEvent event) throws Exception {
        Message message = event.getMessage();
        Member target = message.getAuthorAsMember().block();

        if (message.getMemberMentions().size() > 0) {
            target = message.getMemberMentions().get(0).asFullMember().block();
        }

        Instant joinDate = target.getJoinTime().get();

        EmbedCreateSpec.Builder embedBuilder = EmbedCreateSpec.builder()
                .title(String.format("Join date <t:%s>",joinDate.getEpochSecond()))
                .author(target.getUsername(), null, target.getAvatarUrl());

        return message.getChannel().flatMap(channel ->
                channel.createMessage(embedBuilder.build())
        );
    }
}
