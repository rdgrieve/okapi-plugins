package org.rdg.plugins.utils;

import net.sf.okapi.applications.rainbow.Input;
import net.sf.okapi.applications.rainbow.pipeline.PipelineWrapper;
import net.sf.okapi.common.filters.DefaultFilters;
import net.sf.okapi.common.filters.FilterConfigurationMapper;
import net.sf.okapi.common.plugins.PluginsManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.rdg.plugins.FilterMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilterConfigurationUtils
{

    public FilterConfigurationMapper getFilterMapper(String filterConfigPath,
        PluginsManager plManager) {
        // Initialize filter configurations
        FilterConfigurationMapper fcMapper = new FilterConfigurationMapper();
        DefaultFilters.setMappings(fcMapper, false, true);
        if (plManager != null) {
            fcMapper.addFromPlugins(plManager);
        }
        if (filterConfigPath != null) {
            System.out.println("Loading custom filter configurations from " +
                filterConfigPath);
            fcMapper.setCustomConfigurationsDirectory(filterConfigPath);
            fcMapper.updateCustomConfigurations();
        }
        return fcMapper;
    }


    public List<Input> processFilterMappings(FilterConfigurationMapper fcMapper) throws MojoExecutionException{
        return processFilterMappings(fcMapper, new ArrayList<FilterMapping>());
    }

    public List<Input> processFilterMappings(FilterConfigurationMapper fcMapper, List<FilterMapping> filterMappings) throws
        MojoExecutionException{
        // Convert filter mappings into dummy input files so the extension
        // map is generated.  Also add any custom configurations while we go.
        List<Input> inputFiles = new ArrayList<Input>();
        for (FilterMapping fm : filterMappings) {
            Input input = createInput(fm.getExtension(), fm.getFilterConfig());
            inputFiles.add(input);
            System.out.println("Added filter mapping " + fm.toString());
            if (fcMapper.getConfiguration(input.filterConfigId) == null) {
                System.out.println("Loading " + input.filterConfigId);
                fcMapper.addCustomConfiguration(input.filterConfigId);
                if (fcMapper.getConfiguration(input.filterConfigId) == null) {
                    throw new MojoExecutionException("Could not load filter configuration '"
                        + input.filterConfigId + "'");
                }
            }
        }
        return inputFiles;
    }

    public static Input createInput(String extension, String filterConfigId) {
        // Other fields are unused by BatchConfiguration.exportConfiguration()
        Input input = new Input();
        input.filterConfigId = filterConfigId;
        input.relativePath = "dummy" + extension;
        return input;
    }

}
