/*
 * Directory Content Maven Plugin
 * http://directory-content-maven-plugin.projects.gabrys.biz/
 *
 * Copyright (c) 2015 Adam Gabryś
 *
 * This file is licensed under the BSD 3-Clause (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain:
 * - a copy of the License at project page
 * - a template of the License at https://opensource.org/licenses/BSD-3-Clause
 */
package biz.gabrys.maven.plugins.directory.content.file;

/**
 * Stores data describing files.
 * @since 1.0
 */
public final class FileMetadata {

    private String fullPath;
    private String fullName;
    private String name;
    private String extension;
    private String directory;
    private long size;

    /**
     * Returns full file path (directory + name + extension).
     * @return the full file path.
     * @since 1.0
     */
    public String getFullPath() {
        return fullPath;
    }

    /**
     * Sets full file path (directory + name + extension).
     * @param fullPath the full file path.
     * @since 1.0
     */
    public void setFullPath(final String fullPath) {
        this.fullPath = fullPath;
    }

    /**
     * Returns full file name (name + extension).
     * @return the full file name.
     * @since 1.0
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets full file name (name + extension).
     * @param fullName the full file name.
     * @since 1.0
     */
    public void setFullName(final String fullName) {
        this.fullName = fullName;
    }

    /**
     * Returns file name without extension.
     * @return the file name.
     * @since 1.0
     */
    public String getName() {
        return name;
    }

    /**
     * Sets file name.
     * @param name the file name.
     * @since 1.0
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns file extension.
     * @return the file extension.
     * @since 1.0
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Sets file extension.
     * @param extension the file extension.
     * @since 1.0
     */
    public void setExtension(final String extension) {
        this.extension = extension;
    }

    /**
     * Returns path of directory where file is located.
     * @return the directory path.
     * @since 1.0
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Sets path of directory where file is located.
     * @param directory the directory path.
     * @since 1.0
     */
    public void setDirectory(final String directory) {
        this.directory = directory;
    }

    /**
     * Returns file size in bytes.
     * @return the file size.
     * @since 1.0
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets file size in bytes.
     * @param size the file size.
     * @since 1.0
     */
    public void setSize(final long size) {
        this.size = size;
    }
}
