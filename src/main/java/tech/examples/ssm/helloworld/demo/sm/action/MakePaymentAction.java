package tech.examples.ssm.helloworld.demo.sm.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;
import tech.examples.ssm.helloworld.demo.sm.DomainEvent;
import tech.examples.ssm.helloworld.demo.sm.DomainState;
import tech.examples.ssm.helloworld.demo.sm.MachineConfiguration;

@Slf4j
@Component
public class MakePaymentAction implements Action<DomainState, DomainEvent> {
    @Override
    public void execute(StateContext<DomainState, DomainEvent> context) {
        log.info("Money money money");

        DeferredResult<ResponseEntity<DomainState>> result = (DeferredResult<ResponseEntity<DomainState>>) context
                .getMessage().getHeaders().get(MachineConfiguration.RESULT_HEADER);

        result.setResult(ResponseEntity.ok(context.getTarget().getId()));
    }
}
