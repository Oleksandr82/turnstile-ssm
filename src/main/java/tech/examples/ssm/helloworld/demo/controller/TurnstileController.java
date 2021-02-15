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

import static tech.examples.ssm.helloworld.demo.sm.MachineConfig.DEFERRED_RESULT_HEADER;
import static tech.examples.ssm.helloworld.demo.sm.MachineConfig.USER_ID_HEADER;

@RestController
@RequestMapping("turnstile/user/{userId}")
@RequiredArgsConstructor
public class TurnstileController {

    private final StateMachineService<DomainState, DomainEvent> machineService;

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

    private StateMachine<DomainState, DomainEvent> getMachine(String id) {
        return machineService.acquireStateMachine(id);
    }
}
