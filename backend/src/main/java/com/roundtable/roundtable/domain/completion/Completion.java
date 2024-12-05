package com.roundtable.roundtable.domain.completion;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.event.Category;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Completion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private Category category;

    @NotNull
    @Column
    private String taskName;

    @NotNull
    @Column
    private LocalDate completedDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
