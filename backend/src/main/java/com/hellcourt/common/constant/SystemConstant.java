package com.hellcourt.common.constant;

public interface SystemConstant {
    int ROLE_WUCHANG = 1;
    int ROLE_JUDGE = 2;
    int ROLE_KING = 3;
    int ROLE_ADMIN = 4;

    int SOUL_STATUS_PENDING = 0;
    int SOUL_STATUS_ARRIVED = 1;
    int SOUL_STATUS_JUDGING = 2;
    int SOUL_STATUS_JUDGED = 3;
    int SOUL_STATUS_REBIRTH = 4;

    int JUDGMENT_STATUS_PENDING = 0;
    int JUDGMENT_STATUS_APPROVED = 1;
    int JUDGMENT_STATUS_REJECTED = 2;

    int REVIEW_PASS = 1;
    int REVIEW_REJECT = 2;

    int REBIRTH_HEAVEN = 1;
    int REBIRTH_ASURA = 2;
    int REBIRTH_HUMAN = 3;
    int REBIRTH_ANIMAL = 4;
    int REBIRTH_HUNGRY = 5;
    int REBIRTH_HELL = 6;
}
