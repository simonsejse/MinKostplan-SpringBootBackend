package dk.minkostplan.backend.service;

import dk.minkostplan.backend.entities.Meta;
import dk.minkostplan.backend.exceptions.MetaException;
import dk.minkostplan.backend.repository.MetaRepository;
import org.apache.xmlbeans.XmlNegativeInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MetaService {

    private final MetaRepository metaRepository;

    @Autowired
    public MetaService(MetaRepository metaRepository){
        this.metaRepository = metaRepository;
    }

    public Meta getMeta(String metaName) throws MetaException {
        return metaRepository.findByMeta(metaName)
                .orElseThrow(
                        () -> new MetaException(HttpStatus.NOT_FOUND, String.format("`%s` findes ikke som meta data til maden!", metaName))
        );
    }
}
