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

/**
 * Thrown to indicate that an error occurred during transformation process.
 * @since 1.0
 */
public class TransformException extends Exception {

    private static final long serialVersionUID = 3817895394279751877L;

    /**
     * Constructs a new instance with the specified cause.
     * @param cause the cause.
     * @since 1.0
     */
    public TransformException(final Throwable cause) {
        super(cause);
    }
}
