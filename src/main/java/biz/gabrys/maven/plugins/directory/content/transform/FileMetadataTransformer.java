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
package biz.gabrys.maven.plugins.directory.content.transform;

import biz.gabrys.maven.plugins.directory.content.file.FileMetadata;

/**
 * Responsible for transforming {@link FileMetadata files metadata} to text representation.
 * @since 1.0
 */
public interface FileMetadataTransformer {

    /**
     * Transforms files metadata to text representation.
     * @param metadata the collection with files metadata.
     * @return the text representation of the files metadata.
     * @since 1.0
     */
    String transform(Iterable<FileMetadata> metadata);

    /**
     * Transforms file metadata to text representation.
     * @param metadata the file metadata.
     * @return the text representation of the file metadata.
     * @since 1.0
     */
    String transform(FileMetadata metadata);
}
