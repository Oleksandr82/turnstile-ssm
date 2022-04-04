package tech.examples.ssm.helloworld.demo.sm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;
import tech.examples.ssm.helloworld.demo.sm.action.BeMoreGenerousAction;
import tech.examples.ssm.helloworld.demo.sm.action.GoTroughAction;
import tech.examples.ssm.helloworld.demo.sm.action.MakePaymentAction;
import tech.examples.ssm.helloworld.demo.sm.action.YouShallNotPassAction;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MachineConfigTest {

    @MockBean
    GoTroughAction goTroughAction;

    @MockBean
    MakePaymentAction makePaymentAction;

    @MockBean
    YouShallNotPassAction youShallNotPassAction;

    @MockBean
    BeMoreGenerousAction beMoreGenerousAction;

    @MockBean
    MachineListener machineListener;

    @Autowired
    StateMachineFactory<DomainState, DomainEvent> machineFactory;

    private StateMachine<DomainState, DomainEvent> machine;

    @BeforeEach
    void setUp() {
        machine = machineFactory.getStateMachine("test");
        machine.stop();
    }

    @AfterEach
    void tearDown() {
        machine.stop();
    }

    @Test
    void givenLocked_whenCoin_shouldStayOpen() throws Exception {

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
                        .expectStateChanged(1)
                        .expectState(DomainState.UNLOCKED)
                        .and()
                        .build();
        plan.test();
    }

    @Test
    void givenUnlocked_whenPush_shouldLock() throws Exception {

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
}
