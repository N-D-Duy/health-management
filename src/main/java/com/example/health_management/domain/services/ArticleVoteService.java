package com.example.health_management.domain.services;

import com.example.health_management.common.shared.enums.VoteType;
import com.example.health_management.domain.entities.Article;
import com.example.health_management.domain.entities.ArticleVote;
import com.example.health_management.domain.repositories.ArticleRepository;
import com.example.health_management.domain.repositories.ArticleVoteRepository;
import com.example.health_management.domain.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleVoteService {
    private final ArticleVoteRepository articleVoteRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;


    public void vote(Long articleId, Long userId, VoteType voteType) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with ID: " + articleId));

        // Check if user already voted
        Optional<ArticleVote> existingVote = articleVoteRepository
                .findByArticleIdAndUserId(articleId, userId);

        if (existingVote.isPresent()) {
            ArticleVote vote = existingVote.get();
            if (vote.getType() == voteType) {
                // Remove vote if same type
                articleVoteRepository.deletePermanentlyById(vote.getId());
                updateVoteCount(article, vote.getType(), true);
            } else {
                // Change vote type
                vote.setType(voteType);
                articleVoteRepository.save(vote);
                updateVoteCount(article, VoteType.UPVOTE, vote.getType() == VoteType.DOWNVOTE);
                updateVoteCount(article, VoteType.DOWNVOTE, vote.getType() == VoteType.UPVOTE);
            }
        } else {
            // Create new vote
            ArticleVote vote = new ArticleVote();
            vote.setArticle(article);
            vote.setUser(userRepository.getReferenceById(userId));
            vote.setType(voteType);
            articleVoteRepository.save(vote);
            updateVoteCount(article, voteType, false);
        }

        // Check for rewards (not present yet)
        //checkAndAwardVoteMilestone(article);
    }

    private void updateVoteCount(Article article, VoteType type, boolean isRemove) {
        if (type == VoteType.UPVOTE) {
            article.setUpVoteCount(article.getUpVoteCount() + (isRemove ? -1 : 1));
        } else {
            article.setDownVoteCount(article.getDownVoteCount() + (isRemove ? -1 : 1));
        }
        articleRepository.save(article);
    }
}
