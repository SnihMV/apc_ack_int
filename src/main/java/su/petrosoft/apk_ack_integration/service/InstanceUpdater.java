package su.petrosoft.apk_ack_integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import su.petrosoft.apk_ack_integration.client.PlicanteRestClient;
import su.petrosoft.apk_ack_integration.exception.EntityNotFoundException;
import su.petrosoft.apk_ack_integration.exception.StaleVersionException;
import su.petrosoft.apk_ack_integration.mapper.CashPlanLimitMapper;
import su.petrosoft.apk_ack_integration.model.CashPlanLimit;
import su.petrosoft.apk_ack_integration.model.dto.plicante.UpdateInstanceResponseDto;
import su.petrosoft.apk_ack_integration.model.dto.plicante.attribute.Attribute;

import java.util.List;
import java.util.Optional;

import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.TEMPLATE_TITLE;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.getAttributesToUpdate;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.requestDtoForUpdate;
import static su.petrosoft.apk_ack_integration.util.CashPlanLimitUtil.requestDtoToGetMonetaryFieldsById;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.EMPTY_INSTANCE_ID;
import static su.petrosoft.apk_ack_integration.util.ExceptionMessageClass.INSTANCE_NOT_FOUND_BY_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstanceUpdater {
    private final PlicanteRestClient restClient;
    private final CashPlanLimitMapper cplMapper;

    @Retryable(
            retryFor = StaleVersionException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    public Optional<Long> updateCpl(CashPlanLimit updater, long updatingId) {
        CashPlanLimit updating = findCplToUpdate(updatingId);
        List<Attribute<?>> attributesToUpdate = getAttributesToUpdate(updating, updater);
        if (attributesToUpdate.isEmpty()) {
            return Optional.empty();
        }
        UpdateInstanceResponseDto responseDto = restClient.updateInstance(
                requestDtoForUpdate(updating.getId(), updating.getVersion(), attributesToUpdate));
        return Optional.of(responseDto.id());
    }

    private CashPlanLimit findCplToUpdate(long id) {
        return restClient.getTableAttributesList(requestDtoToGetMonetaryFieldsById(id)).stream()
                .findFirst()
                .map(cplMapper::toEntity)
                .orElseThrow(() -> new EntityNotFoundException(INSTANCE_NOT_FOUND_BY_ID.formatted(id, TEMPLATE_TITLE)));
    }
}
