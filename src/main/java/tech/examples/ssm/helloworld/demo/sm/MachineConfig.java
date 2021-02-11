package tech.examples.ssm.helloworld.demo.sm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class MachineConfig
        extends EnumStateMachineConfigurerAdapter<DomainState, DomainEvent> {

    public static final String DEFERRED_RESULT_HEADER = "RESULT_HEADER";
    public static final String USER_ID_HEADER = "USER_ID_HEADER";

    @Autowired
    private Action<DomainState, DomainEvent> makePaymentAction;

    @Autowired
    private Action<DomainState, DomainEvent> goTroughAction;

    @Autowired
    private Action<DomainState, DomainEvent> beMoreGenerousAction;

    @Autowired
    private Action<DomainState, DomainEvent> youShallNotPassAction;

    @Override
    public void configure(StateMachineConfigurationConfigurer<DomainState, DomainEvent> config) throws Exception {
        config.withConfiguration().autoStartup(true);
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
