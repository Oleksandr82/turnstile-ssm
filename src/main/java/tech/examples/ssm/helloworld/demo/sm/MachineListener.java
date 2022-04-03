package tech.examples.ssm.helloworld.demo.sm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MachineListener extends StateMachineListenerAdapter<DomainState, DomainEvent> {

    @Override
    public void stateChanged(State<DomainState, DomainEvent> from, State<DomainState, DomainEvent> to) {
        log.info("stateChanged (from: {}, to: {})", from, to);
    }
}
