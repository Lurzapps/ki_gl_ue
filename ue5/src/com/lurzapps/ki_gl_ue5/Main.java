package com.lurzapps.ki_gl_ue5;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        CSP task4CSP = new CSP();

        task4CSP.arcs.add(new Arc() {
            @Override
            public boolean eval(int x, int y, int z) {
                return x < y;
            }
        });

	    arc3();
    }

    private static void arc3(CSP csp) {
        Queue<Arc> arcQueue = new LinkedList<>();
        while(!arcQueue.isEmpty()) {
            
        }
    }

}
