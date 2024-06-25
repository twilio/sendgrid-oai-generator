package com.sendgrid.oai;

import java.util.LinkedHashSet;
import java.util.Set;

public class CommonAncestorFinder {

    public static void main(String[] args) {
        // Example usage
        try {
            Class<?> commonAncestor = findNearestCommonAncestor(Integer.class, Double.class);
            System.out.println("Nearest common ancestor: " + commonAncestor.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Class<?> findNearestCommonAncestor(Class<?>... classes) throws ClassNotFoundException {
        if (classes == null || classes.length == 0) {
            throw new IllegalArgumentException("No classes provided");
        }

        // Start with the class hierarchy of the first class
        Set<Class<?>> commonAncestors = getClassHierarchy(classes[0]);

        // Intersect with the class hierarchies of all other classes
        for (int i = 1; i < classes.length; i++) {
            Set<Class<?>> currentClassHierarchy = getClassHierarchy(classes[i]);
            commonAncestors.retainAll(currentClassHierarchy);
        }

        // The nearest common ancestor is the first element in the intersection
        return commonAncestors.iterator().next();
    }

    private static Set<Class<?>> getClassHierarchy(Class<?> clazz) {
        Set<Class<?>> hierarchy = new LinkedHashSet<>();
        while (clazz != null) {
            hierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return hierarchy;
    }
}
