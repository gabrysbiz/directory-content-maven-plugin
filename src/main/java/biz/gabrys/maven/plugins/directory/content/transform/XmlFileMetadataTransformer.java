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
package biz.gabrys.maven.plugins.directory.content.transform;

import java.io.StringWriter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;

import biz.gabrys.maven.plugins.directory.content.file.FileMetadata;

/**
 * Responsible for transforming {@link FileMetadata files metadata} to XML representation.
 * @since 1.0
 */
public class XmlFileMetadataTransformer implements FileMetadataTransformer {

    @Override
    public String transform(final Iterable<FileMetadata> metadata) {
        final StringBuilder xml = new StringBuilder("<files>\n");
        for (final FileMetadata fileMetadata : metadata) {
            xml.append(transform(fileMetadata));
            xml.append('\n');
        }
        xml.append("</files>");
        return xml.toString();
    }

    @Override
    public String transform(final FileMetadata metadata) {
        final XStream stream = new XStream();
        stream.aliasType("file", FileMetadata.class);
        final StringWriter writer = new StringWriter();
        stream.marshal(metadata, new CompactWriter(writer));
        return writer.toString();
    }
}
