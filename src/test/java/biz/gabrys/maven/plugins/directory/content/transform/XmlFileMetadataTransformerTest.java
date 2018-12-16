package biz.gabrys.maven.plugins.directory.content.transform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import biz.gabrys.maven.plugins.directory.content.file.FileMetadata;

public final class XmlFileMetadataTransformerTest {

    @Test
    public void transform_fileWithoutSpecialCharacters_returnsXml() {
        final FileMetadata metadata = new FileMetadata();
        metadata.setFullPath("directoryPath/fileName.ext");
        metadata.setFullName("fileName.ext");
        metadata.setName("fileName");
        metadata.setExtension("ext");
        metadata.setDirectory("directoryPath");
        metadata.setSize(2L);

        final StringBuilder expectedXml = new StringBuilder();
        expectedXml.append("<file>");
        expectedXml.append("<fullPath>directoryPath/fileName.ext</fullPath>");
        expectedXml.append("<fullName>fileName.ext</fullName>");
        expectedXml.append("<name>fileName</name>");
        expectedXml.append("<extension>ext</extension>");
        expectedXml.append("<directory>directoryPath</directory>");
        expectedXml.append("<size>2</size>");
        expectedXml.append("</file>");

        final String xml = new XmlFileMetadataTransformer().transform(metadata);

        assertThat(xml).isEqualTo(expectedXml.toString());
    }

    @Test
    public void transform_fileWithSpecialCharacters_returnsXml() {
        final FileMetadata metadata = new FileMetadata();
        metadata.setFullPath("dir<![CDATA[a]]>dir/<fileName>.e><t");
        metadata.setFullName("<fileName>.e><t");
        metadata.setName("<fileName>");
        metadata.setExtension("e><t");
        metadata.setDirectory("dir<![CDATA[a]]>dir");
        metadata.setSize(2L);

        final StringBuilder expectedXml = new StringBuilder();
        expectedXml.append("<file>");
        expectedXml.append("<fullPath>dir&lt;![CDATA[a]]&gt;dir/&lt;fileName&gt;.e&gt;&lt;t</fullPath>");
        expectedXml.append("<fullName>&lt;fileName&gt;.e&gt;&lt;t</fullName>");
        expectedXml.append("<name>&lt;fileName&gt;</name>");
        expectedXml.append("<extension>e&gt;&lt;t</extension>");
        expectedXml.append("<directory>dir&lt;![CDATA[a]]&gt;dir</directory>");
        expectedXml.append("<size>2</size>");
        expectedXml.append("</file>");

        final String xml = new XmlFileMetadataTransformer().transform(metadata);

        assertThat(xml).isEqualTo(expectedXml.toString());
    }

    @Test
    public void transform_fileWithoutExtension_returnsXml() {
        final FileMetadata metadata = new FileMetadata();
        metadata.setFullPath("directoryPath/fileName");
        metadata.setFullName("fileName");
        metadata.setName("fileName");
        metadata.setExtension(null);
        metadata.setDirectory("directoryPath");
        metadata.setSize(3L);

        final StringBuilder expectedXml = new StringBuilder();
        expectedXml.append("<file>");
        expectedXml.append("<fullPath>directoryPath/fileName</fullPath>");
        expectedXml.append("<fullName>fileName</fullName>");
        expectedXml.append("<name>fileName</name>");
        expectedXml.append("<directory>directoryPath</directory>");
        expectedXml.append("<size>3</size>");
        expectedXml.append("</file>");

        final String xml = new XmlFileMetadataTransformer().transform(metadata);

        assertThat(xml).isEqualTo(expectedXml.toString());
    }

    @Test
    public void transform_twoFiles_returnsXml() {
        final Collection<FileMetadata> metadata = new ArrayList<>();

        final StringBuilder expectedXml = new StringBuilder();
        expectedXml.append("<files>\n");

        final FileMetadata fileMetadata1 = new FileMetadata();
        fileMetadata1.setFullPath("directory1/name1.ext1");
        fileMetadata1.setFullName("name1.ext1");
        fileMetadata1.setName("name1");
        fileMetadata1.setExtension("ext1");
        fileMetadata1.setDirectory("directory1");
        fileMetadata1.setSize(0L);
        metadata.add(fileMetadata1);
        expectedXml.append("<file>");
        expectedXml.append("<fullPath>directory1/name1.ext1</fullPath>");
        expectedXml.append("<fullName>name1.ext1</fullName>");
        expectedXml.append("<name>name1</name>");
        expectedXml.append("<extension>ext1</extension>");
        expectedXml.append("<directory>directory1</directory>");
        expectedXml.append("<size>0</size>");
        expectedXml.append("</file>\n");

        final FileMetadata fileMetadata2 = new FileMetadata();
        fileMetadata2.setFullPath("directory2/name2.ext2");
        fileMetadata2.setFullName("name2.ext2");
        fileMetadata2.setName("name2");
        fileMetadata2.setExtension("ext2");
        fileMetadata2.setDirectory("directory2");
        fileMetadata2.setSize(1L);
        metadata.add(fileMetadata2);
        expectedXml.append("<file>");
        expectedXml.append("<fullPath>directory2/name2.ext2</fullPath>");
        expectedXml.append("<fullName>name2.ext2</fullName>");
        expectedXml.append("<name>name2</name>");
        expectedXml.append("<extension>ext2</extension>");
        expectedXml.append("<directory>directory2</directory>");
        expectedXml.append("<size>1</size>");
        expectedXml.append("</file>\n");

        expectedXml.append("</files>");

        final XmlFileMetadataTransformer transformer = spy(new XmlFileMetadataTransformer());

        final String xml = transformer.transform(metadata);

        assertThat(xml).isEqualTo(expectedXml.toString());
        verify(transformer).transform(metadata);
        verify(transformer).transform(fileMetadata1);
        verify(transformer).transform(fileMetadata2);
        verifyNoMoreInteractions(transformer);
    }
}
