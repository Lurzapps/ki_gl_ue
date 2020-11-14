package com.lurzapps.ki_gl_ue4;

import java.util.Arrays;
import java.util.Objects;

public class SPath {
    SNode[] path;
    double priority; //costs so far

    public SPath(SNode[] p, double pr) {
        path = p;
        priority = pr;
    }

    public double getPriority() {
        return priority;
    }

    SNode u() {
        return path[path.length - 1];
    }

    @Override
    public String toString() {
        return Arrays.toString(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SPath)) return false;
        SPath path1 = (SPath) o;
        return priority == path1.priority &&
                Arrays.equals(path, path1.path);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(priority);
        result = 31 * result + Arrays.hashCode(path);
        return result;
    }
}
