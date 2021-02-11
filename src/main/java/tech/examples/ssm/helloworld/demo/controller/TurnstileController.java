package tech.examples.ssm.helloworld.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import tech.examples.ssm.helloworld.demo.sm.DomainEvent;
import tech.examples.ssm.helloworld.demo.sm.DomainState;

import java.util.HashMap;
import java.util.Map;

import static tech.examples.ssm.helloworld.demo.sm.MachineConfig.DEFERRED_RESULT_HEADER;
import static tech.examples.ssm.helloworld.demo.sm.MachineConfig.USER_ID_HEADER;

@RestController
@RequestMapping("turnstile/user/{userId}")
public class TurnstileController {

    @Autowired
    StateMachineFactory<DomainState, DomainEvent> machineFactory;

    private final Map<String, StateMachine<DomainState, DomainEvent>> machines = new HashMap<>();

    @PostMapping("/coin")
    public DeferredResult<ResponseEntity<DomainState>> dropCoin(@PathVariable String userId) {

        DeferredResult<ResponseEntity<DomainState>> result = new DeferredResult<>();

        getMachine(userId).sendEvent(MessageBuilder
                .withPayload(DomainEvent.COIN)
                .setHeader(DEFERRED_RESULT_HEADER, result)
                .setHeader(USER_ID_HEADER, userId)
                .build());

        return result;
    }

    @PostMapping("/push")
    public DeferredResult<ResponseEntity<DomainState>> pushIt(@PathVariable String userId) {

        DeferredResult<ResponseEntity<DomainState>> result = new DeferredResult<>();

        getMachine(userId).sendEvent(MessageBuilder
                .withPayload(DomainEvent.PUSH)
                .setHeader(DEFERRED_RESULT_HEADER, result)
                .setHeader(USER_ID_HEADER, userId)
                .build());

        return result;
    }

    @GetMapping("/state")
    public DomainState getState(@PathVariable String userId) {
        return getMachine(userId).getState().getId();
    }

    private synchronized StateMachine<DomainState, DomainEvent> getMachine(String id) {
        StateMachine<DomainState, DomainEvent> machine = machines.get(id);
        if (machine == null) {
            machine = machineFactory.getStateMachine(id);
            machines.put(id, machine);
        }
        return machine;
    }
}
