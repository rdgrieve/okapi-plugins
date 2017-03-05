package org.rdg.plugins.bconf;

import net.sf.okapi.applications.rainbow.Input;
import net.sf.okapi.applications.rainbow.Project;
import net.sf.okapi.applications.rainbow.batchconfig.BatchConfiguration;
import net.sf.okapi.applications.rainbow.lib.LanguageManager;
import net.sf.okapi.applications.rainbow.pipeline.PipelineWrapper;
import net.sf.okapi.common.filters.FilterConfigurationMapper;
import net.sf.okapi.common.plugins.PluginsManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.rdg.plugins.utils.FilterConfigurationUtils;

import java.io.File;
import java.util.List;

/**
 * Exports a Batch configuration file from a rnb file.
 */
public class RainbowSettingsBconfExporter implements BconfExporter
{

    private final String filterConfigPath;

    private final PluginsManager plManager;
    private final String bconfPath;
    private final File baseDirectory;

    public RainbowSettingsBconfExporter(File baseDirectory, PluginsManager plManager, String bconfPath, String
        filterConfigPath)
    {
        this.baseDirectory = baseDirectory;
        this.filterConfigPath = filterConfigPath;
        this.plManager = plManager;
        this.bconfPath = bconfPath;
    }

    public void export(String pathToFile, PluginsManager pluginsManager) throws MojoExecutionException
    {

        LanguageManager lm = new LanguageManager(); // ???
        Project project = new Project(lm);
        try {
            project.load(pathToFile);
        }
        catch (Exception e) {
            throw new MojoExecutionException("Couldn't load project from " + pathToFile, e);
        }

        FilterConfigurationUtils filterConfigurationUtils = new FilterConfigurationUtils();
        FilterConfigurationMapper fcMapper = filterConfigurationUtils.getFilterMapper(filterConfigPath, plManager);

        PipelineWrapper pipelineWrapper = new PipelineWrapper(fcMapper, baseDirectory.getPath(),
            plManager, baseDirectory.getPath(), baseDirectory.getPath(),
            null, null, null);
        pipelineWrapper.addFromPlugins(plManager);

        String pln = project.getUtilityParameters("currentProjectPipeline");
        pipelineWrapper.loadFromStringStorageOrReset(pln);

        // Allow <filterMapping> elements to override the contents of the settings.
        // BatchConfiguration.exportConfiguration() always takes the first configuration
        // it finds for a given extension, so we just list the overrides first.
        List<Input> inputFiles = filterConfigurationUtils.processFilterMappings(fcMapper);
        inputFiles.addAll(project.getList(0));

        BatchConfiguration bconfig = new BatchConfiguration();
        System.out.println("Writing batch configuration to " + bconfPath);
        bconfig.exportConfiguration(bconfPath, pipelineWrapper, fcMapper, inputFiles);
    }
}
