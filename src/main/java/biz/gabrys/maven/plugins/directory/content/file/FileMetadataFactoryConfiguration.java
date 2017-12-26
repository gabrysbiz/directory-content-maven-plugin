/*
 * Directory Content Maven Plugin
 * http://directory-content-maven-plugin.projects.gabrys.biz/
 *
 * Copyright (c) 2015 Adam Gabrys
 *
 * This file is licensed under the BSD 3-Clause (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain:
 * - a copy of the License at project page
 * - a template of the License at https://opensource.org/licenses/BSD-3-Clause
 */
package biz.gabrys.maven.plugins.directory.content.file;

import java.io.File;

/**
 * Stores {@link FileMetadataFactory} configuration.
 * @since 1.0
 */
public class FileMetadataFactoryConfiguration {

    private File rootDirectory;
    private String separator;

    /**
     * Returns root directory for files paths.
     * @return the root directory.
     * @since 1.0
     */
    public File getRootDirectory() {
        return rootDirectory;
    }

    /**
     * Sets root directory for files paths.
     * @param rootDirectory the root directory.
     * @since 1.0
     */
    public void setRootDirectory(final File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    /**
     * Returns file path separator.
     * @return the path separator.
     * @since 1.0
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * Sets file path separator.
     * @param separator the separator.
     * @since 1.0
     * @see File#separator
     */
    public void setSeparator(final String separator) {
        this.separator = separator;
    }
}
