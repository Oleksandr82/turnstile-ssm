package tech.examples.ssm.helloworld.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import tech.examples.ssm.helloworld.demo.sm.DomainEvent;
import tech.examples.ssm.helloworld.demo.sm.DomainState;
import tech.examples.ssm.helloworld.demo.sm.MachineConfiguration;

@RestController
@RequestMapping("turnstile/user/{userId}")
@RequiredArgsConstructor
public class TurnstileController {

    private final StateMachineService<DomainState, DomainEvent> machineService;

    private StateMachine<DomainState, DomainEvent> getMachine(String id) {
        return machineService.acquireStateMachine(id);
    }

    @PostMapping("/coin")
    public DeferredResult<ResponseEntity<DomainState>> dropCoin(@PathVariable String userId) {

        DeferredResult<ResponseEntity<DomainState>> result = new DeferredResult<>();

        getMachine(userId).sendEvent(MessageBuilder
                .withPayload(DomainEvent.COIN)
                .setHeader(MachineConfiguration.RESULT_HEADER, result)
                .build());

        return result;
    }

    @PostMapping("/push")
    public DeferredResult<ResponseEntity<DomainState>> pushIt(@PathVariable String userId) {
        DeferredResult<ResponseEntity<DomainState>> result = new DeferredResult<>();

        getMachine(userId).sendEvent(MessageBuilder
                .withPayload(DomainEvent.PUSH)
                .setHeader(MachineConfiguration.RESULT_HEADER, result)
                .build());

        return result;
    }

    @GetMapping("/state")
    public DomainState getState(@PathVariable String userId) {
        return getMachine(userId).getState().getId();
    }
}
