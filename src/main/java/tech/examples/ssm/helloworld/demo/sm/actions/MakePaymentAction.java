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
public class MakePaymentAction implements Action<DomainState, DomainEvent> {

    @Override
    public void execute(StateContext<DomainState, DomainEvent> context) {

        String userId = (String) context.getMessage().getHeaders().get(MachineConfig.USER_ID_HEADER);
        System.out.println("Hi " + userId + "! Money money money");

        DeferredResult<ResponseEntity<DomainState>> result = (DeferredResult<ResponseEntity<DomainState>>) context
                .getMessage().getHeaders().get(MachineConfig.DEFERRED_RESULT_HEADER);

        result.setResult(ResponseEntity.ok(context.getTarget().getId()));
    }
}
