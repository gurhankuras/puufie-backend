package com.kuras.learnspring.learnspring.app_version.dto;

public final class SemVersion implements Comparable<SemVersion> {

    private final int major;
    private final int minor;
    private final int patch;

    public SemVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static SemVersion parse(String version) {
        String[] parts = version.split("\\.");
        int major = parts.length > 0 ? Integer.parseInt(parts[0]) : 0;
        int minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
        int patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
        return new SemVersion(major, minor, patch);
    }

    @Override
    public int compareTo(SemVersion other) {
        int cmp = Integer.compare(this.major, other.major);
        if (cmp != 0) return cmp;

        cmp = Integer.compare(this.minor, other.minor);
        if (cmp != 0) return cmp;

        return Integer.compare(this.patch, other.patch);
    }

    @Override
    public String toString() {
        return major + "." + minor + "." + patch;
    }

    public int major() { return major; }
    public int minor() { return minor; }
    public int patch() { return patch; }
}
