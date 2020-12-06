package com.ariefzuhri.amigo19.interfaces;

import com.ariefzuhri.amigo19.model.Meet;
import java.util.ArrayList;

public interface LoadMeetsCallback {
    void preExecute();
    void postExecute(ArrayList<Meet> meets);
}
