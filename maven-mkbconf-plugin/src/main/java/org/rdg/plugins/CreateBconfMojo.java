package org.rdg.plugins;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Goal which touches a timestamp file.
 *
 * @goal generateBconf
 *
 * @phase package
 */
public class CreateBconfMojo extends AbstractMojo
{

	/**
	 * Output filename.
	 * 
	 * @parameter expression="${project.build.finalName}"
	 * @required
	 */
	private String targetFile;

	/**
	 * Location of the output directory.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File targetDir;

	/**
	 * The maven project.
	 *
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 * */
	private MavenProject mavenProject;

	/**
	 * Files to include.
	 *
	 * @parameter
	 */
	private List includes;

	/**
	 * The name and location of the rainbow settings files.
	 *
	 * @parameter expression="${bconf.rainbowSettingsFile}"
	 */
	private File rainbowSettingsFile;

	/**
	 * The name and location of the pipeline files.
	 *
	 * @parameter expression="${bconf.pipelineFile}"
	 */
	private String pipelineFile;

    /**
     * Any filter mapping to include in the bconf. Must be of the format: <code>
     *     <filterMapping>
     *         <extension>doc</extension><filterConfig>okf_xmlstream@cdata</filterConfig>
     *     </filterMapping>
     * </code>
	 *
	 * @parameter
     */
	private List<FilterMapping> filterMappings;


	public void execute()
		throws MojoExecutionException
	{
		checkConfiguration();
		getLog().info("Path to rainbowFile: " + rainbowSettingsFile.getAbsolutePath());
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
	 * Gets the location of the output directory.
	 * @return The location of the output directory.
	 */
	public File getTargetDir() {
		return targetDir;
	}

	/**
	 * Gets the output filename.
	 * @return The output filename.
	 */
	public String getTargetFile() {
		return targetFile;
	}

	/**
	 * Gets the includes listed in the project's POM.
	 * @return The includes for the project.
	 */
	public List getIncludes() {
		return includes;
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
