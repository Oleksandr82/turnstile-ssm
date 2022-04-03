package tech.examples.ssm.helloworld.demo.sm;

import java.util.EnumSet;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class MachineConfiguration extends EnumStateMachineConfigurerAdapter<DomainState, DomainEvent> {

    public static final String RESULT_HEADER = "RESULT_HEADER";
    private final StateMachineListenerAdapter<DomainState, DomainEvent> listenerAdapter;
    private final Action<DomainState, DomainEvent> makePaymentAction;
    private final Action<DomainState, DomainEvent> goTroughAction;
    private final Action<DomainState, DomainEvent> beMoreGenerousAction;
    private final Action<DomainState, DomainEvent> youShallNotPassAction;
    private final StateMachineRuntimePersister<DomainState, DomainEvent, String> stateMachineRuntimePersister;

    @Override
    public void configure(StateMachineConfigurationConfigurer<DomainState, DomainEvent> config) throws Exception {
        config.withConfiguration().autoStartup(true).listener(listenerAdapter);
        config.withPersistence().runtimePersister(stateMachineRuntimePersister);
    }

    @Override
    public void configure(StateMachineStateConfigurer<DomainState, DomainEvent> states) throws Exception {
        states.withStates()
                .initial(DomainState.LOCKED)
                .states(EnumSet.allOf(DomainState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<DomainState, DomainEvent> transitions) throws Exception {
        transitions
                .withExternal()
                .source(DomainState.LOCKED).target(DomainState.UNLOCKED).event(DomainEvent.COIN).action(makePaymentAction)
                .and()
                .withExternal()
                .source(DomainState.UNLOCKED).target(DomainState.LOCKED).event(DomainEvent.PUSH).action(goTroughAction)
                .and()
                .withExternal()
                .source(DomainState.LOCKED).target(DomainState.LOCKED).event(DomainEvent.PUSH).action(youShallNotPassAction)
                .and()
                .withExternal()
                .source(DomainState.UNLOCKED).target(DomainState.UNLOCKED).event(DomainEvent.COIN).action(beMoreGenerousAction);
    }
}
