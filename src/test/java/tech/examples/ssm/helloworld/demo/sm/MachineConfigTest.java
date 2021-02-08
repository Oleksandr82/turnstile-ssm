package tech.examples.ssm.helloworld.demo.sm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {
        MachineConfig.class
})
class MachineConfigTest {

    @Autowired
    StateMachine<DomainState, DomainEvent> machine;

    @BeforeEach
    void setUp() {
        machine.stop();
    }

    @Test
    void testBasicClosedOpenClosedLoop() throws Exception {

        StateMachineTestPlan<DomainState, DomainEvent> plan =
                StateMachineTestPlanBuilder.<DomainState, DomainEvent>builder()
                        .stateMachine(machine)
                        .step().expectStateMachineStarted(1)
                        .and().step()
                        .expectState(DomainState.LOCKED)
                        .and().step()
                        .sendEvent(DomainEvent.COIN)
                        .expectStateChanged(1)
                        .expectState(DomainState.UNLOCKED)
                        .and().step()
                        .sendEvent(DomainEvent.PUSH)
                        .expectStateChanged(1)
                        .expectState(DomainState.LOCKED)
                        .and()
                        .build();

        plan.test();
    }

    @Test
    void givenLocked_whenPushed_shouldStayLocked() throws Exception {

        StateMachineTestPlan<DomainState, DomainEvent> plan =
                StateMachineTestPlanBuilder.<DomainState, DomainEvent>builder()
                        .stateMachine(machine)
                        .step().expectStateMachineStarted(1)
                        .and().step()
                        .expectState(DomainState.LOCKED)
                        .and().step()
                        .sendEvent(DomainEvent.PUSH)
                        .expectStateChanged(0)
                        .expectState(DomainState.LOCKED)
                        .and()
                        .build();

        plan.test();
    }

    @Test
    void givenOpen_whenCoin_shouldStayOpen() throws Exception {

        StateMachineTestPlan<DomainState, DomainEvent> plan =
                StateMachineTestPlanBuilder.<DomainState, DomainEvent>builder()
                        .stateMachine(machine)
                        .step().expectStateMachineStarted(1)
                        .and().step()
                        .expectState(DomainState.LOCKED)
                        .and().step()
                        .sendEvent(DomainEvent.COIN)
                        .expectStateChanged(1)
                        .expectState(DomainState.UNLOCKED)
                        .and().step()
                        .sendEvent(DomainEvent.COIN)
                        .expectStateChanged(0)
                        .expectState(DomainState.UNLOCKED)
                        .and()
                        .build();

        plan.test();
    }
}