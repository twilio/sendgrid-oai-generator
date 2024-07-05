package com.sendgrid.oai;

import com.sendgrid.oai.constants.EnumConstants.Generator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openapitools.codegen.ClientOptInput;
import org.openapitools.codegen.DefaultGenerator;
import org.openapitools.codegen.config.CodegenConfigurator;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertFalse;

@RequiredArgsConstructor
@RunWith(Parameterized.class)
public class SendgridGeneratorTest {
    @Parameterized.Parameters
    public static Collection<Generator> generators() {
        return Arrays.asList(
                //Generator.SENDGRID_JAVA,
                Generator.SENDGRID_PYTHON);
    }

    private final Generator generator;

    @BeforeClass
    public static void setUp() {
        FileUtils.deleteQuietly(new File("codegen"));
    }

    @Test
    public void launchGenerator() {
        //final String pathname = "examples/spec/tsg_student_api_v3.yaml";
        final String pathname = "/Users/sbansla/Documents/code/sendgrid-oai/spec/yaml/tsg_alerts_v3.yaml";

        File filesList[];
        File directoryPath = new File(pathname);
        if (directoryPath.isDirectory()) {
            filesList = directoryPath.listFiles();
        } else {
            filesList = new File[]{directoryPath};
        }
        for (File file : filesList) {
            final CodegenConfigurator configurator = new CodegenConfigurator()
                    .setGeneratorName(generator.getValue())
                    .setInputSpec(file.getPath())
                    .setOutputDir("codegen/" + generator.getValue())
                   // .setInlineSchemaNameDefaults(Map.of("arrayItemSuffix", ""))
                    .addGlobalProperty("apiTests", "false")
                    .addGlobalProperty("apiDocs", "false")
                    .addGlobalProperty("local", "true");
            final ClientOptInput clientOptInput = configurator.toClientOptInput();
            DefaultGenerator generator = new DefaultGenerator();
            final List<File> output = generator.opts(clientOptInput).generate();
            assertFalse(output.isEmpty());
        }
    }
}