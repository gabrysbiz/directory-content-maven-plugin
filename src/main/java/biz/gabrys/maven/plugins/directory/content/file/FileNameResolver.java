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
 * Responsible for getting file name and extensions from {@link File} objects.
 * @since 1.0
 */
public class FileNameResolver {

    /**
     * Returns file name without extension.
     * @param file the file.
     * @return the file name.
     * @since 1.0
     */
    public String resolveName(final File file) {
        final String fullName = file.getName();
        final int index = fullName.lastIndexOf('.');
        if (index > -1) {
            return fullName.substring(0, index);
        }
        return fullName;
    }

    /**
     * Returns file extension.
     * @param file the file.
     * @return the file extension.
     * @since 1.0
     */
    public String resolveExtension(final File file) {
        final String fullName = file.getName();
        final int index = fullName.lastIndexOf('.');
        if (index > -1) {
            if (index != fullName.length() - 1) {
                return fullName.substring(index + 1);
            } else {
                return "";
            }
        }
        return null;
    }
}
