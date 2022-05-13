package dk.minkostplan.backend.constraints;

import dk.minkostplan.backend.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class MetaExistOrCreateValidator implements ConstraintValidator<MetaExistOrCreate, Set<String>> {

    private final MetaService metaService;

    @Autowired
    public MetaExistOrCreateValidator(MetaService metaService) {
        this.metaService = metaService;
    }

    @Override
    public boolean isValid(Set<String> metas, ConstraintValidatorContext constraintValidatorContext) {
        return metas.stream()
                .filter(Predicate.not(String::isEmpty))
                .allMatch(metaService::doesMetaExistsOrCreate);
    }
}
