package com.socialmediaassignment.team3.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import com.socialmediaassignment.team3.entities.embeddable.Profile;
import com.socialmediaassignment.team3.exceptions.BadRequestException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "user_table")
@JsonIgnoreProperties({"followers", "following", "likedTweets", "tweets", "mentions", "profile", "credential"})
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "username", column = @Column(nullable = false, unique = true)),
            @AttributeOverride(name = "password", column = @Column(nullable = false))
    })
    private Credential credential;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(nullable = false))
    })
    private Profile profile;

    @Column(name = "created_on")
    @CreationTimestamp
    private Date joined;

    private boolean deleted;

    @OneToMany(mappedBy = "author")
    private Set<Tweet> tweets = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tweet_like_mapping",
            joinColumns = {@JoinColumn(name = "tweet_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<Tweet> likedTweets = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "mention_mapping",
        joinColumns = {@JoinColumn(name = "tweet_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<Tweet> mentions = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="follower_following_mapping",
        joinColumns = {@JoinColumn(name="follower_id")},
        inverseJoinColumns = {@JoinColumn(name="following_id")}
    )
    private Set<User> following = new HashSet<>();

    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
    private Set<User> followers = new HashSet<>();

    public void addFollower(User follower) {
        this.followers.add(follower);
        follower.getFollowing().add(this);
    }

    public void removeFollower(User follower) {
        this.followers.remove(follower);
        follower.getFollowing().remove(this);
    }

    public void addFollowing(User following) {
        this.following.add(following);
        following.getFollowers().add(this);
    }

    public void removeFollowing(User following) {
        this.following.remove(following);
        following.getFollowers().remove(this);
    }

    public void addMention(Tweet tweet) {
        this.mentions.add(tweet);
        tweet.getUsersMentioned().add(this);
    }

    public void removeMention(Tweet tweet) {
        this.mentions.remove(tweet);
        tweet.getUsersMentioned().remove(this);
    }

    public void addLikedTweet(Tweet tweet) {
        this.likedTweets.add(tweet);
        tweet.getLikes().add(this);
    }

    public void removeLikedTweet(Tweet tweet) {
        this.likedTweets.remove(tweet);
        tweet.getLikes().remove(this);
    }

    public void validateUser() {
        if (this.getCredential().getUsername() == null)
            throw new BadRequestException("Field 'username' is required");
        if (this.getCredential().getPassword() == null)
            throw new BadRequestException("Field 'password' is required");
        if (this.getProfile().getEmail() == null)
            throw new BadRequestException("Field 'email' is required");
    }

    @Override
    public String toString() {
        return this.getCredential().getUsername();
    }
}
