package com.roundtable.roundtable.business.house;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.delegation.event.CreateDelegationEvent;
import com.roundtable.roundtable.business.house.dto.CreateHouse;
import com.roundtable.roundtable.business.house.dto.HouseMember;
import com.roundtable.roundtable.business.house.event.HouseCreatedEvent;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.global.exception.MemberException.MemberAlreadyHasHouseException;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RecordApplicationEvents
class HouseServiceTest extends IntegrationTestSupport {

    @Autowired
    private HouseService sut;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ApplicationEvents events;

    @Autowired
    private HouseRepository houseRepository;

    @DisplayName("하우스를 생성한다.")
    @Test
    void createHouse() {
        //given
        Member member = appendMember(null, "email");
        Member member2 = appendMember(null, "email2");
        CreateHouse createHouse = new CreateHouse("house", List.of(member2.getEmail()));
        AuthMember authMember = new AuthMember(member.getId(), null);

        //when
        Long result = sut.createHouse(createHouse, authMember);

        //then
        assertThat(result).isNotNull();

        House house = houseRepository.findById(result).orElseThrow();
        assertThat(house)
                .extracting("name")
                .isEqualTo(createHouse.name());
        assertThat(house.getInviteCode()).isNotNull();
        assertThat(member.getHouse().getId()).isEqualTo(result);
        assertThat(events.stream(HouseCreatedEvent.class))
                .hasSize(1)
                .anySatisfy(event -> {
                    assertAll(
                            () -> assertThat(event.houseId()).isEqualTo(house.getId()),
                            () -> assertThat(event.appenderId()).isEqualTo(member.getId()),
                            () -> assertThat(event.invitedEmails()).isEqualTo(List.of(member2.getEmail()))
                    );
                });
    }

    @DisplayName("하우스에 이미 들어간 사람은 하우스를 생성할 수 없다.")
    @Test
    void createHouse_already_enter_house() {
        //given
        CreateHouse createHouse = new CreateHouse("house", new ArrayList<>());
        House house = appendHouse("code");
        Member member = appendMember(house, "email");
        AuthMember authMember = new AuthMember(member.getId(), house.getId());

        //when //then
        assertThatThrownBy(() -> sut.createHouse(createHouse, authMember))
                .isInstanceOf(MemberAlreadyHasHouseException.class)
                .hasMessage(MemberErrorCode.ALREADY_HAS_HOUSE.getMessage());
    }

    @DisplayName("하우스에 속한 맴버를 알 수 있다.")
    @Test
    void findHouseMembers() {
        //given
        House house = appendHouse("code");
        House house2 = appendHouse("code2");

        Member member1 = appendMember(house, "email1");
        Member member2 = appendMember(house, "email2");
        Member member3 = appendMember(house2, "email3");

        //when
        List<HouseMember> result = sut.findHouseMembers(
                new AuthMember(member1.getId(), member1.getHouse().getId()));

        //then
        assertThat(result).hasSize(2)
                .extracting("memberId")
                .contains(
                        member1.getId(), member2.getId()
                );
    }

    private Member appendMember(House house, String email) {
        Member member = Member.builder().name("name").email(email).password("password").house(house).build();
        return memberRepository.save(member);
    }

    private House appendHouse(String code) {
        House house = House.builder().name("house").inviteCode(InviteCode.builder().code(code).build()).build();
        return houseRepository.save(house);
    }

}