package com.saadMeddiche.spring_data_jpa_pagination.utils;

import java.util.SplittableRandom;
import java.util.UUID;

public class FastUUID {

    private static final ThreadLocal<SplittableRandom> RANDOM =
            ThreadLocal.withInitial(SplittableRandom::new);

    public static UUID randomUUID() {
        var r = RANDOM.get();
        return new UUID(r.nextLong(), r.nextLong());
    }

}