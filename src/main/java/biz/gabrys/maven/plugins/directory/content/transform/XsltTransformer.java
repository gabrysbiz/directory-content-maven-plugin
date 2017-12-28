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

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Responsible for transforming XML documents using XSL templates.
 * @since 1.0
 */
public class XsltTransformer {

    /**
     * Transforms XML document using XSL template.
     * @param xml the XML document.
     * @param xslt the XSLT file.
     * @return the transformed document.
     * @throws TransformException if an error occurred during transformation process.
     * @since 1.0
     */
    public String transform(final String xml, final File xslt) throws TransformException {
        final TransformerFactory factory = TransformerFactory.newInstance();
        final Transformer transformer;
        try {
            transformer = factory.newTransformer(new StreamSource(xslt));
        } catch (final TransformerConfigurationException e) {
            throw new TransformException(e);
        }
        final StringWriter writer = new StringWriter();
        try {
            transformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(writer));
        } catch (final TransformerException e) {
            throw new TransformException(e);
        }
        writer.flush();
        return writer.toString();
    }
}
