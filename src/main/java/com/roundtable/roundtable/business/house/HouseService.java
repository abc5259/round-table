package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.house.dto.CreateHouse;
import com.roundtable.roundtable.business.house.dto.event.HouseCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseAppender houseAppender;

    private final MemberHouseManager memberHouseManager;

    private final ApplicationEventPublisher eventPublisher;
    private final HouseReader houseReader;

    public Long createHouse(CreateHouse createHouse, AuthMember authMember) {
        Long houseId = houseAppender.appendHouse(createHouse);
        memberHouseManager.enterHouse(houseId, authMember.memberId());

        eventPublisher.publishEvent(new HouseCreatedEvent(authMember.memberId(), houseId, createHouse.inviteEmails()));
        return houseId;
    }

    public void enterHouse(Long houseId, AuthMember authMember) {
        memberHouseManager.enterHouse(houseId, authMember.memberId());
    }

    public boolean existsHouseName(String name) {
        return houseReader.existsHouseName(name);
    }
}
