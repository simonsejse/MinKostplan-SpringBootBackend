package dk.minkostplan.backend.service;

import dk.minkostplan.backend.entities.Recipe;
import dk.minkostplan.backend.entities.RecipeVote;
import dk.minkostplan.backend.entities.RecipeVoteId;
import dk.minkostplan.backend.entities.User;
import dk.minkostplan.backend.exceptions.VoteException;
import dk.minkostplan.backend.repository.RecipeVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ConcurrentModificationException;

@Service
public class RecipeVoteService {

    private final RecipeVoteRepository voteRepository;

    @Autowired
    public RecipeVoteService(RecipeVoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Transactional
    public void createNewRecipeVoteByRecipeAndUser(Recipe recipe, User user, boolean isUpvote){
        RecipeVoteId recipeVoteId = new RecipeVoteId(recipe, user);
        /*
        if (voteRepository.existsById(recipeVoteId)){
            throw new VoteException("Du har allerede stemt på den her opskrift!", HttpStatus.CONFLICT);
        }*/
        RecipeVote recipeVote = new RecipeVote(recipeVoteId, isUpvote);
        voteRepository.save(recipeVote);
    }


}
