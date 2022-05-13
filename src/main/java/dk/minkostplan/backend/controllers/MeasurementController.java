package dk.minkostplan.backend.controllers;

import dk.minkostplan.backend.models.MeasureType;
import dk.minkostplan.backend.models.dtos.MeasureDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="api/measurements")
public class MeasurementController {

    @GetMapping
    public ResponseEntity<List<MeasureDTO>> getAvailableMeasurements(){

        final MeasureType[] values = MeasureType.values();
        List<MeasureDTO> measureDTOs = Arrays.stream(values)
                .map(MeasureDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(measureDTOs);
    }
}
