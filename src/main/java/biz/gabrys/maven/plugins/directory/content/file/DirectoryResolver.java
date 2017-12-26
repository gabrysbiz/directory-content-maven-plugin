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
 * Responsible for getting parent directory path for {@link File} objects.
 * @since 1.0
 */
public class DirectoryResolver {

    /**
     * Returns parent directory path for file.
     * @param file the file.
     * @param rootDirectory the root directory for file path.
     * @param separator the file path separator used in returned path.
     * @return the parent directory path.
     * @since 1.0
     */
    public String resolve(final File file, final File rootDirectory, final String separator) {
        final int index = rootDirectory.getAbsolutePath().length();
        String path = file.getParentFile().getAbsolutePath().substring(index);
        if (path.startsWith(File.separator)) {
            path = path.substring(File.separator.length());
        }
        if (File.separator.equals(separator)) {
            return path;
        }
        return path.replace(File.separator, separator);
    }
}
