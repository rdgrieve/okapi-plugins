package org.rdg.plugins.bconf;

import net.sf.okapi.common.plugins.PluginsManager;
import org.apache.maven.plugin.MojoExecutionException;

public interface BconfExporter
{

    void export(String pathToFile, PluginsManager pluginsManager) throws MojoExecutionException;

}
