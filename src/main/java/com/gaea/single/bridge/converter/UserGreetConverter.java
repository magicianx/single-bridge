package com.gaea.single.bridge.converter;

import com.gaea.single.bridge.dto.user.GreetMessageRes;
import com.gaea.single.bridge.dto.user.UserGreetConfigRes;
import com.gaea.single.bridge.entity.mongodb.SystemGreetMessage;
import com.gaea.single.bridge.entity.mongodb.UserGreetConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;

/** @author cludy */
public class UserGreetConverter {
  public static final Converter<Pair<UserGreetConfig, List<SystemGreetMessage>>, UserGreetConfigRes>
      toUserGreetConfigRes =
          (pair) -> {
            UserGreetConfig config = pair.getLeft();
            List<SystemGreetMessage> messages = pair.getRight();

            List<GreetMessageRes> systemMessages =
                messages.stream()
                    .map(
                        message -> {
                          boolean isUsing =
                              config.getSystemMessageIds().stream()
                                  .anyMatch(id -> id.equals(message.getId()));
                          return new GreetMessageRes(
                              message.getId(), isUsing, message.getContent());
                        })
                    .collect(Collectors.toList());

            List<GreetMessageRes> customMessages =
                config.getCustomMessages().stream()
                    .map(
                        message -> new GreetMessageRes(message.getId(), true, message.getContent()))
                    .collect(Collectors.toList());

            return new UserGreetConfigRes(systemMessages, customMessages);
          };
}
