package com.example.forums_backend.repository;

import com.example.forums_backend.entity.Voting;
import com.example.forums_backend.entity.my_enum.Subject;
import com.example.forums_backend.entity.my_enum.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Voting, Long> {
    Optional<Voting> findFirstByPost_IdAndAccount_Id(Long postId, Long accountId);

    Optional<Voting> findFirstByComment_IdAndAccount_Id(Long commentId, Long accountId);

    List<Voting> findVotingByTypeAndSubject(VoteType voteType, Subject subject);

    List<Voting> findVotingByTypeAndSubjectAndPost_Id(VoteType voteType, Subject subject, Long id);

    List<Voting> findVotingByTypeAndSubjectAndComment_Id(VoteType voteType, Subject subject, Long commentId);
}
