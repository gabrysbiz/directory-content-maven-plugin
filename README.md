# About
[![License BSD 3-Clause](https://img.shields.io/badge/license-BSD%203--Clause-blue.svg)](http://directory-content-maven-plugin.projects.gabrys.biz/license.txt)
[![Build Status](https://travis-ci.org/gabrysbiz/directory-content-maven-plugin.svg?branch=master)](https://travis-ci.org/gabrysbiz/directory-content-maven-plugin)

Provides a collection of tools for working with directories.

# Goals Overview
* [directory-content:copy](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/copy-mojo.html) - copies files to directory
* [directory-content:copyFile](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/copyFile-mojo.html) - copies file from source to output directory (allow to change name)
* [directory-content:transformList](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/transformList-mojo.html) - transforms files list to a document using [XSLT](http://www.w3.org/TR/xslt) technology
* [directory-content:transformMetadata](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/transformMetadata-mojo.html) - transforms files metadata to documents using [XSLT](http://www.w3.org/TR/xslt) technology

# Requirements
The plugin to run requires:
* Java 5.0 or higher
* Maven 2.0.11 or higher

# Usage
General instructions on how to use the Directory Content Maven Plugin can be found on the [usage](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/usage.html) page. Some more specific use cases are described in the examples given below. Last but not least, users occasionally contribute additional examples, tips or errata to the plugin's [wiki](https://github.com/gabrysbiz/directory-content-maven-plugin/wiki) page.

In case you still have questions regarding the plugin's usage, please have a look at the [FAQ](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/faq.html).

If you feel like the plugin is missing a feature or has a defect, you can fill a feature request or bug report in the [issue tracker](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/issue-tracking.html). When creating a new issue, please provide a comprehensive description of your concern. Especially for fixing bugs it is crucial that the developers can reproduce your problem. For this reason, entire debug logs, POMs or most preferably little demo projects attached to the issue are very much appreciated. Of course, patches are welcome, too. Contributors can check out the project from the [source repository](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/source-repository.html) and will find supplementary information in the [guide to helping with Maven](http://maven.apache.org/guides/development/guide-helping.html).

# Examples
To provide you with better understanding of some usages of the Directory Content Maven Plugin, you can take a look into the following examples:
* [Using include/exclude patterns](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/examples/patterns.html)
* [Copies files from source to output directory](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/examples/copy.html)
* [Copies file to output directory with a new name](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/examples/copyFile.html)
* [Transform files list to file with JavaScript imports](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/examples/transform-list.html)
* [Transform files metadata to download files](http://directory-content-maven-plugin.projects.gabrys.biz/1.2.0/examples/transform-metadata.html)

You can also fetch example projects from [GitHub](https://github.com/gabrysbiz/directory-content-maven-plugin-examples).