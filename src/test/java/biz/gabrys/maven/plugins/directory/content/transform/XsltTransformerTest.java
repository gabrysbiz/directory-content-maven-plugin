package biz.gabrys.maven.plugins.directory.content.transform;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class XsltTransformerTest {

    @Test(expected = TransformException.class)
    public void transform_invalidData_throwsException() throws TransformerException, TransformException {
        final XsltTransformer xsltTransformer = spy(new XsltTransformer());

        final TransformerFactory factory = mock(TransformerFactory.class);
        doReturn(factory).when(xsltTransformer).createTransformerFactory();
        final Transformer transformer = mock(Transformer.class);
        doReturn(transformer).when(xsltTransformer).createTransformer(eq(factory), any(File.class));

        doThrow(TransformerException.class).when(transformer).transform(any(StreamSource.class), any(StreamResult.class));
        final String xml = "xml";
        final File xslt = mock(File.class);

        xsltTransformer.transform(xml, xslt);
    }

    @Test
    public void transform_correctData_returnsTransformedDocument() throws TransformerException, TransformException {
        final XsltTransformer xsltTransformer = spy(new XsltTransformer());

        final TransformerFactory factory = mock(TransformerFactory.class);
        doReturn(factory).when(xsltTransformer).createTransformerFactory();
        final Transformer transformer = mock(Transformer.class);
        doReturn(transformer).when(xsltTransformer).createTransformer(eq(factory), any(File.class));

        doAnswer(new Answer<Void>() {

            @Override
            public Void answer(final InvocationOnMock invocation) throws IOException {
                final StreamResult result = (StreamResult) invocation.getArgument(1);
                result.getWriter().append("document");
                return null;
            }
        }).when(transformer).transform(any(StreamSource.class), any(StreamResult.class));

        final String xml = "xml";
        final File xslt = mock(File.class);

        final String result = xsltTransformer.transform(xml, xslt);

        assertThat(result).isEqualTo("document");
    }

    @Test(expected = TransformException.class)
    public void createTransformer_sourceIsInvalid_throwsException() throws TransformerConfigurationException, TransformException {
        final TransformerFactory factory = mock(TransformerFactory.class);
        when(factory.newTransformer(any(StreamSource.class))).thenThrow(TransformerConfigurationException.class);

        final File xslt = mock(File.class);
        when(xslt.toURI()).thenReturn(URI.create("uri"));

        new XsltTransformer().createTransformer(factory, xslt);
    }

    @Test
    public void createTransformer_sourceIsValid_returnsTransformer() throws TransformerConfigurationException, TransformException {
        final TransformerFactory factory = mock(TransformerFactory.class);
        final Transformer transformer = mock(Transformer.class);
        when(factory.newTransformer(any(StreamSource.class))).thenReturn(transformer);

        final File xslt = mock(File.class);
        final String uri = "file.xslt";
        when(xslt.toURI()).thenReturn(URI.create(uri));

        final Transformer result = new XsltTransformer().createTransformer(factory, xslt);

        assertThat(result).isSameAs(transformer);
        final ArgumentCaptor<StreamSource> captor = ArgumentCaptor.forClass(StreamSource.class);
        verify(factory).newTransformer(captor.capture());
        final StreamSource source = captor.getValue();
        assertThat(source).isNotNull();
        assertThat(source.getSystemId()).isEqualTo(uri);
    }

    @Test
    public void createTransformerFactory() throws TransformException {
        final TransformerFactory result = new XsltTransformer().createTransformerFactory();

        assertThat(result).isNotNull();
        assertThat(result.getFeature(XMLConstants.FEATURE_SECURE_PROCESSING)).isTrue();
    }
}
