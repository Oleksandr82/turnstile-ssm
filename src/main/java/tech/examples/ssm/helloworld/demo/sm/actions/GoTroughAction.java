package tech.examples.ssm.helloworld.demo.sm.actions;

import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import tech.examples.ssm.helloworld.demo.sm.DomainEvent;
import tech.examples.ssm.helloworld.demo.sm.DomainState;
import tech.examples.ssm.helloworld.demo.sm.MachineConfig;

@Component
public class GoTroughAction implements Action<DomainState, DomainEvent> {

    @Override
    public void execute(StateContext<DomainState, DomainEvent> context) {

        System.out.println("Let it go");

        DeferredResult<ResponseEntity<DomainState>> result = (DeferredResult<ResponseEntity<DomainState>>) context
                .getMessage().getHeaders().get(MachineConfig.DEFERRED_RESULT_HEADER);

        result.setResult(ResponseEntity.ok(context.getTarget().getId()));
    }
}
