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
package biz.gabrys.maven.plugins.directory.content;

import java.io.File;
import java.util.Collection;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import biz.gabrys.maven.plugin.util.parameter.ParametersLogBuilder;
import biz.gabrys.maven.plugin.util.parameter.sanitizer.SimpleSanitizer;

/**
 * Parent class for all goals related with <a href="http://www.w3.org/TR/xslt">XSLT</a> transformation.
 * @since 1.0
 */
abstract class AbstractTransformMojo extends AbstractPluginMojo {

    /**
     * The <a href="http://www.w3.org/TR/xslt">XSLT</a> document used to transformation.
     * @since 1.0
     */
    @Parameter(property = "directory.content.xsltFile", required = true)
    protected File xsltFile;

    /**
     * Source and output files encoding.
     * @since 1.0
     */
    @Parameter(property = "directory.content.encoding", defaultValue = "${project.build.sourceEncoding}")
    protected String encoding;

    /**
     * The separator used in full files paths.<br>
     * <b>Default value is</b>: <tt>/</tt> on UNIX systems or <tt>\</tt> on Microsoft Windows systems.
     * @since 1.0
     */
    @Parameter(property = "directory.content.separator")
    protected String separator;

    @Override
    protected final void fillParameters(final ParametersLogBuilder logger) {
        logger.append("xsltFile", xsltFile);
        logger.append("encoding", encoding);
        logger.append("separator", separator, new SimpleSanitizer(separator != null, File.separator));
        fillParameters2(logger);
    }

    protected abstract void fillParameters2(ParametersLogBuilder logger);

    @Override
    protected final void execute(final Collection<File> files) throws MojoFailureException {
        if (separator == null) {
            separator = File.separator;
        }
        execute2(files);
    }

    protected abstract void execute2(Collection<File> files) throws MojoFailureException;
}
