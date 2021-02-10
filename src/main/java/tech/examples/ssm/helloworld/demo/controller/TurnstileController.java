package tech.examples.ssm.helloworld.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.examples.ssm.helloworld.demo.sm.DomainEvent;
import tech.examples.ssm.helloworld.demo.sm.DomainState;

@RestController
@RequestMapping("turnstile")
public class TurnstileController {

    @Autowired
    StateMachine<DomainState, DomainEvent> machine;

    @PostMapping("/coin")
    public ResponseEntity<Void> dropCoin() {
        machine.sendEvent(DomainEvent.COIN);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/push")
    public ResponseEntity<Void> pushIt() {
        machine.sendEvent(DomainEvent.PUSH);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/state")
    public DomainState getState() {
        return machine.getState().getId();
    }
}
