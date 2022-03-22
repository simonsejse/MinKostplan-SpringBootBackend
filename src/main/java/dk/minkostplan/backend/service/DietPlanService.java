package dk.minkostplan.backend.service;

import dk.minkostplan.backend.repository.DietPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DietPlanService {
    private DietPlanRepository dietPlanRepository;

    @Autowired
    public DietPlanService(DietPlanRepository dietPlanRepository){
        this.dietPlanRepository = dietPlanRepository;
    }
}
