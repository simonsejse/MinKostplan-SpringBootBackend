package dk.minkostplan.backend.entities;

import javax.persistence.*;

@Entity(name = "RecipeVote")
@Table(name = "recipe_votes")
public class RecipeVote {

	@EmbeddedId
	private RecipeVoteId recipeVoteId;

	@Column(name="isUpvote")
	private Boolean isUpvote;

	protected RecipeVote(){}

	public RecipeVote(RecipeVoteId recipeVoteId, boolean isUpVote) {
		this.recipeVoteId = recipeVoteId;
		this.isUpvote = isUpVote;
	}

	public RecipeVoteId getRecipeApprovalsId() {
		return recipeVoteId;
	}

	public boolean isUpvote() {
		return isUpvote;
	}
}


