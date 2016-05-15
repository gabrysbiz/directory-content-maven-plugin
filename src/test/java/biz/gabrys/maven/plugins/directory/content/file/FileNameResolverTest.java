package biz.gabrys.maven.plugins.directory.content.file;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public final class FileNameResolverTest {

    @Test
    public void resolveName_fileWithoutExtension_returnFullName() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = Mockito.mock(File.class);
        Mockito.when(file.getName()).thenReturn("name");
        Assert.assertEquals("File name.", "name", resolver.resolveName(file));
        Mockito.verify(file).getName();
        Mockito.verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveExtension_fileWithoutExtension_returnNull() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = Mockito.mock(File.class);
        Mockito.when(file.getName()).thenReturn("name");
        Assert.assertEquals("File extension.", null, resolver.resolveExtension(file));
        Mockito.verify(file).getName();
        Mockito.verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveName_fileWithExtension_returnNameWithoutExtension() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = Mockito.mock(File.class);
        Mockito.when(file.getName()).thenReturn("name.ext");
        Assert.assertEquals("File name.", "name", resolver.resolveName(file));
        Mockito.verify(file).getName();
        Mockito.verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveExtension_fileWithExtension_returnExtension() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = Mockito.mock(File.class);
        Mockito.when(file.getName()).thenReturn("name.ext");
        Assert.assertEquals("File extension.", "ext", resolver.resolveExtension(file));
        Mockito.verify(file).getName();
        Mockito.verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveName_fileWithExtensionWithManyDots_returnNameWithoutExtension() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = Mockito.mock(File.class);
        Mockito.when(file.getName()).thenReturn("na.me.ext");
        Assert.assertEquals("File name.", "na.me", resolver.resolveName(file));
        Mockito.verify(file).getName();
        Mockito.verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveExtension_fileWithExtensionWithManyDots_returnExtension() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = Mockito.mock(File.class);
        Mockito.when(file.getName()).thenReturn("na.me.ext");
        Assert.assertEquals("File extension.", "ext", resolver.resolveExtension(file));
        Mockito.verify(file).getName();
        Mockito.verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveName_fileWithDotAtTheEnd_returnNameWithoutLastDot() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = Mockito.mock(File.class);
        Mockito.when(file.getName()).thenReturn("name.");
        Assert.assertEquals("File name.", "name", resolver.resolveName(file));
        Mockito.verify(file).getName();
        Mockito.verifyNoMoreInteractions(file);
    }

    @Test
    public void resolveExtension_fileWithDotAtTheEnd_returnEmptyText() {
        final FileNameResolver resolver = new FileNameResolver();
        final File file = Mockito.mock(File.class);
        Mockito.when(file.getName()).thenReturn("name.");
        Assert.assertEquals("File extension.", "", resolver.resolveExtension(file));
        Mockito.verify(file).getName();
        Mockito.verifyNoMoreInteractions(file);
    }
}
