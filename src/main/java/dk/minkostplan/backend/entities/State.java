package dk.minkostplan.backend.entities;

import dk.minkostplan.backend.models.Approval;

import javax.persistence.*;

@Table(name = "state")
@Entity(name = "State")
public class State {

    @Id
    private Long id;

    @Enumerated(value=EnumType.STRING)
    @Column(name="approval")
    private Approval approval;

    @Column(name="upvotes")
    private int upvotes;

    @Column(name="downvotes")
    private int downvotes;


    @JoinColumn(name="recipe_id")
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Recipe recipe;

    public int getUpvotes() {
        if (approval == Approval.ACCEPTED)
            throw new IllegalStateException("You cannot access upvotes after recipes closed.");
        return upvotes;
    }

    public int getDownvotes() {
        if (approval == Approval.ACCEPTED)
            throw new IllegalStateException("You cannot access upvotes after recipes closed.");
        return downvotes;
    }
}