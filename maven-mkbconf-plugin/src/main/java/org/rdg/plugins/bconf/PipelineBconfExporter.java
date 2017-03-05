package org.rdg.plugins.bconf;

import net.sf.okapi.applications.rainbow.Input;
import net.sf.okapi.applications.rainbow.batchconfig.BatchConfiguration;
import net.sf.okapi.applications.rainbow.pipeline.PipelineWrapper;
import net.sf.okapi.common.filters.FilterConfigurationMapper;
import net.sf.okapi.common.plugins.PluginsManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.rdg.plugins.utils.FilterConfigurationUtils;

import java.io.File;
import java.util.List;

/**
 * Exports a Batch configuration file from a pipeline file.
 */
public class PipelineBconfExporter implements BconfExporter
{

    private final String filterConfigPath;

    private final PluginsManager plManager;
    private final String bconfPath;
    private final File baseDirectory;

    public PipelineBconfExporter(File baseDirectory, PluginsManager plManager, String bconfPath, String
        filterConfigPath)
    {
        this.baseDirectory = baseDirectory;
        this.filterConfigPath = filterConfigPath;
        this.plManager = plManager;
        this.bconfPath = bconfPath;
    }

    public void export(String pathToFile, PluginsManager pluginsManager) throws MojoExecutionException
    {

        FilterConfigurationUtils filterConfigurationUtils = new FilterConfigurationUtils();
        // Initialize things and load the pipeline.  This will produce
        // a warning if the pipeline references unavailable steps.
        // However, it will not break the build.
        FilterConfigurationMapper fcMapper = filterConfigurationUtils.getFilterMapper(filterConfigPath, plManager);

        PipelineWrapper pipelineWrapper = new PipelineWrapper(fcMapper, baseDirectory.getPath(),
            plManager, baseDirectory.getPath(), baseDirectory.getPath(),
            null, null, null);
        pipelineWrapper.addFromPlugins(plManager);

        pipelineWrapper.load(pathToFile);
        List<Input> inputFiles = filterConfigurationUtils.processFilterMappings(fcMapper);

        BatchConfiguration bconfig = new BatchConfiguration();
        System.out.println("Writing batch configuration to " + bconfPath);
        bconfig.exportConfiguration(bconfPath, pipelineWrapper, fcMapper, inputFiles);
    }
}
