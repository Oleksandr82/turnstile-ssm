package tech.examples.ssm.helloworld.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import tech.examples.ssm.helloworld.demo.sm.DomainEvent;
import tech.examples.ssm.helloworld.demo.sm.DomainState;

import static tech.examples.ssm.helloworld.demo.sm.MachineConfig.DEFERRED_RESULT_HEADER;

@RestController
@RequestMapping("turnstile")
public class TurnstileController {

    @Autowired
    StateMachine<DomainState, DomainEvent> machine;

    @PostMapping("/coin")
    public DeferredResult<ResponseEntity<DomainState>> dropCoin() {

        DeferredResult<ResponseEntity<DomainState>> result = new DeferredResult<>();

        machine.sendEvent(MessageBuilder
                .withPayload(DomainEvent.COIN)
                .setHeader(DEFERRED_RESULT_HEADER, result)
                .build());

        return result;
    }

    @PostMapping("/push")
    public DeferredResult<ResponseEntity<DomainState>> pushIt() {

        DeferredResult<ResponseEntity<DomainState>> result = new DeferredResult<>();

        machine.sendEvent(MessageBuilder
                .withPayload(DomainEvent.PUSH)
                .setHeader(DEFERRED_RESULT_HEADER, result)
                .build());

        return result;
    }

    @GetMapping("/state")
    public DomainState getState() {
        return machine.getState().getId();
    }
}
