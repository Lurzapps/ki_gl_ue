package com.lurzapps.ki_gl_ue4;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class APath {

    //costs so far
    double g;
    SNode[] path;

    SNode u;
    double f;

    public APath(@NotNull SNode[] path0, double g0) {
        path = path0;
        g = g0;

        u = path[path.length - 1];
        f = g + Main.h(u);
    }

    //f = cost so far + heuristics
    //f = g + h
    //f is the priority
    public double f() {
        return f;
    }

    // there is a path with a lower f that has the same last node
    public boolean compare(Collection<APath> coll) {
        for(APath ap : coll) {
            if(ap.u.equals(u) &&
                ap.f < f)
                return true;
        }

        return false;
    }

    public void clean(Collection<APath> coll) {
        List<APath> remove = new LinkedList<>();
        for(APath ap : coll) {
            if(ap.u.equals(u))
                remove.add(ap);
        }

        for(APath ap : remove)
            coll.remove(ap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof APath)) return false;
        APath aPath = (APath) o;
        return g == aPath.g &&
                Arrays.equals(path, aPath.path);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(g);
        result = 31 * result + Arrays.hashCode(path);
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(path);
    }

    public APath expand(@NotNull SNode newLast) {
        SNode[] newP = new SNode[path.length + 1];
        System.arraycopy(path, 0, newP, 0, path.length);
        newP[path.length] = newLast;

        return new APath(newP, g + newLast.cost);
    }
}
