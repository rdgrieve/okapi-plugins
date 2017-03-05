package org.rdg.plugins;

import net.sf.okapi.common.plugins.PluginsManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.rdg.plugins.bconf.PipelineBconfExporter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Mojo which outputs a bconf file.
 */
@Mojo(name = "generateBconf", defaultPhase = LifecyclePhase.PACKAGE)
public class CreateBconfMojo extends AbstractMojo
{

	/**
	 * Output filename.
	 */
	@Parameter( defaultValue = "${project.build.finalName}", required = true )
	private String targetFile;

	/**
	 * Location of the output directory.
	 */
	@Parameter( defaultValue = "${project.build.directory}", required = true )
	private File targetDir;

	/**
	 * The current project instance.
	 */
	@Parameter( defaultValue = "${project}", readonly = true, required = true )
	private MavenProject mavenProject;

	/**
	 * Files to include.
	 *
	 */
	@Parameter
	private List includes;

	/**
	 * The name and location of the rainbow settings files.
	 */
	@Parameter(property = "bconf.rainbowSettingsFile")
	private File rainbowSettingsFile;

	/**
	 * The name and location of the pipeline files.
	 */
	@Parameter(property = "bconf.pipelineFile")
	private String pipelineFile;

    @Parameter( property = "bconf.filterConfigurationDir", defaultValue = "${basedir}/src/main/resources")
    private File filterConfigurationDir;

    /**
     * Any filter mapping to include in the bconf. Must be of the format: <code>
     *     <filterMapping>
     *         <extension>doc</extension><filterConfig>okf_xmlstream@cdata</filterConfig>
     *     </filterMapping>
     * </code>
	 *
     */
    @Parameter
	private List<FilterMapping> filterMappings;


	public void execute()
		throws MojoExecutionException
	{
		checkConfiguration();
		getLog().info("Path to rainbowFile: " + rainbowSettingsFile.getAbsolutePath());


		PluginsManager pluginsManager = new PluginsManager();
		//TODO if there are jars to include create a workspace in the build directory to temporarily copy them into
		//so that they can be automatically discovered, the alternative is to list the jars as dependencies and to use
		//mavens artifactRepository to pull them in.

		if (getPipelineFile() !=  null) {

			//PipelineBconfExporter bconfExporter = new PipelineBconfExporter()

		} else {
			//getRainbowSettingsFile();
		}


	}

	private void checkConfiguration() throws MojoExecutionException
	{
		if (getPipelineFile() == null && getRainbowSettingsFile() == null) {
			throw new MojoExecutionException("One of pipelineFile and rainbowSettingsFile must be set.");
		}
		if (getPipelineFile()  != null && getRainbowSettingsFile() != null) {
			throw new MojoExecutionException("Only one of pipelineFile or rainbowSettingsFile may be set.");
		}

		if(filterMappings != null) {

			for (FilterMapping filterMapping : filterMappings) {
				getLog().info(filterMapping.toString());
				if(filterMapping.getExtension() == null || filterMapping.getFilterConfig() == null) {
					throw new MojoExecutionException("Each filterMapping must have both the extension and the "
						+ "filterConfig value set.");
				}

			}

		}
	}

	private List getDefaultIncludes() {
		List<String> result = new ArrayList<String>();
		result.add(getMavenProject().getBasedir().getName() + "/**/*.xml");

		return result;
	}


	/**
	 * Gets a reference to the maven project.
	 * @return The maven project.
	 */
	public MavenProject getMavenProject() {
		return mavenProject;
	}

	/**
	 * Gets the rainbow settings filename.
	 * @return The rainbow settings filename.
	 */
	public File getRainbowSettingsFile()
	{
		return rainbowSettingsFile;
	}

	/**
	 * Gets the pipeline filename.
	 * @return The pipeline filename.
	 */
	public String getPipelineFile()
	{
		return pipelineFile;
	}
}
