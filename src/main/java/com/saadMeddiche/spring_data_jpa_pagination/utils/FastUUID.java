package com.saadMeddiche.spring_data_jpa_pagination.utils;

import java.util.SplittableRandom;
import java.util.UUID;

public class FastUUID {

    private final static SplittableRandom random = new SplittableRandom();

    public static UUID randomUUID() {
        return new UUID(random.nextLong(), random.nextLong());
    }

}