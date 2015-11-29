package com.morgan.design.utils;

import com.morgan.design.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmileyIconUtils {

    public static int getDefaultSmiley() {
        return R.drawable.face_plain;
    }

    public static List<Integer> determineIconList(final int resultsSize) {
        if (0 == resultsSize) {
            return new ArrayList<>();
        }

        final List<Integer> images = new ArrayList<Integer>();
        for (int i = 0; i < resultsSize; i++) {
            images.add(R.drawable.face_plain);
        }

        images.set(0, R.drawable.face_grin);
        images.set(1, R.drawable.face_surprise);
        images.set(resultsSize - 1, R.drawable.face_monkey);

        if (resultsSize >= 5) {
            images.set(resultsSize - 2, R.drawable.face_crying);
        }

        Collections.reverse(images);

        return images;
    }

}
