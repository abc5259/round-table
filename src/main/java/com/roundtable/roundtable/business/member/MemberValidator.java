package com.roundtable.roundtable.business.member;

import static java.util.Objects.*;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.MemberRepository;
import com.roundtable.roundtable.global.exception.MemberException.MemberNoHouseException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNotSameHouseException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateMemberInHouse(Member member) {
        if(member.getHouse() == null) {
            throw new MemberNoHouseException();
        }
    }

    public void validateMembersSameHouse(List<Member> members, House house) {
        members.forEach(member -> {
            validateMemberInHouse(member);
            if(!member.isSameHouse(house)) {
                throw new MemberNotSameHouseException();
            }
        });
    }

    public void validateMemberBelongsToHouse(Long memberId, Long houseId) {
        if(isNull(houseId)) {
            throw new MemberNoHouseException();
        }

        if(!memberRepository.existsByIdAndHouse(memberId, House.Id(houseId))) {
            throw new MemberNotSameHouseException();
        }
    }
}

