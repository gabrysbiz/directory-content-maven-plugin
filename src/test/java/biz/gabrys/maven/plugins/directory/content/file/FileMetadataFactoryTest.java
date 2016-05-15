package biz.gabrys.maven.plugins.directory.content.file;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public final class FileMetadataFactoryTest {

    @Test
    public void create_oneFile_returnMetadata() {
        final FileMetadataFactoryConfiguration configuration = new FileMetadataFactoryConfiguration();
        final File rootDirectory = new File("/root");
        configuration.setRootDirectory(rootDirectory);
        final String separator = "separator";
        configuration.setSeparator(separator);

        final File file = Mockito.mock(File.class);
        final String fullName = "name.extension";
        Mockito.when(file.getName()).thenReturn(fullName);

        final FileNameResolver fileNameResolver = Mockito.mock(FileNameResolver.class);
        final String name = "name";
        Mockito.when(fileNameResolver.resolveName(file)).thenReturn(name);
        final String extension = "extension";
        Mockito.when(fileNameResolver.resolveExtension(file)).thenReturn(extension);

        final DirectoryResolver directoryResolver = Mockito.mock(DirectoryResolver.class);
        final String directory = "directory";
        Mockito.when(directoryResolver.resolve(file, rootDirectory, separator)).thenReturn(directory);

        final String fullPath = directory + separator + name + "." + extension;

        final long size = 1;
        Mockito.when(file.length()).thenReturn(size);

        final FileMetadataFactory factory = Mockito.spy(new FileMetadataFactory(configuration, fileNameResolver, directoryResolver));

        final FileMetadata metadata = factory.create(file);
        Assert.assertEquals("Full path.", fullPath, metadata.getFullPath());
        Assert.assertEquals("Full name.", fullName, metadata.getFullName());
        Assert.assertEquals("File name.", name, metadata.getName());
        Assert.assertEquals("File extension.", extension, metadata.getExtension());
        Assert.assertEquals("Directory.", directory, metadata.getDirectory());
        Assert.assertEquals("Size.", size, metadata.getSize());

        Mockito.verify(file).getName();
        Mockito.verify(factory).create(file);
        Mockito.verify(fileNameResolver).resolveName(file);
        Mockito.verify(fileNameResolver).resolveExtension(file);
        Mockito.verify(directoryResolver).resolve(file, rootDirectory, separator);
        Mockito.verifyNoMoreInteractions(factory, fileNameResolver, directoryResolver);
    }

    @Test
    public void create_twoFiles_returnMetadata() {
        final FileMetadataFactoryConfiguration configuration = new FileMetadataFactoryConfiguration();
        final File rootDirectory = new File("/root");
        configuration.setRootDirectory(rootDirectory);
        final String separator = "separator";
        configuration.setSeparator(separator);

        final File file1 = Mockito.mock(File.class);
        final String fullName1 = "name1.extension1";
        Mockito.when(file1.getName()).thenReturn(fullName1);
        final File file2 = Mockito.mock(File.class);
        final String fullName2 = "name2.extension2";
        Mockito.when(file2.getName()).thenReturn(fullName2);

        final FileNameResolver fileNameResolver = Mockito.mock(FileNameResolver.class);
        final String name1 = "name1";
        Mockito.when(fileNameResolver.resolveName(file1)).thenReturn(name1);
        final String extension1 = "extension1";
        Mockito.when(fileNameResolver.resolveExtension(file1)).thenReturn(extension1);
        final String name2 = "name2";
        Mockito.when(fileNameResolver.resolveName(file2)).thenReturn(name2);
        final String extension2 = "extension2";
        Mockito.when(fileNameResolver.resolveExtension(file2)).thenReturn(extension2);

        final DirectoryResolver directoryResolver = Mockito.mock(DirectoryResolver.class);
        final String directory1 = "directory1";
        Mockito.when(directoryResolver.resolve(file1, rootDirectory, separator)).thenReturn(directory1);
        final String directory2 = "directory2";
        Mockito.when(directoryResolver.resolve(file2, rootDirectory, separator)).thenReturn(directory2);

        final String fullPath1 = directory1 + separator + name1 + "." + extension1;
        final String fullPath2 = directory2 + separator + name2 + "." + extension2;

        final long size1 = 1;
        Mockito.when(file1.length()).thenReturn(size1);
        final long size2 = 1;
        Mockito.when(file2.length()).thenReturn(size2);

        final FileMetadataFactory factory = Mockito.spy(new FileMetadataFactory(configuration, fileNameResolver, directoryResolver));

        final List<File> files = Arrays.asList(file1, file2);
        final List<FileMetadata> metadata = factory.create(files);
        FileMetadata fileMetadata = metadata.get(0);
        Assert.assertEquals("First file full path.", fullPath1, fileMetadata.getFullPath());
        Assert.assertEquals("First file full name.", fullName1, fileMetadata.getFullName());
        Assert.assertEquals("First file file name.", name1, fileMetadata.getName());
        Assert.assertEquals("First file file extension.", extension1, fileMetadata.getExtension());
        Assert.assertEquals("First file directory.", directory1, fileMetadata.getDirectory());
        Assert.assertEquals("First file size.", size1, fileMetadata.getSize());
        fileMetadata = metadata.get(1);
        Assert.assertEquals("Second file full path.", fullPath2, fileMetadata.getFullPath());
        Assert.assertEquals("Second file full name.", fullName2, fileMetadata.getFullName());
        Assert.assertEquals("Second file file name.", name2, fileMetadata.getName());
        Assert.assertEquals("Second file file extension.", extension2, fileMetadata.getExtension());
        Assert.assertEquals("Second file directory.", directory2, fileMetadata.getDirectory());
        Assert.assertEquals("Second file size.", size2, fileMetadata.getSize());

        Mockito.verify(file1).getName();
        Mockito.verify(file2).getName();
        Mockito.verify(factory).create(files);
        Mockito.verify(factory).create(file1);
        Mockito.verify(fileNameResolver).resolveName(file1);
        Mockito.verify(fileNameResolver).resolveExtension(file1);
        Mockito.verify(directoryResolver).resolve(file1, rootDirectory, separator);
        Mockito.verify(factory).create(file2);
        Mockito.verify(fileNameResolver).resolveName(file2);
        Mockito.verify(fileNameResolver).resolveExtension(file2);
        Mockito.verify(directoryResolver).resolve(file2, rootDirectory, separator);
        Mockito.verifyNoMoreInteractions(factory, fileNameResolver, directoryResolver);
    }
}
