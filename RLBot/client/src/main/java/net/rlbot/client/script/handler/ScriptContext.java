package net.rlbot.client.script.handler;

import lombok.Value;
import net.rlbot.api.script.Script;
import net.rlbot.api.script.randoms.RandomEventHandler;

import java.util.Set;

@Value
public class ScriptContext {

    Script script;

    Thread  thread;
}
