package tech.examples.ssm.helloworld.demo.sm;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class MachineConfig
        extends EnumStateMachineConfigurerAdapter<DomainState, DomainEvent> {

    public static final String DEFERRED_RESULT_HEADER = "RESULT_HEADER";
    public static final String USER_ID_HEADER = "USER_ID_HEADER";

    private final Action<DomainState, DomainEvent> makePaymentAction;
    private final Action<DomainState, DomainEvent> goTroughAction;
    private final Action<DomainState, DomainEvent> beMoreGenerousAction;
    private final Action<DomainState, DomainEvent> youShallNotPassAction;

    private final StateMachineRuntimePersister<DomainState, DomainEvent, String> stateMachineRuntimePersister;

    @Override
    public void configure(StateMachineConfigurationConfigurer<DomainState, DomainEvent> config) throws Exception {
        config.withConfiguration().autoStartup(true);
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
                .source(DomainState.LOCKED).target(DomainState.UNLOCKED)
                .event(DomainEvent.COIN)
                .action(makePaymentAction)
                .and()
                .withExternal()
                .source(DomainState.UNLOCKED).target(DomainState.LOCKED)
                .event(DomainEvent.PUSH)
                .action(goTroughAction)
                .and()
                .withExternal()
                .source(DomainState.UNLOCKED).target(DomainState.UNLOCKED)
                .event(DomainEvent.COIN)
                .action(beMoreGenerousAction)
                .and()
                .withExternal()
                .source(DomainState.LOCKED).target(DomainState.LOCKED)
                .event(DomainEvent.PUSH)
                .action(youShallNotPassAction);
    }
}
