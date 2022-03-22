package dk.minkostplan.backend.controllers;

import dk.minkostplan.backend.service.DietPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="api/diet-plans")
public class DietPlanController {

    private final DietPlanService dietPlanService;

    @Autowired
    public DietPlanController(DietPlanService dietPlanService) {
        this.dietPlanService = dietPlanService;
    }

    @PostMapping(value = "/create-diet-plan")
    @ResponseBody
    public ResponseEntity<?> createDietPlan(Authentication authentication){
        String name = authentication.getName();

        return ResponseEntity.ok("OK");
    }
}
