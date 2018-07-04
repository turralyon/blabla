package com.example.pgeraet81.blabla;

import android.net.Uri;

import java.io.File;

/**
 * Created by Pascal on 4-7-2018.
 */

public class Employee {

private String name;
private Uri finalPicture;

    public Uri getFinalPicture() {
        return finalPicture;
    }

    public void setFinalPicture(Uri finalPicture) {
        this.finalPicture = finalPicture;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
